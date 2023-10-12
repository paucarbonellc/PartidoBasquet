package me.pau.team;

import me.pau.point.Point;

public class MatchTeam extends BaseTeam {

    private int points = 0;

    public MatchTeam(String name) {
        super(name);
    }

    public int addPoints(Point point) {
        points += point.getAmount();
        return points;
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
