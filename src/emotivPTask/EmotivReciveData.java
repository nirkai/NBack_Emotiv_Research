package emotivPTask;


import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import include.Iedk.Edk;
import include.Iedk.EdkErrorCode;
import include.Iedk.EmoState;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import static include.Iedk.Edk.IEE_DataChannels_t.*;


public class EmotivReciveData {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");

	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 56);
		cal.set(Calendar.SECOND, 47);
		cal.clear(Calendar.MILLISECOND);

		long k = cal.getTimeInMillis() + 184814;
		cal.setTimeInMillis(k);
		System.out.println("First word: "+cal.getTime());

		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 00);
		cal.clear(Calendar.MILLISECOND);

		long l = cal.getTimeInMillis() + 30000;
		cal.setTimeInMillis(l);
		System.out.println("From click: "+cal.getTime());

		int counter = 1000 / 5 ;
		System.out.println();
		Timestamp t1 = new Timestamp(cal.getTimeInMillis());
		java.util.Date parsedDate = new Date();
		parsedDate.setTime(t1.getTime());
		System.out.println("time is: "+ sdf.format(t1));
		for (int i = 0; i < 5; i++) {
			cal.add(Calendar.MILLISECOND, counter);
			t1 = new Timestamp(cal.getTimeInMillis());
			System.out.println("time is: "+ sdf.format(t1));
			parsedDate.setTime(parsedDate.getTime() + counter);
			System.out.println("parsDate time is: "+ sdf.format(parsedDate));
		}

		
		
		
		int MINUT_TO_MILLIS = 60000;
		Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
		Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();

		IntByReference userID = null;
		boolean ready = false;
		int state = 0;

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		//System.out.println(timestamp.getTimeTicks().toAsn1String());
//	/*	
//		for (int i = 0; i < 10; i++) {
//			timestamp = new Timestamp(System.currentTimeMillis());
//			System.out.println(sdf.format(timestamp));
//			// TODO loop with thread.sleep of 1500 and continue the loop with +1500.
//			// that be equal to get the current TimeMillis
//			try {
//				Thread.sleep(1500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		*/
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Enter task duration [minutes]: ");
		double duration = reader.nextDouble(); // Scans the next token of the input as an int.
		System.out.println("How many checks: ");
		int numOfChecks = reader.nextInt();
		System.out.println("Please choose your headset: (press 1 for Epoc or 2 for Insight)");
		int choose = reader.nextInt();
		reader.close(); 
		
		
		duration *= MINUT_TO_MILLIS;  
		double timeToCheck = (double)duration / numOfChecks;
		System.out.println("check every " + timeToCheck + " milliSecond");
		
		Edk.IEE_DataChannels_t[] InsightChannel = {IED_AF3, IED_AF4, IED_Pz, IED_T7, IED_T8};
		Edk.IEE_DataChannels_t[] EpocChannel = {IED_AF3, IED_F7, IED_F3, IED_FC5, IED_T7, IED_P7, IED_Pz, IED_O1, IED_O2, IED_P8, IED_T8, IED_FC6, IED_F4, IED_F8, IED_AF4};

		userID = new IntByReference(0);

		if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
			System.out.println("Emotiv Engine start up failed.");
			return;
		}

		System.out.println("Start receiving Data!");
		System.out.println("TimeStamp, Theta, Alpha, Low_beta, High_beta, Gamma");

		File file = new File("C:/JavaProjects/java/output/signals4.csv");
		file.getParentFile().mkdirs();
		PrintWriter fout     = null;
		
		try {
			fout= new PrintWriter(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		String headerFile = "TimeStamp, Channel, Alpha, Low_beta, High_beta, Gamma, Theta, Quality";
		fout.print(headerFile);
	    fout.println();
	    
	    
		String start = sdf.format(new Timestamp(System.currentTimeMillis()));

		for(double d = 0; d < duration; d += timeToCheck)	{
			state = Edk.INSTANCE.IEE_EngineGetNextEvent(eEvent);

			// New event needs to be handled
			if (state == EdkErrorCode.EDK_OK.ToInt()) {
				int eventType = Edk.INSTANCE.IEE_EmoEngineEventGetType(eEvent);
				Edk.INSTANCE.IEE_EmoEngineEventGetUserId(eEvent, userID);
				if (eventType == 0x0040)
					Edk.INSTANCE.IEE_EmoEngineEventGetEmoState(eEvent, eState);
				// Log the EmoState if it has been updated
				if (eventType == Edk.IEE_Event_t.IEE_UserAdded.ToInt())
					if (userID != null) {

						System.out.println("User added: " + userID.getValue());
						ready = true;
					}
			} else if (state != EdkErrorCode.EDK_NO_EVENT.ToInt()) {
				System.out.println("Internal error in Emotiv Engine!");
				break;
			}

			if (ready) {
				DoubleByReference alpha     = new DoubleByReference(0);
				DoubleByReference low_beta  = new DoubleByReference(0);
				DoubleByReference high_beta = new DoubleByReference(0);
				DoubleByReference gamma     = new DoubleByReference(0);
				DoubleByReference theta     = new DoubleByReference(0);

				
				
				// TODO for loop by timestamp 
				
				try {
					
				    
				    // There is no Emotiv API to indicate which headset that's in used.
				    // Implement your own API or change this channel name according to your headset.
				    if (choose == 2) {
				    	/*for (Edk.IEE_DataChannels_t aInsightChannel : InsightChannel) {
					    	timestamp = new Timestamp(System.currentTimeMillis());
					    	int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(), aInsightChannel.getType(), theta, alpha, low_beta, high_beta, gamma);
					    	if (result == EdkErrorCode.EDK_OK.ToInt()) {
					    		System.out.format("TimeStamp: %s, Channel: %s, Alpha: %f, Low Beta: %f, High Beta: %f, Gamma: %f, Theta: %f \n", 
					    				 sdf.format(timestamp), aInsightChannel.name(), alpha.getValue(), low_beta.getValue(), high_beta.getValue(), gamma.getValue(), theta.getValue());
					    		fout.printf("%s, %s, %f, %f, %f, %f, %f \n", 
					    				 sdf.format(timestamp), aInsightChannel.name(), alpha.getValue(), low_beta.getValue(), high_beta.getValue(), gamma.getValue(), theta.getValue());
					    	}
					    }*/
					}
				    else {
				    	for(Edk.IEE_DataChannels_t aInsightChannel : EpocChannel)	{
				    		timestamp = new Timestamp(System.currentTimeMillis());
				    		int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(), aInsightChannel.getType(), theta, alpha, low_beta, high_beta, gamma);

				    		IntByReference xP = new IntByReference(0);
				    		IntByReference yP = new IntByReference(0);
				    		int r = Edk.INSTANCE.IEE_HeadsetGetGyroDelta(userID.getValue(), xP, yP);
							//System.out.println(aInsightChannel.getType());
							if (result == EdkErrorCode.EDK_OK.ToInt() && r == EdkErrorCode.EDK_OK.ToInt()) {
								/*String s = String.format("%i", EmoState.INSTANCE.IS_GetContactQuality(eState,
										aInsightChannel.getType()));
								System.out.println(s);*/
								/*System.out.print(aInsightChannel + " ");
								System.out.println(EmoState.INSTANCE.IS_GetContactQuality(eState,
										EmoState.IEE_InputChannels_t.values()[aInsightChannel.getType()].getType()));*/

								int quality = EmoState.INSTANCE.IS_GetContactQuality(eState,
										EmoState.IEE_InputChannels_t.values()[aInsightChannel.getType()].getType());
								/*quality.setValue(EmoState.INSTANCE.IS_GetContactQuality(eState,
									aInsightChannel.getType()));
								System.out.println(quality.getValue());*/
								System.out.format("TimeStamp: %s, Channel: %s, Alpha: %f, Low Beta: %f, High Beta: %f, Gamma: %f, Theta: %f, Quality: %d, pXOut: %d, pYOut: %d\n",
				    					sdf.format(timestamp), aInsightChannel.name(), alpha.getValue(), low_beta.getValue(), high_beta.getValue(), gamma.getValue(), theta.getValue(), quality, xP.getValue(), yP.getValue());
				    			fout.printf("%s, %s, %f, %f, %f, %f, %f, %d \n",
				    					sdf.format(timestamp), aInsightChannel.name(), alpha.getValue(), low_beta.getValue(), high_beta.getValue(), gamma.getValue(), theta.getValue(), quality);
				    		}
				    	}
				    }

				} 
				catch (Exception e) {
					System.out.print("Exception");
				}
                
			}
			try {
				Thread.sleep((long) timeToCheck);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fout.flush(); 
		System.out.println("file saved");
					fout.close();	
		Edk.INSTANCE.IEE_EmoStateFree(eState);
		Edk.INSTANCE.IEE_EmoEngineEventFree(eEvent);
		Edk.INSTANCE.IEE_EngineDisconnect();

		String end = sdf.format(new Timestamp(System.currentTimeMillis()));
		System.out.println("start in: " + start);
		System.out.println("end in: " + end);
		System.out.println("Disconnected!");
	}

}
