import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static utils.LoadData.createChampionsAtInit;
import static utils.LoadData.createTeamsAtInit;
import static utils.ResponseParser.isJsonEquals;
import static utils.ResponseParser.parseResponse;
import static utils.UrlPaths.BEGIN_GAME_URL;
import static utils.UrlPaths.PREDICT_WINNER_URL;

class UseCase4IntegrationTest {

    Javalin app = new ServerJava().javalinApp(); // inject any dependencies you might have

    private static final String SRC_TEST_RESOURCES_DATASETS = "src/test/resources/datasets/prediction/";


    @Test
    void test_prediction_ok() {

        JavalinTest.test(app, (server, client) -> {
            // GIVEN
            String prediction = new String(Files.readAllBytes(Paths.get(SRC_TEST_RESOURCES_DATASETS + "prediction.json")));
            createChampionsAtInit(client);
            createTeamsAtInit(client);
            client.get(BEGIN_GAME_URL);

            // WHEN
            Response response = client.get(PREDICT_WINNER_URL);
            assertThat(response.code()).isEqualTo(200);
            assertThat(parseResponse(response).getJSONObject(0).toString()).is(isJsonEquals(prediction));
        });
    }

    @Test
    void test_prediction_game_still_not_begun() {

        JavalinTest.test(app, (server, client) -> {
            createChampionsAtInit(client);
            createTeamsAtInit(client);

            assertThat(client.get(PREDICT_WINNER_URL).code()).isEqualTo(400);
        });
    }
}
