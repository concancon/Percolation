import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    int t;
    int n;
    double trialsToPercolation;
    int sitesNumber;
    double average = 0.0;
    double totalThresholds = 0.0;
    double[] percolationThreshold;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int t) {

        //throw an IllegalArgumentException in the constructor if either n ≤ 0 or trials ≤ 0.
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException("either n or trials was let than 0");
        }
        this.n = n;
        this.t = t;
        percolationThreshold = new double[t];
        sitesNumber = n * n;

        for (int i = 0; i < t; i++) {
            Percolation percolation = new Percolation(n);
            trialsToPercolation = 0.0;
            while (!percolation.percolates()) {

                //     Choose a site uniformly at random among all blocked sites.
                //Returns a random real number uniformly in [a, b).
                int x = StdRandom.uniform(1, n + 1);
                int y = StdRandom.uniform(1, n + 1);
                //Open the site.Pick a site at random
                // and use this site if it is blocked; if not, repeat.
                if (!percolation.isOpen(x, y)) {
                    //these are the collisions
                    trialsToPercolation = trialsToPercolation + 1;
                    percolation.open(x, y);
                }
            }
            double threshold = trialsToPercolation / sitesNumber;
            percolationThreshold[i] = threshold;


        }
        System.out.println("mean: " + mean());
        System.out.println("std dev: " + stddev());
        System.out.println(
                "95% confidence interval: [" + confidenceLo() + ", " + confidenceHi() + "]");
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(this.percolationThreshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(this.percolationThreshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(this.t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(this.t));
    }

    // test client (see below)


    public static void main(String[] args) {
      /*  int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, t);*/

    }
}
