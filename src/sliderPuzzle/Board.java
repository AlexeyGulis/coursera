package sliderPuzzle;


import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] tiles;
    private final int dimension;
    private int blankSquareI;
    private int blankSquareJ;
    private int cacheDistance;

    public Board(int[][] tiles) {
        dimension = tiles.length;
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankSquareI = i;
                    blankSquareJ = j;
                }
            }
        }
        cacheDistance = manhattan();
    }

    private void addStack(int i, int j, Stack<Board> neighborsStack) {
        tiles[blankSquareI][blankSquareJ] = tiles[i][j];
        tiles[i][j] = 0;
        Board newBoard = new Board(tiles);
        tiles[i][j] = tiles[blankSquareI][blankSquareJ];
        tiles[blankSquareI][blankSquareJ] = 0;
        newBoard.cacheDistance = cacheDistance;
        newBoard.cacheDistance -= Math.abs(i - (tiles[i][j] - 1) / dimension()) + Math.abs(j - (tiles[i][j] - 1) % dimension());
        newBoard.cacheDistance += Math.abs(blankSquareI - (tiles[i][j] - 1) / dimension()) + Math.abs(blankSquareJ - (tiles[i][j] - 1) % dimension());
        neighborsStack.push(newBoard);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                stringBuilder.append(String.format("%2d ", tiles[i][j]));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (tiles[i][j] != 0 && twoDimTo1Dim(i, j) + 1 != tiles[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    private int twoDimTo1Dim(int row, int col) {
        return row * dimension() + col;
    }

    public int manhattan() {
        if (cacheDistance == 0) {
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    if (tiles[i][j] != 0) {
                        cacheDistance += Math.abs(i - (tiles[i][j] - 1) / dimension()) + Math.abs(j - (tiles[i][j] - 1) % dimension());
                    }
                }
            }
        }
        return cacheDistance;
    }


    public boolean isGoal() {
        return cacheDistance == 0;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y != null && y.getClass() == this.getClass()) {
            Board p = (Board) y;
            if (p.dimension() == this.dimension()) {
                for (int i = 0; i < dimension(); i++) {
                    for (int j = 0; j < dimension(); j++) {
                        if (p.tiles[i][j] != this.tiles[i][j]) {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int i = blankSquareI;
        int j = blankSquareJ;
        Stack<Board> neighborsStack = new Stack<>();
        if (i != 0) {
            addStack(i - 1, j, neighborsStack);
        }
        if (i != tiles.length - 1) {
            addStack(i + 1, j, neighborsStack);
        }
        if (j != 0) {
            addStack(i, j - 1, neighborsStack);
        }
        if (j != tiles.length - 1) {
            addStack(i, j + 1, neighborsStack);
        }
        Iterable<Board> neighbors = new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    @Override
                    public boolean hasNext() {
                        return !neighborsStack.isEmpty();
                    }

                    @Override
                    public Board next() {
                        if (!hasNext()) throw new NoSuchElementException();
                        return neighborsStack.pop();
                    }
                };
            }
        };
        return neighbors;
    }

    public Board twin() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension - 1; j++) {
                if (tiles[i][j] > 0 && tiles[i][j + 1] > 0) {
                    int temp = tiles[i][j];
                    tiles[i][j] = tiles[i][j + 1];
                    tiles[i][j + 1] = temp;
                    Board copy = new Board(tiles);
                    tiles[i][j + 1] = tiles[i][j];
                    tiles[i][j] = temp;
                    return copy;
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        int n = StdIn.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = StdIn.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println(initial.isGoal());
    }
}