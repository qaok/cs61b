import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private Map<String, Node> nodes = new HashMap<>();
    private Map<String, Edge> edges = new HashMap<>();
   
    // Node class
    class Node {
        private String id;
        private double lon, lat;
        private String location;
        private List<String> neighbors = new ArrayList<>();
        private List<String> edges = new ArrayList<>();
        
        public Node(String id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
        }
        
        public void connect(String nid) {   // neighbors list中加入nid
            neighbors.add(nid);
        }
        
        public void setEdge(String eid) {   // edges list中加入nid
            edges.add(eid);
        }
        
        public void setLocation(String location) { // 确定location
            this.location = location;
        }
        
        public String getId() {
            return id;
        }
        
        public List<String> getNeighbors() {
            return neighbors;
        }
        
        public List<String> getEdges() {
            return edges;
        }
        
        public double getLon() {
            return lon;
        }
        
        public double getLat() {
            return lat;
        }
    }
    
    // Edge class
    class Edge {
        private String id;
        private String name;
        private String maxspeed;
        private boolean valid;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public void setMaxspeed(String maxspeed) {
            this.maxspeed = maxspeed;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }
    
    public Node getNode(String id) { // 得到node.id
        return nodes.get(id);
    }
    
    public void addNode(Node node) { // 添加node
        nodes.put(node.id, node);
    }
    
    public void addEdge(Edge edge) { // 添加edge
        edges.put(edge.id, edge);
    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // Your code here.
        List<String> alones = new ArrayList<>();
        for (String id : nodes.keySet()) {
            if (nodes.get(id).neighbors.isEmpty()) {
                alones.add(id);
            }
        }
        for (String id : alones) {
            nodes.remove(id);
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        List<Long> vertices = new ArrayList<>();
        for (String id : nodes.keySet()) {
            vertices.add(Long.parseLong(id));
        }
        return vertices;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        List<Long> vertices = new ArrayList<>();
        Node node = nodes.get(Long.toString(v));
        for (String id : node.neighbors) {
            vertices.add(Long.parseLong(id));
        }
        return vertices;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double min = Integer.MAX_VALUE;
        String minId = null;
        for (String id : nodes.keySet()) {
            Node node = nodes.get(id);
            double nlon = node.lon;
            double nlat = node.lat;
            if (distance(lon, lat, nlon, nlat) < min) {
                min = distance(lon, lat, nlon, nlat);
                minId = node.id;
            }
        }
        return Long.parseLong(minId);
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        String id = Long.toString(v);
        return nodes.get(id).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        String id = Long.toString(v);
        return nodes.get(id).lat;
    }
}
