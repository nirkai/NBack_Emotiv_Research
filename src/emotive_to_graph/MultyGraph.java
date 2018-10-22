package emotive_to_graph;

import java.io.*;
import java.util.*;

public class MultyGraph {
    String signalsFile;
    String time="";
    int FIRST_WAVE_COLUMN = 3;
    int NUM_OF_COLUMNS = 8;
    int NUM_OF_WAVES = 5;
    HashMap<String, Signals> signalsMap = new HashMap<>();
    String []channelsNames = {	"IED_AF3", "IED_F7", "IED_F3", "IED_FC5", "IED_T7", "IED_P7",
            "IED_Pz", "IED_O1", "IED_O2", "IED_P8", "IED_T8", "IED_FC6", "IED_F4", "IED_F8", "IED_AF4"
    };
    //String[] waves = {"alpha", "lBetha", "hBetha", "gamma", "theta" };

    public MultyGraph(String fileName) {
        //init();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            String s = scanner.nextLine();
            time = scanner.next();
            scanner.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        signalsFile = fileName;
        fromFileToMap();
   //     printWaves();
    }

    public void init(String [] waveNames){
        for (String channelsName : channelsNames) {
            signalsMap.put(channelsName, new Signals(channelsName, waveNames));
        }
    }

    public void fromFileToMap(){
        try {
            String[] digitsOfSignals;
            FileReader file = new FileReader(signalsFile);
            BufferedReader br = new BufferedReader(file);
            String firstLine = br.readLine();
            String[] fl = firstLine.split(", ");
            String[] waves = Arrays.copyOfRange(fl, FIRST_WAVE_COLUMN, fl.length);

            init(waves);
            for (String line; (line = br.readLine()) != null ;){
                digitsOfSignals = line.split(", ");
                if (digitsOfSignals.length == NUM_OF_COLUMNS){
                    HashMap<String, List<Double>> ws = signalsMap.get(digitsOfSignals[1]).getWavesSignals();
                    //signalsMap.get(digitsOfSignals[1]).setTime(digitsOfSignals[0]);
                    for (int i = 0; i < NUM_OF_WAVES; i++) {
                        List<Double> ad = ws.get(waves[i]);
                        double d= Double.parseDouble(digitsOfSignals[i + FIRST_WAVE_COLUMN]);
                        ad.add(d);
                       // signalsMap.get(digitsOfSignals[1]).getWavesSignals().get(i).add(Double.parseDouble(digitsOfSignals[i + 2]));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printWaves(){
        for (Double aDouble : signalsMap.get(channelsNames[0]).getWavesSignals().get("Alpha")) {
            System.out.println(aDouble);
        }
    }

    public List<Double> getSignalsOfWaveInChannel(String channel, String wave){
        List<Double> list = new ArrayList<>();
        for (Double aDouble : signalsMap.get(channel).getWavesSignals().get(wave)) {
            //System.out.println(aDouble);
            list.add(aDouble);
        }
        return list;
    }

    public HashMap<String, Signals> getSignalsMap() {
        return signalsMap;
    }

    public String getStartOfNBack(){
        return time;
    }

}
