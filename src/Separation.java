import java.util.ArrayList;

public class Separation implements Steering {

    private double detectionRadius;
    private ArrayList<Bird> birds;

    public Separation(ArrayList<Bird> birds, double detectionRadius) {
        this.birds = birds;
        this.detectionRadius = detectionRadius;
    }

    public Vector2d update(Bird bird) {

        if (bird.getNeighbors().isEmpty()) {
            return new Vector2d(0, 0);
        }

        Vector2d desiredVelocity = new Vector2d(0, 0);

        for (Bird neighbor : bird.getNeighbors()) {

            if (Vector2d.isInDetectionRange(detectionRadius, bird.getPosition(), neighbor.getPosition())) {
                Vector2d toAgent = Vector2d.substractVector(bird.getPosition(), neighbor.getPosition());
                desiredVelocity = Vector2d.addVector(desiredVelocity, Vector2d.divideVector(Vector2d.normalize(toAgent), Vector2d.getMagnitude(toAgent)));

            }else{
                continue;
            }
        }

        return Vector2d.multiplyVector(
                Vector2d.normalize(desiredVelocity),
                bird.getMaxSpeed()
        );
    }
}
