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

    // Method to find the min distance
    public static double closestPair(Point[] points) {
        int n = points.length;
        Point[] xSorted = new Point[n];
        Point[] ySorted = new Point[n];

        for (int i = 0; i < n; i++) {
            xSorted[i] = points[i];
            ySorted[i] = points[i];
        }
        // sort array using x coordinate
        Arrays.sort(xSorted, (p1, p2) -> p1.x() - p2.x());

        // sort array using y coordinate
        Arrays.sort(ySorted, (p1, p2) -> p1.y() - p2.y());

        return closestPair(xSorted, ySorted, 0, n - 1);
    }

    // recursive call to find the closest pair distance
    private static double closestPair(Point[] px, Point[] py, int low, int high) {
        // count points in the search space
        int n = high - low + 1;

        //If there are 2 or 3 points, then use brute force
        if (n <= 3) {
            return closestPairUsingBruteForce(px);
        }

        // find middle element of the search space
        // to divide the space into two halves
        int mid = low + (high - low) / 2;
        Point midPoint = px[mid];

        // find left and right min recursively
        double leftMin = closestPair(px, py, low, mid);
        double rightMin = closestPair(px, py, mid + 1, high);

        // find the min distance from left and right search space
        double minDistance = Math.min(leftMin, rightMin);

        // there might be possibility that min distance might be there by one point from left and one point from right
        // to find such scenarios create strip of distance minDistance from both sides
        // find the strip of values which are closer at a distance of d
        int stripLeft = -1;
        int stripRight = -1;

        for (int i = low; i < high; i++) {
            if (Math.abs(py[i].x() - midPoint.x()) < minDistance) {
                if (stripLeft == -1) {
                    stripLeft = i;
                } else {
                    stripRight = i;
                }
            }
        }

        double minFromStrip = getMinStripeDistance(py, stripLeft, stripRight);
        return Math.min(minDistance, minFromStrip);

//        List<Point> strip = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            if (Math.abs(py[i].x() - midPoint.x()) < minDistance) {
//                strip.add(py[i]);
//            }
//        }
//
//        int stripSize = strip.size();
//        for (int i = 0; i < stripSize; i++) {
//            for (int j = i + 1; j < stripSize; j++) {
//                if (strip.get(j).y() - strip.get(i).y() > minDistance) {
//                    break;
//                } else {
//                    double distance = strip.get(i).distanceTo(strip.get(j));
//                    if (distance < minDistance) {
//                        minDistance = distance;
//                        bestPair = new Pair(strip.get(i), strip.get(j), minDistance);
//                    }
//                }
//            }
//        }
//
//        return minDistance;

    }


    public static double closestPairUsingBruteForce(Point[] points) {
        double min = Double.MAX_VALUE;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = points[i].distanceTo(points[j]);
                if(dist < min) {
                    min = dist;
                    bestPair = new Pair(points[i], points[j], min);
                }
                min = Math.min(min, dist);

            }
        }
        return min;
    }

    // min distance in strip of points
    private static double getMinStripeDistance(Point[] ySorted, int low, int high) {
        double min = Double.MAX_VALUE ;

        //Pick all points one by one and try the next points till the difference
        //between y coordinates is smaller than d.
        //This is a proven fact that this loop runs at most 6 times
        for(int i=low; i<=high; i++) {
            for(int j=i+1; j<=high; j++) {
                min = Math.min(min, ySorted[i].distanceTo(ySorted[j]));
                bestPair = new Pair(ySorted[i], ySorted[j], min);
            }
        }
        return min;
    }



//    // brute force method to check min distance
//    // this method is used only if we have <=3 points in the plane
//    public static double closestPairUsingBruteForce(Point[] points) {
//        double min = Double.MAX_VALUE;
//
//        for (int i = 0; i < points.length; ++i) {
//            for (int j = i + 1; j < points.length; ++j) {
//                if (points[i].distanceTo(points[j]) < min) {
//                    min = points[i].distanceTo(points[j]);
//                    bestPair = new Pair(points[i], points[j], min);
//                }
//            }
//        }
//
//        return min;
//    }



    public static Point[] findClosestPairOfPoints(Point[] points) {
        closestPair(points);
        return bestPair.toPointArray();
    }

}
