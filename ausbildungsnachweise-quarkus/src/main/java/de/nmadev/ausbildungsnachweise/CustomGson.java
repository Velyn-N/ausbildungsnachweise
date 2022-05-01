package de.nmadev.ausbildungsnachweise;

import com.google.gson.*;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class CustomGson {
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Getter
    private Gson gson;

    public CustomGson() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                                LocalDate.parse(json.getAsJsonPrimitive().getAsString(), dateFormatter))
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                                new JsonPrimitive(src.format(dateFormatter)))
                .setPrettyPrinting()
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
