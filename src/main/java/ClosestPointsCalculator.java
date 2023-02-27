import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosestPointsCalculator {

    private static Pair bestPair = null;

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

        public void updatePair(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
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

    static double closestUtil(Point[] Px, Point[] Py, int n) {

        if (n <= 3) {
            return bruteForce(Px, n);
        }

        int mid = n / 2;
        Point midPoint = Px[mid];

        Point[] Pyl = new Point[mid];
        Point[] Pyr = new Point[n - mid];

        Point[] Pxr = Arrays.copyOfRange(Px, mid, n);

        int li = 0, ri = 0;

        for (int i = 0; i < n; i++) {
            if ((Py[i].x() < midPoint.x() || (Py[i].x() == midPoint.x() && Py[i].y() < midPoint.y())) && li < mid) {
                Pyl[li++] = Py[i];
            } else {
                Pyr[ri++] = Py[i];
            }
        }

        double dl = closestUtil(Px, Pyl, mid);
        double dr = closestUtil(Pxr, Pyr, n - mid);

        double d = Math.min(dl, dr);

        List<Point> strip = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (Math.abs(Py[i].x() - midPoint.x()) < d) {
                strip.add(Py[i]);
            }
        }

        int stripSize = strip.size();
        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize; j++) {
                if (strip.get(j).y() - strip.get(i).y() > d) {
                    break;
                } else {
                    double distance = strip.get(i).distanceTo(strip.get(j));
                    if (distance < d) {
                        d = distance;
                        bestPair = new Pair(strip.get(i), strip.get(j), d);
                    }
                }
            }
        }

        return d;

    }

    static double bruteForce(Point[] points, int n) {

        double min = Double.MAX_VALUE;

        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (points[i].distanceTo(points[j]) < min) {
                    min = points[i].distanceTo(points[j]);
                    bestPair = new Pair(points[i], points[j], min);
                }
            }
        }

        return min;
    }

    static double findClosest(Point[] points, int n) {

        Point[] Px = points.clone();
        Point[] Py = points.clone();

        Arrays.sort(Px, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(Py, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        return closestUtil(Px, Py, n);
    }


    public static Point[] findClosestPairOfPoints(Point[] points) {
        findClosest(points, points.length);
        return bestPair.toPointArray();
    }

}
