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
    int openSites;

    private void validate(int row, int col) {

        //row = convertToZeroBased(row);
        //col = convertToZeroBased(col);

        int max = this.grid.length;

        if (row < 0 || row >= max) {
            throw new IllegalArgumentException(
                    "row is not between 0 and " + (max - 1));
        }
        if (col < 0 || col >= max) {
            throw new IllegalArgumentException(
                    "col is not between 0 and " + (max - 1));

        }

    }

    private int convertToZeroBased(int arg) {
        return arg - 1;
    }

    private int[] convertToZeroBased(int row, int col) {
        int[] result = { row - 1, col - 1 };
        return result;
    }

    private boolean isInTopRow(int row) {
        //row = convertToZeroBased(row);

        return row == 0;
    }

    private boolean isInBottomRow(int row) {
        //row = convertToZeroBased(row);
        return row == grid.length - 1;
    }

    private boolean isInLeftColumn(int col) {
        //col = convertToZeroBased(col);
        return col == 0;
    }

    private boolean isInRightColumn(int col) {
        //col = convertToZeroBased(col);
        return col == grid[0].length - 1;
    }

    private int xyTo1D(int row, int col) {
        //row = convertToZeroBased(row);
        //col = convertToZeroBased(col);

        return ((row) * this.grid[0].length) + col;

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
        this.openSites = 0;
        int initializer = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < n; j++) {
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

        row = convertToZeroBased(row);
        col = convertToZeroBased(col);

        // First, it should validate the indices of the site that it receives.
        validate(row, col);

        // Second, it should somehow mark the site as open.
        this.isOpen[row][col] = true;
        this.openSites++;
        // Third, it should perform some sequence of WeightedQuickUnionUF operations
        //we convert to 1d
        int siteAsIndex = xyTo1D(row, col);


        if (isInLeftColumn(col)) {
            //dont check the left neighbor
        }
        //if left neighbor is open
        else if (isOpen[row][col - 1]) {
            int leftNeighbor = xyTo1D(row, col - 1);
            quick.union(leftNeighbor, siteAsIndex);
        }

        if (isInRightColumn(col)) {
            //dont check the right neighbor
        }
        //if right neighbor is open
        else if (isOpen[row][col + 1]) {
            int rightNeighbor = xyTo1D(row, col + 1);
            quick.union(rightNeighbor, siteAsIndex);
        }
        //special measures for top and bottom
        //connect to virtual top if on top row and open
        if (isInTopRow(row)) {
            quick.union(virtualTop, siteAsIndex);
        }
        //if top neighbor is open
        else if (isOpen[row - 1][col]) {
            int topNeighbor = xyTo1D(row - 1, col);
            quick.union(topNeighbor, siteAsIndex);
        }

        if (isInBottomRow(row)) {
            quick.union(virtualBottom, siteAsIndex);
        }
        //if bottom if open
        else if (isOpen[row + 1][col]) {
            int bottomNeighbor = xyTo1D(row + 1, col);
            quick.union(bottomNeighbor, siteAsIndex);
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        row = convertToZeroBased(row);
        col = convertToZeroBased(col);
        validate(row, col);

        return isOpen[row][col];
    }

    // is the site (row, col) full?
    //A full site is an open site that can
    // be connected to an open site in the
    // top row via a chain of neighboring (left, right, up, down) open sites
    public boolean isFull(int row, int col) {
        row = convertToZeroBased(row);
        col = convertToZeroBased(col);

        //validate(row, col);
        if (isOpen[row][col]) {
            // a little optimization can be done by keeping track of full sites
            if (isFull[row][col]) {
                //this means that we have already marked this one as full
                return true;

            }
            //now we want to know if its connected to any site on the top row

            int siteAsIndex = xyTo1D(row, col);
            boolean result = quick.connected(siteAsIndex, this.virtualTop);
            isFull[row][col] = result; // add to our matrix so we dont have to look it up again
            return result;

        }
        return false;

    }

    // returns the number of open sites
    public int numberOfOpenSites() {

        return this.openSites;

    }

    // does the system percolate?
    public boolean percolates() {

        return quick.connected(virtualTop, virtualBottom);

    }


    public static void main(String[] args) {


        Percolation percolation = new Percolation(4);

        //open(1, 1) and open(1, 2)
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);
        percolation.open(4, 1);

        System.out.println("is 4 1 connected to virtual top? " + percolation.quick
                .connected(12, percolation.virtualTop));
        System.out.println("is 4 1 full? " + percolation.isFull(4, 1));
        System.out.println("does the system percolate? " + percolation.percolates());
        System.out.println("# of open sites " + percolation.numberOfOpenSites());
        System.out.println("is 2 1 open?  " + percolation.isOpen(2, 1));

       /*  percolation.open(2, 2);
        percolation.open(3, 2);

        int siteAsIndex = percolation.xyTo1D(0, 0);
        int siteAsIndexTWO = percolation.xyTo1D(1, 1);
        int siteAsIndexTHREE = percolation.xyTo1D(2, 2);
        int siteAsIndexFOUR = percolation.xyTo1D(3, 2);
        //int second = percolation.xyTo1D(0, 2);
        System.out.println("is 3 2 connected to virtual top? " + percolation.quick
                .connected(siteAsIndexFOUR, percolation.virtualTop));
        System.out.println("if so it must be full, right? " + percolation.isFull(3, 2));

        System.out.println("does the system percolate? " + percolation.percolates());
        System.out.println("# of open sites " + percolation.numberOfOpenSites());
        System.out.println("is 0 2 open?  " + percolation.isOpen(0, 2));*/

    }
}
