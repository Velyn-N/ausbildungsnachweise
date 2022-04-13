package de.nmadev.ausbildungsnachweise.rest.user;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@ApplicationScoped
public class ApiAuthenticationBean {
    private static final Key jwtKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    final String jwtUserIdClaim = "uid";
    final String jwtSubject = "NachweisAuth";
    final String jwtIssuer = "AusbildungsNachweisTool";

    @Inject
    UserDao userDao;

    private LoadingCache<String, User> jwtUserCache;
    private Supplier<User> defaultUserSupplier;

    @PostConstruct
    private void init() {
        defaultUserSupplier = Suppliers.memoizeWithExpiration(User::new, 12, TimeUnit.HOURS);

        jwtUserCache = CacheBuilder.newBuilder()
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public User load(String key) {
                        return userDao.getUser(parseJWT(key)).orElse(defaultUserSupplier.get());
                    }
                });
    }

    public User getUser(String jwt) {
        return (jwt != null) ? jwtUserCache.getUnchecked(jwt) : defaultUserSupplier.get();
    }

    /**
     * <b>Cached</b> access to the Default User
     */
    public User getDefaultUser() {
        return defaultUserSupplier.get();
    }

    public String createJWT(Long userId) {
        Date now = Date.from(Instant.now());

        Map<String, String> claims = new HashMap<>();
        claims.put(jwtUserIdClaim, userId.toString());

        return Jwts.builder()
                .setSubject(jwtSubject)
                .setIssuedAt(now)
                .setIssuer(jwtIssuer)
                .setClaims(claims)
                .signWith(jwtKey)
                .compact();
    }

    public Long parseJWT(String jwt) {
        if (StringUtils.isBlank(jwt)) return null;
        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwt.trim());
            String claimedUserId = jws.getBody().get(jwtUserIdClaim, String.class);

            return NumberUtils.toLong(claimedUserId);
        } catch (JwtException e) {
            log.warn("Could not Parse JWT: {}, Error: {}", jwt, e.getMessage());
            return null;
        }
    }
}
