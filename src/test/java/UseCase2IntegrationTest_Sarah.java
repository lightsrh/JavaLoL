import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utils.LoadData.createChampionsAtInit;
import static utils.UrlPaths.CREATE_TEAM_URL;

class UseCase2IntegrationTest_Sarah {

    Javalin app = new ServerJava().javalinApp();
    String CREATE_TEAM_URL = "/api/team";
    private static final String SRC_TEST_RESOURCES_DATASETS = "src/test/resources/datasets/teams/";
    private Javalin createNewApp() {
        return new ServerJava().javalinApp();
    }

    @Test
    void useCase2IntegrationTest_teamTooBig() throws IOException {
        String createTeamJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/create_team_KO_too_big.json")));

        JavalinTest.test(app, (server, client) -> {
            assertThat(client.post(CREATE_TEAM_URL, createTeamJson).code()).isEqualTo(400);
        });
    }
}
