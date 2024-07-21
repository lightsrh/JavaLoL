import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UseCase1_1IntegrationTest_Sarah {

    Javalin app = new ServerJava().javalinApp(); // inject any dependencies you might have
    String CREATE_URL = "/api/create";
    /**
     * Cas nominal pour l'use case 1 : pas de soucis, code retour 200
     */
    @Test
    void useCase1_IntegrationTest_nominalCase() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get("src/test/resources/datasets/add_several_champs.json")));
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.post(CREATE_URL, jsonContent).code()).isEqualTo(200);
        });
    }
}