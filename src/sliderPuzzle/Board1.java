package sliderPuzzle;


import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.Stack;

public class Board1 implements Comparable<Board1>{
    private int[] tiles;
    private int dimension;
    private int blankSquare;
    private Stack<Board1> neighborsStack;
    private Stack<Board1> twins;
    public int cacheDistance;
    private int moves;
    public Board1 prev;

    public Board1(int[][] tiles) {
        cacheDistance = 0;
        moves = 0;
        prev = null;
        neighborsStack = new Stack<>();
        twins = new Stack<>();
        dimension = tiles.length;
        this.tiles = new int[dimension * dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.tiles[twoDimTo1(i, j)] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankSquare = twoDimTo1(i, j);
                }
            }
        }
    }

    public Board1(int[] tiles) {
        neighborsStack = new Stack<>();
        twins = new Stack<>();
        dimension = (int) Math.sqrt(tiles.length);
        this.tiles = new int[dimension * dimension];
        for (int i = 0; i < dimension * dimension; i++) {
            this.tiles[i] = tiles[i];
            if (tiles[i] == 0) {
                blankSquare = i;
            }
        }
    }

    private int twoDimTo1(int row, int col) {
        return row * dimension + col;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dimension + "\n");
        for (int i = 0; i < dimension * dimension; i++) {
            stringBuilder.append(String.format("%2d ",  tiles[i]));
            if (i % dimension == dimension - 1) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public int manhattan() {
        if (cacheDistance == 0) {
            for (int i = 0; i < dimension * dimension; i++) {
                if (tiles[i] != 0) cacheDistance += Math.abs(tiles[i] - (i + 1)) / dimension + Math.abs(tiles[i] - (i + 1)) % dimension;
            }
        }
        return cacheDistance;
    }

    public boolean isGoal() {
        return cacheDistance == 0;
    }

    public int dimension() {
        return dimension;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y != null && y.getClass() == this.getClass()) {
            Board1 p = (Board1) y;
            if (p.dimension() == this.dimension()) {
                for (int i = 0; i < dimension() * dimension(); i++) {
                    if (p.tiles[i] != tiles[i]) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private void addStack(int i) {
        tiles[blankSquare] = tiles[blankSquare + i];
        tiles[blankSquare + i] = 0;
        Board1 newBoard = new Board1(tiles);
        newBoard.prev = this;
        tiles[blankSquare + i] = tiles[blankSquare];
        tiles[blankSquare] = 0;
        newBoard.cacheDistance = cacheDistance;
        newBoard.cacheDistance -= Math.abs(tiles[blankSquare + i] - (blankSquare + i + 1)) / dimension + Math.abs(tiles[blankSquare + i] - (blankSquare + i + 1)) % dimension;
        newBoard.cacheDistance += Math.abs(tiles[blankSquare + i] - (blankSquare + 1)) / dimension + Math.abs(tiles[blankSquare + i] - (blankSquare + 1)) % dimension;
        newBoard.moves = this.moves + 1;
        neighborsStack.push(newBoard);

    }

    public Iterable<Board1> neighbors() {
        int i = blankSquare;
        if (i / dimension != 0) {
            addStack(-dimension);
        }
        if (i / dimension != dimension - 1) {
            addStack(dimension);
        }
        if (i % dimension != 0) {
            addStack(-1);
        }
        if (i % dimension != dimension - 1) {
            addStack(+1);
        }
        Iterable<Board1> neighbors = new Iterable<Board1>() {
            @Override
            public Iterator<Board1> iterator() {
                return new Iterator<Board1>() {
                    @Override
                    public boolean hasNext() {
                        return !neighborsStack.empty();
                    }

                    @Override
                    public Board1 next() {
                        return neighborsStack.pop();
                    }
                };
            }
        };
        return neighbors;
    }

    public int getMoves(){
        return moves;
    }

    public int compareTo(Board1 o) {
        if (this.getMoves() + this.cacheDistance > o.getMoves() + o.cacheDistance)
            return 1;
        else if (this.getMoves() + this.cacheDistance < o.getMoves() + o.cacheDistance) {
            return -1;
        } else return 0;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = StdIn.readInt();
        Board1 board = new Board1(tiles);
        StdOut.println(board.toString());
        StdOut.println(board.manhattan());
        for (Board1 b : board.neighbors()
        ) {
            StdOut.println(b.toString());
            StdOut.println(b.manhattan());
        }
    }

}
