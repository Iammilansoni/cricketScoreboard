import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Player {
    private String name;
    private int runsScored;
    private int ballsFaced;

    public Player(String name) {
        this.name = name;
        this.runsScored = 0;
        this.ballsFaced = 0;
    }

    public void addRuns(int runs) {
        this.runsScored += runs;
    }

    public void addBall() {
        this.ballsFaced++;
    }

    public float getStrikeRate() {
        if (this.ballsFaced == 0) {
            return 0.0f;
        }
        return (this.runsScored * 100.0f) / this.ballsFaced;
    }

    public String getName() {
        return this.name;
    }

    public int getRunsScored() {
        return this.runsScored;
    }

    public int getBallsFaced() {
        return this.ballsFaced;
    }
}

class Team {
    private String name;
    private String[] players = new String[11];
    private int runsScored = 0;
    private int wickets = 0;

    public Team(String name, String[] players) {
        this.name = name;
        this.players = players;
    }

    public String getName() {
        return name;
    }

    public String[] getPlayers() {
        return players;
    }

    public int getRunsScored() {
        return runsScored;
    }

    public void setRunsScored(int runsScored) {
        this.runsScored = runsScored;
    }

    public int getWickets() {
        return wickets;
    }

    public void setWickets(int wickets) {
        this.wickets = wickets;
    }

    public void resetScore() {
        this.runsScored = 0;
        this.wickets = 0;
    }
}

class Game {
    private Team battingTeam;
    private Team bowlingTeam;
    private int overs;
    private Scanner input;

    public Game(Team team1, Team team2, int overs) {
        this.overs = overs;
        this.battingTeam = team1;
        this.bowlingTeam = team2;
        this.input = new Scanner(System.in);
    }

    private Player striker;
    private Player nonStriker;

    public void startInning(Team battingTeam, Team bowlingTeam) {
        striker = new Player(battingTeam.getPlayers()[0]);
        nonStriker = new Player(battingTeam.getPlayers()[1]);

        int currentOver = 1;

        do {
            System.out.println("Over " + currentOver);
            System.out.println("Enter the Bowler from " + bowlingTeam.getName() + " for Over " + currentOver + ": ");
            String bowler = input.next();
            while (!Arrays.asList(bowlingTeam.getPlayers()).contains(bowler)) {
                System.out.println("Bowler not found in " + bowlingTeam.getName());
                System.out.println("Enter the Bowler from " + bowlingTeam.getName() + " for Over " + currentOver + ": ");
                bowler = input.next();
            }

            int balls = 0;
            while (balls < 6 && battingTeam.getWickets() < 10) {
                System.out.println("Ball " + (balls + 1));
                System.out.println("Strike: " + striker.getName() + ", Non-Strike: " + nonStriker.getName());
                System.out.println("Enter the type of ball: ");
                System.out.println("1. Regular ball");
                System.out.println("2. Dot ball");
                System.out.println("3. No ball");
                System.out.println("4. Wide ball");
                System.out.println("5. Bye");
                System.out.println("6. Leg bye");
                System.out.println("7. Wicket");
                int ballType = input.nextInt();

                switch (ballType) {
                    case 1:
                        System.out.println("Enter the runs scored on this ball: ");
                        int runs = input.nextInt();
                        battingTeam.setRunsScored(battingTeam.getRunsScored() + runs);
                        striker.addRuns(runs);
                        striker.addBall();

                        if (runs % 2 != 0) {
                            Player temp = striker;
                            striker = nonStriker;
                            nonStriker = temp;
                        }
                        balls++;
                        break;
                    case 2:
                        System.out.println("Dot ball, no run.");
                        striker.addBall();
                        balls++;
                        break;
                    case 3:
                        System.out.println("No ball, 1 run to " + battingTeam.getName());
                        battingTeam.setRunsScored(battingTeam.getRunsScored() + 1);
                        // No increase in balls count for no ball
                        break;
                    case 4:
                        System.out.println("Wide ball, 1 run to " + battingTeam.getName());
                        battingTeam.setRunsScored(battingTeam.getRunsScored() + 1);
                        // No increase in balls count for wide ball
                        break;
                    case 5:
                        System.out.println("Bye run");
                        balls++;
                        break;
                    case 6:
                        System.out.println("Leg bye run");
                        balls++;
                        break;
                    case 7:
                        System.out.println("Wicket!");
                        battingTeam.setWickets(battingTeam.getWickets() + 1);

                        // Update striker after a wicket
                        if (battingTeam.getWickets() < 10) {
                            striker = new Player(battingTeam.getPlayers()[battingTeam.getWickets() + 1]);
                        } else {
                            System.out.println("All out");
                            break;
                        }
                        balls++;
                        break;
                    default:
                        System.out.println("Invalid choice");
                }

                // Display the current score and strike rates
                System.out.println("Score: " + battingTeam.getName() + " - " + battingTeam.getRunsScored() + "/" + battingTeam.getWickets());
                System.out.println("Striker: " + striker.getName() + " | Runs: " + striker.getRunsScored() + " | Balls: " + striker.getBallsFaced() + " | Strike Rate: " + striker.getStrikeRate());
                System.out.println("Non-Striker: " + nonStriker.getName() + " | Runs: " + nonStriker.getRunsScored() + " | Balls: " + nonStriker.getBallsFaced() + " | Strike Rate: " + nonStriker.getStrikeRate());
            }
            String[] commentaryLines = {
                    "What a shot!",
                    "Good cricket!",
                    "Well played!",
                    "Shot of the match!",
                    "Beautiful drive!",
                    "What a catch!",
                    "Good bowling!",
                    "Brilliant fielding!"
                };
                Random random = new Random();
                int randomIndex = random.nextInt(commentaryLines.length);
                System.out.println(commentaryLines[randomIndex]);
        } while (currentOver <= overs && battingTeam.getWickets() < 10);
    }

    public void startGame() {
       // Simulate a coin toss
System.out.println("Tossing a coin...");
String[] tossOutcomes = {"Heads", "Tails"};
new Random().nextInt(tossOutcomes.length);
System.out.println(battingTeam.getName() + " won the toss!");

System.out.println("You won the toss! Do you want to bat or bowl?");
System.out.println("1. Bat");
System.out.println("2. Bowl");
int tossChoice = input.nextInt();
if (tossChoice == 1) {
    System.out.println(battingTeam.getName() + " is batting");
} else {
    System.out.println(bowlingTeam.getName() + " is batting");
    Team temp = battingTeam;
    battingTeam = bowlingTeam;
    bowlingTeam = temp;
}
startInning(battingTeam, bowlingTeam);
System.out.println("First innings over. Score: " + battingTeam.getName() + " - " + battingTeam.getRunsScored() + "/" + battingTeam.getWickets());

// Swap teams for second inning
System.out.println("Second Innings: " + bowlingTeam.getName() + " is batting");
Team temp = battingTeam;
battingTeam = bowlingTeam;
bowlingTeam = temp;
startInning(battingTeam, bowlingTeam);

// Display both innings final score
System.out.println("First innings over. Score: " + battingTeam.getName() + " - " + battingTeam.getRunsScored() + "/" + battingTeam.getWickets());

System.out.println("Second innings over. Score: " + battingTeam.getName() + " - " + battingTeam.getRunsScored() + "/" + battingTeam.getWickets());

// Compare scores
if (battingTeam.getRunsScored() > bowlingTeam.getRunsScored()) {
    System.out.println(battingTeam.getName() + " wins the match!");
} else if (battingTeam.getRunsScored() < bowlingTeam.getRunsScored()) {
    System.out.println(bowlingTeam.getName() + " wins the match!");
} else {
    System.out.println("The match is a tie!");
}


    }
}

public class cricketScoreboard {
    private static Team[] teams = new Team[4];
    private static int teamsCount = 0;

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            int choice;
            do {
                //("********** CRICKET SCOREBOARD **********");
                printScoreboard();
                System.out.println("1. View Teams ");
                System.out.println("2. Add Teams ");
                System.out.println("3. Start Match");
                System.out.println("4. Exit");
                choice = input.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println(teamsCount + " teams created so far");
                        if (teamsCount > 0) {
                            for (int i = 0; i < teamsCount; i++)
                                System.out.println((i + 1) + ". " + teams[i].getName());
                        } else {
                            System.out.println("No teams created yet");
                        }
                        break;
                    case 2:
                        System.out.println("Enter the name of Team " + (teamsCount + 1) + ": ");
                        String teamName = input.next();
                        String[] teamPlayers = new String[11];
                        System.out.println("Enter the 11 players of the team");
                        for (int i = 0; i < 11; i++) {
                            System.out.println("Enter player " + (i + 1) + ": ");
                            teamPlayers[i] = input.next();
                        }
                        Team team = new Team(teamName, teamPlayers);
                        teams[teamsCount++] = team;
                        break;
                        case 3:
                        System.out.println("Select the teams for the match");
                        if (teamsCount >= 2) {
                            for (int i = 0; i < teamsCount; i++)
                                System.out.println((i + 1) + ". " + teams[i].getName());
                            int team1Index = input.nextInt() - 1;
                            int team2Index;
                            do {
                                System.out.println("Select the second team: ");
                                team2Index = input.nextInt() - 1;
                            } while (team1Index == team2Index);
                            System.out.println("Enter the number of overs: ");
                            int overs = input.nextInt();
                            Game game = new Game(teams[team1Index], teams[team2Index], overs);
                            game.startGame();
                        } else {
                            System.out.println("Need at least 2 teams to play the game.");
                        }
                        break;

                    case 4:
                        System.out.println("Thanks for playing!");
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } while (choice != 4);
        }
    }


private static void printScoreboard() {
    try (BufferedReader br = new BufferedReader(new FileReader("cricketScoreboard.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }
}

}
