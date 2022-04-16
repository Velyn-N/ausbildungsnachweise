package de.nmadev.ausbildungsnachweise.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.nmadev.ausbildungsnachweise.JsonStorageFileManager;
import de.nmadev.ausbildungsnachweise.entity.JsonEntity;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public abstract class FileDao<T extends JsonEntity> {

    @Inject
    JsonStorageFileManager fileManager;

    private Cache<Long, T> cache = CacheBuilder.newBuilder().build();

    @PostConstruct
    void init() {
        reloadCacheFromFile();
    }

    /**
     * Invalidates the Cache and reloads it from file.
     */
    protected void reloadCache() {
        reloadCacheFromFile();
    }



    /**
     * Gets the identified Entity if present.
     * @param id the identifier
     * @return an {@link Optional}
     */
    protected Optional<T> getById(Long id) {
        T entity = cache.getIfPresent(id);
        if (entity != null) return Optional.of(entity);

        reloadCacheFromFile();
        return Optional.ofNullable(cache.getIfPresent(id));
    }

    /**
     * Gets all Entities present in the Cache
     * @return Collection of Entities
     */
    protected Collection<T> getAllEntities() {
        return cache.asMap().values();
    }

    /**
     * Method for simple filtering of all existing Entities. <br>
     * Load scales with Number of Entities <br>
     * <br>
     * Wrapper for {@link java.util.stream.Stream#filter(Predicate)}
     * @param predicate predicate to filter with
     * @return a List of filtered Entities
     */
    protected List<T> getEntitiesMatching(Predicate<T> predicate) {
        return cache.asMap()
                .values()
                .stream()
                .filter(predicate)
                .toList();
    }



    /**
     * Saves a given Entity by merging it into an existing one or creating a new entry in the storage.
     * @param entity the entity to save
     * @return if the operation was successful
     */
    protected boolean saveEntity(T entity) {
        boolean success = handleEntitySave(entity);
        updateFile();
        return success;
    }

    protected void saveEntityList(List<T> entityList) {
        entityList.forEach(this::handleEntitySave);
        updateFile();
    }

    private boolean handleEntitySave(T entity) {
        if (entity == null || !entity.isValid()) return false;

        if (entity.getId() == null) {
            Optional<Long> highestId = cache.asMap().keySet().stream().max(Comparator.comparingLong(Long::longValue));
            entity.setId(highestId.map(aLong -> (aLong + 1)).orElse(1L));
        }
        cache.put(entity.getId(), entity);
        return true;
    }



    private void updateFile() {
        fileManager.updateDataFile(getDataFile(), cache.asMap().values());
    }

    private void reloadCacheFromFile() {
        cache.invalidateAll();

        List<T> list = fileManager.loadDataListFromFile(getDataFile(), getArrayType());
        list.forEach(t -> cache.put(t.getId(), t));
    }

    protected abstract JsonStorageFileManager.DataFile getDataFile();
    protected abstract Class<T[]> getArrayType();
}
