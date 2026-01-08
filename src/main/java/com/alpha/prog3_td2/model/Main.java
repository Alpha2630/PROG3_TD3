package com.alpha.prog3_td2.model;

import com.alpha.prog3_td2.model.*;
import com.alpha.prog3_td2.model.DataRetriever;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(" TEST DE L'APPLICATION FOOTBALL ");

        try {
            DataRetriever retriever = new DataRetriever();

            System.out.println("\n1. Test connexion...");
            boolean connected = retriever.testConnection();
            if (connected) {
                System.out.println("Connexion à la base: OK");
            } else {
                System.out.println("Connexion à la base: ÉCHEC");
                return;
            }

            System.out.println("\n2. Test findTeamById...");
            Team realMadrid = retriever.findTeamById(1);

            if (realMadrid != null) {
                System.out.println("Équipe trouvée: " + realMadrid.getName());
                System.out.println("Continent: " + realMadrid.getContinent());
                System.out.println("Nombre de joueurs: " + realMadrid.getPlayers().size());

                System.out.println("\nJoueurs de " + realMadrid.getName() + ":");
                for (Player p : realMadrid.getPlayers()) {
                    String buts = (p.getGoalNb() != null) ? p.getGoalNb().toString() : "NULL";
                    System.out.println("- " + p.getName() + " (" + p.getPosition() +
                            "), buts: " + buts);
                }

                System.out.println("\n3. Test getPlayersGoals()...");
                try {
                    Integer totalButs = realMadrid.getPlayersGoals();
                    System.out.println("Total buts marqués: " + totalButs);
                } catch (RuntimeException e) {
                    System.out.println("Exception (attendu si joueur NULL): " + e.getMessage());
                }
            } else {
                System.out.println("Équipe non trouvée (ID 1)");
            }

            System.out.println("\n4. Test pagination (findPlayers)...");
            List<Player> joueursPage1 = retriever.findPlayers(1, 3);
            System.out.println("Page 1 (3 joueurs): " + joueursPage1.size() + " joueurs");

            if (!joueursPage1.isEmpty()) {
                System.out.println("Premier joueur: " + joueursPage1.get(0).getName());
            }

            System.out.println("\n5. Test findTeamsByPlayerName('an')...");
            List<Team> equipes = retriever.findTeamsByPlayerName("an");
            System.out.println("Équipes trouvées: " + equipes.size());
            for (Team t : equipes) {
                System.out.println("- " + t.getName());
            }

            System.out.println("\nTESTS TERMINÉS AVEC SUCCÈS");

        } catch (Exception e) {
            System.out.println("\n=== ERREUR ===");
            System.out.println("Type: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}