package sliderPuzzle;

import edu.princeton.cs.algs4.StdOut;

public class Board {

    private int[][] tiles;
    private int dimension;

    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            System.arraycopy(tiles[i], 0, this.tiles[i], 0, tiles.length);
        }
        dimension = tiles.length;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dimension);
        stringBuilder.append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                stringBuilder.append(tiles[i][j]).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public int dimension() {
        return 0;
    }

    public int hamming() {
        return 0;
    }

    public int manhattan() {
        return 0;
    }

    public boolean isGoal() {
        return true;
    }

    public boolean equals(Object y) {
        return true;
    }

    public Iterable<Board> neighbors() {
        return null;
    }

    public Board twin() {
        return null;
    }

    public static void main(String[] args) {
        int[][] array = new int[3][3];
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                array[i][j] = count++;
            }
        }
        Board board = new Board(array);
        StdOut.println(board.toString());
    }
}
