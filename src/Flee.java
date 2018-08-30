public class Flee implements Steering {

    private Vector2d target;

    public Flee(Vector2d target) {
        this.target = target;
    }

    public Vector2d update(Bird bird) {
        return Vector2d.multiplyVector(
                Vector2d.normalize(Vector2d.substractVector(bird.getPosition(), target)),
                bird.getMaxSpeed()
        );
    }
}
