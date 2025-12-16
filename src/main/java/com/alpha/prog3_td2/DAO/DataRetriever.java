package com.alpha.prog3_td2.DAO;
import com.alpha.prog3_td2.model.ContinentEnum;
import com.alpha.prog3_td2.model.Player;
import com.alpha.prog3_td2.model.PositionEnum;
import com.alpha.prog3_td2.model.Team;
import com.alpha.prog3_td2.util.databaseConnection;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
public class DataRetriever {
    public Team findTeamById(Integer id){
        throw new RuntimeException("Does not implemented");
    }
    public List<Player> findPlayers(int page, int size){
        throw new RuntimeException("Does not implemented");
    }
    public List<Player>  createPlayers(List<Player>  newPlayers){
        throw new RuntimeException("Does not implemented");
    }
    public  Team  saveTeam(Team  teamToSave){
        throw new RuntimeException("Does not implemented");
    }
    public  List<Team>  findTeamsByPlayerName(String  playerName){
        throw new RuntimeException("Does not implemented");
    }
    public
    List<Player>
    findPlayersByCriteria(String playerName, PositionEnum position, String  teamName, ContinentEnum continent, int  page, int  size){
        throw new RuntimeException("Does not implemented")
    }
}
