import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private static final double ROOT_ULLAT = MapServer.ROOT_ULLAT,
            ROOT_ULLON = MapServer.ROOT_ULLON,
            ROOT_LRLAT = MapServer.ROOT_LRLAT,
            ROOT_LRLON = MapServer.ROOT_LRLON;
    private double requestedULLon, requestedULLat, requestedLRLon, requestedLRLat;
    private int xLeft, yLeft, xRight, yRight, curdepth;
    private double xDiff, yDiff, maxLevel;
    private Map<String, Object> results;
    
    // 每像素的纵向距离 (LonDPP), 此处容量为8是因为所提供的图片等级最大为7（从0开始），D = 8
    private static double[] depthLonDPP = new double[8];
    static {
        depthLonDPP[0] = (ROOT_LRLON - ROOT_ULLON) / MapServer.TILE_SIZE;
        for (int i = 1; i < 8; i++) {
            // 随着等级提高，缩放程度越来越大，因此LonDPP也越来越小
            // 此处÷2是因为呈2的倍数增长
            depthLonDPP[i] = depthLonDPP[i - 1] / 2;
        }
    }
    
    // 此方法用于查找当前所在深度等级，即D=？
    private int getDepth(double requestedLonDPP) {
        int depth = 0;
        while (requestedLonDPP < depthLonDPP[depth]) {
            depth++;
            if (depth == depthLonDPP.length - 1) {
                break;
            }
        }
        return depth;
    }
    
    // Extra Details and Corner Cases
    private boolean illegalQuery() {
        if (requestedLRLat >= ROOT_ULLAT || requestedLRLon <= ROOT_ULLON
                || requestedULLat <= ROOT_LRLAT || requestedULLon >= ROOT_LRLON
                || requestedULLon >= requestedLRLon || requestedULLat <= requestedLRLat) {
            // 此处get中的命名来自MapServer.java中的REQUIRED_RASTER_RESULT_PARAMS
            results.put("query_success", false);
            results.put("depth", 0);
            results.put("render_grid", null);
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            return true;
        }
        return false;
    }
    
    private void depthLonDPPQuery(Map<String, Double> params) {
        // 查找当前所在深度等级
        double requestedLonDPP = (requestedLRLon - requestedULLon) / params.get("w");
        curdepth = getDepth(requestedLonDPP);
        results.put("depth", curdepth);  // 放置深度等级
        // k = 2**D - 1，从0开始，此处确认dD_xk_yk间的关系
        maxLevel = Math.pow(2, curdepth);
        // 确定x和y的像素宽度
        xDiff = (ROOT_LRLON - ROOT_ULLON) / maxLevel;
        yDiff = (ROOT_LRLAT - ROOT_ULLAT) / maxLevel;
    }
    
    private void queryUpdate(double xDiff, double yDiff, double maxLevel) {
        xLeft = 0; yLeft = 0; xRight = 0; yRight = 0;
        // 根据经纬度来确认所查询的对象的x究竟在哪张图片上
        for (double x = ROOT_ULLON; x <= ROOT_LRLON; x += xDiff) {
            if (x <= requestedULLon) {
                xLeft++;
            }
            if (xRight < maxLevel && x <= requestedLRLon) {
                xRight++;
            }
        }
        // 根据经纬度来确认所查询的对象的y究竟在哪张图片上
        for (double y = ROOT_ULLAT; y >= ROOT_LRLAT; y += yDiff) {
            if (y >= requestedULLat) {
                yLeft++;
            }
            if (yRight < maxLevel && y >= requestedLRLat) {
                yRight++;
            }
        }
        // 此处减1是因为前面并未的maxLevel没减，k = 2**D - 1
        if (xLeft != 0) {
            xLeft--;
        }
        if (yLeft != 0) {
            yLeft--;
        }
        if (xRight != 0) {
            xRight--;
        }
        if (yRight != 0) {
            yRight--;
        }
    }

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        results = new HashMap<>();
        // @source 此处get中的命名来自MapServer.java中的REQUIRED_RASTER_REQUEST_PARAMS
        requestedULLon = params.get("ullon");
        requestedULLat = params.get("ullat");
        requestedLRLon = params.get("lrlon");
        requestedLRLat = params.get("lrlat");
        
        if (illegalQuery()) {
            return results;
        }
        depthLonDPPQuery(params);// 查找当前所在深度等级
        queryUpdate(xDiff, yDiff, maxLevel); // 确认xy的位置
        
        // 查询完毕，找出对应图片
        String[][] files = new String[yRight - yLeft + 1][xRight - xLeft + 1];
        for (int y = yLeft; y <= yRight; y++) {
            for (int x = xLeft; x <= xRight; x++) {
                files[y - yLeft][x - xLeft] = "d" + curdepth + "_x" + x + "_y" + y + ".png";
            }
        }
        
        results.put("render_grid", files);
        results.put("raster_ul_lon", ROOT_ULLON + xLeft * xDiff);
        results.put("raster_ul_lat", ROOT_ULLAT + yLeft * yDiff);
        results.put("raster_lr_lon", ROOT_ULLON + (xRight + 1) * xDiff);
        results.put("raster_lr_lat", ROOT_ULLAT + (yRight + 1) * yDiff);
        results.put("query_success", true);
        System.out.println(results);
        
        return results;
    }
    

}
