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
        //Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        //return findClosestPair(points, 0, points.length - 1).toPointArray();

        Point[] sortedByX = new Point[points.length];
        Point[] sortedByY = new Point[points.length];

        for (int i = 0; i < points.length; i++){
            sortedByX[i] = points[i];
            sortedByY[i] = points[i];
        }
        Arrays.sort(sortedByX, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        Arrays.sort(sortedByY, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        return findClosestPair(sortedByX, sortedByY).toPointArray();
    }

    private static Pair findClosestPair(Point[] pointsByX, Point[] pointsByY) {
        if (pointsByX.length == 1) {
            return new Pair(pointsByX[0], pointsByX[0], Double.POSITIVE_INFINITY);
        }
        if (pointsByX.length == 2) {
            return new Pair(pointsByX[0], pointsByX[1], pointsByX[0].distanceTo(pointsByX[1]));
        }

        int midpoint = (pointsByX.length / 2);

        Point[] XL = java.util.Arrays.copyOfRange(pointsByX, 0, midpoint);
        Point[] XR = java.util.Arrays.copyOfRange(pointsByX, midpoint, pointsByX.length);

        Point[] YL = new Point[midpoint];
        Point[] YR = new Point[pointsByY.length - midpoint];

        int yTrackerLeft = 0;
        int yTrackerRight = 0;
        int m = 0;
        while (m < pointsByX.length) {
            if (pointsByY[m].x() < pointsByX[midpoint].x()) {
                YL[yTrackerLeft] = pointsByY[m];
                yTrackerLeft++;
            } else {
                YR[yTrackerRight] = pointsByY[m];
                yTrackerRight++;
            }
            m++;
        }

        Pair distL = findClosestPair(XL, YL);
        Pair distR = findClosestPair(XR, YR);

        Point mPoint = pointsByX[midpoint];
        double lrDist = Math.min(distL.distance, distR.distance);

        //Construct yStrip

        ArrayList<Point> yStrip = new ArrayList<Point>();

        int c = 0;
        while (c < pointsByY.length) {
            if (Math.abs(pointsByY[c].x() - mPoint.x()) < lrDist) {
                yStrip.add(pointsByY[c]);
            }
            c++;
        }

        Pair answer;
        double minDist = lrDist;

        //The only distance that matters is the smallest one, that distance becomes the distance for the answer
        if (minDist == distL.distance) {
            answer = distL;
        } else {
            answer = distR;
        }

        //Now see if there are two points with a smaller distance within yStrip
        for (int j = 0; j < yStrip.size(); j++) {
            int k = j + 1;
            while ((k < yStrip.size()) && (yStrip.get(k).y() - yStrip.get(j).y() <= lrDist)) {
                double dst = yStrip.get(j).distanceTo(yStrip.get(k));
                if (dst < minDist) {
                    minDist = dst;
                    answer = new Pair(yStrip.get(j), yStrip.get(k), dst);
                }
                k++;
            }
        }
        //This is the final answer
        return answer;
    }
}
