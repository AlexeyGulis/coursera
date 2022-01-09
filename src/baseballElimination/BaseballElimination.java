package baseballElimination;

import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private FordFulkerson fordFulkerson;
    private FlowNetwork flowNetwork;
    private ArrayList<String> teams;
    private int size;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        size = in.readInt();
        teams = new ArrayList<>(size);
        w = new int[size];
        r = new int[size];
        l = new int[size];
        g = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 4 + size; j++) {
                if (j == 0) {
                    teams.add(in.readString());
                } else if (j == 1) {
                    w[i] = in.readInt();
                } else if (j == 2) {
                    l[i] = in.readInt();
                } else if (j == 3) {
                    r[i] = in.readInt();
                } else {
                    g[i][j - 4] = in.readInt();
                }
            }
        }
    }

    public int numberOfTeams() {
        return size;
    }

    public Iterable<String> teams() {
        return teams;
    }

    private int checkTeam(String team) {
        if(team == null) throw new IllegalArgumentException();
        for (int i = 0; i < size; i++) {
            if (team.equals(teams.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int wins(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        return w[i];
    }

    public int losses(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        return l[i];
    }

    public int remaining(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        return r[i];
    }

    public int against(String team1, String team2) {
        int i1 = checkTeam(team1);
        int i2 = checkTeam(team2);
        if (i1 == -1 || i2 == -1) throw new IllegalArgumentException();
        return g[i1][i2];
    }

    public boolean isEliminated(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        System.out.println("Number of teams: " + division.numberOfTeams());
        System.out.println("NY number wins: " + division.wins("New_York"));
        System.out.println("BO number losses: " + division.losses("Boston"));
        System.out.println("TO number remaining: " + division.remaining("Toronto"));
        System.out.println("Games between BO DE: " + division.against("Boston", "Toronto"));
        System.out.println("Teams: ");
        for (String t : division.teams()
             ) {
            System.out.println(t);
        }
    }
}
