package wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Simulation;
import wrapper.vehicle.VehicleManager;

/**
 * High level entry point for TraCI based simulations. This class hides the
 * static utility surface exposed by TraaS behind an object-oriented API and
 * exposes strongly typed collaborators for vehicle management.
 *
 * <p>The wrapper formalizes the usual TraaS flow:</p>
 * <ol>
 *     <li>Configure a {@link SumoTraciConnection} with step-length/start flags.</li>
 *     <li>Call {@link #start()} once to boot the SUMO server and build vehicle proxies.</li>
 *     <li>Iteratively call {@link #step()} to advance time and refresh vehicle snapshots.</li>
 *     <li>Drive the simulation through the {@link #getVehicles()} manager instead of
 *     scattered static calls.</li>
 * </ol>
 *
 * The class concentrates lifecycle validation and refresh wiring so that higher-level
 * code can stay focused on domain logic instead of procedural TraaS plumbing.
 */
public class TraasSimulation {
    private final SumoTraciConnection connection;
    private final double stepLength;
    private final VehicleManager vehicleManager;
    private boolean running;

    public TraasSimulation(String sumoBinary, String configFile, double stepLength) {
        this.connection = new SumoTraciConnection(sumoBinary, configFile);
        this.stepLength = stepLength;
        this.connection.addOption("step-length", stepLength + "");
        this.connection.addOption("start", "true");
        this.vehicleManager = new VehicleManager(connection);
    }

    public TraasSimulation(String configFile, double stepLength) {
        this("sumo", configFile, stepLength);
    }

    public VehicleManager getVehicles() {
        return vehicleManager;
    }

    public double getStepLength() {
        return stepLength;
    }

    public void start() {
        ensureNotRunning();
        try {
            connection.runServer();
            connection.setOrder(1);
            running = true;
            vehicleManager.refreshVehicles();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to start TraCI session", ex);
        }
    }

    public double step() {
        ensureRunning();
        try {
            connection.do_timestep();
            vehicleManager.refreshVehicles();
            return (double) connection.do_job_get(Simulation.getTime());
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to progress simulation", ex);
        }
    }

    public void close() {
        if (!running) {
            return;
        }
        connection.close();
        running = false;
    }

    private void ensureRunning() {
        if (!running) {
            throw new IllegalStateException("Simulation is not running. Call start() first.");
        }
    }

    private void ensureNotRunning() {
        if (running) {
            throw new IllegalStateException("Simulation already started");
        }
    }
}
