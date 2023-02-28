import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosestPointsCalculator {

    private static class Pair {
        private Point firstPoint, secondPoint;
        double distance;

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

        @Override
        public String toString() {
            return "Pair{" +
                    "p1=" + firstPoint +
                    ", p2=" + secondPoint +
                    ", dist=" + distance +
                    '}';
        }
    }

    public static Point[] findClosestPairOfPoints(Point[] points) {

        Point[] sortedX = points.clone();
        Point[] sortedY = points.clone();

        Arrays.sort(sortedX, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(sortedY, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        ArrayList<Point> sortedYArrayList = new ArrayList<>(List.of(sortedY));

        return findClosestPair(sortedX, sortedYArrayList, 0, points.length - 1).toPointArray();
    }

    private static Pair findClosestPair(Point[] sortedX, ArrayList<Point> sortedY, int left, int right) {

        int length = right - left + 1;

        if (length <= 3) {
            return bruteForceMinPair(sortedX, left, right);
        }

        int mid = (left + right) / 2;
        Point midPoint = sortedX[mid];

        ArrayList<Point> sortedYLeft = new ArrayList<>();
        ArrayList<Point> sortedYRight = new ArrayList<>();

        for (Point p : sortedY) {
            if (p.x() < midPoint.x()) {
                sortedYLeft.add(p);
            } else {
                sortedYRight.add(p);
            }
        }

        Pair leftPair = findClosestPair(sortedX, sortedYLeft, left, mid);
        Pair rightPair = findClosestPair(sortedX, sortedYRight, mid + 1, right);

        Pair closestPair = (leftPair.distance < rightPair.distance) ? leftPair : rightPair;

        List<Point> strip = new ArrayList<>();
        for (int i = 0; i < sortedY.size(); i++) {
            if (Math.abs(sortedY.get(i).x() - midPoint.x()) < closestPair.distance) {
                strip.add(sortedY.get(i));
            }
        }

        int n = strip.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (strip.get(j).y() - strip.get(i).y() > closestPair.distance) {
                    break;
                } else {
                    double distance = strip.get(i).distanceTo(strip.get(j));
                    if (distance < closestPair.distance) {
                        closestPair.updatePair(strip.get(i), strip.get(j), distance);
                    }
                }

            }
        }

        return closestPair;
    }

    private static Pair bruteForceMinPair(Point[] points, int left, int right) {
        Pair minPair = new Pair(null, null, Double.POSITIVE_INFINITY);
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minPair.distance) {
                    //minPair = new Pair(points[i], points[j], distance);
                    minPair.updatePair(points[i], points[j], distance);
                }
            }
        }
        return minPair;
    }
}
