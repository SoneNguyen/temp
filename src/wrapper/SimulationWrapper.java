package wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Trafficlight;

import java.util.List;
import java.util.ArrayList;

public class SimulationWrapper {
    protected SumoTraciConnection conn;
    protected final List<TrafficLightWrapper> TrafficLightList = new ArrayList<TrafficLightWrapper>();
    // Constructor 1
    public SimulationWrapper(String sumocfg, double step_length){
        String sumo_bin = "sumo";
        conn = new SumoTraciConnection(sumo_bin, sumocfg);
        conn.addOption("step-length", step_length + "");
        conn.addOption("start", "true"); //start sumo immediately
        System.out.println("Simulation created");
    }
    // Constructor 2
    SimulationWrapper(String sumocfg){
        String sumo_bin = "sumo";
        double step_length = 1;
        conn = new SumoTraciConnection(sumo_bin, sumocfg);
        conn.addOption("step-length", step_length + "");
        conn.addOption("start", "true"); //start sumo immediately
        System.out.println("Simulation created");
    }
//===== SIMULATION STUFF ==================================
    // Start simulation, update TrafficLightList, more will be implemented
    public void Start(){
        try {
            conn.runServer();
            conn.setOrder(1);
            TrafficLightWrapper.updateTrafficLightIDs(this);
            System.out.println("Started successfully.");
        }
        catch(Exception e) {System.out.println("Failed to start.");}
    }
    // Do a simulation's time step
    public void Step(){
        try {conn.do_timestep();}
        catch(Exception e) {System.out.println("Failed to step.");}
    }
    // Close simulation
    public void End() {
        conn.close();
    }
    // Get simulation time
    public double getTime(int po) {
        try {
            double time = (double)conn.do_job_get(Simulation.getTime());
            if (po == 1) {System.out.println("Current Time: " + time);}
            return time;
        }
        catch(Exception e) {System.out.println("Can't get the time.");}
        return -1;
    }


//===== TRAFFIC LIGHT STUFF ===============================
    // print all traffic light IDs
    public void printTrafficLightList() {
        System.out.println("List of Traffic Light IDs:");
        for (TrafficLightWrapper x : TrafficLightList) {
            x.getID(1);
        }
    }
    // get phase of a traffic light
    public int getTLPhase(int temp) {
        TrafficLightWrapper x = TrafficLightList.get(temp);
        int phase = x.getPhase(this, 1);
        return phase;
    }
//===== VEHICLE STUFF =====================================
    // not implemented
}