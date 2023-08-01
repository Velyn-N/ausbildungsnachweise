package de.nmadev.ausbildungsnachweise;

import com.google.gson.*;
import lombok.Getter;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CustomGson {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Getter
    private final Gson gson;

    public CustomGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                                LocalDate.parse(json.getAsJsonPrimitive().getAsString(), dateFormatter))
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                new JsonPrimitive(src.format(dateFormatter)))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) ->
                                LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), dateTimeFormatter))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                                new JsonPrimitive(src.format(dateTimeFormatter)))
//                .setPrettyPrinting()
                .create();
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
