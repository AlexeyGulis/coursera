package percolation.montekarlo;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    private int countOpenSites;
    private WeightedQuickUnionUF monteKarlo;
    private int size;
    private int arraySitesOpen[];

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        monteKarlo = new WeightedQuickUnionUF(n * n);
        arraySitesOpen = new int[n * n];
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) throws IllegalArgumentException {
        if (row <= 0 || col <= 0 || row > size || col > size) {
            throw new IllegalArgumentException();
        }
        arraySitesOpen[(row - 1) * size + (col - 1)] = 1;
        if (row != 1) {
            if (this.isOpen(row - 1, col)) {
                monteKarlo.union((row - 1) * size + (col - 1), (row - 2) * size + (col - 1));
            }
        }
        if (row != size) {
            if (this.isOpen(row + 1, col)) {
                monteKarlo.union((row - 1) * size + (col - 1), row * size + (col - 1));
            }
        }
        if (col != 1) {
            if (this.isOpen(row, col - 1)) {
                monteKarlo.union((row - 1) * size + (col - 1), (row - 1) * size + (col - 2));
            }
        }
        if (col != size) {
            if (this.isOpen(row, col + 1)) {
                monteKarlo.union((row - 1) * size + (col), (row - 1) * size + (col));
            }
        }
        countOpenSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) throws IllegalArgumentException {
        if (row <= 0 || col <= 0 || row > size || col > size) {
            throw new IllegalArgumentException();
        }
        if (arraySitesOpen[(row - 1) * size + (col - 1)] == 0) {
            return false;
        } else return true;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) throws IllegalArgumentException {
        if (row <= 0 || col <= 0 || row > size || col > size) {
            throw new IllegalArgumentException();
        }
        if (this.isOpen(row, col)) {
            int temp = monteKarlo.find((row - 1) * size + (col - 1));
            for (int i = 0; i < size; i++) {
                if (temp == monteKarlo.find(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return countOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        for (int i = 1; i <= size; i++) {
            if (this.isFull(size, i)) {
                return true;
            }
        }
        return false;
    }

    public void printArray() {
        for (int i = 0; i < size * size; i++) {
            if ((i + size) % size == 0) {
                System.out.println();
            }
            if (this.isFull((i + size) / size,  (i + size) % size == 0 ? 1 : (i + size) % size + 1)) {
                System.out.print(" = ");
            } else if (this.isOpen((i + size) / size,  (i + size) % size == 0 ? 1 : (i + size) % size + 1)) {
                System.out.print(" + ");
            } else {
                System.out.print(" - ");
            }
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 10;
        Percolation percolation = new Percolation(n);
        percolation.printArray();
        int countOpenSites = 48;
        int i = 0;
        while(i < countOpenSites){
            int row = StdRandom.uniform(1,n + 1);
            int col = StdRandom.uniform(1,n + 1);
            if(percolation.isOpen(row,col)){
                continue;
            }else{
                percolation.open(row,col);
                i++;
            }
        }
        System.out.println();
        System.out.println("Open " + percolation.numberOfOpenSites() + " sites!");
        percolation.printArray();
        System.out.println();
        System.out.println("Percolates ? - " + percolation.percolates());
    }
}
