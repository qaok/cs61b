import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    
    // 编写一个名为SearchNode的类， 该类具备hashcode()方法和 priority值
    private static class SearchNode {
        private GraphDB.Node node;
        private double priority;
        
        public SearchNode(GraphDB.Node node, double priority) {
            this.node = node;
            this.priority = priority;
        }
        
        @Override
        public boolean equals(Object o) {  // 重写equals()方法
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SearchNode searchNode = (SearchNode) o;
            return this.node.getId().equals(searchNode.node.getId());
        }
        
        @Override
        public int hashCode() {          // 覆盖hashcode()方法
            return node.getId().hashCode();
        }
    }
    
    private static class SearchNodeComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            SearchNode s1 = (SearchNode) o1;
            SearchNode s2 = (SearchNode) o2;
            return Double.compare(s1.priority, s2.priority);
        }
    }
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    
    /**private void sss(GraphDB g, double stlon, double stlat,
                     double destlon, double destlat) {
        
    }*/
    
    /**  @source https://docs.google.com/presentation/d/1ggqEZJdNX4Rif99997Pqww
     * OYnfzEe6NUa6qg76joC3w/edit#slide=id.g1dd12bbfa5_81_107      approach 3x
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        List<Long> stPath = new ArrayList<>();
        // stLong和destLong分别为st和dest的最近点的id,为long格式
        long stLong = g.closest(stlon, stlat);
        long destLong = g.closest(destlon, destlat);
        // 新建distTo(d)，edgeTo(“Parent” of every vertex)，marked，PriorityQueue(fringe)
        Map<String, Double> distTo = new HashMap<>();
        Map<String, String> edgeTo = new HashMap<>();
        Set<String> marked = new HashSet<>();
        // new SearchNodeComparator() means define a Comparator<SearchNode> and pass it to the queue object
        PriorityQueue<SearchNode> pq = new PriorityQueue<>(new SearchNodeComparator());
        // 此处的star和target是stLong和destLong由long格式转为String格式后
        GraphDB.Node start = g.getNode(Long.toString(stLong));
        GraphDB.Node target = g.getNode(Long.toString(destLong));
        
        // g(n) is the shortest known path distance from s
        double dis = 0.0;
        // h(n) is the heuristic distance, the great-circle distance from n to t
        double h = g.distance(stLong, destLong);
        distTo.put(start.getId(), dis);
        // f(n) = g(n) + h(n)
        pq.add(new SearchNode(start, dis + h));
        
        while (!pq.isEmpty()) {
            SearchNode curN = pq.poll();      // 删除pq中的SearchNode并赋值curN
            String curID = curN.node.getId(); // 根据curN赋值curID
            marked.add(curID);                // 标记curID, curID即为v
            if (curID.equals(target.getId())) {
                break;
            }
            for (String w: curN.node.getNeighbors()) { // w为v的neighbors
                GraphDB.Node node = g.getNode(w);
                // 计算h(w)
                h = g.distance(Long.parseLong(w), Long.parseLong(target.getId()));
                // d(s,v) + d(v,w)
                dis = distTo.get(curID)
                        + g.distance(Long.parseLong(curID), Long.parseLong(w));
                // 如果d(s,v) + d(v,w) < d(s,w) 或者是distTo中没有w时
                if (!distTo.containsKey(w) || dis < distTo.get(w)) {
                    distTo.put(w, dis);       // 将w与其distance放入distTo中
                    edgeTo.put(w, curID);     // 记录w的parent为v(curID)
                    SearchNode neighborw = new SearchNode(node, distTo.get(w) + h);
                    if (pq.contains(neighborw)) { // 如果pq中已有w，则改变其priority
                        neighborw.priority = distTo.get(w) + h;
                    }
                    if (!marked.contains(w)) {    // 如果w未被标记
                        pq.add(neighborw);
                    }
                }
            }
        }
        
        String tId = target.getId();         // 将targetID赋值为tID
        while (!tId.equals(start.getId())) { // 只要tId不为startId
            stPath.add(Long.parseLong(tId));
            tId = edgeTo.get(tId);          // 不停的找parent
            if (tId == null) {              // parent为null时停止
                break;
            }
        }
        stPath.add(Long.parseLong(start.getId()));  // 最后加上startId
        Collections.reverse(stPath);         // 最后需要反过来
        
        return stPath;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        return null; // FIXME
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
