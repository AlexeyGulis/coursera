package sliderPuzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private final Node goalNode;
    private final boolean solve;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        Board topMin = initial;
        Board secondTopMin = initial.twin();
        MinPQ<Node> priorityQueue = new MinPQ<>();
        MinPQ<Node> secondPriorityQueue = new MinPQ<>();
        priorityQueue.insert(new Node(topMin, topMin.manhattan(), 0));
        secondPriorityQueue.insert(new Node(secondTopMin, secondTopMin.manhattan(), 0));
        Node first = priorityQueue.delMin();
        Node second = secondPriorityQueue.delMin();
        while (!first.that.isGoal() && !second.that.isGoal()) {
            for (Board b : first.that.neighbors()
            ) {
                priorityQueue.insert(new Node(b, b.manhattan(), first.moves + 1));
            }
            for (Board b : second.that.neighbors()
            ) {
                secondPriorityQueue.insert(new Node(b, b.manhattan(), first.moves + 1));
            }
            first = priorityQueue.delMin();
            second = secondPriorityQueue.delMin();
        }
        goalNode = first;
        solve = first.that.isGoal();
    }

    private class Node implements Comparable<Node> {
        public Board that;
        public int manhattan;
        public int moves;

        public Node(Board that, int manhattan, int moves) {
            this.that = that;
            this.manhattan = manhattan;
            this.moves = moves;
        }

        @Override
        public int compareTo(Node o) {
            if (manhattan + moves > o.manhattan + o.moves)
                return 1;
            else if (manhattan + moves < o.manhattan + o.moves) {
                return -1;
            } else return 0;
        }
    }


    public boolean isSolvable() {
        return solve;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return goalNode.moves;
    }

    public Iterable<Board> solution() {
        return goalNode.that.neighbors();
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = StdIn.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
