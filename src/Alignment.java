import java.util.ArrayList;

public class Alignment implements Steering {

    private ArrayList<Bird> birds;

    public Alignment(ArrayList<Bird> birds) {
        this.birds = birds;
    }

    public Vector2d update(Bird bird) {

        int neighborCount = bird.getNeighbors().size();


        Vector2d desiredVelocity;
        Vector2d averageHeading = new Vector2d(0, 0);

        for (Bird neighbor : bird.getNeighbors()) {
            averageHeading = Vector2d.addVector(averageHeading, neighbor.getHeading());
        }

        if (neighborCount > 0) {
            averageHeading = Vector2d.divideVector(averageHeading, (double) neighborCount);
            desiredVelocity = Vector2d.substractVector(averageHeading, bird.getHeading());
        } else {
            desiredVelocity = new Vector2d(0, 0);
        }

        return Vector2d.multiplyVector(
                Vector2d.normalize(desiredVelocity),
                bird.getMaxSpeed()
        );

    }
}
