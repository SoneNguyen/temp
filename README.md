### How to run
Change directory to src," cd src " then 
```
javac -cp "../lib/TraaS.jar;." TestTraaS.java; java -cp "../lib/TraaS.jar;." TestTraaS
```
. : to start at current directory
### File Usage
1. TestTraaS.java 
- Use for testing TraaS without wrapper
2. MainTest.java
- Use for testing TraaS with wrapper
3. CodeLog.txt
- For logging (not implemented)
### Requirement
- Create JavaDoc for every class created
## Cores Features
### Features for wrapper (Need to specify library's branch)
#### Basic Description
TraaS uses static methods and procedural patterns. Your task is to build a clean, reusable, 
and extensible object-oriented wrapper around it. This wrapper should: 
- Encapsulate vehicle, edge, and traffic light logic
- Support querying and control via instance methods 
- Enable grouping, filtering, and event handling

Source for building wrapper:
https://sumo.dlr.de/javadoc/traas/ 
1. Use the TraaS API to connect to a running SUMO simulation.
From it.polito.appeal.traci.SumoTraciConnection:
``` 
SumoTraciConnection  conn  =  new  SumoTraciConnection(sumo_bin, config_file);
```
2. Step through the simulation in real time
From it.polito.appeal.traci.SumoTraciConnection:
```
conn.do_timestep();
```
3. Traffic Light Management
- Display traffic light states and phase durations:
From de.tudresden.sumo.cmd.Trafficlight:
```
int  tlsPhase  = (int)conn.do_job_get(Trafficlight.getPhase("InSertNameHere"));
String  tlsPhaseName  = (String)conn.do_job_get(Trafficlight.getPhaseName("InserNameHere"));
```
- Show traffic lights with current phase indicators. (same with above??)
- Allow users to adjust phase durations and observe effects on traffic flow.
- Control traffic lights programmatically.
4. Vehicle Related
- Display moving vehicles with color-coded icons.
- Show subsets of vehicles according to their properties (filtered by e.g. color, speed, 
or location)
- Inject vehicles
5. Edge / Route Related
- ???
6. Statistics & Analytics
  Track metrics such as: 
- Average speed 
- Vehicle density per edge 
- Congestion hotspots 
- Travel time distribution 
- Display charts and summaries in real time. 
- Read telemetry
### Features unrelated with wrapper (maybe)
1. Interactive Map Visualization 
- Render the road network.
- Support zooming, panning, and camera rotation. 
2. Vehicle Injection & Control 
- Allow users to create vehicles on specific edges via GUI. (could be in wrapper)
- Support batch injection for stress testing. 
- Enable control over vehicle parameters (speed, color, route). (could be in wrapper)
3. Traffic Light Management 
- Enable manual phase switching via GUI. (base need in wrapper)
4. Exportable Reports 
- Save simulation statistics to CSV for external analysis. 
- Generate PDF summaries with charts, metrics, and timestamps. 
- Include filters (e.g. only red cars, only congested edges) in exports.
## Additional Recommended Features 
1. Stress Testing Tools 
- Simulate heavy traffic on selected edges.
- Observe system behavior under load.
- Compare static vs adaptive traffic light strategies. 
2. Adaptive Traffic Control 
- Allow users to experiment with traffic light timing to improve flow. 
- Provide feedback on performance metrics after adjustments. 
- Optionally integrate simple rule-based adaptation. 







