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

    public static Point[] findClosestPairOfPoints(Point[] points) {
        Arrays.sort(points, (p1, p2) -> Double.compare(p1.x(), p2.x()));
        //return bruteForceMinPair(points, 0, points.length - 1).toPointArray();
        return findClosestPair(points, 0, points.length - 1).toPointArray();
    }

    private static Pair findClosestPair(Point[] points, int left, int right) {

        int length = right - left + 1;

        //När det bara är 3 eller mindre som ska "jämföras" så "bruteforcas" det minsta paret fram.
        //Även om det är en loop i en loop så påverkas inte tidskomplexiteten för att det är så få varv.
        if (length <= 3) {
            return bruteForceMinPair(points, left, right);
        }

        int mid = (left + right) / 2;
        Point midPoint = points[mid];

        //Antingen så kommer närmsta paret ligga i vänstra delen, högra delen eller i en av varje.

        //Anropar vänstra delen och högra delen rekursivt.
        //För att hitta närmsta paren i bägge delar.
        //Kommer tillslut hamna i "basfallet" och då kommer det minsta paret "bruteforcas".
        Pair leftPair = findClosestPair(points, left, mid);
        Pair rightPair = findClosestPair(points, mid + 1, right);

        Pair closestPair = (leftPair.distance < rightPair.distance) ? leftPair : rightPair;

        //Det finns en chans att närmsta paret har en punkt i vänstra och en punkt i högra.
        //Ett sådant par kan vara närmst om den ligger i strippen.
        //Strippen är closestPair.distance från mitten.

        //Lägger till Points som ligger i "strippen".
        List<Point> strip = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            if (Math.abs(points[i].x() - midPoint.x()) < closestPair.distance) {
                strip.add(points[i]);
            }
        }

        //Denna sortering görs bara på Pointsen som ligger i strippen.
        //I ett värsta fall ligger alla Points i strippen men i praktiken antar jag att det sker sällan
        //I det fallet blir denna sortering O(N log N) vilket då även gör det rekursiva anropet O(N log N).
        strip.sort((p1, p2) -> Double.compare(p1.y(), p2.y()));

        int n = strip.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                //Eftersom listan är sorterad på punkternas y-koorinater så kan man stoppa en sökning från en
                //punkt om dess y värde och punkten den jämförs meds y värde skuljer sig mer än closestPair.distance.
                if (strip.get(j).y() - strip.get(i).y() > closestPair.distance) {
                    break;
                } else {
                    double distance = strip.get(i).distanceTo(strip.get(j));
                    //Om en Point har lägre distance än den hittils minsta distance så är det den nya minsta
                    if (distance < closestPair.distance) {
                        //closestPair = new Pair(strip.get(i), strip.get(j), distance);
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
                if (distance < minPair.distance) {
                    //minPair = new Pair(points[i], points[j], distance);
                    minPair.updatePair(points[i], points[j], distance);
                }
            }
        }
        return minPair;
    }

}
