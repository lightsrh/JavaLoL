package app.utils;

import java.util.List;

public class Team {
    private String teamName;
    private List<ChampionDistribution> championsDistribution;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<ChampionDistribution> getChampionsDistribution() {
        return championsDistribution;
    }

    public void setChampionsDistribution(List<ChampionDistribution> championsDistribution) {
        this.championsDistribution = championsDistribution;
    }
}
