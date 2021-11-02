package percolation.montekarlo;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFINDANCECONST = 1.96;

    private final double[] array;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Invalid arguments: n or T");
        }
        array = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!percolation.isOpen(row, col)) {
                    percolation.open(row, col);
                }
            }
            array[i] = 1.0 * percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(array);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return array.length == 1 ? Double.NaN : StdStats.stddev(array);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFINDANCECONST * stddev() / Math.sqrt(array.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFINDANCECONST * stddev() / Math.sqrt(array.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats s = new PercolationStats(n, t);
        StdOut.println("mean                    = " + s.mean());
        StdOut.println("stddev                  = " + s.stddev());
        StdOut.println("95% confidence interval = [" + s.confidenceLo()
                + ", " + s.confidenceHi() + "]");
    }
}
