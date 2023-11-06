package hw2;

import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] doubles;       // 双精度数组
    private int T;
    
    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        doubles = new double[T];
        for (int i = 0; i < T; i += 1) {
            Percolation per = pf.make(N);
            while (!per.percolates()) {         // 在未渗流时，随机生成行列号，并开锁
                int row = StdRandom.uniform(N); // 返回一个统一在 [0, N) 中的随机整数。
                int col = StdRandom.uniform(N);
                per.open(row, col);
            }
            doubles[i] = per.numberOfOpenSites() * 1.0 / (N * N);
        }
    }
    
    public double mean() {
        return StdStats.mean(doubles);   // 返回指定数组中的平均值
    }
    public double stddev() {
        return StdStats.stddev(doubles); // 返回指定数组中的样本标准差。
    }
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

}
