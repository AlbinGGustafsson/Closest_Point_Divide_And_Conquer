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

        Point[] sortedByX = new Point[points.length];
        Point[] pointsY = new Point[points.length];

        for(int i = 0; i < points.length; i++){
            sortedByX[i] = points[i];
            pointsY[i] = points[i];
        }

        Arrays.sort(sortedByX, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        //Arrays.sort(sortedByY, (p1, p2) -> Double.compare(p1.y(), p2.y()));

        return findClosestPair(sortedByX, pointsY, points.length - 1).toPointArray();
    }

    private static Pair findClosestPair(Point[] sortedByX, Point[] pointsY, int size) {
        if (size < 3) {
            return bruteForceMinimumPair(sortedByX);
        }

        int mid = size / 2;
        Point midPoint = sortedByX[mid];

        List<Point> leftSubPointsY = new ArrayList<>();
        List<Point> leftSubPointsSortedX = new ArrayList<>();
        List<Point> rightSubPointsY = new ArrayList<>();
        List<Point> rightSubPointsSortedX = new ArrayList<>();

        for (int index = 0; index < size; ++index){
            if (pointsY[index].x() <= midPoint.x()){
                leftSubPointsY.add(pointsY[index]);
                leftSubPointsSortedX.add(sortedByX[index]);
            }else{
                rightSubPointsY.add(pointsY[index]);
                rightSubPointsSortedX.add(sortedByX[index]);
            }
        }



        Pair leftPair = findClosestPair(points, left, mid);
        Pair rightPair = findClosestPair(points, mid + 1, right);

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
            for (int j = i + 1; j < n; j++) {
                 if (strip.get(j).y() - strip.get(i).y() > minPair.distance){
                     break;
                 }else {
                     double dist = strip.get(i).distanceTo(strip.get(j));
                     if (dist < minPair.distance) {
                         minPair = new Pair(strip.get(i), strip.get(j), dist);
                     }
                 }

            }
        }

        return minPair;
    }

    private static Pair bruteForceMinimumPair(Point[] sortedByX) {
        return null;
    }
}
