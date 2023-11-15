package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.HashSet;
import java.util.ArrayList;

public class Solver {
    // 创建SearchNode类
    private class SearchNode implements Comparable<SearchNode> {
        WorldState worldstate;
        int moves;
        SearchNode prev;
        
        public SearchNode(WorldState worldstate, int moves, SearchNode prev) {
            this.worldstate = worldstate;
            this.moves = moves;
            this.prev = prev;
        }
        
        // 覆盖compareTo方法
        @Override
        public int compareTo(SearchNode x) {     // 比较(M1 + E1) < (M2 + E2),找到最小优先级搜索节点x
            return (this.moves + this.worldstate.estimatedDistanceToGoal())
                    - (x.moves + x.worldstate.estimatedDistanceToGoal());
        }
        
    }
    
    private final MinPQ<SearchNode> pq = new MinPQ<>();  // 创建priority queue
    private final ArrayList<WorldState> solution = new ArrayList<>();
    private SearchNode F = null;
    
    /**Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        // insert an “initial search node” into the priority queue
        pq.insert(new SearchNode(initial, 0, null));
        // The algorithm then proceeds as follows:
        while (!pq.isEmpty()) {
            SearchNode X = pq.delMin();
            if (X.worldstate.isGoal()) {  // X与目标F是否一致
                F = X;
                break;
            }
            
            for (WorldState neighbor: X.worldstate.neighbors()) {
                // X的每一个邻居的moves都加1，并且将X绑定为其邻居的previous SearchNode
                SearchNode newNode = new SearchNode(neighbor, X.moves + 1, X);
                /** 避免重复添加neighbor到pq中
                 * 即在考虑搜索节点的邻居时，如果邻居的世界状态与前一个搜索节点的世界状态相同，则不要将其入列
                 */
                if (X.prev != null && neighbor.equals(X.prev.worldstate)) {
                    continue;
                }
                pq.insert(newNode);
            }
        }
        
    }
    
    /**Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState.*/
    public int moves() {
        if (F == null) {
            return -1;
        }
        return F.moves;
    }
    
    /**Returns a sequence of WorldStates from the initial WorldState
     to the solution.*/
    public Iterable<WorldState> solution() {
        SearchNode node = F;
        while (node != null) {
            solution.add(0, node.worldstate); // arraylist的index从0开始
            node = node.prev;
        }
        return solution;
    }
}
