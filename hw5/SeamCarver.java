import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    int xplus, yplus;
    int xminus, yminus;
    double xR, xG, xB;
    double yR, yG, yB;
    
    private boolean illegalIndex(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException();
        }
        return (x >= 0 && x < width && y >= 0 && y < height);
    }
    
    private void energyHelper(int x, int y) {
        if (illegalIndex(x, y)) {
            xplus = x + 1;
            yplus = y + 1;
            xminus = x - 1;
            yminus = y - 1;
            if (x + 1 == width) {
                xplus = 0;
            }
            if (y + 1 == height) {
                yplus = 0;
            }
            if (x == 0) {
                xminus = width - 1;
            }
            if (y == 0) {
                yminus = height - 1;
            }
            Color xmRgb = picture.get(xminus, y);
            Color xpRgb = picture.get(xplus, y);
            Color ymRgb = picture.get(x, yminus);
            Color ypRgb = picture.get(x, yplus);
            
            xR = xpRgb.getRed() - xmRgb.getRed();
            xG = xpRgb.getGreen() - xmRgb.getGreen();
            xB = xpRgb.getBlue() - xmRgb.getBlue();
            
            yR = ypRgb.getRed() - ymRgb.getRed();
            yG = ypRgb.getGreen() - ymRgb.getGreen();
            yB = ypRgb.getBlue() - ymRgb.getBlue();
        }
    }
    
    private double findVerticalSeamHelper(int x, int y, double[][] cost) {
        if (y <= 0) {
            throw new IllegalArgumentException();
        }
        /** 此处是假定最小值为M(i, j-1)，因为此时只有row 0有energy值
         *  假定为M(i, j-1)时，可避免i = 0和i = width - 1时的indexout
         */
        double mincost = cost[y - 1][x];
        if (x > 0) {
            mincost = Math.min(mincost, cost[y - 1][x - 1]);
        }
        if (x < width - 1) {
            mincost = Math.min(mincost, cost[y - 1][x + 1]);
        }
        return mincost;
    }
    
    private int minIndex(double[] rowCost, int start, int end) {
        if (start > end) {
            throw new IndexOutOfBoundsException();
        }
        start = Math.max(start, 0);
        end = Math.min(end, rowCost.length - 1);
        int minIdx = start;
        // 此步是为了找出这一行中的所给定的start和end索引值中最小的cost的index
        // 即从start开始，到end结束，在给定范围内的最小的cost的index
        for (int i = start + 1; i <= end; i++) {
            if (rowCost[i] < rowCost[minIdx]) {
                minIdx = i;
            }
        }
        return minIdx;
    }
    
    public SeamCarver(Picture picture) {
        this.picture = picture;
        this.width = picture.width();
        this.height = picture.height();
    }
    
    public Picture picture() {  // current picture
        return picture;
    }
    
    public int width() {        // width of current picture
        return width;
    }
    
    public int height() {       // height of current picture
        return height;
    }
    
    public double energy(int x, int y) { // energy of pixel at column x and row y
        energyHelper(x, y);
        double deX = (xR * xR + xG * xG + xB * xB);
        double deY = (yR * yR + yG * yG + yB * yB);
        return deX + deY;
    }
    
    public int[] findHorizontalSeam() { // sequence of indices for horizontal seam
        Picture temp = new Picture(height(), width());  // 把picture的的高宽进行交换
        for (int i = 0; i < height(); i++) {            // 此步是改变rgb
            for (int j = 0; j < width(); j++) {
                temp.set(i, j, picture.get(j, i));
            }
        }
        SeamCarver sc = new SeamCarver(temp);
        return sc.findVerticalSeam();                   // 利用垂直接缝来得到水平接缝
    }
    
    public int[] findVerticalSeam() {   // sequence of indices for vertical seam
        double[][] cost = new double[height][width];
        for (int i = 0; i < width; i++) {
            cost[0][i] = energy(i, 0);
        }
        // 二维数组为[row][col], 即设定中的[y][x]，因此二维数组的xy与energy中的xy相反
        for (int i = 1; i < height; i++) {
            // 遍历每一行
            for (int j = 0; j < width; j++) {
                //遍历这行中的元素
                cost[i][j] = energy(j, i) + findVerticalSeamHelper(j, i, cost);
            }
        }
        
        int[] minIndexs = new int[height()];
        for (int i = 0; i < height; i++) {
            if (i == 0) {
                // 确认第一行的最小值
                minIndexs[i] = minIndex(cost[i], 0, width - 1);
            } else {
                // 在确认了第一行的最小值后，再依据其索引，给定start和end的范围进行搜索
                minIndexs[i] = minIndex(cost[i], minIndexs[i - 1] - 1,
                        minIndexs[i - 1] + 1);
            }
        }
        return minIndexs;
    }
    
    public void removeHorizontalSeam(int[] seam) { // remove horizontal seam from picture
        if (seam.length != width || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(picture, seam);
    }
    public void removeVerticalSeam(int[] seam) {   // remove vertical seam from picture
        if (seam.length != height || !isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(picture, seam);
    }
    
    private boolean isValidSeam(int[] seam) {
        for (int i = 0, j = 1; j < seam.length; i++, j++) {   // 此时作业要求禁用Math.abs
            if (seam[i] - seam[j] > 1) {
                return false;
            } else if (seam[i] - seam[j] < 0) {
                if (seam[j] - seam[i] > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}
