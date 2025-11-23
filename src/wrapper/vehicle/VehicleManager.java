package wrapper.vehicle;

import de.tudresden.sumo.cmd.Vehicle;
import it.polito.appeal.traci.SumoTraciConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Maintains the current set of vehicles in the simulation and notifies listeners
 * about lifecycle events. It provides higher-level grouping and filtering
 * helpers to make bulk operations straightforward.
 *
 * <p>Wrapper formula: {@code refreshVehicles()} snapshots the current TraCI state,
 * materializes {@link VehicleProxy proxies} on first sight, and forwards
 * lifecycle deltas to {@link VehicleEventListener listeners}. All consumer code
 * can therefore rely on {@link #all()} and {@link #filter(java.util.function.Predicate)}
 * to operate on consistent, object-oriented vehicle views without touching
 * static TraaS calls directly.</p>
 */
public class VehicleManager {
    private final SumoTraciConnection connection;
    private final Map<String, VehicleProxy> vehicles = new HashMap<>();
    private final List<VehicleEventListener> listeners = new ArrayList<>();

    public VehicleManager(SumoTraciConnection connection) {
        this.connection = connection;
    }

    public void addListener(VehicleEventListener listener) {
        listeners.add(listener);
    }

    public VehicleProxy getVehicle(String vehicleId) {
        return vehicles.get(vehicleId);
    }

    public VehicleGroup all() {
        return new VehicleGroup(new ArrayList<>(vehicles.values()));
    }

    public VehicleGroup filter(Predicate<VehicleProxy> predicate) {
        List<VehicleProxy> filtered = new ArrayList<>();
        for (VehicleProxy vehicle : vehicles.values()) {
            if (predicate.test(vehicle)) {
                filtered.add(vehicle);
            }
        }
        return new VehicleGroup(filtered);
    }

    public void refreshVehicles() {
        List<String> ids = fetchVehicleIds();
        Set<String> seen = new HashSet<>(ids);

        for (String id : ids) {
            VehicleProxy vehicle = vehicles.computeIfAbsent(id, key -> {
                VehicleProxy proxy = new VehicleProxy(key, connection);
                listeners.forEach(listener -> listener.onVehicleAdded(proxy));
                return proxy;
            });

            VehicleSnapshot previous = vehicle.getSnapshot();
            vehicle.refresh();
            VehicleSnapshot current = vehicle.getSnapshot();
            if (previous == null || !previous.equals(current)) {
                listeners.forEach(listener -> listener.onVehicleUpdated(vehicle, previous));
            }
        }

        removeMissingVehicles(seen);
    }

    private void removeMissingVehicles(Set<String> presentVehicles) {
        Set<String> toRemove = new HashSet<>(vehicles.keySet());
        toRemove.removeAll(presentVehicles);
        for (String missingId : toRemove) {
            vehicles.remove(missingId);
            listeners.forEach(listener -> listener.onVehicleRemoved(missingId));
        }
    }

    private List<String> fetchVehicleIds() {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) connection.do_job_get(Vehicle.getIDList());
            return ids;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to query vehicles from TraCI", ex);
        }
    }

    public Collection<VehicleProxy> getVehicles() {
        return Collections.unmodifiableCollection(vehicles.values());
    }
}
