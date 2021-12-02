package sliderPuzzle;

import edu.princeton.cs.algs4.*;

import java.util.Comparator;
import java.util.Iterator;

public class Solver {

    private MinPQ<Board> priorityQueue;
    private Stack<Board> solution;
    private int moves;
    private Board init;
    private boolean solve;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        init = initial;
        Board topMin = initial;
        Board secondTopMin = initial.twin();
        solution = new Stack<>();
        topMin.manhattan();
        secondTopMin.manhattan();
        moves = 0;
        priorityQueue = new MinPQ<>(comparator());
        MinPQ<Board> secondPriorityQueue = new MinPQ<>(comparator());
        priorityQueue.insert(topMin);
        topMin = priorityQueue.delMin();
        secondPriorityQueue.insert(secondTopMin);
        secondTopMin = secondPriorityQueue.delMin();
        while (!topMin.isGoal() && !secondTopMin.isGoal()) {
            for (Board b : topMin.neighbors()
            ) {
                priorityQueue.insert(b);
            }
            for (Board b : secondTopMin.neighbors()
            ) {
                secondPriorityQueue.insert(b);
            }
            topMin = priorityQueue.delMin();
            secondTopMin = secondPriorityQueue.delMin();
            moves = topMin.getMoves();
        }
        solve = topMin.isGoal();
        while (topMin.getPrev() != null) {
            solution.push(topMin);
            topMin = topMin.getPrev();
        }
        solution.push(topMin);
    }

    private Comparator<Board> comparator() {
        return new Comparator<Board>() {
            @Override
            public int compare(Board o1, Board o2) {
                if (o1.getMoves() + o1.hamming() > o2.getMoves() + o2.hamming())
                    return 1;
                else if (o1.getMoves() + o1.hamming() < o2.getMoves() + o2.hamming()) {
                    return -1;
                } else return 0;
            }
        };
    }

    public boolean isSolvable() {
        return solve;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    @Override
                    public boolean hasNext() {
                        return !solution.isEmpty();
                    }

                    @Override
                    public Board next() {
                        return solution.pop();
                    }
                };
            }
        };
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
