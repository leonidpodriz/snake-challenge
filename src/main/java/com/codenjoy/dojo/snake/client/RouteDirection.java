package com.codenjoy.dojo.snake.client;

import com.codenjoy.dojo.services.Point;

import java.util.Optional;

public class RouteDirection {
    private final Point point;
    private final RouteDirection parentDirection;

    public RouteDirection(Point point, RouteDirection parentDirection) {
        this.point = point;
        this.parentDirection = parentDirection;
    }

    public RouteDirection(Point point) {
        this.point = point;
        this.parentDirection = null;
    }

    public Point getPoint() {
        return point;
    }

    public Optional<RouteDirection> getParentDirection() {
        return Optional.ofNullable(parentDirection);
    }

    public int getLength() {
        return 1 + getParentDirection().map(RouteDirection::getLength).orElse(0);
    }

    private boolean hasParentDirection() {
        return getParentDirection().isPresent();
    }

    private void copyParentDirectionPointsTo(Point[] to) {
        if (hasParentDirection()) {
            Point[] parentPoints = parentDirection.getDirectionPoints();
            System.arraycopy(parentPoints, 0, to, 0, parentDirection.getLength());
        }
    }

    public Point[] getDirectionPoints() {
        int length = getLength();
        Point[] points = new Point[length];

        copyParentDirectionPointsTo(points);
        points[length - 1] = point;

        return points;
    }
}
