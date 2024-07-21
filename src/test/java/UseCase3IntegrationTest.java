import app.utils.ServerJava;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static utils.LoadData.*;
import static utils.UrlPaths.BEGIN_GAME_URL;
import static utils.UrlPaths.CREATE_TEAM_URL;

class UseCase3IntegrationTest {

    Javalin app = new ServerJava().javalinApp(); // inject any dependencies you might have

    private static final String SRC_TEST_RESOURCES_DATASETS = "src/test/resources/datasets/teams/";

    private Javalin createNewApp() {
        return new ServerJava().javalinApp();
    }

    @Test
    void test_begin_game_noTeams() {
        Javalin app = createNewApp();

        JavalinTest.test(app, (server, client) -> {
            createChampionsAtInit(client);
            client.get(BEGIN_GAME_URL);
            assertThat(client.get(BEGIN_GAME_URL).code()).isEqualTo(400);
        });
    }

    @Test
    void test_begin_game_NotEnoughTeam() {

        JavalinTest.test(app, (server, client) -> {
            createChampionsAtInit(client);
            createOneTeam(client);
            assertThat(client.get(BEGIN_GAME_URL).code()).isEqualTo(400);
        });
    }

    @Test
    void test_begin_game_teamsAreNotFull() {

        JavalinTest.test(app, (server, client) -> {
            createChampionsAtInit(client);
            createInequalTeams(client);
            assertThat(client.get(BEGIN_GAME_URL).code()).isEqualTo(400);
        });
    }
}
