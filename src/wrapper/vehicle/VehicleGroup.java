package wrapper.vehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Represents a collection of vehicles with convenience helpers for bulk
 * operations and further filtering.
 *
 * <p>This type carries the "group then act" pattern used throughout the
 * wrapper: {@link wrapper.vehicle.VehicleManager VehicleManager} returns groups
 * so callers can filter and issue collective commands (like {@link #setSpeedForAll(double)})
 * without reimplementing loops or leaking TraaS specifics.</p>
 */
public class VehicleGroup {
    private final List<VehicleProxy> vehicles;

    public VehicleGroup(List<VehicleProxy> vehicles) {
        this.vehicles = new ArrayList<>(vehicles);
    }

    public VehicleGroup filter(Predicate<VehicleProxy> predicate) {
        List<VehicleProxy> filtered = new ArrayList<>();
        for (VehicleProxy vehicle : vehicles) {
            if (predicate.test(vehicle)) {
                filtered.add(vehicle);
            }
        }
        return new VehicleGroup(filtered);
    }

    public void forEach(Consumer<VehicleProxy> action) {
        vehicles.forEach(action);
    }

    public void setSpeedForAll(double speed) {
        forEach(vehicle -> vehicle.setSpeed(speed));
    }

    public List<VehicleProxy> asList() {
        return Collections.unmodifiableList(vehicles);
    }
}
