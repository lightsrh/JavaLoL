import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCase3IntegrationTest_Sarah {

    Javalin app = new ServerJava().javalinApp();
    String BEGIN_URL = "/api/begin";
    String TEAM1_JSON = "src/test/resources/datasets/create_team_OK.json";
    String TEAM2_JSON = "src/test/resources/datasets/create_team_OK_2.json";


    @Test
    void useCase3IntegrationTest_nominalCase() throws IOException {
        // Crée deux équipes
        String createChampionJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/add_several_champs.json")));

        String createTeam1Json = new String(Files.readAllBytes(Paths.get(TEAM1_JSON)));
        String createTeam2Json = new String(Files.readAllBytes(Paths.get(TEAM2_JSON)));

        JavalinTest.test(app, (server, client) -> {
            client.post("/api/create", createChampionJson);

            client.post("/api/team", createTeam1Json);
            client.post("/api/team", createTeam2Json);

            // Lancer la partie
            assertThat(client.get(BEGIN_URL).code()).isEqualTo(200);
        });
    }

    @Test
    void useCase3IntegrationTest_notEnoughChampions() throws IOException {
        String createTeam1Json = new String(Files.readAllBytes(Paths.get(TEAM1_JSON)));
        String createTeam2Json = new String(Files.readAllBytes(Paths.get(TEAM2_JSON)));


        JavalinTest.test(app, (server, client) -> {
            client.post("/api/team", createTeam1Json);

            // Lancer la partie sans la deuxième équipe
            assertThat(client.get(BEGIN_URL).code()).isEqualTo(400);
            client.post("/api/team", createTeam2Json);
            client.post("/api/begin");
        });
    }
}
