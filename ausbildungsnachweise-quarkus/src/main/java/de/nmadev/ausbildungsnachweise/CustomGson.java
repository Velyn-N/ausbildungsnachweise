package de.nmadev.ausbildungsnachweise;

import com.google.gson.*;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CustomGson {
    @Getter
    private Gson gson;

    public CustomGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                                ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString())
                                        .toLocalDateTime())
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME)))
                .setPrettyPrinting()
                .create();
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
