package app.utils;

import java.util.ArrayList;
import java.util.List;

public class Champion {
    static List<Champion> pokemons = new ArrayList<>();

    private String championName;
    private Type championType;
    private int lifePoints;
    private List<Ability> abilities;

    public Champion() {
    }

    public void addToList() {
        pokemons.add(this);
    }

    public boolean create() {
        if (pokemons.contains(this)) {
            return false; // Champion already exists
        }
        addToList();
        return true;
    }

    public String getChampionName() {
        return championName;
    }

    public void setChampionName(String championName) {
        this.championName = capitalize(championName);
    }

    public Type getChampionType() {
        return championType;
    }

    public void setChampionType(Type championType) {
        this.championType = championType;
    }

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}
