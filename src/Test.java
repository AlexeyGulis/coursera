import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import sliderPuzzle.Board;

public class Test {
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
        StdOut.println(board.toString());
        StdOut.println(board.manhattan());
    }
}
