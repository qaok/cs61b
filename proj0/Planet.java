public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public Planet(double xP, double yP, double xV,
          double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
        }
    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName; 
    }
    public double calcDistance(Planet p) {
      double dx2 = (this.xxPos - p.xxPos) * (this.xxPos - p.xxPos);
      double dy2 = (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos);
      double r = Math.sqrt(dx2 + dy2);
      return r;
    }
    public double calcForceExertedBy(Planet p) {
      final double G = 6.67e-11;
      double r2 = this.calcDistance(p) * this.calcDistance(p);
      double F = G * this.mass * p.mass / r2;
      return F;
    }
    public double calcForceExertedByX(Planet p) {
      double dx = p.xxPos - this.xxPos;
      double Fx = this.calcForceExertedBy(p) * dx / calcDistance(p);
      return Fx;
    }
    public double calcForceExertedByY(Planet p) {
      double dy = p.yyPos - this.yyPos;
      double Fy = this.calcForceExertedBy(p) * dy / calcDistance(p);
      return Fy;
    }   
    public double calcNetForceExertedByX(Planet[] planets) {
      int n = planets.length;
      double Fnetx = 0;
      for (int i = 0; i < n; i = i +1) {
        if (this.equals(planets[i])) {
          continue;
        }
        Fnetx = Fnetx + this.calcForceExertedByX(planets[i]);
      }
      return Fnetx;
    }
    public double calcNetForceExertedByY(Planet[] planets) {
      int n = planets.length;
      double Fnety = 0;
      for (int i = 0; i < n; i = i +1) {
        if (this.equals(planets[i])) {
          continue;
        }
        Fnety = Fnety + this.calcForceExertedByY(planets[i]);
      }
      return Fnety;
    }
    public void update(double dt, double fx, double fy) {
      double ax = fx / mass;
      double ay = fy / mass;
      xxVel = xxVel + dt * ax;
      yyVel = yyVel + dt * ay;
      xxPos = xxPos + dt * xxVel;
      yyPos = yyPos + dt * yyVel;
    }
    public void draw() {
      StdDraw.picture(this.xxPos,this.yyPos,"./images/"+this.imgFileName);
    }
}
