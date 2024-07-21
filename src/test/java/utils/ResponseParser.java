package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.assertj.core.api.Condition;
import org.json.JSONArray;

import java.io.IOException;

public class ResponseParser {

    public static JSONArray parseResponse(Response response) throws IOException {
        assert response.body() != null;
        return new JSONArray(response.body().string());
    }

    public static Condition<String> isJsonEquals(String s) {
        ObjectMapper mapper = new ObjectMapper();
        return new Condition<>(value -> {
            try {
                return mapper.readTree(value).equals(mapper.readTree(s));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }, "JSON are not equals ");
    }
}
