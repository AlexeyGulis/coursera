package sliderPuzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
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
        priorityQueue.insert(new Node(topMin, topMin.manhattan(), 0, null));
        secondPriorityQueue.insert(new Node(secondTopMin, secondTopMin.manhattan(), 0, null));
        Node first = priorityQueue.delMin();
        Node second = secondPriorityQueue.delMin();
        while (!first.that.isGoal() && !second.that.isGoal()) {
            for (Board b : first.that.neighbors()
            ) {
                if (first.prev == null || !first.prev.that.equals(b)) {
                    priorityQueue.insert(new Node(b, b.manhattan(), first.moves + 1, first));
                }
            }
            for (Board b : second.that.neighbors()
            ) {
                if (second.prev == null || !second.prev.that.equals(b)) {
                    secondPriorityQueue.insert(new Node(b, b.manhattan(), first.moves + 1, second));
                }
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
        public Node prev;

        public Node(Board that, int manhattan, int moves, Node prev) {
            this.that = that;
            this.manhattan = manhattan;
            this.moves = moves;
            this.prev = prev;
        }

        @Override
        public int compareTo(Node n) {
            if (manhattan + moves > n.manhattan + n.moves)
                return 1;
            else if (manhattan + moves < n.manhattan + n.moves) {
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
        if (!isSolvable()) return null;
        Stack<Board> solution = new Stack<>();
        Node sol = goalNode;
        while (sol.prev != null) {
            solution.push(sol.that);
            sol = sol.prev;
        }
        solution.push(sol.that);
        return solution;
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
            for (Board board : solver.solution()){
                StdOut.println(board);
            }
        }
    }
}
