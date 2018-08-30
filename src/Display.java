import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

enum SLOWING_PACE {

    SLOW(0.1),
    MEDIUM(0.2),
    FAST(0.3);

    private final double SLOWING_FACTOR;

    SLOWING_PACE(double SLOWING_FACTOR) {
        this.SLOWING_FACTOR = SLOWING_FACTOR;
    }

    public double getSLOWING_FACTOR() {
        return SLOWING_FACTOR;
    }
}

public class Display extends JPanel {

    private final int WIDTH = 1362;
    private final int HEIGHT = 763;
    private final double DETECTION_RADIUS = 50;
    private final double SEPARATION_RADIUS = 20;

    private double birdMass;
    private int birdSpeed;
    private Vector2d target;
    private ArrayList<Bird> birds;
    private int birdsNumber;
    private HashMap<String, Steering> steerings;
    private HashMap<String, Double> weightedTruncatedSum;
    private boolean showVectors;
    private boolean arrive;
    private double slowDownRadius;
    private SLOWING_PACE pace;
    private SpaceDivider spaceDivider;
    private boolean seekTarget;

    public Display(boolean seekTarget, boolean showVectors, int birdsNumber, double birdMass, int birdSpeed) {
        this.seekTarget = seekTarget;
        this.birdMass = birdMass;
        this.birdSpeed = birdSpeed;
        this.weightedTruncatedSum = new HashMap<>();
        this.steerings = new HashMap<>();
        this.showVectors = showVectors;
        this.birdsNumber = birdsNumber;
        this.arrive = false;
        this.spaceDivider = new SpaceDivider(WIDTH, HEIGHT, (int) DETECTION_RADIUS, birds);
        initBird();
        initTarget();
        this.steerings.put("wander", new Wander());
        this.weightedTruncatedSum.put("wander", 0.1);
        this.steerings.put("separation", new Separation(birds, SEPARATION_RADIUS));
        this.weightedTruncatedSum.put("separation", 2.5);
        this.steerings.put("cohesion", new Cohesion(birds));
        this.weightedTruncatedSum.put("cohesion", 1.0);
        this.steerings.put("alignment", new Alignment(birds));
        this.weightedTruncatedSum.put("alignment", 1.0);
    }

    public Display(boolean seekTarget, boolean showVectors, int birdsNumber, double birdMass, int birdSpeed, int slowDownRadius, SLOWING_PACE pace) {
        this(seekTarget, showVectors, birdsNumber, birdMass, birdSpeed);
        this.arrive = true;
        this.slowDownRadius = slowDownRadius;
        this.pace = pace;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (seekTarget) {
            g.setColor(Color.ORANGE);
            g.fillOval((int) target.getX(), (int) target.getY(), 10, 10);
        }

        g.setColor(Color.BLACK);
        for (Bird bird : birds) {

            if (showVectors) {
                g.setColor(Color.GREEN);
                g.drawLine((int) bird.getPosition().getX(), (int) bird.getPosition().getY(), (int) bird.getPosition().getX() + (int) bird.getVelocity().getX(), (int) bird.getPosition().getY() + (int) bird.getVelocity().getY());

            }

            g.setColor(Color.BLACK);
            //g.fillOval((int) bird.getPosition().getX(), (int) bird.getPosition().getY(), 3, 3);



            Point point = new Point((int)bird.getPosition().getX(),(int)bird.getPosition().getY());
            double deg = calcRotationAngleInDegrees(point, new Point((int)bird.getLastPosition().getX(), (int)bird.getLastPosition().getY()));


            int xPoly[] = {(int)bird.getPosition().getX() - 4, (int)bird.getPosition().getX(), (int)bird.getPosition().getX() + 4};
            int yPoly[] = {(int)bird.getPosition().getY() - 12, (int)bird.getPosition().getY(), (int)bird.getPosition().getY() - 12};
            Shape s = new Polygon(xPoly, yPoly, xPoly.length);
            drawRotatedShape( (Graphics2D)g, s, deg, (float)bird.getPosition().getX(), (float)bird.getPosition().getY());
        }
    }

    public static double calcRotationAngleInDegrees(Point centerPt, Point targetPt)
    {
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.
        // NOTE: By preserving Y and X param order to atan2,  we are expecting
        // a CLOCKWISE angle direction.
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        // rotate the theta angle clockwise by 90 degrees
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.
        // subtracting would rotate it counter-clockwise
        theta += Math.PI/2.0;
        return theta;
    }

    public static void drawRotatedShape(final Graphics2D g2, final Shape shape,
                                        final double angle,
                                        final float x, final float y) {

        final AffineTransform saved = g2.getTransform();
        final AffineTransform rotate = AffineTransform.getRotateInstance(
                angle, x, y);
        g2.transform(rotate);
        g2.draw(shape);
        g2.setTransform(saved);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }

    public void initTarget() {
        Random rand = new Random();
        target = new Vector2d(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
    }

    public void initBird() {
        birds = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < birdsNumber; i++) {
            Bird bird = new Bird(rand.nextInt(WIDTH), rand.nextInt(HEIGHT), birdMass, birdSpeed);
            bird.setCurrentCell(spaceDivider.getCorrespondingBirdsCell((int) bird.getPosition().getX(), (int) bird.getPosition().getY()));
            birds.add(bird);
        }
    }

    public void update(double timeElapsed) {

        for (Bird bird : birds) {

            int xBeforeUpdate = (int) bird.getPosition().getX();
            int yBeforeUpdate = (int) bird.getPosition().getY();

            // searching neighbors in birds cells and flanked cells
            bird.getNeighbors().clear();
            for (Bird neighborBird : birds) {
                if (
                        neighborBird != bird &&
                        (Math.abs(neighborBird.getCurrentCell().getX() - bird.getCurrentCell().getX()) < 2) &&
                        (Math.abs(neighborBird.getCurrentCell().getY() - bird.getCurrentCell().getY()) < 2)
                        ) {
                    if (Vector2d.isInDetectionRange(DETECTION_RADIUS, bird.getPosition(), neighborBird.getPosition())) {
                        bird.getNeighbors().add(neighborBird);
                    }
                }
            }

            Vector2d steerForce = new Vector2d(0,0);

            // weighted truncated sum
            steerForce = Vector2d.addVector(steerForce, Vector2d.multiplyVector(steerings.get("wander").update(bird), weightedTruncatedSum.get("wander")));
            steerForce = Vector2d.addVector(steerForce, Vector2d.multiplyVector(steerings.get("separation").update(bird), weightedTruncatedSum.get("separation")));
            steerForce = Vector2d.addVector(steerForce, Vector2d.multiplyVector(steerings.get("cohesion").update(bird), weightedTruncatedSum.get("cohesion")));
            steerForce = Vector2d.addVector(steerForce, Vector2d.multiplyVector(steerings.get("alignment").update(bird), weightedTruncatedSum.get("alignment")));

            Vector2d forceNeeded = Vector2d.substractVector(steerForce, bird.getVelocity());
            Vector2d acceleration = Vector2d.divideVector(Vector2d.addVector(bird.getVelocity(), forceNeeded), bird.getMass());

            if(arrive){
                double distanceFromTarget = Vector2d.getMagnitude(Vector2d.substractVector(target, bird.getPosition()));
                if(distanceFromTarget < slowDownRadius){
                    bird.setVelocity(
                            Vector2d.multiplyVector(
                                    Vector2d.multiplyVector(
                                            Vector2d.normalize(Vector2d.addVector(bird.getVelocity(), Vector2d.multiplyVector(acceleration, timeElapsed))),
                                            bird.getMaxSpeed()
                                    ), ((distanceFromTarget / slowDownRadius) + pace.getSLOWING_FACTOR())
                            )
                    );
                }else{
                    bird.setVelocity(
                            Vector2d.multiplyVector(
                                    Vector2d.normalize(Vector2d.addVector(bird.getVelocity(), Vector2d.multiplyVector(acceleration, timeElapsed))),
                                    bird.getMaxSpeed()
                            )
                    );
                }
            }else{
                bird.setVelocity(
                        Vector2d.multiplyVector(
                                Vector2d.normalize(Vector2d.addVector(bird.getVelocity(), Vector2d.multiplyVector(acceleration, timeElapsed))),
                                bird.getMaxSpeed()
                        )
                );
            }

            bird.setPosition(Vector2d.addVector(bird.getPosition(), Vector2d.multiplyVector(bird.getVelocity(), timeElapsed)), WIDTH, HEIGHT);
            // update birds cell
            bird.setCurrentCell(spaceDivider.getCorrespondingBirdsCell((int) bird.getPosition().getX(), (int) bird.getPosition().getY()));

            int xAfterUpdate = (int) bird.getPosition().getX();
            int yAfterUpdate = (int) bird.getPosition().getY();

            if (seekTarget && passedTarget(xBeforeUpdate, xAfterUpdate, yBeforeUpdate, yAfterUpdate)) {
                deplaceTarget();
            }

        }
    }

    public boolean passedTarget(int xBeforeUpdate, int xAfterUpdate, int yBeforeUpdate, int yAfterUpdate) {
        return (((xBeforeUpdate <= target.getX() && target.getX() <= xAfterUpdate) ||
                (xBeforeUpdate >= target.getX() && target.getX() >= xAfterUpdate))
                &&
                ((yBeforeUpdate <= target.getY() && target.getY() <= yAfterUpdate) ||
                        (yBeforeUpdate >= target.getY() && target.getY() >= yAfterUpdate)));
    }

    public void deplaceTarget() {
        Random rand = new Random();
        target.setX(rand.nextInt(WIDTH));
        target.setY(rand.nextInt(HEIGHT));
    }
}
