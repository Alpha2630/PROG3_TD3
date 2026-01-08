package com.alpha.prog3_td2.model;

import java.util.List;
import java.util.Objects;

public class Team {
    private int id;
    private String name;
    private ContinentEnum continent;
    private List<Player> players;
    public Team() {
    }

    public Team(int id, String name, ContinentEnum continent, List<Player> players) {
        this.id = id;
        this.name = name;
        this.continent = continent;
        this.players = players;
    }
    public Integer getPlayersCount() {
        return (players != null) ? players.size() : 0;
    }
    public Integer getPlayersGoals() {
        if (players == null) {
            return 0;
        }

        int total = 0;

        for (Player player : players) {
            if (player.getGoalNb() == null) {
                throw new RuntimeException(
                        "Nombre de buts du joueur " + player.getName() + " inconnu"
                );
            }
            total += player.getGoalNb();
        }

        return total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContinentEnum getContinent() {
        return continent;
    }

    public void setContinent(ContinentEnum continent) {
        this.continent = continent;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}