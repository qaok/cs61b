package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;
import java.util.Arrays;

public class Board implements WorldState{
    private final int N;
    private int[][] board;
    private final int BLANK = 0;
    
    /**Constructs a board from an N-by-N array of tiles where
    tiles[i][j] = tile at row i, column j*/
    public Board(int[][] tiles) {
        N = tiles.length;     // 表示二维数组tiles有N个一维数组
        board = new int[N][N];
        for (int i = 0; i < N; i += 1) {      // 遍历二维数组并copy
            for (int j = 0; j < N; j += 1) {
                board[i][j] = tiles[i][j];
            }
        }
    }
    
    /**Returns value of tile at row i, column j (or 0 if blank)*/
    public int tileAt(int i, int j) {
        if (i <0 || i >= N || j < 0 || j >= N) {
            throw new IndexOutOfBoundsException();
        }
        return board[i][j];
    }
    
    /**Returns the board size N*/
    public int size() {
        return N;
    }
    
    /**Returns the neighbors of the current board
     * @source http://joshh.ug/neighbors.html
     */
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int n = size();
        int row = -1;
        int col = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tileAt(i, j) == BLANK) {  // 找出空白处（即0）最初所在的行列数
                    row = i;
                    col = j;
                }
            }
        }
        int[][] boardTiles = new int[n][n];
        for (int i = 0; i < n; i++) {         // 遍历二维数组并copy
            for (int j = 0; j < n; j++) {
                boardTiles[i][j] = tileAt(i, j);
            }
        }
        for (int i = 0; i < n; i += 1) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(-row + i) + Math.abs(j - col) - 1 == 0) {
                    // swap blank tile and a near tile
                    boardTiles[row][col] = boardTiles[i][j];
                    boardTiles[i][j] = BLANK;
                    Board neighbor = new Board(boardTiles);
                    neighbors.enqueue(neighbor);
                    // then swap back for searching next neighbor
                    boardTiles[i][j] = boardTiles[row][col];
                    boardTiles[row][col] = BLANK;
                }
            }
        }
        return neighbors;
    }
    
    private int xyTo1D(int i, int j) {
        return i * N + j + 1;
    }
    
    /**Hamming estimate described below*/
    public int hamming() {
        int ditance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == BLANK) {  // 跳过空白处
                    continue;
                }
                if (board[i][j] != xyTo1D(i, j)) {
                    ditance++;
                }
            }
        }
        return ditance;
    }
    
    private int[] intToXY(int s) {
        return new int[] { (s - 1) / N, (s - 1) % N };
    }
    
    private int getXYDiff(int s, int i, int j) {
        int res = 0;
        int[] rightPos = intToXY(s);
        res += rightPos[0] > i ? rightPos[0] - i : i - rightPos[0];
        res += rightPos[1] > j ? rightPos[1] - j : j - rightPos[1];
        return res;
    }
    
    /**Manhattan estimate described below*/
    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == BLANK) {
                    continue;
                }
                if (board[i][j] != xyTo1D(i, j)) {
                    distance += getXYDiff(board[i][j], i, j);
                }
            }
        }
        return distance;
    }
    
    /**Estimated distance to goal. This method should
     * simply return the results of manhattan() when submitted to
     * Gradescope.
     */
    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    
    /**Returns true if this board's tile values are the same
     * position as y's
     */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board board1 = (Board) y;
        if (N != board1.N) {
            return false;
        }
        return Arrays.deepEquals(board, board1.board);
    }
    
    @Override
    public int hashCode() { // 覆盖equals()就必须覆盖hashCode()
        return super.hashCode();
    }
    
    /** Returns the string representation of the board.*/
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
