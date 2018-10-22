package e_prime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReadFromEprime implements IReadFromEprimeEdat{

	int TIME = 0,
		SPACE = 12,
		T1_OnsetTime_Trial = 64, // trigger time from start of trial
		T1_RT_Trial=66,			//	reaction from the trigger time
		T1_ACC_Trial = 60,		// response correct or not
		T1_RTT_Trial = 67;		//	reaction from start of trial

	private static List<EPrime_Triggered> ePrime_triggereds = new ArrayList<>();
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private static List<Timestamp> T1_Onset_List = new ArrayList<>();

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		timestamp.setTime(timestamp.getTime());

		try {
			timestamp.setTime(sdf.parse("20:26:10").getTime());
			System.out.println(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(timestamp);
		String file = "C:\\JavaProjects\\NBack_Emotiv_Research\\out\\Tester\\0001edatOther1.txt";
		//String file = "C:\\JavaProjects\\NBack_Emotiv_Research\\out\\output\\signals22_4.edat";
		FileReader inputFile = new FileReader(file);
		if (inputFile == null) {
			System.out.println("File not found!");
			return;
		}

		BufferedReader bufferReader = new BufferedReader(inputFile);
		String line="";
		List<String[]> lines = new ArrayList<>();

		int j = 0;
		boolean f = true;
		Timestamp dTime = new Timestamp(timestamp.getTime());
		while ((line = bufferReader.readLine()) != null){
			String [] s = line.split(" ");
			lines.add(s);
			int k = 0;

			for (String s1 : s) {
				if (k++ == 16 && j > 8) {
					EPrime_Triggered ept = new EPrime_Triggered();
					String[] s2 = s1.split("\t");
					String t1 = s2[64];
					String t = t1.replaceAll("[^0-9.]", "");
					Long l = Long.parseLong(t);
					dTime.setTime(timestamp.getTime() + l);
					String T1_Onset = sdf.format(dTime);
					T1_Onset_List.add(dTime);
					System.out.print(T1_Onset + " ");
					ept.setT1_OnsetTime(l);

					String T1_RTT = s2[67];
					T1_RTT = T1_RTT.replaceAll("[^0-9.]", "");
					l = Long.parseLong(T1_RTT);
					dTime.setTime(timestamp.getTime() + l);
					T1_RTT = sdf.format(dTime);
					System.out.println(T1_RTT);
					ept.setT1_RTT(l);

					ePrime_triggereds.add(ept);



				}
			}
			j++;
		}

		System.out.println("----------------------------------------------");
		for (EPrime_Triggered ePrime_triggered : ePrime_triggereds) {
			System.out.println(ePrime_triggered.getT1_OnsetTime() + " " + ePrime_triggered.getT1_RTT());
		}
	}

	@Override
	public List<String> getRTTList() {
		return null;
	}

	@Override
	public List<String> getACCList() {
		return null;
	}
}
