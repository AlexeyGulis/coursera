package sliderPuzzle;

import edu.princeton.cs.algs4.*;

import java.util.Iterator;
import java.util.Stack;

public class Solver {

    private MinPQ<Board> priorityQueue;
    private MinPQ<Board> gameTree;
    private Stack<Board> solutionStack;
    private int moves;

    public Solver(Board initial) {
        Board topMin = initial;
        topMin.manhattan();
        solutionStack = new Stack<>();
        moves = 0;
        priorityQueue = new MinPQ<>();
        gameTree = new MinPQ<>();
        while (!topMin.isGoal()) {
            priorityQueue.insert(topMin);
            gameTree.insert(topMin);
            for (Board b : topMin.neighbors()
            ) {
                boolean flag = true;
                for (Board c : gameTree
                ) {
                    if (b.equals(c)) {
                        flag = false;
                    }
                }
                if (flag) {
                    priorityQueue.insert(b);
                    gameTree.insert(b);
                }
            }
            priorityQueue.delMin();
            topMin = priorityQueue.delMin();
            moves = topMin.getMoves();
        }
        StdOut.println();
    }

    public boolean isSolvable() {
        return false;
    }

    public int moves() {
        if (isSolvable()) return -1;
        return moves;
    }

    public Iterable<Board> solution() {
        MinPQ<Board> copy = new MinPQ<>();
        for (Board c : gameTree) {
            copy.insert(c);
        }
        return new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    @Override
                    public boolean hasNext() {
                        return !copy.isEmpty();
                    }

                    @Override
                    public Board next() {
                        return copy.delMin();
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
        StdOut.println(initial.toString());
        Solver solver = new Solver(initial);
        for (Board c : solver.solution()
        ) {
            StdOut.println(c);
        }
        StdOut.println(solver.moves());
    }
}
