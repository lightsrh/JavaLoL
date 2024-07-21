import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCase2IntegrationTest_Sarah {

    Javalin app = new ServerJava().javalinApp();
    String CREATE_TEAM_URL = "/api/team";



    @Test
    void useCase2IntegrationTest_nominalCase() throws IOException {
        String createChampionJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/add_several_champs.json")));
        String createTeamJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/create_team_OK.json")));


        JavalinTest.test(app, (server, client) -> {
            client.post("/api/create", createChampionJson);

            assertThat(client.post(CREATE_TEAM_URL, createTeamJson).code()).isEqualTo(200);
        });
    }

    @Test
    void useCase2IntegrationTest_teamTooBig() throws IOException {
        String createTeamJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/create_team_KO_too_big.json")));

        JavalinTest.test(app, (server, client) -> {
            assertThat(client.post(CREATE_TEAM_URL, createTeamJson).code()).isEqualTo(400);
        });
    }
}
