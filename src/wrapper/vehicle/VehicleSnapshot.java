package wrapper.vehicle;

import de.tudresden.sumo.objects.SumoPosition2D;

import java.util.Objects;

/**
 * Immutable data snapshot describing a vehicle at a point in time.
 */
public class VehicleSnapshot {
    private final String id;
    private final String typeId;
    private final String laneId;
    private final SumoPosition2D position;
    private final double speed;

    public VehicleSnapshot(String id, String typeId, String laneId, SumoPosition2D position, double speed) {
        this.id = Objects.requireNonNull(id, "Vehicle id is required");
        this.typeId = typeId;
        this.laneId = laneId;
        this.position = position;
        this.speed = speed;
    }

    public String getId() {
        return id;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getLaneId() {
        return laneId;
    }

    public SumoPosition2D getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleSnapshot)) return false;
        VehicleSnapshot that = (VehicleSnapshot) o;
        return Double.compare(that.speed, speed) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(typeId, that.typeId)
                && Objects.equals(laneId, that.laneId)
                && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, typeId, laneId, position, speed);
    }

    @Override
    public String toString() {
        return "VehicleSnapshot{" +
                "id='" + id + '\'' +
                ", typeId='" + typeId + '\'' +
                ", laneId='" + laneId + '\'' +
                ", position=" + position +
                ", speed=" + speed +
                '}';
    }
}
