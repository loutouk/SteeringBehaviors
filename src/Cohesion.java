import java.util.ArrayList;

public class Cohesion implements Steering {

    private ArrayList<Bird> birds;

    public Cohesion(ArrayList<Bird> birds) {
        this.birds = birds;
    }

    public Vector2d update(Bird bird) {

        int neighborCount = bird.getNeighbors().size();

        if (bird.getNeighbors().isEmpty()) {
            return new Vector2d(0, 0);
        }

        Vector2d centerOfMass = new Vector2d(0, 0);

        for (Bird neighbor : bird.getNeighbors()) {
            centerOfMass = Vector2d.addVector(centerOfMass, neighbor.getPosition());
        }

        if (neighborCount > 0) {
            centerOfMass = Vector2d.divideVector(centerOfMass, neighborCount);
        }


        return Vector2d.multiplyVector(
                Vector2d.normalize(Vector2d.substractVector(centerOfMass, bird.getPosition())),
                bird.getMaxSpeed()
        );

    }
}
