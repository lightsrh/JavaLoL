import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCase1_2IntegrationTest_Sarah {

    Javalin app = new ServerJava().javalinApp();
    String MODIFY_URL = "/api/modify";

    @Test
    void useCase1_2IntegrationTest_nominalCase() throws IOException {
        // Préparation du champion
        String createChampionJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/add_several_champs.json")));
        String modifyChampionJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/modify_champ.json")));

        JavalinTest.test(app, (server, client) -> {
            // Créer un champion
            client.post("/api/create", createChampionJson);

            // Modifier le champion
            assertThat(client.post(MODIFY_URL, modifyChampionJson).code()).isEqualTo(200);
        });
    }

    @Test
    void useCase1_2IntegrationTest_championNotFound() throws IOException {
        String modifyChampionJson = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/modify_nonexistent_champ.json")));

        JavalinTest.test(app, (server, client) -> {
            assertThat(client.post(MODIFY_URL, modifyChampionJson).code()).isEqualTo(400);
        });
    }
}
