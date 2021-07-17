package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DirectionBuilder {
    final Board board;
    final Point from;
    final Point to;
    List<Point> usedPoints = new ArrayList<>();

    public DirectionBuilder(Board board, Point from, Point to) {
        this.board = board;
        this.from = from;
        this.to = to;
    }

    private boolean isPointsEquals(Point point1, Point point2) {
        return point1.getX() == point2.getX() && point1.getY() == point2.getY();
    }

    private boolean isPointIsTarget(Point point) {
        return isPointsEquals(point, to);
    }

    private boolean isPointUsed(Point point) {
        return usedPoints.contains(point);
    }

    private boolean isPointIsBarrier(Point point) {
        return board.getBarriers().contains(point);
    }

    public boolean canBePointUsed(Point point) {
        return isPointIsTarget(point) || (!isPointIsBarrier(point) && !isPointUsed(point));
    }

    public void markPointUsed(Point point) {
        usedPoints.add(point);
    }

    public Point[] getChildPointsFrom(Point point) {
        return new Point[]{
                new PointImpl(point.getX() + 1, point.getY()),
                new PointImpl(point.getX() - 1, point.getY()),
                new PointImpl(point.getX(), point.getY() - 1),
                new PointImpl(point.getX(), point.getY() + 1)
        };
    }

    public List<Point> getUnusedPointsFrom(Point point) {
        return Arrays.stream(getChildPointsFrom(point))
                .filter(this::canBePointUsed)
                .collect(Collectors.toList());
    }

    public List<RouteDirection> getDirections() {
        List<Point> points = getUnusedPointsFrom(from);
        List<RouteDirection> routes = points.stream().map(RouteDirection::new).collect(Collectors.toList());

        points.forEach(this::markPointUsed);

        return getDirections(routes);
    }

    public List<RouteDirection> getDirections(List<RouteDirection> routes) {
        List<RouteDirection> targetRoutes = routes.stream().filter(r -> isPointIsTarget(r.getPoint())).collect(Collectors.toList());

        if (targetRoutes.size() > 0) {
            return targetRoutes;
        }

        if (routes.size() == 0) {
            return routes;
        }

        List<RouteDirection> newRoutes = routes
                .stream()
                .flatMap(r -> getUnusedPointsFrom(r.getPoint()).stream().map(p -> new RouteDirection(p, r)))
                .collect(Collectors.toList());

        return getDirections(newRoutes);
    }
}