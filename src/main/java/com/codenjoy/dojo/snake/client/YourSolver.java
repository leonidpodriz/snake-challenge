package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;

import java.util.List;
import java.util.Optional;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {
    final static private String playerID = "ylxu1wajnfb131cbg80v";
    final static private String playerCode = "3841699689546149885";
    final static private String urlTemplate = "http://167.99.241.128:80/codenjoy-contest/board/player/%s?code=%s";
    final static private String url = String.format(urlTemplate, playerID, playerCode);

    public YourSolver(Dice dice) {

    }

    static public Direction getDirection(Point p1, Point p2) {
        Direction direction = Direction.UP;

        if (p1.getX() > p2.getX()) {
            direction = Direction.LEFT;
        } else if (p1.getX() < p2.getX()) {
            direction = Direction.RIGHT;
        }

        if (p1.getY() > p2.getY()) {
            direction = Direction.DOWN;
        } else if (p1.getY() < p2.getY()) {
            direction = Direction.UP;
        }

        return direction;
    }

    private boolean isGameOver(Board board) {
        return board.isGameOver();
    }

    private Point getApple(Board board) {
        return board.getApples().get(0);
    }

    private Point getStone(Board board) {
        return board.getStones().get(0);
    }

    private int getSnakeSize(Board board) {
        return board.getSnake().size();
    }

    private Point getSnakeHead(Board board) {
        return board.getHead();
    }

    private Optional<RouteDirection> getRouteDirection(Board board, Point from, Point to) {
        List<RouteDirection> routeDirections = new DirectionBuilder(board, from, to).getDirections();

        if (routeDirections.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(routeDirections.get(0));
    }

    private Optional<Point[]> getDirectionPoints(Board board, Point from, Point to) {
        return getRouteDirection(board, from, to).map(RouteDirection::getDirectionPoints);
    }

    public Direction getDirection(Board board) {
        if (isGameOver(board)) return Direction.UP;

        Point apple = getApple(board);
        Point stone = getStone(board);
        Point head = getSnakeHead(board);
        Point target = getSnakeSize(board) > 20 ? stone : apple;
        Optional<Point[]> directionPoints = getDirectionPoints(board, head, target);
        Point nextPoint = directionPoints.map(dp -> dp[0]).orElse(new PointImpl(1, 1));

        return getDirection(head, nextPoint);
    }

    @Override
    public String get(Board board) {
        return getDirection(board).toString();
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                url,
                new YourSolver(new RandomDice()),
                new Board()
        );
    }

}
