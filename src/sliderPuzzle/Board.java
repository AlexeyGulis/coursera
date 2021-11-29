package sliderPuzzle;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.Stack;

public class Board {

    private int[][] tiles;
    private int dimension;
    private int blankSquareI;
    private int blankSquareJ;
    private Stack<Board> twins;

    public Board(int[][] tiles) {
        twins = new Stack<Board>();
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankSquareI = i;
                    blankSquareJ = j;
                }
            }
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, tiles.length);
        }
        dimension = tiles.length;
    }

    private void twinsAddStack(int i, int j) {
        tiles[blankSquareI][blankSquareJ] = tiles[i][j];
        tiles[i][j] = 0;
        Board newBoard = new Board(tiles);
        tiles[i][j] = tiles[blankSquareI][blankSquareJ];
        tiles[blankSquareI][blankSquareJ] = 0;
        twins.push(newBoard);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                stringBuilder.append(String.format("%2d ",tiles[i][j]));
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
                if (i == dimension() - 1 && j == dimension() - 1 && tiles[i][j] != 0) {
                    count++;
                } else if (twoDimTo1Dim(i, j) + 1 != tiles[i][j]) {
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
        int distance = 0;
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                if (i == dimension() - 1 && j == dimension() - 1 && tiles[i][j] != 0) {
                    distance += tiles[i][j];
                } else if (twoDimTo1Dim(i, j) + 1 != tiles[i][j]) {
                    distance += Math.abs(tiles[i][j] - twoDimTo1Dim(i, j) - 1);
                }
            }
        }
        return distance;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y != null && y.getClass() != this.getClass()) {
            Board p = (Board) y;
            if (p.dimension() == this.dimension()) {
                for (int i = 0; i < dimension(); i++) {
                    for (int j = 0; j < dimension(); j++) {
                        if (p.tiles[i][j] != tiles[i][j]) {
                            return false;
                        }
                    }
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        int i = blankSquareI;
        int j = blankSquareJ;
        if (i != 0) {
            twinsAddStack(i - 1, j);
        }
        if (i != tiles.length - 1) {
            twinsAddStack(i + 1, j);
        }
        if (j != 0) {
            twinsAddStack(i, j - 1);
        }
        if (j != tiles.length - 1) {
            twinsAddStack(i, j + 1);
        }
        Iterable<Board> neighbors = new Iterable<Board>() {
            @Override
            public Iterator<Board> iterator() {
                return new Iterator<Board>() {
                    @Override
                    public boolean hasNext() {
                        return !twins.empty();
                    }

                    @Override
                    public Board next() {
                        return twins.pop();
                    }
                };
            }
        };
        return neighbors;
    }

    public Board twin() {
        return null;
    }

    public static void main(String[] args) {
        int[] shufArray = new int[9];
        int[][] array = new int[3][3];
        for (int i = 0; i < shufArray.length; i++) {
            shufArray[i] = i;
        }
        StdRandom.shuffle(shufArray);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                array[i][j] = shufArray[i * array.length + j];
            }
        }
        Board board = new Board(array);
        /*StdRandom.shuffle(shufArray);
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                array[i][j] = shufArray[i * array.length + j];
            }
        }
        Board board1 = new Board(array);
        StdOut.println(board.toString());
        StdOut.println(board.manhattan());
        StdOut.println(board.hamming());
        StdOut.println(board1.toString());
        StdOut.println(board.equals(board1));
         */
        StdOut.println(board.toString());
        for (Board b : board.neighbors()
        ) {
            StdOut.println(b);
        }

    }


}
