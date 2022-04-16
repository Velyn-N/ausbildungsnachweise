package de.nmadev.ausbildungsnachweise;

import io.quarkus.runtime.Startup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@ApplicationScoped @Startup
public class JsonStorageFileManager {

    @ConfigProperty(name = "nachweise.storage.dir")
    String storageDir;
    @Inject
    CustomGson gson;

    Map<DataFile, File> dataFileFileMap = new HashMap<>();

    /**
     * Loads all Files into Cache and checks for Read & Write Permissions
     */
    @PostConstruct
    public void preloadFiles() {
        for (DataFile df : DataFile.values()) {
            File file = new File(storageDir + df.filename);

            if (!file.exists()) {
                try {
                    boolean created = file.createNewFile();
                    if (created) {
                        log.info("Created new Storage File: {}", file.getPath());
                    }
                } catch (IOException e) {
                    log.error("Tried to create File {}, but failed! Error: {}", file.getPath(), e.getMessage());
                }
            }
            if (!file.canRead()) {
                log.error("Can not read File: {}", file.getPath());
            }
            if (!file.canWrite()) {
                log.error("Can not write File: {}", file.getPath());
            }
            dataFileFileMap.put(df, file);
        }
    }

    /**
     * Reads the specified File and parses its Data using Gson.
     * @param target the File
     * @param clazz the Data's Type
     * @param <T> the Data's Type
     * @return parsed File contents
     */
    public <T> List<T> loadDataListFromFile(DataFile target, Class<T[]> clazz) {
        String fileContent = readFile(dataFileFileMap.get(target));
        T[] arr = gson.fromJson(fileContent, clazz);
        return (arr != null) ? Arrays.asList(arr) : new ArrayList<>();
    }

    /**
     * Parses the given Data using Gson and writes it to the specified File.
     * @param target the File
     * @param data the Data
     * @param <T> the Data's Type
     */
    public <T> void updateDataFile(DataFile target, T data) {
        writeFile(dataFileFileMap.get(target), gson.toJson(data));
    }

    private void writeFile(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Could not access File for writing: {}, Error: {}", file.getPath(), e.getMessage());
        }
    }

    private String readFile(File file) {
        try {
            return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            log.error("Could not access File for reading: {}, Error: {}", file.getPath(), e.getMessage());
        } catch (IOException e) {
            return "";
        }
        return "";
    }

    public enum DataFile {
        USERS("/users.json"),
        ACTIVITIES("/activities.json"),
        NACHWEISE("/nachweise.json");

        public String filename;

        DataFile(String filename) {
            this.filename = filename;
        }
    }
}
