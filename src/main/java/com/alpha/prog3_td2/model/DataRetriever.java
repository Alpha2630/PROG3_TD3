package com.alpha.prog3_td2.model;
import com.alpha.prog3_td2.util.databaseConnection; 
import com.alpha.prog3_td2.model.*;
import com.alpha.prog3_td2.model.*; 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    private final databaseConnection dbConnection = new databaseConnection();

    public Team findTeamById(Integer id) throws SQLException {
        String teamSql = "SELECT * FROM Team WHERE id = ?";
        String playersSql = "SELECT * FROM Player WHERE id_team = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement teamStmt = conn.prepareStatement(teamSql)) {

            teamStmt.setInt(1, id);
            ResultSet teamRs = teamStmt.executeQuery();

            if (!teamRs.next()) {
                return null;
            }

            Team team = new Team();
            team.setId(teamRs.getInt("id"));
            team.setName(teamRs.getString("name"));
            team.setContinent(ContinentEnum.valueOf(teamRs.getString("continent")));
            team.setPlayers(new ArrayList<>());

            try (PreparedStatement playersStmt = conn.prepareStatement(playersSql)) {
                playersStmt.setInt(1, id);
                ResultSet playersRs = playersStmt.executeQuery();

                while (playersRs.next()) {
                    Player player = new Player();
                    player.setId(playersRs.getInt("id"));
                    player.setName(playersRs.getString("name"));
                    player.setAge(playersRs.getInt("age"));
                    player.setPosition(PositionEnum.valueOf(playersRs.getString("position")));
                    player.setTeam(team);
                    player.setGoalNb(playersRs.getObject("goal_nb", Integer.class)); 
                    team.getPlayers().add(player);
                }
            }
            return team;
        }
    }

    public List<Player> findPlayers(int page, int size) throws SQLException {
        List<Player> players = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT p.*, t.id as team_id, t.name as team_name, t.continent as team_continent " +
                "FROM Player p " +
                "LEFT JOIN Team t ON p.id_team = t.id " +
                "ORDER BY p.id LIMIT ? OFFSET ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player();
                    player.setId(rs.getInt("id"));
                    player.setName(rs.getString("name"));
                    player.setAge(rs.getInt("age"));
                    player.setPosition(PositionEnum.valueOf(rs.getString("position")));
                    player.setGoalNb(rs.getObject("goal_nb", Integer.class));

                    Integer teamId = rs.getObject("team_id", Integer.class);
                    if (teamId != null) {
                        Team team = new Team();
                        team.setId(teamId);
                        team.setName(rs.getString("team_name"));
                        team.setContinent(ContinentEnum.valueOf(rs.getString("team_continent")));
                        player.setTeam(team);
                    }

                    players.add(player);
                }
            }
        }
        return players;
    }

    public List<Player> createPlayers(List<Player> newPlayers) throws SQLException {
        if (newPlayers == null || newPlayers.isEmpty()) {
            return new ArrayList<>();
        }

        Connection conn = null;
        List<Player> createdPlayers = new ArrayList<>();

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);

            String checkSql = "SELECT COUNT(*) FROM Player WHERE id = ?";
            String insertSql = "INSERT INTO Player (id, name, age, position, id_team, goal_nb) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

                for (Player player : newPlayers) {
                    checkStmt.setInt(1, player.getId());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        conn.rollback();
                        throw new RuntimeException("Le joueur avec l'id " + player.getId() + " existe déjà");
                    }

                    insertStmt.setInt(1, player.getId());
                    insertStmt.setString(2, player.getName());
                    insertStmt.setInt(3, player.getAge());
                    insertStmt.setString(4, player.getPosition().name());

                    if (player.getTeam() != null) {
                        insertStmt.setInt(5, player.getTeam().getId());
                    } else {
                        insertStmt.setNull(5, Types.INTEGER);
                    }

                    if (player.getGoalNb() != null) {
                        insertStmt.setInt(6, player.getGoalNb());
                    } else {
                        insertStmt.setNull(6, Types.INTEGER);
                    }

                    insertStmt.addBatch();
                    createdPlayers.add(player);
                }

                insertStmt.executeBatch();
                conn.commit();
                return createdPlayers;

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
               
                }
            }
        }
    }

    public Team saveTeam(Team teamToSave) throws SQLException {
        Connection conn = null;

        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);


            String checkSql = "SELECT id FROM Team WHERE id = ?";
            boolean teamExists = false;

            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, teamToSave.getId());
                ResultSet rs = checkStmt.executeQuery();
                teamExists = rs.next();
            }

            if (teamExists) {
                String updateSql = "UPDATE Team SET name = ?, continent = ? WHERE id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, teamToSave.getName());
                    updateStmt.setString(2, teamToSave.getContinent().name());
                    updateStmt.setInt(3, teamToSave.getId());
                    updateStmt.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO Team (id, name, continent) VALUES (?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, teamToSave.getId());
                    insertStmt.setString(2, teamToSave.getName());
                    insertStmt.setString(3, teamToSave.getContinent().name());
                    insertStmt.executeUpdate();
                }
            }


            if (teamToSave.getPlayers() != null) {
    
                String dissociateSql = "UPDATE Player SET id_team = NULL WHERE id_team = ?";
                try (PreparedStatement dissociateStmt = conn.prepareStatement(dissociateSql)) {
                    dissociateStmt.setInt(1, teamToSave.getId());
                    dissociateStmt.executeUpdate();
                }

                if (!teamToSave.getPlayers().isEmpty()) {
                    String updatePlayerSql = "UPDATE Player SET id_team = ?, goal_nb = ? WHERE id = ?";
                    try (PreparedStatement updatePlayerStmt = conn.prepareStatement(updatePlayerSql)) {
                        for (Player player : teamToSave.getPlayers()) {
                            if (player != null) {
                                updatePlayerStmt.setInt(1, teamToSave.getId());

                                if (player.getGoalNb() != null) {
                                    updatePlayerStmt.setInt(2, player.getGoalNb());
                                } else {
                                    updatePlayerStmt.setNull(2, Types.INTEGER);
                                }

                                updatePlayerStmt.setInt(3, player.getId());
                                updatePlayerStmt.addBatch();
                            }
                        }
                        updatePlayerStmt.executeBatch();
                    }
                }
            }

            conn.commit();
            return teamToSave;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public List<Team> findTeamsByPlayerName(String playerName) throws SQLException {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT DISTINCT t.* FROM Team t " +
                "INNER JOIN Player p ON t.id = p.id_team " +
                "WHERE LOWER(p.name) LIKE LOWER(?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + playerName + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Team team = new Team();
                    team.setId(rs.getInt("id"));
                    team.setName(rs.getString("name"));
                    team.setContinent(ContinentEnum.valueOf(rs.getString("continent")));
                    teams.add(team);
                }
            }
        }
        return teams;
    }


    public boolean testConnection() {
        try (Connection conn = dbConnection.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}