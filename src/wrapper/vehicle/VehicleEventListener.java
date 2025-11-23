package wrapper.vehicle;

/**
 * Listener hooks invoked by {@link VehicleManager} when vehicle lifecycle
 * events occur. Default implementations are no-ops so callers can override only
 * the callbacks they care about.
 */
public interface VehicleEventListener {
    default void onVehicleAdded(VehicleProxy vehicle) {}

    default void onVehicleUpdated(VehicleProxy vehicle, VehicleSnapshot previousState) {}

    default void onVehicleRemoved(String vehicleId) {}
}
