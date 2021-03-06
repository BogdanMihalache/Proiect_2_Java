import java.io.*;
import java.util.*;
import java.awt.geom.Point2D;

public class Proiect {

    public static List<Point2D> generate_points(int nPoints) {
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < nPoints; i++)
        {
            points.add(new Point2D.Double(Math.random()*1.4 - 0.7, Math.random()*1.4 - 0.7));
        }
        return points;
    }

    public static List<Point2D> generate_polygon(int nPoints) {
        List<Point2D> points = generate_points(nPoints);
        Point2D p1 = removeRandom(points);
        Point2D p2 = removeRandom(points);

        List<Point2D> group1 = new ArrayList<>();
        List<Point2D> group2 = new ArrayList<>();
        partition(points, p1, p2, group1, group2);

        List<Point2D> path1 = buildPath(p1, p2, group1);
        List<Point2D> path2 = buildPath(p2, p1, group2);

        List<Point2D> result = new ArrayList<>();
        result.add(p1);
        result.addAll(path1);
        result.add(p2);
        result.addAll(path2);
        return result;
    }

    public static List<Point2D> buildPath(Point2D p1, Point2D p2, List<Point2D> points) {
        if (points.isEmpty()) {
            return new ArrayList<>();
        } else {
            Point2D c = removeRandom(points);
            Point2D c2 = randomBetween(p1, p2);

            List<Point2D> group1 = new ArrayList<>();
            List<Point2D> group2 = new ArrayList<>();
            partition(points, c2, c, group1, group2);

            List<Point2D> result = new ArrayList<>();
            if (isLeft(c2, c, p1)) {
                result.addAll(buildPath(p1, c, group1));
                result.add(c);
                result.addAll(buildPath(c, p2, group2));
            } else {
                result.addAll(buildPath(p1, c, group2));
                result.add(c);
                result.addAll(buildPath(c, p2, group1));
            }
            return result;
        }
    }

    public static void partition(List<Point2D> points, Point2D p1, Point2D p2, List<Point2D> group1, List<Point2D> group2) {
        for (Point2D p : points) {
            if (isLeft(p1, p2, p)) {
                group1.add(p);
            } else {
                group2.add(p);
            }
        }
    }

    public static Point2D randomBetween(Point2D p1, Point2D p2) {
        double t = Math.random();
        return new Point2D.Double(p1.getX() * t + p2.getX() * (1 - t),
                p1.getY() * t + p2.getY() * (1 - t));
    }

    public static boolean isLeft(Point2D l1, Point2D l2, Point2D p) {
        double lx = l2.getX() - l1.getX();
        double ly = l2.getY() - l1.getY();
        return lx * (p.getY() - l1.getY()) - ly * (p.getX() - l1.getX()) > 0;
    }

    public static Point2D removeRandom(List<Point2D> pts) {
        return pts.remove(new Random().nextInt(pts.size()));
    }

    public static void writeToSvg(String fname, List<Point2D> points) {
        PrintWriter w = null;
        try {
            w = new PrintWriter(fname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        w.println("<svg width=\"1024px\" height=\"1024px\">");
        for (int q = 0; q < points.size() - 1; q++) {
            Point2D p1 = points.get(q);
            Point2D p2 = points.get(q+1);
            w.printf("<polyline points=\"%f,%f %f,%f\" fill=\"none\" stroke-width=\"2\" stroke=\"black\" />\n",
                    p1.getX()*512 + 512, -p1.getY()*512 + 512,
                    p2.getX()*512 + 512, -p2.getY()*512 + 512);
        }
        Point2D p1 = points.get(points.size() - 1);
        Point2D p2 = points.get(0);
        w.printf("<polyline points=\"%f,%f %f,%f\" fill=\"none\" stroke-width=\"2\" stroke=\"black\" />\n",
                p1.getX()*512 + 512, -p1.getY()*512 + 512,
                p2.getX()*512 + 512, -p2.getY()*512 + 512);
        w.println("</svg>");
        w.close();
    }

    public static void writeToText(String fname, List<Point2D> points){
        PrintWriter w = null;
        try {
            w = new PrintWriter(fname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Point2D p : points) {
            w.printf("%s,%s\n", p.getX(), p.getY());
        }
        w.close();
    }

    public static void main(String[] args) {
        //List<Point2D> point2DS = generate_points(10);
        //System.out.println(point2DS);
        List<Point2D> point2DS1 = generate_polygon(10);
        writeToSvg("polylines", point2DS1);
        writeToText("points",point2DS1);
    }
}