package sliderPuzzle;

import edu.princeton.cs.algs4.*;

import java.util.Iterator;

public class Solver1 {

    private MinPQ<Board1> priorityQueue;
    private Stack<Board1> solution;
    private int moves;

    public Solver1(Board1 initial) {
        Board1 topMin = initial;
        solution = new Stack<>();
        topMin.manhattan();
        moves = 0;
        priorityQueue = new MinPQ<>();
        priorityQueue.insert(topMin);
        topMin.prev = null;
        topMin = priorityQueue.delMin();
        while (!topMin.isGoal()) {
            for (Board1 b : topMin.neighbors()
            ) {
                if (topMin.prev == null) {
                    priorityQueue.insert(b);
                } else if (!topMin.prev.equals(b)) {
                    priorityQueue.insert(b);
                }
            }
            topMin = priorityQueue.delMin();
            moves = topMin.getMoves();
        }
        while (topMin.prev != null) {
            solution.push(topMin);
            topMin = topMin.prev;
        }
    }

    public boolean isSolvable() {
        return false;
    }

    public int moves() {
        if (isSolvable()) return -1;
        return moves;
    }

    public Iterable<Board1> solution() {
        return new Iterable<Board1>() {
            @Override
            public Iterator<Board1> iterator() {
                return new Iterator<Board1>() {
                    @Override
                    public boolean hasNext() {
                        return !solution.isEmpty();
                    }

                    @Override
                    public Board1 next() {
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
        Board1 initial = new Board1(tiles);
        StdOut.println(initial.toString());
        Solver1 solver = new Solver1(initial);
        for (Board1 b : solver.solution()
        ) {
            StdOut.println(b);
        }
        StdOut.println(solver.moves());
    }
}
