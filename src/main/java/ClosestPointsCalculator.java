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

    static double minDist = Double.POSITIVE_INFINITY;

    public static Point[] findClosestPairOfPoints(Point[] points) {
        //Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));

        Point[] sortedByX = Arrays.copyOf(points, points.length);
        Point[] sortedByY = Arrays.copyOf(points, points.length);

        Arrays.sort(sortedByX,(p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(sortedByY,(p1, p2) -> Double.compare(p1.y(), p2.y()));

        return findClosestPoint(sortedByX, sortedByY,  0, points.length - 1).toPointArray();
    }

    public static Pair findMinDistance(ArrayList<Point> list) {
        Point[] xSorted = preProcessX(list);
        Point[] ySorted = preProcessY(list);

        Pair minPair = findClosestPoint(xSorted, ySorted, 0, list.size() - 1);
        return minPair;
    }

    public static Pair findClosestPoint(Point[] xSorted, Point[] ySorted, int bot, int top) {
        if (top <= bot) {
            return new Pair(null, null, Double.POSITIVE_INFINITY);
        }

        int mid = (bot + top) / 2;
        Point median = xSorted[mid];

        Pair delta1 = findClosestPoint(xSorted, ySorted, bot, mid);
        Pair delta2 = findClosestPoint(xSorted, ySorted, mid + 1, top);
        Pair delta = (delta1.distance < delta2.distance) ? delta1 : delta2;

        //used to shorten down runtime when only 2 nodes are inside a "block".
        //Skips second for-loop using this.
        if (top - bot == 1 && delta.distance == Double.POSITIVE_INFINITY) {
            delta = new Pair(xSorted[bot], xSorted[top], xSorted[bot].distanceTo(xSorted[top]));
            minDist = delta.distance;
        }

        ArrayList<Point> strip = new ArrayList<Point>();

        for (int i = 0; i < ySorted.length; i++) {
            if (Math.abs(ySorted[i].x() - median.x()) < delta.distance) {
                strip.add(ySorted[i]);
            }
        }
        //loop skipped when if statement above is fullfilled
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() - 1; j++) {
                if (Math.abs(strip.get(i).y() - strip.get(i).y()) > delta.distance) {
                    break;
                } else if (distance(strip.get(i), strip.get(j)) < delta.distance) {
                    double distance = strip.get(i).distanceTo(strip.get(j));
                    if (distance < minDist) {
                        minDist = distance;
                        delta = new Pair(strip.get(i), strip.get(j), minDist);

                        System.out.printf("%s is now the closest pairs%n", delta);
                    }
                }
            }
        }

        return delta;
    }

    public static double distance(Point p1, Point p2) {
        int x = p1.x() - p2.x();
        int y = p1.y() - p2.y();
        double distance = Math.sqrt(x * x + y * y);
        return distance;
    }

    public static Point[] preProcessX(ArrayList<Point> list) {
        Point[] sortedByX = new Point[list.size()];

        for (int i = 0; i < list.size(); i++) {
            sortedByX[i] = list.get(i);
        }

        Arrays.sort(sortedByX, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.x() - p2.x();
            }
        });
        return sortedByX;
    }

    public static Point[] preProcessY(ArrayList<Point> list) {
        Point[] sortedByY = new Point[list.size()];

        for (int i = 0; i < list.size(); i++) {
            sortedByY[i] = list.get(i);
        }

        Arrays.sort(sortedByY, new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.y() - p2.y();
            }
        });
        return sortedByY;
    }
}
