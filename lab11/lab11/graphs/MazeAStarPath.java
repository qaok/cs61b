package lab11.graphs;

import java.util.PriorityQueue;
import java.util.Comparator;
/**
 *  @author qaok
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private int targetX;
    private int targetY;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        this.targetX = targetX;
        this.targetY = targetY;
        distTo[s] = 0;
        edgeTo[s] = s;
    }
    
    private class Node {
        private int value;
        private int priority;
        
        public Node(int v) {
            this.value = v;
            // 此处的优先级为v到source的距离 + v到target的距离
            this.priority = distTo[v] + h(v);
        }
    }
    
    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.priority - o2.priority;
        }
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int curX = maze.toX(v);
        int curY = maze.toY(v);
        return Math.abs(curX - targetX) + Math.abs(curY - targetY);
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());
        Node node = new Node(s);
        pq.add(node);
        marked[s] = true;
        
        while (!pq.isEmpty()) {
            node = pq.poll();
            for (int w : maze.adj(node.value)) {
                if (!marked[w]) {
                    edgeTo[w] = node.value;
                    distTo[w] = distTo[node.value] + 1;
                    marked[w] = true;
                    announce();
                    if (w == t) {
                        targetFound = true;
                        return;
                    }
                    pq.add(new Node(w));
                }
            }
        }
        
    }

    @Override
    public void solve() {
        astar(s);
    }

}

