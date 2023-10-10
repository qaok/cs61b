public class TestPlanet {
    public static void main(String[] args) {
        Planet a = new Planet(13.5, 9.5, 5.5, 5, 500, "ball.gif");
        Planet b = new Planet(5.5, 15.5, 3.1, 5, 800, "endor.gif");
        System.out.println(a.calcForceExertedBy(b));
    }
}
