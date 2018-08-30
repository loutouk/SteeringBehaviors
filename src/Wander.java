import java.util.Random;

public class Wander implements Steering {

    public Vector2d update(Bird bird) {

        Vector2d randJitter = new Vector2d(randomClamped() * bird.getWanderJitter(), randomClamped() * bird.getWanderJitter());
        randJitter = Vector2d.normalize(randJitter);
        randJitter = Vector2d.multiplyVector(randJitter, bird.getWanderRadius());
        bird.setAssociatedTarget(Vector2d.addVector(bird.getAssociatedTarget(), randJitter));

        Vector2d projectedTarget = Vector2d.addVector(bird.getAssociatedTarget(), bird.getPosition());
        projectedTarget = Vector2d.addVector(projectedTarget, new Vector2d(bird.getWanderDistance(), 0));

        return Vector2d.multiplyVector(
                Vector2d.normalize(Vector2d.substractVector(projectedTarget, bird.getPosition())),
                bird.getMaxSpeed()
        );
    }

    public double randomClamped() {
        Random rand = new Random();
        return rand.nextDouble() * 2 - 1;
    }
}
