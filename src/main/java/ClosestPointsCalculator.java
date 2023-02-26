import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosestPointsCalculator {

    private static class Pair {
        private Point p1, p2;
        double distance;

        public Pair(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }

        public Pair(Point[] points) {
            p1 = points[0];
            p2 = points[1];
            distance = p1.distanceTo(p2);
        }

        public Point[] toPointArray() {
            return new Point[]{p1, p2};
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "p1=" + p1 +
                    ", p2=" + p2 +
                    ", dist=" + distance +
                    '}';
        }
    }

    public static Point[] findClosestPairOfPoints(Point[] points) {
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        return closestPairHelper(points, 0, points.length - 1).toPointArray();

//        Pair closest = closestPair(points);
//        return closest.toPointArray();

//        Point[] pts = new Point[2];
//        pts[0] = closest.p1;
//        pts[1] = closest.p2;
//        return pts;
    }

    public static Pair closestPair(Point[] points) {
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        return closestPairHelper(points, 0, points.length - 1);
        //return bruteForce(points, 0, points.length - 1);
    }

    private static Pair closestPairHelper(Point[] points, int left, int right) {
        if (right - left <= 3) {
            System.out.println("ja");
            return new Pair(points);
            //return new Pair(points[0], points[1], points[0].distanceTo(points[1]));
            //return bruteForce(points, left, right);
        }
        System.out.println("nej");
        int mid = (left + right) / 2;
        Point midPoint = points[mid];

        Pair leftPair = closestPairHelper(points, left, mid);
        Pair rightPair = closestPairHelper(points, mid + 1, right);

        Pair minPair;
        if (leftPair.distance < rightPair.distance) {
            minPair = leftPair;
        } else {
            minPair = rightPair;
        }
        //Pair minPair = (leftPair.distance < rightPair.distance) ? leftPair : rightPair;

        List<Point> strip = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (Math.abs(points[i].x() - midPoint.x()) < minPair.distance) {
                strip.add(points[i]);
            }
        }
        strip.sort((p1, p2) -> Double.compare(p1.y(), p2.y()));

        int n = strip.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n && strip.get(j).y() - strip.get(i).y() < minPair.distance; j++) {
                //double dist = distance(strip.get(i), strip.get(j));
                double dist = strip.get(i).distanceTo(strip.get(j));
                if (dist < minPair.distance) {
                    minPair = new Pair(strip.get(i), strip.get(j), dist);
                }
            }
        }

        //Typ här man ska sätta dem, inte i loopen, pga hur rekursiviteten fungerar
        //System.out.printf("%s has now the smallest distance%n", minPair);
        return minPair;
    }

    private static Pair bruteForce(Point[] points, int left, int right) {
        Pair minPair = new Pair(null, null, Double.POSITIVE_INFINITY);
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                //double dist = distance(points[i], points[j]);
                double dist = points[i].distanceTo(points[j]);
                if (dist < minPair.distance) {
                    minPair = new Pair(points[i], points[j], dist);
                }
            }
        }
        return minPair;
    }

//    private static double distance(Point p1, Point p2) {
//        double dx = p1.x() - p2.x();
//        double dy = p1.y() - p2.y();
//        return Math.sqrt(dx * dx + dy * dy);
//    }
}
