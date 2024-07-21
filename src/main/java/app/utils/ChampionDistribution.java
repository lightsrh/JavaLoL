package app.utils;

public class ChampionDistribution {
    private String championName;
    private Lane lane;

    public ChampionDistribution() {}

    public ChampionDistribution(String championName, Lane lane) {
        this.championName = championName;
        this.lane = lane;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = championName;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }
}
