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

    private static double MIN_VAL = Double.MAX_VALUE;

    public static Point[] findClosestPairOfPoints(Point[] points) {
        //Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));

        Point[] sortedByX = Arrays.copyOf(points, points.length);
        Point[] sortedByY = Arrays.copyOf(points, points.length);

        Arrays.sort(sortedByX, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(sortedByY, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        return findClosestPair(sortedByX, sortedByY, 0, points.length - 1).toPointArray();
    }

    private static Pair findClosestPair(Point[] sortedByX, Point[] sortedByY, int left, int right) {

        if (right - left <= 3) {
            return new Pair(sortedByY);
        }

        int mid = (left + right) / 2;
        Point midPoint = sortedByX[mid];

        Pair leftPair = findClosestPair(sortedByX, sortedByY, left, mid);
        Pair rightPair = findClosestPair(sortedByX, sortedByY, mid + 1, right);

        Pair minPair = (leftPair.distance < rightPair.distance) ? leftPair : rightPair;

        int stripLeft = -1;
        int stripRight = -1;

        for (int i = left; i < right; i++) {
            if (Math.abs(sortedByY[i].x() - midPoint.x()) < minPair.distance) {
                if (stripLeft == -1) {
                    stripLeft = i;
                } else {
                    stripRight = i;
                }
            }
        }

        for (int i = stripLeft; i <= stripRight; i++) {
            for (int j = i + 1; j <= stripRight; j++) {
                double dist = sortedByY[i].distanceTo(sortedByY[j]);
                if (dist < minPair.distance) {
                    minPair = new Pair(sortedByY[i], sortedByY[j], dist);
                }
            }
        }

        return minPair;

//        List<Point> strip = new ArrayList<>();
//        for (int i = left; i <= right; i++) {
//            if (Math.abs(sortedByY[i].x() - midPoint.x()) < minPair.distance) {
//                strip.add(sortedByY[i]);
//            }
//        }

        //strip.sort((p1, p2) -> Double.compare(p1.y(), p2.y()));

//        int n = strip.size();
//        for (int i = 0; i < n; i++) {
//            for (int j = i + 1; j < n; j++) {
//                 if (strip.get(j).y() - strip.get(i).y() > minPair.distance){
//                     break;
//                 }else {
//                     double dist = strip.get(i).distanceTo(strip.get(j));
//                     if (dist < minPair.distance) {
//                         minPair = new Pair(strip.get(i), strip.get(j), dist);
//                     }
//                 }
//
//            }
//        }
//
//        return minPair;
    }
}
