package app.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class ServerJava {

    private final Javalin app;
    private static final Logger logger = LoggerFactory.getLogger(ServerJava.class);
    private static final int MAX_TEAM_SIZE = 5;
    private static final Set<String> allChampions = new HashSet<>();
    private static boolean gameStarted = false;
    private static final Map<String, List<ChampionDistribution>> teams = new HashMap<>();

    public ServerJava() {
        // TODO : compléter les endpoints pour que les tests passent au vert !
        app = Javalin.create()
                .get("/api/status", ctx -> {
                    logger.debug("Status handler triggered", ctx);
                    ctx.status(200);
                    ctx.json("Hello World");
                })
                // USE CASE 1.1 - Création de champion
                // Endpoint de création de champion corrigé
                .post("/api/create", ctx -> {
                    try {
                        // Utiliser Jackson ObjectMapper pour la désérialisation
                        ObjectMapper objectMapper = new ObjectMapper();
                        List<Champion> champions = objectMapper.readValue(ctx.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Champion.class));

                        // Vérifier que la liste n'est pas vide
                        if (champions == null || champions.isEmpty()) {
                            ctx.status(400).result("Champion list is empty.");
                            return;
                        }

                        // Itérer sur chaque champion pour validation et ajout
                        for (Champion champion : champions) {
                            // Validation des données du champion
                            if (!validateChampion(champion)) {
                                ctx.status(400).result("Invalid champion data for champion: " + champion.getChampionName());
                                return;
                            }

                            // Vérifier si le champion existe déjà
                            if (Champion.pokemons.stream().anyMatch(c -> c.getChampionName().equalsIgnoreCase(champion.getChampionName()))) {
                                ctx.status(400).result("Champion already exists: " + champion.getChampionName());
                                return;
                            }

                            // Normaliser le nom du champion
                            champion.setChampionName(champion.capitalize(champion.getChampionName()));

                            // Ajouter le champion à la liste
                            champion.addToList();
                        }

                        ctx.status(200).result("Champions created successfully.");
                    } catch (Exception e) {
                        ctx.status(400).result("Error processing request: " + e.getMessage());
                    }
                })

        // USE CASE 1.2 - Modification de champion
                .post("/api/modify", ctx -> {
                    try {
                        Champion updateRequest = ctx.bodyAsClass(Champion.class);
                        Optional<Champion> existingChampion = Champion.pokemons.stream()
                                .filter(c -> c.getChampionName().equalsIgnoreCase(updateRequest.getChampionName()))
                                .findFirst();

                        if (existingChampion.isEmpty()) {
                            ctx.status(404).result("Champion not found.");
                            return;
                        }

                        Champion championToUpdate = existingChampion.get();

                        // Mettre à jour les points de vie et le rôle
                        if (updateRequest.getLifePoints() > 0) {
                            championToUpdate.setLifePoints(updateRequest.getLifePoints());
                        }
                        if (updateRequest.getChampionType() != null) {
                            championToUpdate.setChampionType(updateRequest.getChampionType());
                        }

                        // Ajouter de nouvelles compétences
                        if (updateRequest.getAbilities() != null && !updateRequest.getAbilities().isEmpty()) {
                            for (Ability newAbility : updateRequest.getAbilities()) {
                                if (championToUpdate.getAbilities().stream().noneMatch(a -> a.getAbilityName().equals(newAbility.getAbilityName()))) {
                                    championToUpdate.getAbilities().add(newAbility);
                                }
                            }
                        }

                        ctx.status(200).result("Champion updated successfully.");
                    } catch (Exception e) {
                        ctx.status(404).result("Error processing request: " + e.getMessage());
                    }
                })

                // USE CASE 2 - Remplissage de l'équipe
                .post("/api/team", ctx -> {
                    try {
                        Team team = ctx.bodyAsClass(Team.class);
                        if (team.getChampionsDistribution().size() > MAX_TEAM_SIZE) {
                            ctx.status(400).result("A team cannot have more than 5 Champions.");
                            return;
                        }

                        // Vérifier les doublons, la casse et l'existence des champions
                        Set<String> teamChampions = new HashSet<>();
                        for (ChampionDistribution distribution : team.getChampionsDistribution()) {
                            String championName = distribution.getChampionName().toLowerCase();

                            // Vérifier l'existence du champion
                            boolean championExists = Champion.pokemons.stream()
                                    .anyMatch(c -> c.getChampionName().equalsIgnoreCase(distribution.getChampionName()));

                            if (!championExists) {
                                ctx.status(400).result("Champion " + distribution.getChampionName() + " does not exist.");
                                return;
                            }

                            // Vérifier les doublons dans l'équipe
                            if (!teamChampions.add(championName)) {
                                ctx.status(400).result("Duplicate champions in the team.");
                                return;
                            }

                            // Vérifier si le champion est déjà dans une autre équipe
                            if (!allChampions.add(championName)) {
                                ctx.status(400).result("Champion " + distribution.getChampionName() + " already in another team.");
                                return;
                            }
                        }

                        // Ajouter l'équipe à la liste des équipes
                        teams.put(team.getTeamName(), team.getChampionsDistribution());

                        ctx.status(200).result("Team " + team.getTeamName() + " created successfully.");
                    } catch (Exception e) {
                        ctx.status(400).result("Error processing request: " + e.getMessage());
                    }
                })


                // USE CASE 3 - Lancement de la partie
                .get("/api/begin", ctx -> {
                    if (!gameStarted && allChampions.size() == 10) { // Assuming 2 teams of 5 champions each
                        gameStarted = true;
                        ctx.status(200).result("Bienvenue sur la Faille de l'Invocateur !");
                    } else {
                        ctx.status(400).result("The game cannot begin. Please check the number of Champions in each team.");
                    }
                })

                .get("/api/prediction", ctx -> {
                    if (!gameStarted) {
                        ctx.status(400).result("The game has not started yet.");
                        return;
                    }

                    List<LaneResult> results = new ArrayList<>();
                    for (Lane lane : Lane.values()) {
                        String winningTeam = predictLaneWinner(lane);
                        results.add(new LaneResult(lane.name(), winningTeam));
                    }

                    ctx.status(200).json(results);
                });

        // USE CASE 4 - Rechercher les champions par lane


        ;
    }

    private String predictLaneWinner(Lane lane) {
        List<ChampionDistribution> blueTeam = teams.getOrDefault("BLEU", Collections.emptyList());
        List<ChampionDistribution> redTeam = teams.getOrDefault("ROUGE", Collections.emptyList());

        int blueTeamDamage = calculateTeamDamage(blueTeam, lane);
        int redTeamDamage = calculateTeamDamage(redTeam, lane);

        if (blueTeamDamage > redTeamDamage) {
            return "BLEU";
        } else if (redTeamDamage > blueTeamDamage) {
            return "ROUGE";
        } else {
            return "NONE";
        }
    }

    private int calculateTeamDamage(List<ChampionDistribution> team, Lane lane) {
        return team.stream()
                .filter(dist -> dist.getLane() == lane)
                .mapToInt(dist -> {
                    Optional<Champion> champion = Champion.pokemons.stream()
                            .filter(c -> c.getChampionName().equalsIgnoreCase(dist.getChampionName()))
                            .findFirst();

                    return champion.map(c -> c.getAbilities().stream()
                            .mapToInt(Ability::getDamage)
                            .max()
                            .orElse(0)).orElse(0);
                }).sum();
    }


    private boolean validateChampion(Champion pokemon) {
        if (pokemon == null) {
            return false;
        }
        if (pokemon.getChampionName() == null || pokemon.getChampionName().isEmpty()) {
            return false;
        }
        if (pokemon.getChampionType() == null) {
            return false;
        }
        if (pokemon.getLifePoints() < 0) {
            return false;
        }
        return pokemon.getAbilities() != null && !pokemon.getAbilities().isEmpty();
    }

    public Javalin javalinApp() {
        return app;
    }
}

