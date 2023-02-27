import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

        public void updatePair(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
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
        Point[] sortedByX = Arrays.copyOf(points, points.length);
        Point[] sortedByY = Arrays.copyOf(points, points.length);

        Arrays.sort(sortedByX, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(sortedByY, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        //return bruteForceMinPair(sortedByX, 0, points.length - 1).toPointArray();
        return findClosestPoint(sortedByX, sortedByY, 0, points.length - 1).toPointArray();
    }

    public static Pair findClosestPoint(Point[] xSorted, Point[] ySorted, int left, int right) {

        if (right - left + 1 <= 3) {
            return bruteForceMinPair(xSorted, left, right);
        }

        int midIndex = (left + right) / 2;
        Point midPoint = xSorted[midIndex];

        Pair leftMin = findClosestPoint(xSorted, ySorted, left, midIndex);
        Pair rightMin = findClosestPoint(xSorted, ySorted, midIndex + 1, right);
        Pair minPair = (leftMin.distance < rightMin.distance) ? leftMin : rightMin;


        Point[] strip = new Point[ySorted.length];
        int stripSize = 0;
        for (int i = 0; i < ySorted.length; i++) {
            if (Math.abs(ySorted[i].x() - midPoint.x()) < minPair.distance) {
                strip[stripSize++] = ySorted[i];
            }
        }

        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize; j++) {
                if (strip[j].y() - strip[i].y() > minPair.distance) {
                    break;
                } else {
                    double dist = strip[i].distanceTo(strip[j]);
                    if (dist < minPair.distance) {
                        minPair.updatePair(strip[i], strip[j], dist);
                    }
                }
            }
        }

//        List<Point> strip = new ArrayList<>();
//        for (int i = 0; i < ySorted.length; i++) {
//            if (Math.abs(ySorted[i].x() - midPoint.x()) < minPair.distance) {
//                strip.add(ySorted[i]);
//            }
//        }
//
//        int stripSize = strip.size();
//        for (int i = 0; i < stripSize; i++) {
//            for (int j = i + 1; j < stripSize; j++) {
//                if (strip.get(j).y() - strip.get(i).y() > minPair.distance) {
//                    break;
//                } else {
//                    double dist = strip.get(i).distanceTo(strip.get(j));
//                    if (dist < minPair.distance) {
//                        //minPair = new Pair(strip.get(i), strip.get(j), dist);
//                        minPair.updatePair(strip.get(i), strip.get(j), dist);
//                    }
//                }
//
//            }
//        }

        //OLD from the K
//        ArrayList<Point> strip = new ArrayList<Point>();
//
//        for (int i = 0; i < ySorted.length; i++) {
//            if (Math.abs(ySorted[i].x() - midPoint.x()) < minPair.distance) {
//                strip.add(ySorted[i]);
//            }
//        }
//        //loop skipped when if statement above is fullfilled
//        for (int i = 0; i < strip.size(); i++) {
//            for (int j = i + 1; j < strip.size() - 1; j++) {
//                if (strip.get(i).y() - strip.get(j).y() > minPair.distance) {
//                    break;
//                } else if (distance(strip.get(i), strip.get(j)) < minPair.distance) {
//                    double distance = strip.get(i).distanceTo(strip.get(j));
//                    if (distance < minDist) {
//                        minDist = distance;
//                        minPair = new Pair(strip.get(i), strip.get(j), minDist);
//
//                        System.out.printf("%s is now the closest pairs%stripSize", minPair);
//                    }
//                }
//            }
//        }

        return minPair;
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
