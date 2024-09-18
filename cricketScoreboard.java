import java.util.InputMismatchException;
import java.util.Scanner;

class Team {
    String name;
    Player[] players;
    int numPlayers;

    Team(String name, int numPlayers) {
        this.name = name;
        this.numPlayers = numPlayers;
        this.players = new Player[numPlayers];
    }
}

class Player {
    String name;
    String role;
    int individualScore;
    int ballsFaced;
    int ballsBowled;
    int wicketsTaken;

    Player(String name, String role) {
        this.name = name;
        this.role = role;
        this.individualScore = 0;
        this.ballsFaced = 0;
        this.ballsBowled = 0;
        this.wicketsTaken = 0;
    }

    double getStrikeRate() {
        return ballsFaced == 0 ? 0 : (individualScore * 100.0) / ballsFaced;
    }
}

class Match {
    Team team1;
    Team team2;
    int overs;
    int currentOver;
    int scoreTeam1;
    int scoreTeam2;
    int wicketsTeam1;
    int wicketsTeam2;
    Player batsmanOnStrike;
    Player batsmanNonStrike;
    Player bowler;
    int ballsInOver;
    int[] bowlerOvers;
    boolean isTeam1Batting;
    Player[] battingOrderTeam1;
    Player[] battingOrderTeam2;
    String fieldingStrategy;

    Match(Team team1, Team team2, int overs, boolean isTeam1Batting) {
        this.team1 = team1;
        this.team2 = team2;
        this.overs = overs;
        this.currentOver = 0;
        this.scoreTeam1 = 0;
        this.scoreTeam2 = 0;
        this.wicketsTeam1 = 0;
        this.wicketsTeam2 = 0;
        this.batsmanOnStrike = isTeam1Batting ? team1.players[0] : team2.players[0];
        this.batsmanNonStrike = isTeam1Batting ? team1.players[1] : team2.players[1];
        this.bowler = isTeam1Batting ? team2.players[0] : team1.players[0];
        this.ballsInOver = 0;
        this.bowlerOvers = new int[team2.numPlayers];
        this.isTeam1Batting = isTeam1Batting;
        this.battingOrderTeam1 = team1.players.clone();
        this.battingOrderTeam2 = team2.players.clone();
        this.fieldingStrategy = "Balanced"; // Default strategy
    }
}

public class cricketScoreboard {
    Team[] teams;
    int numTeams;
    Scanner scanner;

    cricketScoreboard() {
        this.teams = new Team[10];
        this.numTeams = 0;
        this.scanner = new Scanner(System.in);
    }

    void displayMenu() {
        System.out.println("1. View Teams");
        System.out.println("2. Add Team");
        System.out.println("3. Start Match");
        System.out.println("4. Exit");
    }

    void viewTeams() {
        if (numTeams == 0) {
            System.out.println("No teams available.");
        } else {
            for (int i = 0; i < numTeams; i++) {
                System.out.println("Team " + (i + 1) + ": " + teams[i].name);
            }
        }
    }

    void addTeam() {
        System.out.print("Enter team name: ");
        String teamName = scanner.nextLine();
        int numPlayers = 0;
        while (true) {
            System.out.print("Enter number of players (at least 11): ");
            try {
                numPlayers = scanner.nextInt();
                scanner.nextLine(); // consume newline left-over

                if (numPlayers >= 11) {
                    break;
                } else {
                    System.out.println("Error: Please enter at least 11 players.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input. Please enter a numeric value.");
                scanner.nextLine(); // consume the invalid input
            }
        }

        Team team = new Team(teamName, numPlayers);
        System.out.println("Enter player details:");
        System.out.println("------------------------");
        System.out.println("| Player # | Name         | Role         |");
        System.out.println("------------------------");

        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String playerName = scanner.nextLine();
            System.out.print("Enter player " + (i + 1) + " role: ");
            String playerRole = scanner.nextLine();

            team.players[i] = new Player(playerName, playerRole);
            System.out.println("| " + (i + 1) + "         | " + playerName + " | " + playerRole + " |");
        }

        System.out.println("------------------------");

        teams[numTeams] = team;
        numTeams++;

        System.out.print("Do you want to add another team? (yes/no): ");
        String response = scanner.nextLine();
        if (response.equalsIgnoreCase("yes")) {
            addTeam();
        }
    }

    public void startMatch() {
        if (numTeams < 2) {
            System.out.println("At least 2 teams are required to start a match.");
        } else {
            System.out.print("Enter team 1 name: ");
            String team1Name = scanner.nextLine();
            System.out.print("Enter team 2 name: ");
            String team2Name = scanner.nextLine();
    
            Team team1 = null;
            Team team2 = null;
            for (int i = 0; i < numTeams; i++) {
                if (teams[i].name.equals(team1Name)) {
                    team1 = teams[i];
                }
                if (teams[i].name.equals(team2Name)) {
                    team2 = teams[i];
                }
            }
    
            if (team1 == null || team2 == null) {
                System.out.println("One or both teams not found.");
            } else {
                System.out.print("Enter number of overs: ");
                int overs = scanner.nextInt();
                scanner.nextLine(); // consume newline left-over
    
                System.out.print("Enter the team that won the toss: ");
                String tossWinningTeamName = scanner.nextLine();
                Team tossWinningTeam = null;
                Team tossLosingTeam = null;
                boolean isTeam1Batting = false;
    
                if (team1.name.equals(tossWinningTeamName)) {
                    tossWinningTeam = team1;
                    tossLosingTeam = team2;
                    System.out.println("Team " + tossLosingTeam.name + " is batting second.");
                } else if (team2.name.equals(tossWinningTeamName)) {
                    tossWinningTeam = team2;
                    tossLosingTeam = team1;
                    System.out.println("Team " + tossLosingTeam.name + " is batting second.");
                } else {
                    System.out.println("Invalid team name.");
                    return;
                }
    
                System.out.print("Does the toss-winning team choose to bat or bowl? (bat/bowl): ");
                String choice = scanner.nextLine();
                if (choice.equalsIgnoreCase("bat")) {
                    isTeam1Batting = tossWinningTeam == team1;
                } else if (choice.equalsIgnoreCase("bowl")) {
                    isTeam1Batting = tossWinningTeam != team1;
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
    
                System.out.print("Choose fielding strategy (Aggressive/Defensive/Balanced): ");
                String fieldingStrategy = scanner.nextLine();
                Match match = new Match(team1, team2, overs, isTeam1Batting);
                match.fieldingStrategy = fieldingStrategy;
                playMatch(match);
    
                if (match.wicketsTeam1 < 10 && match.currentOver < match.overs) {
                    System.out.println("First inning over. Team 2 needs " + (match.scoreTeam1 + 1) + " runs to win.");
                    match.isTeam1Batting = false;
                    match.currentOver = 0;
                    match.ballsInOver = 0;
                    match.batsmanOnStrike = match.battingOrderTeam1[0];
                    match.batsmanNonStrike = match.battingOrderTeam1[1];
                    match.bowler = match.team1.players[0];
                    playMatch(match);
                    playSecondInning(match);
                }
    
                displayResults(match);
            }
        }
    }
    
    void playMatch(Match match) {
        int totalBalls = 0;
        int totalWickets = 0;

        while (match.currentOver < match.overs && (match.isTeam1Batting ? match.wicketsTeam1 : match.wicketsTeam2) < 10) {
            System.out.println("Over " + (match.currentOver + 1));
            System.out.print("Enter the name of the bowler for this over: ");
            String bowlerName = scanner.nextLine();
            match.bowler = getPlayerByName(bowlerName, match.isTeam1Batting ? match.team2 : match.team1);
            match.ballsInOver = 0;
            while (match.ballsInOver < 6) {
                System.out.println("Batsman on strike: " + match.batsmanOnStrike.name);
                System.out.println("Batsman on non-strike: " + match.batsmanNonStrike.name);

                System.out.print("Enter type of delivery (1. Regular Ball, 2. Wide Ball, 3. No Ball, 4. Leg Byes, 5. Byes, 6. Wicket): ");
                int deliveryType = scanner.nextInt();
                scanner.nextLine(); // consume newline left-over

                if (deliveryType == 1) {
                    System.out.print("Enter number of runs scored: ");
                    int runsScored = scanner.nextInt();
                    scanner.nextLine(); // consume newline left-over

                    if (match.isTeam1Batting) {
                        match.scoreTeam1 += runsScored;
                        match.batsmanOnStrike.individualScore += runsScored;
                        match.batsmanOnStrike.ballsFaced++;
                        System.out.println("Score: " + match.scoreTeam1 + "/" + match.wicketsTeam1);
                    } else {
                        match.scoreTeam2 += runsScored;
                        match.batsmanOnStrike.individualScore += runsScored;
                        match.batsmanOnStrike.ballsFaced++;
                        System.out.println("Score: " + match.scoreTeam2 + "/" + match.wicketsTeam2);
                        System.out.println("Team 2 needs " + (match.scoreTeam1 + 1 - match.scoreTeam2) + " runs to win.");
                    }
                    match.ballsInOver++;
                    totalBalls++;
                    rotateStrike(runsScored, match);
                } else if (deliveryType == 2 || deliveryType == 3) {
                    if (match.isTeam1Batting) {
                        match.scoreTeam1++;
                        System.out.println("Score: " + match.scoreTeam1 + "/" + match.wicketsTeam1);
                    } else {
                        match.scoreTeam2++;
                        System.out.println("Score: " + match.scoreTeam2 + "/" + match.wicketsTeam2);
                        System.out.println("Team 2 needs " + (match.scoreTeam1 + 1 - match.scoreTeam2) + " runs to win.");
                    }
                    // do not increment ball count
                } else if (deliveryType == 4 || deliveryType == 5) {
                    System.out.print("Enter number of runs scored: ");
                    int runsScored = scanner.nextInt();
                    scanner.nextLine(); // consume newline left-over

                    if (match.isTeam1Batting) {
                        match.scoreTeam1 += runsScored;
                        System.out.println("Score: " + match.scoreTeam1 + "/" + match.wicketsTeam1);
                    } else {
                        match.scoreTeam2 += runsScored;
                        System.out.println("Score: " + match.scoreTeam2 + "/" + match.wicketsTeam2);
                        System.out.println("Team 2 needs " + (match.scoreTeam1 + 1 - match.scoreTeam2) + " runs to win.");
                    }
                    match.ballsInOver++;
                    totalBalls++;
                } else if (deliveryType == 6) {
                    System.out.print("Enter the batsman's name: ");
                    String batsmanName = scanner.nextLine();
                    System.out.print("Enter how the player got out (Bowled, Run Out, LBW, etc.): ");
                    String howOut = scanner.nextLine();
                    System.out.println(batsmanName + " is out by " + howOut + " bowled by " + match.bowler.name);

                    if (match.isTeam1Batting) {
                        match.wicketsTeam1++;
                        totalWickets++;
                        if (match.wicketsTeam1 < 10) {
                            match.batsmanOnStrike = match.battingOrderTeam1[match.wicketsTeam1 + 1];
                        }
                    } else {
                        match.wicketsTeam2++;
                        totalWickets++;
                        if (match.wicketsTeam2 < 10) {
                            match.batsmanOnStrike = match.battingOrderTeam2[match.wicketsTeam2 + 1];
                        }
                    }
                    match.ballsInOver++;
                    totalBalls++;
                }

                System.out.println("Current Batsmen Stats:");
                System.out.println("Batsman on strike: " + match.batsmanOnStrike.name + " - " + match.batsmanOnStrike.individualScore + " runs (Strike Rate: " + match.batsmanOnStrike.getStrikeRate() + ")");
                System.out.println("Batsman on non-strike: " + match.batsmanNonStrike.name + " - " + match.batsmanNonStrike.individualScore + " runs (Strike Rate: " + match.batsmanNonStrike.getStrikeRate() + ")");
                System.out.println("Bowler: " + match.bowler.name + " - " + match.bowler.ballsBowled + " balls, " + match.bowler.wicketsTaken + " wickets");

                System.out.print("Do the players want to exchange their strike? (yes/no): ");
                String exchangeStrikeResponse = scanner.nextLine();
                if (exchangeStrikeResponse.equalsIgnoreCase("yes")) {
                    Player temp = match.batsmanOnStrike;
                    match.batsmanOnStrike = match.batsmanNonStrike;
                    match.batsmanNonStrike = temp;
                }

                System.out.println("Batsman on strike: " + match.batsmanOnStrike.name);
            }

            match.currentOver++;
            match.bowlerOvers[getBowlerIndex(match.bowler, match.team2)]++;
            match.bowler.ballsBowled += 6;

            if (match.currentOver < match.overs && (match.isTeam1Batting ? match.wicketsTeam1 : match.wicketsTeam2) < 10) {
                changeBowler(match);
            }
        }

        System.out.println("Innings over. Final score: " + (match.isTeam1Batting ? match.scoreTeam1 : match.scoreTeam2) + "/" + (match.isTeam1Batting ? match.wicketsTeam1 : match.wicketsTeam2));
        System.out.println("Total Balls: " + totalBalls);
        System.out.println("Total Wickets: " + totalWickets);

        if (match.isTeam1Batting) {
            System.out.println("Team 1 scored " + match.scoreTeam1 + "/" + match.wicketsTeam1);
            System.out.println("Team 2 needs " + (match.scoreTeam1 + 1 - match.scoreTeam2) + " runs to win.");
        } else {
            System.out.println("Team 2 scored " + match.scoreTeam2 + "/" + match.wicketsTeam2);
            System.out.println("Team 1 needs " + (match.scoreTeam2 + 1 - match.scoreTeam1) + " runs to win.");
        }

        System.out.print("Do you want to play the 2nd innings? (yes/no): ");
        String playSecondInnings = scanner.nextLine();
        if (playSecondInnings.equalsIgnoreCase("yes")) {
            playSecondInning(match);
        }
    }

    void playSecondInning(Match match) {
        match.isTeam1Batting = !match.isTeam1Batting;
        match.currentOver = 0;
        match.ballsInOver = 0;
        match.wicketsTeam1 = 0;
        match.wicketsTeam2 = 0;
        match.scoreTeam1 = 0;
        match.scoreTeam2 = 0;
        match.batsmanOnStrike = match.battingOrderTeam1[0];
        match.batsmanNonStrike = match.battingOrderTeam1[1];
        match.bowler = match.team2.players[0];

        playMatch(match);
    }

    Player getPlayerByName(String name, Team team) {
        for (Player player : team.players) {
            if (player.name.equals(name)) {
                return player;
            }
        }
        return null;
    }

    void rotateStrike(int runsScored, Match match) {
        if (runsScored % 2 != 0) {
            Player temp = match.batsmanOnStrike;
            match.batsmanOnStrike = match.batsmanNonStrike;
            match.batsmanNonStrike = temp;
        }
    }

    void changeBowler(Match match) {
        Player lastBowler = match.bowler;  // Keep track of the previous bowler
        
        while (true) {
            System.out.print("Enter the next bowler's name: ");
            String nextBowlerName = scanner.nextLine();
            
            // Iterate through the bowling team's players
            for (Player player : match.isTeam1Batting ? match.team2.players : match.team1.players) {
                if (player.name.equals(nextBowlerName)) {
                    int bowlerIndex = getBowlerIndex(player, match.isTeam1Batting ? match.team2 : match.team1);

                     // Calculate allowed overs
                     int allowedOvers = (int) Math.ceil((double) match.overs / 5);

                    // Debugging statements
                    System.out.println("Checking bowler: " + player.name);
                    System.out.println("Bowler index: " + bowlerIndex);
                    System.out.println("Bowler overs: " + match.bowlerOvers[bowlerIndex]);
                    System.out.println("Allowed overs: " + (match.overs / 5));
                    
                    // Ensure the player hasn't bowled more than allowed overs and is not the same as the last bowler
                    if (match.bowlerOvers[bowlerIndex] < allowedOvers) {
                        
                        if (!player.equals(lastBowler)) {
                            match.bowler = player;  // Set the new bow


                    // Debugging statements
                    System.out.println("Checking bowler: " + player.name);
                    System.out.println("Bowler index: " + bowlerIndex);
                    System.out.println("Bowler overs: " + match.bowlerOvers[bowlerIndex]);
                    System.out.println("Allowed overs: " + (match.overs / 5));
                    
                    // Ensure the player hasn't bowled more than allowed overs and is not the same as the last bowler
                    if (match.bowlerOvers[bowlerIndex] < allowedOvers) {
                        
                        if (!player.equals(lastBowler)) {
                            match.bowler = player;  // Set the new bowler
                            return;
                        } else {
                            System.out.println("The bowler cannot bowl consecutive overs. Choose a different bowler.");
                        }
                    } else {
                        System.out.println("This bowler has reached their over limit.");
                    }
                }
            }
        } else {
            System.out.println("No bowlers available to bowl.");
        }
    }
    } }

    int getBowlerIndex(Player bowler, Team team) {
        for (int i = 0; i < team.players.length; i++) {
            if (team.players[i].name.equals(bowler.name)) {
                return i;
            }
        }
        return -1;
    }

    void displayResults(Match match) {
        System.out.println("Match Results:");
        System.out.println("Team 1 Score: " + match.scoreTeam1 + "/" + match.wicketsTeam1);
        System.out.println("Team 2 Score: " + match.scoreTeam2 + "/" + match.wicketsTeam2);
        if (match.scoreTeam1 > match.scoreTeam2) {
            System.out.println("Team 1 wins!");
        } else if (match.scoreTeam2 > match.scoreTeam1) {
            System.out.println("Team 2 wins!");
        } else {
            System.out.println("The match is a tie!");
        }

        System.out.println("Individual Scores:");
        for (Player player : match.team1.players) {
            System.out.println(player.name + ": " + player.individualScore + " runs (Strike Rate: " + player.getStrikeRate() + ")");
        }
        for (Player player : match.team2.players) {
            System.out.println(player.name + ": " + player.individualScore + " runs (Strike Rate: " + player.getStrikeRate() + ")");
        }
    }



    public static void main(String[] args) {
        cricketScoreboard scoreboard = new cricketScoreboard();

        while (true) {
            scoreboard.displayMenu();
            System.out.print("Enter your choice: ");
            int choice = scoreboard.scanner.nextInt();
            scoreboard.scanner.nextLine(); // consume newline left-over

            switch (choice) {
                case 1:
                    scoreboard.viewTeams();
                    break;
                case 2:
                    scoreboard.addTeam();
                    break;
                case 3:
                    scoreboard.startMatch();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
