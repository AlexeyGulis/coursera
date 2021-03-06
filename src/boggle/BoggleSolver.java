package boggle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;


import java.util.TreeSet;

public class BoggleSolver {

    private String[] dictionary;
    private TrieSet testTries;
    private int row;
    private int col;
    private boolean[] marked;
    private BoggleBoard board;
    private TreeSet<String> setWords;

    public BoggleSolver(String[] dictionary) {
        testTries = new TrieSet();
        this.dictionary = new String[dictionary.length];
        for (int i = 0; i < dictionary.length; i++) {
            this.dictionary[i] = dictionary[i];
            testTries.add(dictionary[i]);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        setWords = new TreeSet<>();
        row = board.rows();
        col = board.cols();
        marked = new boolean[row * col];
        this.board = board;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board.getLetter(i, j) != 'Q') {
                    dfs(new StringBuilder().append(board.getLetter(i, j)), i, j, null);
                } else {
                    dfs(new StringBuilder().append("QU"), i, j, null);
                }
            }
        }
        return setWords;
    }

    private void dfs(StringBuilder key, int i, int j, TrieSet.Node temp) {
        marked[oneDtoTwoD(i, j)] = true;
        if (key.length() >= 3) {
            if (temp == null) {
                temp = testTries.containsPrefix(key.toString());
            } else {
                temp = testTries.containsPrefix(temp, key.toString(), key.length() - 1);
            }
            if (temp == null) {
                marked[oneDtoTwoD(i, j)] = false;
                return;
            }
        }
        if (key.length() >= 3 && testTries.contains(key.toString())) {
            setWords.add(key.toString());
        }
        if (i != 0 && !marked[oneDtoTwoD(i - 1, j)]) {
            if (board.getLetter(i - 1, j) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i - 1, j)), i - 1, j, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i - 1, j, null);
            }
        }
        if (i != 0 && j != col - 1 && !marked[oneDtoTwoD(i - 1, j + 1)]) {
            if (board.getLetter(i - 1, j + 1) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i - 1, j + 1)), i - 1, j + 1, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i - 1, j + 1, null);
            }
        }
        if (j != col - 1 && !marked[oneDtoTwoD(i, j + 1)]) {
            if (board.getLetter(i, j + 1) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i, j + 1)), i, j + 1, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i, j + 1, null);
            }
        }
        if (i != row - 1 && j != col - 1 && !marked[oneDtoTwoD(i + 1, j + 1)]) {
            if (board.getLetter(i + 1, j + 1) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i + 1, j + 1)), i + 1, j + 1, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i + 1, j + 1, null);
            }
        }
        if (i != row - 1 && !marked[oneDtoTwoD(i + 1, j)]) {
            if (board.getLetter(i + 1, j) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i + 1, j)), i + 1, j, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i + 1, j, null);
            }
        }
        if (i != row - 1 && j != 0 && !marked[oneDtoTwoD(i + 1, j - 1)]) {
            if (board.getLetter(i + 1, j - 1) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i + 1, j - 1)), i + 1, j - 1, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i + 1, j - 1, null);
            }
        }
        if (j != 0 && !marked[oneDtoTwoD(i, j - 1)]) {
            if (board.getLetter(i, j - 1) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i, j - 1)), i, j - 1, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i, j - 1, null);
            }
        }
        if (j != 0 && i != 0 && !marked[oneDtoTwoD(i - 1, j - 1)]) {
            if (board.getLetter(i - 1, j - 1) != 'Q') {
                dfs(new StringBuilder().append(key).append(board.getLetter(i - 1, j - 1)), i - 1, j - 1, temp);
            } else {
                dfs(new StringBuilder().append(key).append("QU"), i - 1, j - 1, null);
            }
        }
        marked[oneDtoTwoD(i, j)] = false;
    }

    private int oneDtoTwoD(int i, int j) {
        return i * col + j;
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
