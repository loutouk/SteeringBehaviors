import java.util.ArrayList;
import java.util.Random;

class Bird {

    private final double wanderRadius = 200;
    private final double wanderDistance = 50;
    private final double wanderJitter = 500;
    private Vector2d position;
    private Vector2d velocity;
    private double maxSpeed;
    private double mass;
    private ArrayList<Bird> neighbors;
    private Vector2d associatedTarget;
    private Cell currentCell;
    private Vector2d lastPosition;

    public Bird(double x, double y, double mass, double maxSpeed) {
        position = new Vector2d(x, y);
        lastPosition = new Vector2d(x, y);
        velocity = new Vector2d(0, 0);
        this.mass = mass;
        this.maxSpeed = maxSpeed;
        neighbors = new ArrayList<>();
        Random rand = new Random();
        double randX = rand.nextDouble() * 2 - 1;
        double randY = rand.nextDouble() * 2 - 1;
        associatedTarget = new Vector2d(randX * wanderJitter, randY * wanderJitter);
        associatedTarget = Vector2d.normalize(associatedTarget);
        associatedTarget = Vector2d.multiplyVector(associatedTarget, wanderRadius);
    }

    public Bird(double x, double y) {
        this(x, y, 0.2, 400);
    }

    public ArrayList<Bird> getNeighbors() {
        return neighbors;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Vector2d getPosition() {
        return position;
    }

    public void setPosition(Vector2d position, int WIDTH, int HEIGHT) {
        if (position.getX() > WIDTH || position.getX() < 0) position.setX(Math.floorMod((int) position.getX(), WIDTH));
        if (position.getY() > HEIGHT || position.getY() < 0)
            position.setY(Math.floorMod((int) position.getY(), HEIGHT));
        this.lastPosition = this.position;
        this.position = position;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2d velocity) {
        this.velocity = velocity;
    }

    public Vector2d getHeading() {
        return Vector2d.normalize(velocity);
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public Vector2d getAssociatedTarget() {
        return associatedTarget;
    }

    public void setAssociatedTarget(Vector2d associatedTarget) {
        this.associatedTarget = associatedTarget;
    }

    public double getWanderRadius() {
        return wanderRadius;
    }

    public double getWanderDistance() {
        return wanderDistance;
    }

    public Vector2d getLastPosition() {
        return lastPosition;
    }

    public double getWanderJitter() {
        return wanderJitter;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }
}