import java.util.*;

public class ClosestPointsCalculator {

    private static class Pair {
        private Point p1, p2;
        double distance;

        public Pair(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }

        public Pair(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            distance = p1.distanceTo(p2);
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
        cloest(points);
        System.out.println(bestDistance);
        return bestPair.toPointArray();
    }

    private static double bestDistance = Double.POSITIVE_INFINITY;
    private static Pair bestPair = new Pair(null, null, Double.POSITIVE_INFINITY);

    public static double dist(Point p1, Point p2) {
        return Math.sqrt((p1.x() - p2.x()) * (p1.x() - p2.x()) + (p1.y() - p2.y()) * (p1.y() - p2.y()));
    }

    public static double bruteForce(Point[] p) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < p.length - 1; i++) {
            for (int j = 1; j < p.length; j++) {
                double dist = dist(p[i], p[j]);
                if (dist < min) min = dist;
            }
        }
        return min;

    }

    public static double cloest(Point[] p) {
        int n = p.length;
        Point[] pointsByX = new Point[n];
        Point[] pointsByY = new Point[n];
        for (int i = 0; i < n; i++) {
            pointsByX[i] = p[i];

        }
        Arrays.sort(pointsByX, (p1, p2) -> p1.x() - p2.x());
        for (int i = 0; i < n; i++) {
            pointsByY[i] = pointsByX[i];
        }
        Point[] aux = new Point[n];
        return helper(pointsByX, pointsByY, aux, 0, n - 1);
    }

    public static double helper(Point[] pointsByX, Point[] pointsByY, Point[] aux, int lo, int hi) {
        if (lo >= hi){
            return Double.POSITIVE_INFINITY;
        }

        int mid = lo + (hi - lo) / 2;
        Point midPoint = pointsByX[mid];

        // compute the closest pair in left sub part and right sub part
        double delta1 = helper(pointsByX, pointsByY, aux, lo, mid);
        double delta2 = helper(pointsByX, pointsByY, aux, mid + 1, hi);
        double delta = Math.min(delta1, delta2);

        // merge back so pointsByY[lo...hi] are sorted by y-coordinate.
        merge(pointsByY, aux, lo, mid, hi);

        // aux[0...m-1]  = sequence of points closer than delta, sorted by y-coordiante
        int m = 0;
        for (int i = lo; i <= hi; i++) {
            if (Math.abs(pointsByY[i].x() - midPoint.x()) < delta) {
                aux[m++] = pointsByY[i];
            }
        }

        // compare each point to its neighbors with y-coordinate closer than delta
        for (int i = 0; i < m; i++) {
            // a geometric packing argument shows that this loop iterates at most 7 times
            for (int j = i + 1; (j < m) && (aux[j].y() - aux[i].y() < delta); j++) {
                double distance = dist(aux[i], aux[j]);
                if (distance < delta) {
                    delta = distance;
                    if (distance < bestDistance) {
                        bestDistance = delta;
                        bestPair = new Pair(aux[i], aux[j], distance);
                    }
                }
            }
        }
        return delta;

    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // merge back to a[]
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (aux[i].y() < aux[j].y()) a[k] = aux[i++];
            else a[k] = aux[j++];
        }
    }


}
