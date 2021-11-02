package percolation.montekarlo;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private int countOpenSites;
    private final WeightedQuickUnionUF monteKarlo;
    private final WeightedQuickUnionUF backwashMonteKarlo;
    private final int size;
    private final boolean[] arraySitesOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Invalid argument n : must be >= 1");
        }
        size = n;
        monteKarlo = new WeightedQuickUnionUF(n * n + 2);
        backwashMonteKarlo = new WeightedQuickUnionUF(n * n + 2);
        arraySitesOpen = new boolean[n * n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        checkRange(row, col);
        if (arraySitesOpen[xyTo1D(row, col)]) {
            return;
        } else {
            arraySitesOpen[xyTo1D(row, col)] = true;
        }
        if (row != 1) {
            if (isOpen(row - 1, col)) {
                monteKarlo.union(xyTo1D(row, col), xyTo1D(row - 1, col));
                backwashMonteKarlo.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            }
        } else {
            monteKarlo.union(xyTo1D(row, col), size * size);
            backwashMonteKarlo.union(xyTo1D(row, col), size * size);
        }
        if (row != size) {
            if (isOpen(row + 1, col)) {
                monteKarlo.union(xyTo1D(row, col), xyTo1D(row + 1, col));
                backwashMonteKarlo.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            }
        } else {
            backwashMonteKarlo.union(xyTo1D(row, col), size * size + 1);
        }
        if (col != 1) {
            if (isOpen(row, col - 1)) {
                monteKarlo.union(xyTo1D(row, col), xyTo1D(row, col - 1));
                backwashMonteKarlo.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            }
        }
        if (col != size) {
            if (this.isOpen(row, col + 1)) {
                monteKarlo.union(xyTo1D(row, col), xyTo1D(row, col + 1));
                backwashMonteKarlo.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            }
        }
        countOpenSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return arraySitesOpen[xyTo1D(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return isOpen(row, col) && (monteKarlo.find(size * size) == monteKarlo.find(xyTo1D(row, col)));
    }

    private void checkRange(int row, int col) {
        if (row <= 0 || col <= 0 || row > size || col > size) {
            throw new IllegalArgumentException("Invalid row and column: must be >=1 and <= size");
        }
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return countOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return backwashMonteKarlo.find(size * size) == backwashMonteKarlo.find(size * size + 1);
    }
}
