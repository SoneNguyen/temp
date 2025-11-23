package wrapper.vehicle;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoPosition2D;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.Objects;

/**
 * Object-oriented representation of a vehicle that exposes typed query and
 * control operations instead of static TraaS calls.
 *
 * <p>Each proxy caches a {@link VehicleSnapshot} produced by {@link #refresh()},
 * making it safe to ask for the latest lane or speed without issuing additional
 * TraCI calls. Control helpers (speed, lane, stop) provide the common knobs
 * required by most simulations while keeping the TraaS primitives encapsulated.
 * </p>
 */
public class VehicleProxy {
    private final String id;
    private final SumoTraciConnection connection;
    private VehicleSnapshot snapshot;

    VehicleProxy(String id, SumoTraciConnection connection) {
        this.id = Objects.requireNonNull(id, "Vehicle id is required");
        this.connection = Objects.requireNonNull(connection, "TraCI connection is required");
    }

    public String getId() {
        return id;
    }

    public VehicleSnapshot getSnapshot() {
        return snapshot;
    }

    public VehicleSnapshot refresh() {
        try {
            double speed = (double) connection.do_job_get(Vehicle.getSpeed(id));
            SumoPosition2D position = (SumoPosition2D) connection.do_job_get(Vehicle.getPosition(id));
            String lane = (String) connection.do_job_get(Vehicle.getLaneID(id));
            String type = (String) connection.do_job_get(Vehicle.getTypeID(id));
            VehicleSnapshot current = new VehicleSnapshot(id, type, lane, position, speed);
            this.snapshot = current;
            return current;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to refresh vehicle state for " + id, ex);
        }
    }

    public void setSpeed(double speed) {
        try {
            connection.do_job_set(Vehicle.setSpeed(id, speed));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to set speed for " + id, ex);
        }
    }

    public void slowDown(double speed, double durationSeconds) {
        try {
            connection.do_job_set(Vehicle.slowDown(id, speed, durationSeconds));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to slow down vehicle " + id, ex);
        }
    }

    public void changeLane(String laneId, int durationSeconds) {
        try {
            connection.do_job_set(Vehicle.changeLane(id, laneId, durationSeconds));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to change lane for " + id, ex);
        }
    }

    public void stopAt(String edgeId, double position, int laneIndex) {
        try {
            connection.do_job_set(Vehicle.stop(id, edgeId, position, (byte) laneIndex, 0.0));
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to stop vehicle " + id + " at edge " + edgeId, ex);
        }
    }
}
