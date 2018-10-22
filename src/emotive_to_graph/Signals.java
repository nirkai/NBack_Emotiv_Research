package emotive_to_graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Signals {
    String brainPoint;
    HashMap<String, List<Double>> wavesSignals;
    String time;

    public Signals(String brainPoint, String[] waves) {
        this.brainPoint = brainPoint;
        //String[] waves = {"alpha", "lBetha", "hBetha", "gamma", "theta" };
        wavesSignals = new HashMap<>();
        for (String wave : waves) {
            wavesSignals.put(wave, new ArrayList<>());
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBrainPoint() {
        return brainPoint;
    }

    public HashMap<String, List<Double>> getWavesSignals() {
        return wavesSignals;
    }
}
