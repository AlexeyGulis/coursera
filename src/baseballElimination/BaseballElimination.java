package baseballElimination;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {
    private FordFulkerson[] fordFulkerson;
    private FlowNetwork[] flowNetwork;
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
        flowNetwork = new FlowNetwork[size];
        fordFulkerson = new FordFulkerson[size];
    }

    public int numberOfTeams() {
        return size;
    }

    public Iterable<String> teams() {
        return teams;
    }

    private int checkTeam(String team) {
        if (team == null) throw new IllegalArgumentException();
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

    private void calcFlowNetwork(int i) {
        if (flowNetwork[i] == null) {
            flowNetwork[i] = new FlowNetwork(((size - 1) * (size - 2) / 2) + (size - 1) + 2);

            int countj = 0;
            int countk = 0;
            for (int j = 0; j < size; j++) {
                for (int k = j + 1; k < size; k++) {
                    if (j != i && k != i) {
                        countk++;
                        flowNetwork[i].addEdge(new FlowEdge(0, countk, g[j][k]));
                        flowNetwork[i].addEdge(new FlowEdge(countk, ((size - 1) * (size - 2) / 2) + 1 + (j >= i ? j - 1 : j), Double.POSITIVE_INFINITY));
                        flowNetwork[i].addEdge(new FlowEdge(countk, ((size - 1) * (size - 2) / 2) + 1 + (k >= i ? k - 1 : k), Double.POSITIVE_INFINITY));
                    }
                }
                if (j != i) {
                    flowNetwork[i].addEdge(new FlowEdge(((size - 1) * (size - 2) / 2) + 1 + countj, ((size - 1) * (size - 2) / 2) + (size - 1) + 1, w[i] + r[i] - w[j]));
                    countj++;
                }
            }
            fordFulkerson[i] = new FordFulkerson(flowNetwork[i], 0, ((size - 1) * (size - 2) / 2) + (size - 1) + 1);
        }
    }

    private int[] trivialCalc(int i) {
        int[] p = new int[size];
        for (int j = 0; j < size; j++) {
            if (i != j && w[i] + r[i] < w[j]) {
                p[j] = 1;
            }
        }
        return p;
    }

    public boolean isEliminated(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        int[] p = trivialCalc(i);
        for (int j = 0; j < p.length; j++) {
            if (p[j] == 1) return true;
        }
        calcFlowNetwork(i);
        for (int j = 0; j < size - 1; j++) {
            if (fordFulkerson[i].inCut(((size - 1) * (size - 2) / 2) + j + 1) == true) return true;
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        int i = checkTeam(team);
        if (i == -1) throw new IllegalArgumentException();
        ArrayList<String> teams = new ArrayList<>(size);
        int[] p = trivialCalc(i);
        boolean flag = false;
        for (int j = 0; j < p.length; j++) {
            if (p[j] == 1) {
                teams.add(this.teams.get(j));
                flag = true;
            }
        }
        if (flag) return teams;
        calcFlowNetwork(i);
        if (isEliminated(team)) {
            for (int j = 0; j < size; j++) {
                if (fordFulkerson[i].inCut(((size - 1) * (size - 2) / 2) + j + 1) == true) {
                    if (j >= i) {
                        teams.add(this.teams.get(j + 1));
                    }else{
                        teams.add(this.teams.get(j));
                    }
                }
            }
            return teams;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
