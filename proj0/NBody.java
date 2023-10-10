public class NBody {
    public static double readRadius(String filename) {
        In in = new In(filename);
        int numbers = in.readInt();
        double radius = in.readDouble();
        return radius;
    }
    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        int numbers = in.readInt();
        double radius = in.readDouble();
        Planet[] p = new Planet[numbers];
        for (int i = 0; i < numbers; i = i + 1) {
        double x = in.readDouble();
        double y = in.readDouble();
        double xvel = in.readDouble();
        double yvel = in.readDouble();
        double m = in.readDouble();
        String name = in.readString();
        p[i] = new Planet(x, y, xvel, yvel, m, name);
        }
        return p;
    }
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        double radius = readRadius(args[2]);
        Planet[] planets = readPlanets(args[2]);
        StdDraw.setScale(-radius, radius);     //设置画布半径
        StdDraw.clear();                       //设置画布颜色（默认为白色）
        StdDraw.picture(0, 0, "./images/starfield.jpg");
        for (Planet p : planets){
            p.draw();
        }
        StdDraw.show();                        //将屏幕外画布复制到屏幕上画布

        StdDraw.enableDoubleBuffering();
        double time = 0;
        while (time < T) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i = 0; i < planets.length; i = i + 1) {
            xForces[i] = planets[i].calcNetForceExertedByX(planets);
            yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < planets.length; i = i + 1) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "./images/starfield.jpg");
            for (Planet p : planets){
                p.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time = time + dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                planets[i].yyVel, planets[i].mass, planets[i].imgFileName);  
        }
    }
}
