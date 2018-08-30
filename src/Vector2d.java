public class Vector2d {

    private double x;
    private double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2d addVector(Vector2d a, Vector2d b) {
        return new Vector2d(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public static Vector2d substractVector(Vector2d a, Vector2d b) {
        return new Vector2d(a.getX() - b.getX(), a.getY() - b.getY());
    }

    public static Vector2d multiplyVector(Vector2d v, double mutiplier) {
        return new Vector2d(v.getX() * mutiplier, v.getY() * mutiplier);
    }

    public static Vector2d divideVector(Vector2d v, double divider) {
        return new Vector2d(v.getX() / divider, v.getY() / divider);
    }

    public static double getMagnitude(Vector2d v) {
        return Math.sqrt(Math.pow(v.getX(), (double) 2) + Math.pow(v.getY(), (double) 2));
    }

    public static double dotProduct(Vector2d a, Vector2d b) {
        Vector2d aNormalize = normalize(a);
        Vector2d bNormalize = normalize(b);
        return Math.toDegrees(Math.acos((aNormalize.getX() * bNormalize.getX()) + (aNormalize.getY() * bNormalize.getY())));
    }

    public static Vector2d normalize(Vector2d v) {
        if (v.getX() == 0 || v.getY() == 0) return new Vector2d(0, 0);
        else return new Vector2d(v.getX() / getMagnitude(v), v.getY() / getMagnitude(v));
    }

    public static boolean isInDetectionRange(double detectionRadius, Vector2d a, Vector2d b) {
        double distance = Math.abs(getMagnitude(substractVector(a, b)));
        return distance < detectionRadius;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}