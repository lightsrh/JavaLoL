package app.utils;

public class LaneResult {
    private String laneName;
    private String winningTeam;

    public LaneResult(String laneName, String winningTeam) {
        this.laneName = laneName;
        this.winningTeam = winningTeam;
    }

    public String getLaneName() {
        return laneName;
    }

    public void setLaneName(String laneName) {
        this.laneName = laneName;
    }

    public String getWinningTeam() {
        return winningTeam;
    }

    public void setWinningTeam(String winningTeam) {
        this.winningTeam = winningTeam;
    }
}
