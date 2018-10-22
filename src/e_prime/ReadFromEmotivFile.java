package e_prime;

import emotive_to_graph.Signals;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.*;
import java.util.*;

public class ReadFromEmotivFile {
    String signalsFile;
    String time="";
    int FIRST_WAVE_COLUMN = 3;
    int NUM_OF_COLUMNS = 8;
    int NUM_OF_WAVES = 5;

    public String[] getChannelsNames() {
        return channelsNames;
    }

    HashMap<String, Signals> signalsMap = new HashMap<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
    private String []channelsNames = {	"IED_AF3", "IED_F7", "IED_F3", "IED_FC5", "IED_T7", "IED_P7",
            "IED_Pz", "IED_O1", "IED_O2", "IED_P8", "IED_T8", "IED_FC6", "IED_F4", "IED_F8", "IED_AF4"
    };
    Timestamp timeOfStartNBack;
    //String[] waves = {"alpha", "lBetha", "hBetha", "gamma", "theta" };

    public ReadFromEmotivFile(String fileName, String startOfTrial) {
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
        timeOfStartNBack = new Timestamp(0);
        try {
            timeOfStartNBack.setTime(sdf.parse(startOfTrial).getTime()); // start of the NBack trial;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        signalsFile = fileName;
        fromFileToMap();
    //    printWaves();
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

            br.readLine();

            boolean isTimeLow = true;
            Timestamp timestamp = new Timestamp(0);
            for (String line; isTimeLow ; ){
                if (timestamp.compareTo(timeOfStartNBack) == 0)
                    isTimeLow = false;
                else {
                    line = br.readLine();
                    digitsOfSignals = line.split(", ");
                    try {
                        String t = digitsOfSignals[0];
                        timestamp.setTime(sdf.parse(digitsOfSignals[0]).getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        System.out.println("error:" + digitsOfSignals[0]);
                    }
                }
            }
            System.out.println(sdf.format(timestamp.getTime()));


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
        return new HashMap<>(signalsMap);
    }

    public String getStartOfNBack(){
        return time;
    }

}
