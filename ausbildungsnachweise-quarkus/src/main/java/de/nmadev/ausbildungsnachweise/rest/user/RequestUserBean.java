package de.nmadev.ausbildungsnachweise.rest.user;

import de.nmadev.ausbildungsnachweise.dao.UserDao;
import de.nmadev.ausbildungsnachweise.entity.User;
import io.vertx.codegen.annotations.Nullable;
import io.vertx.core.http.HttpServerRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@Slf4j
@RequestScoped
public class RequestUserBean {

    @Inject
    ApiAuthenticationBean apiAuthBean;
    @Inject
    UserDao userDao;
    @Inject
    HttpServerRequest request;

    @Getter
    private User user;

    @PostConstruct
    void init() {
        String jwtHeader = request.getHeader("Authentication");
        user = apiAuthBean.getUser((StringUtils.isNotBlank(jwtHeader)) ? jwtHeader : "");
    }

    public Long getUserId() {
        return (user != null) ? user.getId() : null;
    }

    /**
     * Logs in the given User if credentials are right.
     * @return A JWT for that User
     */
    public @Nullable String loginAndGetJWT(String username, String password) {
        Optional<User> userOptional = userDao.loginUser(username, password);
        if (userOptional.isPresent()) {
            this.user = userOptional.get();
            log.info("Logged in User: {}", user.getEmail());
            return apiAuthBean.createJWT(userOptional.get().getId());
        }
        return null;
    }
}
