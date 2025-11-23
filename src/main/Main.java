import wrapper.TraasSimulation;
import wrapper.vehicle.VehicleEventListener;
import wrapper.vehicle.VehicleGroup;
import wrapper.vehicle.VehicleProxy;
import wrapper.vehicle.VehicleSnapshot;

/**
 * Example bootstrap demonstrating the object-oriented wrapper for TraaS.
 * The main method intentionally avoids running long simulations; it simply shows
 * how to bootstrap the API and register listeners for vehicle events.
 */
public class Main {
    public static void main(String[] args) {
        String configFile = "../resource/test_1.sumocfg";
        double stepLengthSeconds = 1.0;

        TraasSimulation simulation = new TraasSimulation(configFile, stepLengthSeconds);
        simulation.getVehicles().addListener(new LoggingVehicleListener());

        simulation.start();
        for (int i = 0; i < 3; i++) {
            double time = simulation.step();
            VehicleGroup group = simulation.getVehicles().all();
            // Example: reduce speed of vehicles in lane J1_0 to demonstrate control.
            group.filter(vehicle -> {
                VehicleSnapshot snapshot = vehicle.getSnapshot();
                return snapshot != null && "J1_0".equals(snapshot.getLaneId());
            }).setSpeedForAll(5.0);
            System.out.println("Advanced to t=" + time + "s with " + group.asList().size() + " vehicles tracked.");
        }
        simulation.close();
    }

    private static class LoggingVehicleListener implements VehicleEventListener {
        @Override
        public void onVehicleAdded(VehicleProxy vehicle) {
            System.out.println("Vehicle joined: " + vehicle.getId());
        }

        @Override
        public void onVehicleUpdated(VehicleProxy vehicle, VehicleSnapshot previousState) {
            VehicleSnapshot current = vehicle.getSnapshot();
            if (previousState != null && current != null && previousState.getLaneId() != null
                    && !previousState.getLaneId().equals(current.getLaneId())) {
                System.out.printf("Vehicle %s changed lane %s -> %s%n", vehicle.getId(), previousState.getLaneId(), current.getLaneId());
            }
        }

        @Override
        public void onVehicleRemoved(String vehicleId) {
            System.out.println("Vehicle left: " + vehicleId);
        }
    }
}
