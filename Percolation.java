import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {


    //the n by n grid
    int[][] grid;
    //the library implementation of WeightedQuickUnionFind
    WeightedQuickUnionUF quick;
    // a matrix to represent which sites are open
    boolean[][] isOpen;
    // a matrix to represent which sites are full
    boolean[][] isFull;

    int virtualTop;
    int virtualBottom;

    private void validate(int row, int col) {
        int max = this.grid.length;

        if (row < 0 || row >= max || col < 0 || col >= max) {
            throw new IllegalArgumentException(
                    "either row or col is not between 0 and " + (max - 1));
        }

    }

    private boolean isInTopRow(int row) {
        return row == 0;
    }

    private boolean isInBottomRow(int row) {
        return row == grid.length;
    }

    private boolean isInLeftColumn(int col) {
        return col == 0;
    }

    private boolean isInRightColumn(int col) {
        return col == grid[0].length - 1;
    }

    private int xyTo1D(int row, int col) {
        return row * this.grid[0].length + col;

    }

    public static boolean contains(final int[] array, final int v) {

        boolean result = false;

        for (int i : array) {
            if (i == v) {
                result = true;
                break;
            }
        }

        return result;
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        quick = new WeightedQuickUnionUF(n * n);
        this.grid = new int[n][n]; // [x][y]
        this.isOpen = new boolean[n][n];
        this.isFull = new boolean[n][n];
        int initializer = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j]
                        = initializer++;
                isOpen[i][j] = false;
            }
        }


        //introduce virtual sites to simplify
        //initalize virtual nodes
        virtualTop = 0;
        virtualBottom = (grid[0].length * grid.length) - 1;
        //leave virtualTop and virtualBottom disconnected from grid at startup


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        // First, it should validate the indices of the site that it receives.

        validate(row, col);

        // Second, it should somehow mark the site as open.
        isOpen[row][col] = true;
        // Third, it should perform some sequence of WeightedQuickUnionUF operations
        if (isInLeftColumn(col)) {
            //dont check the left neighbor
        }
        //if left neighbor is open
        else if (isOpen[row][col - 1]) {
            quick.union(grid[row][col - 1], grid[row][col]);
        }

        if (isInRightColumn(col)) {
            //dont check the right neighbor
        }
        //if right neighbor is open
        else if (isOpen[row][col + 1]) {
            quick.union(grid[row][col + 1], grid[row][col]);
        }
        //special measures for top and bottom
        //connect to virtual top if on top row and open
        if (isInTopRow(row)) {
            int siteAsIndex = xyTo1D(row, col);
            quick.union(virtualTop, siteAsIndex);
        }
        //if top neighbor is open
        else if (isOpen[row - 1][col]) {
            quick.union(grid[row - 1][col], grid[row][col]);
        }


        if (isInBottomRow(row)) {
            int siteAsIndex = xyTo1D(row, col);
            quick.union(virtualTop, siteAsIndex);
        }
        //if bottom if open
        else if (isOpen[row + 1][col]) {
            quick.union(grid[row + 1][col], grid[row][col]);
        }


    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return isOpen[row][col];
    }

    // is the site (row, col) full?
    //A full site is an open site that can
    // be connected to an open site in the
    // top row via a chain of neighboring (left, right, up, down) open sites
    public boolean isFull(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {

            boolean isFull = false;
            //now we want to know if its connected to any site on the top row

            int siteAsIndex = xyTo1D(row, col);
            return quick.connected(siteAsIndex, this.virtualTop);


        }
        return false;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int openSites = 0;
        for (int i = 0; i < grid.length; i++) {

            for (int j = 0; j < grid[0].length; j++) {

                if (isOpen(i, j)) {
                    openSites++;
                }

            }
        }
        return openSites;

    }

    // does the system percolate?
    public boolean percolates() {
        // //the system percolates if the root of one of the bottom sites
        // //is a top site
        // int[] toprow = new int[this.grid[0].length];
        //
        // //first lets get all sites on the top row into an array:
        // for (int i = 0; i < grid[0].length; i++) {
        //     toprow[i] = grid[0][i];
        // }
        //
        // System.out.println("length of grid[0] " + grid[0].length);
        // for (int i = 0; i < grid[0].length; i++) {
        //     //iterate through bottom row and get all its roots
        //     int rootOfBottomElement = quick.find(grid[grid[0].length - 1][i]);
        //
        //     if (contains(toprow, rootOfBottomElement)) {
        //         return true;
        //     }
        //
        // }
        // return false;


        return quick.connected(virtualTop, virtualBottom);

    }


    public static void main(String[] args) {


        Percolation percolation = new Percolation(4);
        //open(1, 1) and open(1, 2)
        percolation.open(0, 2);
        percolation.open(1, 2);
        percolation.open(2, 2);

        int siteAsIndex = percolation.xyTo1D(0, 0);
        int siteAsIndexTWO = percolation.xyTo1D(1, 1);
        int siteAsIndexTHREE = percolation.xyTo1D(2, 2);

        //int second = percolation.xyTo1D(0, 2);
        System.out.println("is 2 2 connected to virtual top? " + percolation.quick
                .connected(siteAsIndexTHREE, percolation.virtualTop));
        System.out.println("if so it must be full, right? " + percolation.isFull(2, 2));


    }
}
