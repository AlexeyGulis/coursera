package boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class BoggleSolver {

    private String[] dictionary;
    private StringST<Integer> testTries;
    private int row;
    private int col;
    private boolean[] marked;
    private BoggleBoard board;
    private TreeSet<String> setWords;

    public BoggleSolver(String[] dictionary) {
        testTries = new StringST<>();
        this.dictionary = new String[dictionary.length];
        for (int i = 0; i < dictionary.length; i++) {
            this.dictionary[i] = dictionary[i];
            testTries.put(dictionary[i], i);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        setWords = new TreeSet<>();
        String key = "";
        row = board.rows();
        col = board.cols();
        marked = new boolean[row * col];
        this.board = board;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                dfs(key + board.getLetter(i, j), i, j);
            }
        }
        return setWords;
    }

    private void dfs(String key, int i, int j) {
        marked[oneDtoTwoD(i, j)] = true;
        if (!testTries.containsPrefix(key.toString())) {
            marked[oneDtoTwoD(i, j)] = false;
            return;
        }
        if (key.length() >= 3 && testTries.contains(key)) {
            setWords.add(dictionary[testTries.get(key)]);
        }
        if (i != 0) {
            if (!marked[oneDtoTwoD(i - 1, j)]) {
                dfs(key + board.getLetter(i - 1, j), i - 1, j);
            }
        }
        if (i != 0 && j != col - 1) {
            if (!marked[oneDtoTwoD(i - 1, j + 1)]) {
                dfs(key + board.getLetter(i - 1, j + 1), i - 1, j + 1);
            }
        }
        if (j != col - 1) {
            if (!marked[oneDtoTwoD(i, j + 1)]) {
                dfs(key + board.getLetter(i, j + 1), i, j + 1);
            }
        }
        if (i != row - 1 && j != col - 1) {
            if (!marked[oneDtoTwoD(i + 1, j + 1)]) {
                dfs(key + board.getLetter(i + 1, j + 1), i + 1, j + 1);
            }
        }
        if (i != row - 1) {
            if (!marked[oneDtoTwoD(i + 1, j)]) {
                dfs(key + board.getLetter(i + 1, j), i + 1, j);
            }
        }
        if (i != row - 1 && j != 0) {
            if (!marked[oneDtoTwoD(i + 1, j - 1)]) {
                dfs(key + board.getLetter(i + 1, j - 1), i + 1, j - 1);
            }
        }
        if (j != 0) {
            if (!marked[oneDtoTwoD(i, j - 1)]) {
                dfs(key + board.getLetter(i, j - 1), i, j - 1);
            }
        }
        if (j != 0 && i != 0) {
            if (!marked[oneDtoTwoD(i - 1, j - 1)]) {
                dfs(key + board.getLetter(i - 1, j - 1), i - 1, j - 1);
            }
        }
        marked[oneDtoTwoD(i, j)] = false;
    }

    private int oneDtoTwoD(int i, int j) {
        return i * row + j;
    }

    public int scoreOf(String word) {
        if (word != null && testTries.contains(word)) {
            int i = word.length();
            if (i < 3) {
                return 0;
            }
            if (i >= 3 && i <= 4) return 1;
            if (i == 5) return 2;
            if (i == 6) return 3;
            if (i == 7) return 5;
            if (i >= 8) return 11;
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
