import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClosestPointsCalculator {

    public static Point[] findClosestPairOfPoints(Point[] points) {

        Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        List<Point> sortedY = Arrays.stream(points).sorted((p1, p2) -> Double.compare(p1.y(), p2.y())).toList();
//        ArrayList<Point> sortedY = new ArrayList<>(List.of(points));
//        sortedY.sort((p1, p2) -> Double.compare(p1.y(), p2.y()));

        return findClosestPair(points, sortedY, 0, points.length - 1).toPointArray();
    }

    private static Pair findClosestPair(Point[] sortedX, List<Point> sortedY, int left, int right) {
        int length = right - left + 1;
        if (length <= 3) {
            return bruteForceMinPair(sortedX, left, right);
        }

        int mid = (left + right) / 2;
        Point midPoint = sortedX[mid];

        List<Point> sortedYLeft = new ArrayList<>();
        List<Point> sortedYRight = new ArrayList<>();

        for (Point p : sortedY) {
            if (p.x() < midPoint.x()) {
                sortedYLeft.add(p);
            } else {
                sortedYRight.add(p);
            }
        }

        Pair leftPair = findClosestPair(sortedX, sortedYLeft, left, mid);
        Pair rightPair = findClosestPair(sortedX, sortedYRight, mid + 1, right);

        Pair closestPair = (leftPair.getDistance() < rightPair.getDistance()) ? leftPair : rightPair;

        List<Point> strip = new ArrayList<>();
        for (int i = 0; i < sortedY.size(); i++) {
            if (Math.abs(sortedY.get(i).x() - midPoint.x()) < closestPair.getDistance()) {
                strip.add(sortedY.get(i));
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size(); j++) {
                if (strip.get(j).y() - strip.get(i).y() > closestPair.getDistance()) {
                    break;
                } else {
                    double distance = strip.get(i).distanceTo(strip.get(j));
                    if (distance < closestPair.getDistance()) {
                        closestPair.updatePair(strip.get(i), strip.get(j), distance);
                    }
                }

            }
        }

        return closestPair;
    }

    private static Pair bruteForceMinPair(Point[] points, int left, int right) {
        Pair minPair = new Pair(null, null, Double.POSITIVE_INFINITY);
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < minPair.getDistance()) {
                    //minPair = new Pair(points[i], points[j], distance);
                    minPair.updatePair(points[i], points[j], distance);
                }
            }
        }
        return minPair;
    }
}
