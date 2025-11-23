package wrapper;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Trafficlight;

import java.util.List;
import java.util.ArrayList;

public class TrafficLightWrapper {
    String ID;
    TrafficLightWrapper(String temp){
        ID = temp;
        System.out.println("Added " + temp + ".");
    }
    // get ID
    public String getID(int po) {
        if (po == 1) {System.out.println(ID);}
        return ID;
    }
    // get phase
    public int getPhase(SimulationWrapper temp, int po){
        try {
            int tlsPhase = (int)temp.conn.do_job_get(Trafficlight.getPhase(ID));
            if (po == 1) {System.out.println(String.format("tlsPhase of %s: %d", ID, tlsPhase));}
            return tlsPhase;
        }
        catch(Exception B) {
            System.out.println("Didn't work");
        }
        return -1;
    }
    // update all traffic light IDs of simulation
    public static void updateTrafficLightIDs(SimulationWrapper temp) {
        try {
            List<String> IDsList = (List<String>)temp.conn.do_job_get(Trafficlight.getIDList());
            for (String x : IDsList) {
                TrafficLightWrapper y = new TrafficLightWrapper(x);
                temp.TrafficLightList.add(y);
            }
        }
        catch (Exception A) {
            System.out.println("Didn't work");
        }
    }
}