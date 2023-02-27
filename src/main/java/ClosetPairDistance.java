import java.util.Arrays;

public class ClosetPairDistance {

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

    // Method to find the min distance
    public Pair closestPair(Point[] points) {
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
    private Pair closestPair(Point[] px, Point[] py, int low, int high) {
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
        // now find the min distance from strip of points
        double minFromStrip = getMinStripeDistance(py, stripLeft, stripRight);
        // finally min distance id the one min of left, right and from the strip
        return Math.min(minDistance, minFromStrip);
    }

    // brute force method to check min distance
    // this method is used only if we have <=3 points in the plane
    public Pair closestPairUsingBruteForce(Point[] points) {
        Pair minPair = new Pair(null, null, MIN_VAL);

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dist = points[i].distanceTo(points[j]);
                if (dist < minPair.distance) {
                    minPair = new Pair(points[i], (points[j]), dist);
                }
                minPair = Math.min(min, dist);

            }
        }
        return min;
    }

    // min distance in strip of points
    private Pair getMinStripeDistance(Point[] ySorted, int low, int high) {
        Pair minPair = new Pair(null, null, MIN_VAL);

        //Pick all points one by one and try the next points till the difference
        //between y coordinates is smaller than d.
        //This is a proven fact that this loop runs at most 6 times
        for (int i = low; i <= high; i++) {
            for (int j = i + 1; j <= high; j++) {

                double dist = ySorted[i].distanceTo(ySorted[j]);
                if (dist < minPair.distance) {
                    minPair = new Pair(ySorted[i], (ySorted[j]), dist);
                }
                //min = Math.min(min, ySorted[i].distanceTo(ySorted[j]));
            }
        }
        return minPair;
    }

    // method to find min between two values

    // driver method
    public static void main(String[] args) {
        int[] x = {2, 12, 40, 5, 12, 3};
        int[] y = {3, 30, 50, 1, 10, 4};

        int n = x.length;
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = new Point(x[i], y[i]);
        }

        ClosetPairDistance obj = new ClosetPairDistance();
        Pair distance = obj.closestPair(points);

        System.out.println(distance);
    }

}