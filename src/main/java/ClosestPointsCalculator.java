import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

        Point[] sortedByX = new Point[points.length];
        Point[] pointsY = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            sortedByX[i] = points[i];
            pointsY[i] = points[i];
        }

        Arrays.sort(sortedByX, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(pointsY, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        List<Point> xList = new ArrayList<>(List.of(sortedByX));
        List<Point> yList = new ArrayList<>(List.of(pointsY));

        return findClosestPair(xList, yList, xList.size()).toPointArray();
    }

    private static Pair findClosestPair(List<Point> sortedByX, List<Point> pointsY, int size) {
        if (size <= 3) {
            return bruteForceMinimumPair(sortedByX);
        }

        int mid = size / 2;
        Point midPoint = sortedByX.get(mid);

        List<Point> leftSubPointsY = new ArrayList<>();
        List<Point> leftSubPointsSortedX = new ArrayList<>();
        List<Point> rightSubPointsY = new ArrayList<>();
        List<Point> rightSubPointsSortedX = new ArrayList<>();

        for (int index = 0; index < size; ++index) {
            if (pointsY.get(index).x() <= midPoint.x()) {
                leftSubPointsY.add(pointsY.get(index));
                leftSubPointsSortedX.add(sortedByX.get(index));
            } else {
                rightSubPointsY.add(pointsY.get(index));
                rightSubPointsSortedX.add(sortedByX.get(index));
            }
        }

        //leftMin, rightMin, minPair
        Pair sigmaLeft = findClosestPair(leftSubPointsSortedX, leftSubPointsY, mid);
        Pair sigmaRight = findClosestPair(rightSubPointsSortedX, rightSubPointsY, size - mid);
        Pair sigma = (sigmaLeft.distance < sigmaRight.distance) ? sigmaLeft : sigmaRight;

        List<Point> strip = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            if (Math.abs(pointsY.get(i).x() - midPoint.x()) < sigma.distance) {
                strip.add(pointsY.get(i));
            }
        }

        Pair minDistanceStrip = findMinimumDistanceInStrip(strip, sigma);

        return sigma.distance < minDistanceStrip.distance ? sigma : minDistanceStrip;

//        int n = strip.size();
//        for (int i = 0; i < n; i++) {
//            for (int j = i + 1; j < n; j++) {
//                if (strip.get(j).y() - strip.get(i).y() > sigma.distance) {
//                    break;
//                } else {
//                    double dist = strip.get(i).distanceTo(strip.get(j));
//                    if (dist < sigma.distance) {
//                        sigma = new Pair(strip.get(i), strip.get(j), dist);
//                    }
//                }
//
//            }
//        }

        //return sigma;
    }

    private static Pair findMinimumDistanceInStrip(List<Point> strip, Pair sigma) {

        Pair minDistance = sigma;

        for (int i = 0; i < strip.size(); ++i) {
            for (int j = i + 1; j < strip.size(); ++j) {
                if (strip.get(j).y() - strip.get(i).y() > sigma.distance) {
                    break;
                } else {
                    double dist = strip.get(i).distanceTo(strip.get(j));
                    if (dist < sigma.distance) {
                        minDistance = new Pair(strip.get(i), strip.get(j), dist);
                    }
                }
            }

        }
        return minDistance;

    }

    private static Pair bruteForceMinimumPair(List<Point> points) {
        Pair minPair = new Pair(null, null, Double.POSITIVE_INFINITY);
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                //double dist = distance(points[i], points[j]);
                double dist = points.get(i).distanceTo(points.get(j));
                if (dist < minPair.distance) {
                    minPair = new Pair(points.get(i), points.get(j), dist);
                }
            }
        }
        return minPair;
    }

}
