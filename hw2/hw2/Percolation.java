package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int opensites;
    private WeightedQuickUnionUF wquSet;  // 加权联合
    private WeightedQuickUnionUF wquSet2;
    private boolean[][] sites;     // 二维布尔数组
    private int dummyTop;          // 引入顶部虚拟节点
    private int dummyBottom;       // 引入底部虚拟节点
    private int N;

    
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N must be greater than 0.");
        }
        sites = new boolean[N][N];  // 此处默认值为false
        this.N = N;
        dummyTop = N * N;
        dummyBottom = N * N + 1;
        wquSet = new WeightedQuickUnionUF(N * N + 2);    // 有顶和底
        wquSet2 = new WeightedQuickUnionUF(N * N + 1);   // 只有顶
        opensites = 0;
    }
    
    private int xyTo1D(int row, int col) {       // 转换二维坐标为一维坐标
        return row * N + col;
    }
    
    private boolean Index(int row, int col) {   // 索引是否存在
        return (row >= 0 && row < N && col >= 0 && col < N);
    }
    
    private void judgeIndex(boolean Index) {    // 索引是否正确
        if (!Index) {
            throw new IndexOutOfBoundsException();
        }
    }
    
    private void unionNeighbor(int row, int col, int newrow, int newcol) {
        if (newrow < 0 || newrow >= N || newcol < 0 || newcol >= N) {
            return;
        }
        if (sites[newrow][newcol]) {
            int past = xyTo1D(row, col);
            int cur = xyTo1D(newrow, newcol);
            wquSet.union(past, cur);
            wquSet2.union(past, cur);
        }
    }
    
    public void open(int row, int col) {
        judgeIndex(Index(row, col));       // 判断索引
        if (!sites[row][col]) {            // 开锁
            sites[row][col] = true;
            opensites += 1;
        }
        if (row == 0) {                    // 接水
            wquSet.union(dummyTop, xyTo1D(row, col));
        }
        if (row == N - 1) {                // 准备渗流
            wquSet.union(dummyBottom, xyTo1D(row, col));
        }
        if (row == 0) {                    // 接水
            wquSet2.union(dummyTop, xyTo1D(row, col));
        }
        // 连接上下左右
        unionNeighbor(row, col, row - 1, col);
        unionNeighbor(row, col, row + 1, col);
        unionNeighbor(row, col, row, col - 1);
        unionNeighbor(row, col, row, col + 1);
    }
    
    
    public boolean isOpen(int row, int col) {    // 锁开没
        judgeIndex(Index(row, col));
        return sites[row][col];
    }
    
    public boolean isFull(int row, int col) {    // 来水没
        judgeIndex(Index(row, col));
        int num = xyTo1D(row, col);
        return (isOpen(row, col) && wquSet2.connected(dummyTop, num));
    }
    
    public int numberOfOpenSites() {
        return opensites;
    }
    
    public boolean percolates() {
        return wquSet.connected(dummyTop, dummyBottom);  // 此方法存在一个问题，即反渗流，未被连到的底部也会渗流————已解决
    }
    
    /** 解决的原理：
     * 就是新建两个加权联合set，一个有顶有底，一个只有顶，
     * 判定是否渗流的时候用有顶有底的，判定是否来水的时候，
     * 就用只有顶的那个set，这样就能避免反渗流。
     *
     * 因为如果已经成功渗流的话，有顶有底的那个set，此时
     * 已经完全渗流（即使未与顶相连），所以在判定新的小方格
     * isFull的时候就要启用只有顶的set，来避免反渗流。
     *
     * 这么做是因为我们采用的两个虚拟节点已经完成相交，后续
     * 新建的小方格只要与其中任意一个相连，就会来水，所以要
     * 新建判定set。
     */
    
    public static void main(String[] args) {
        Percolation p = new Percolation(8);
    }
}
