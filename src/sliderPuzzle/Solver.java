package sliderPuzzle;

import edu.princeton.cs.algs4.*;

import sliderPuzzle.Board;
import java.util.Iterator;

public class Solver {

    private MinPQ<Board> priorityQueue;
    private MinPQ<Board> gameTree;
    private int moves;

    public Solver(Board initial) {
        Board topMin = initial;
        topMin.manhattan();
        moves = 0;
        priorityQueue = new MinPQ<>();
        gameTree = new MinPQ<>();
        priorityQueue.insert(topMin);
        topMin.prev = topMin;
        gameTree.insert(topMin);
        topMin = priorityQueue.delMin();
        boolean flag = true;
        while (!topMin.isGoal()) {
            for (Board b : topMin.neighbors()
            ) {
                if(!topMin.prev.equals(b)) {
                    gameTree.insert(b);
                    priorityQueue.insert(b);
                }
            }
            topMin = priorityQueue.delMin();
            moves = topMin.getMoves();
        }
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
        StdOut.println(solver.moves());
    }
}
