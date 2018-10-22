package info_processing;

import emotive_to_graph.Signals;

import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;

public class CombineEmotiveEPrime {
    String signalsFile;
    String time="";
    int TIME_COLUMN = 0;
    int FIRST_WAVE_COLUMN = 3;
    int NUM_OF_COLUMNS = 8;
    int NUM_OF_WAVES = 5;
    //HashMap<String, Signals> signalsMap = new HashMap<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    String []channelsNames = {	"IED_AF3", "IED_F7", "IED_F3", "IED_FC5", "IED_T7", "IED_P7",
            "IED_Pz", "IED_O1", "IED_O2", "IED_P8", "IED_T8", "IED_FC6", "IED_F4", "IED_F8", "IED_AF4"
    };
    Timestamp timeOfStartNBack;
    public CombineEmotiveEPrime(String fileName, String startOfTrial) {
        edit_EmotivFile(fileName, startOfTrial);
    }

    private File edit_EmotivFile(String fileName, String startOfTrial){
        BufferedReader reader = null;
        File updarteFile = null;
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));
           // reader.readLine();
           // reader.readLine();
            updarteFile = removeLine(reader, file, sdf.parse(shortFormat(startOfTrial)).getTime());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return updarteFile;

    }

    private File removeLine(BufferedReader br , File f, long time) throws IOException, ParseException {
        File temp = new File("temp.txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        long removeID = time;
        String currentLine;
        String eprime_columns = ", T1.OnsetTime, T1.RT, T1.RTTTime, T1.ACC";
        bw.write(br.readLine() + eprime_columns + System.getProperty("line.separator"));
        bw.write(br.readLine() + eprime_columns + System.getProperty("line.separator"));
        while((currentLine = br.readLine()) != null){
            String [] splitLine = currentLine.split(",");
            String t = splitLine[TIME_COLUMN];

            String trimmedLine = currentLine.trim();
            if(sdf.parse(shortFormat(t)).getTime() < time){
                currentLine = "";
            }
            else
                bw.write(currentLine + System.getProperty("line.separator"));

        }
        bw.close();
        br.close();
        boolean delete = f.delete();
        boolean b = temp.renameTo(f);
        return temp;
    }

    private String shortFormat(String inputTime) throws ParseException {
        SimpleDateFormat inputSdf = new SimpleDateFormat("HH.mm.ss.SSS");
        Timestamp ts = new Timestamp(inputSdf.parse(inputTime).getTime());
        String s = sdf.format(ts);
        return s;



    }
    public static void main(String[] args) {
        String s1 = args[0];
        String s2 = args[1];
        CombineEmotiveEPrime combineEmotiveEPrime = new CombineEmotiveEPrime(args[0], args[1]);
        try {
            Timestamp t = new Timestamp(sdf.parse("08:42:53").getTime());
            t.setTime(t.getTime() + 241747);
            System.out.println(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
