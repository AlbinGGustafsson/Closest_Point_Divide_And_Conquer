public class Pair {
    private Point firstPoint;
    private Point secondPoint;
    private double distance;

    public Pair(Point firstPoint, Point secondPoint, double distance) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.distance = distance;
    }

    public Point[] toPointArray() {
        return new Point[]{firstPoint, secondPoint};
    }

    public void updatePair(Point firstPoint, Point secondPoint, double distance) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "p1=" + firstPoint +
                ", p2=" + secondPoint +
                ", dist=" + distance +
                '}';
    }
}