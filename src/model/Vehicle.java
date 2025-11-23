package model;

import java.util.Objects;

import de.tudresden.sumo.objects.Position;

public class Vehicle {

    private final String id;
    private final String type;
    private final String color;
    private double speed;
    private Position position;

    public Vehicle(String id, String type, String color) {
        this.id = Objects.requireNonNull(id, "Vehicle ID can't be null");
        this.type = type;
        this.color = color;
        this.speed = 0.0;
        this.position = new Position(0, 0);
    }

    public Position getPosition() {
        return position;
    }

    public double getspeed() {
        return speed;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setPosition(Position position) {
        this.position = Objects.requireNonNull(position);
    }

    public void remove() {
    }

}
