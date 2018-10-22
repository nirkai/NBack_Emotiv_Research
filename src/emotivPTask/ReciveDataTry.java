package emotivPTask;


import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import gui.StartAndFinishListener;
import include.Iedk.Edk;
import include.Iedk.EdkErrorCode;
import gui.ReceivingEmotivDataListener;
import include.Iedk.EmoState;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import static include.Iedk.Edk.IEE_DataChannels_t.*;


public class ReciveDataTry extends Thread implements StartAndFinishListener{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss.SSS");
	private boolean active = true;
	private String emotivSignalsFile;
	public void run(){
		reciveData();
	}

	private ReceivingEmotivDataListener listener;
	//private  boolean isReceiveData = false;
	private void notifyStartListener(int isReceiveData) {
		listener.isReceive(isReceiveData);
	}

	public ReciveDataTry(String fileName) {
		emotivSignalsFile = fileName;
	}

	public void reciveData() {
		
		int MINUT_TO_MILLIS = 60000;
		int TIME_TO_CHECK_SIGNALS = 1000;
		Pointer eEvent = Edk.INSTANCE.IEE_EmoEngineEventCreate();
		Pointer eState = Edk.INSTANCE.IEE_EmoStateCreate();

		IntByReference userID = null;
		boolean ready = false;
		int state = 0;

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	//	Scanner reader = new Scanner(System.in);  // Reading from System.in
	//	System.out.println("Enter task duration [minutes]: ");
	//	double duration = reader.nextDouble(); // Scans the next token of the input as an int.
	//	System.out.println("How many checks: ");
	//	int numOfChecks = reader.nextInt();
	//	System.out.println("Please choose your headset: (press 1 for Epoc or 2 for Insight)");
	//	int choose = reader.nextInt();
		int choose = 1;
	//	reader.close();
		
		double duration = 0;
		int numOfChecks = 0;
		duration *= MINUT_TO_MILLIS;  
		double timeToCheck = (double)duration / numOfChecks;
	//	System.out.println("check every " + timeToCheck + " milliSecond");
		
		Edk.IEE_DataChannels_t[] InsightChannel = {IED_AF3, IED_AF4, IED_Pz, IED_T7, IED_T8};
		Edk.IEE_DataChannels_t[] EpocChannel = {IED_AF3, IED_F7, IED_F3, IED_FC5, IED_T7, IED_P7, IED_Pz, IED_O1, IED_O2, IED_P8, IED_T8, IED_FC6, IED_F4, IED_F8, IED_AF4};

		userID = new IntByReference(0);

		if (Edk.INSTANCE.IEE_EngineConnect("Emotiv Systems-5") != EdkErrorCode.EDK_OK.ToInt()) {
			System.out.println("Emotiv Engine start up failed.");
			return;
		}

		String headerFile = "TimeStamp, Channel, Quality, Alpha, Low_beta, High_beta, Gamma, Theta";
		System.out.println("Start receiving Data!");
		System.out.println(headerFile);

		File file = new File(emotivSignalsFile);
		file.getParentFile().mkdirs();
		PrintWriter fout     = null;
		
		try {
			fout= new PrintWriter(file);
		} catch (FileNotFoundException e1) {
			notifyStartListener(-1);
			e1.printStackTrace();
		}

		//String headerFile = "TimeStamp, Channel, Quality, Alpha, Low_beta, High_beta, Gamma, Theta";
		fout.print(headerFile);
	    fout.println("\n");
		notifyStartListener(1);
	    
		String start = sdf.format(new Timestamp(System.currentTimeMillis()));
				
		while(active) {//for(double d = 0; d < duration; d += timeToCheck)	{
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
				notifyStartListener(1);
				DoubleByReference alpha     = new DoubleByReference(0);
				DoubleByReference low_beta  = new DoubleByReference(0);
				DoubleByReference high_beta = new DoubleByReference(0);
				DoubleByReference gamma     = new DoubleByReference(0);
				DoubleByReference theta     = new DoubleByReference(0);

				try {
					Edk.IEE_DataChannels_t[] channels_ts;
					if (choose == 2)
						channels_ts = InsightChannel;
					else
						channels_ts = EpocChannel;
				    
				    // There is no Emotiv API to indicate which headset that's in used.
				    // Implement your own API or change this channel name according to your headset.

					for(Edk.IEE_DataChannels_t aInsightChannel : channels_ts)	{
						timestamp = new Timestamp(System.currentTimeMillis());
						int result = Edk.INSTANCE.IEE_GetAverageBandPowers(userID.getValue(), aInsightChannel.getType(), theta, alpha, low_beta, high_beta, gamma);
						if (result == EdkErrorCode.EDK_OK.ToInt()) {
							int quality = EmoState.INSTANCE.IS_GetContactQuality(eState,
									EmoState.IEE_InputChannels_t.values()[aInsightChannel.getType()].getType());
							System.out.format("TimeStamp: %s, Channel: %s, Quality: %d, Alpha: %f, Low Beta: %f, High Beta: %f, Gamma: %f, Theta: %f \n",
									sdf.format(timestamp), aInsightChannel.name(),quality, alpha.getValue(), low_beta.getValue(), high_beta.getValue(), gamma.getValue(), theta.getValue());
							fout.printf("%s, %s, %d, %f, %f, %f, %f, %f \n",
									sdf.format(timestamp), aInsightChannel.name(), quality, alpha.getValue(), low_beta.getValue(), high_beta.getValue(), gamma.getValue(), theta.getValue());
						}
					}
				} 
				catch (Exception e) {
					notifyStartListener(-1);
					System.out.print("Exception");
				}
                
			}
			try {
				Thread.sleep((long) TIME_TO_CHECK_SIGNALS);
			} catch (InterruptedException e) {
				notifyStartListener(-1);
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

	@Override
	public void isStart(boolean start) {
		active = start;
	}

	public void setClass(Object c){
		listener = (ReceivingEmotivDataListener) c;
	}

}
