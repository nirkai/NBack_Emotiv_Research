package info_processing;

import e_prime.EprimeData_handle;
import e_prime.EprimeTimeCalc_Control;
import e_prime.ReadFromEprime;
import e_prime.ReadFromEprimeXLS;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class testing {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    public static void main(String[] args) {
        try {
            Timestamp t = new Timestamp(sdf.parse("08:42:53").getTime());
            t.setTime(t.getTime()+ 241747);//
            Timestamp s = new Timestamp(sdf.parse("08:44:16.328").getTime());
            t.setTime(t.getTime() - s.getTime());
            int seconds = (int)TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS);
            int minuts = seconds / 60;
            seconds = seconds % 60;
            System.out.println(minuts + ":" + seconds);


            t = new Timestamp(sdf.parse("09:34:37").getTime());
            t.setTime(t.getTime()+ 268976);//
            s = new Timestamp(sdf.parse("09:38:26").getTime());
            t.setTime(t.getTime() - s.getTime());
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            t = new Timestamp(sdf.parse("10:07:34").getTime());
            t.setTime(t.getTime()+ 335370);//
            s = new Timestamp(sdf.parse("10:12:25.490").getTime());
            t.setTime(t.getTime() - s.getTime());
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            t = new Timestamp(sdf.parse("08:42:53").getTime());
            t.setTime(t.getTime()+ 241747);//
            s = new Timestamp(sdf.parse("08:42:53").getTime());
            s.setTime(s.getTime() + 439385);
            t.setTime(s.getTime() - t.getTime() + 24000);
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            t = new Timestamp(sdf.parse("08:42:53").getTime());
            t.setTime(t.getTime()+ 493603);//
            s = new Timestamp(sdf.parse("08:42:53").getTime());
            s.setTime(s.getTime() + 687209);
            t.setTime(s.getTime() - t.getTime() + 28000);
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));
            seconds = (int)TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS);
            minuts = seconds / 60;
            seconds = seconds % 60;
            System.out.println(minuts + ":" + seconds);

            t = new Timestamp(sdf.parse("08:44:16").getTime());
            t.setTime(t.getTime()+ 30000 + 216000 + 2000);//
            s = new Timestamp(sdf.parse("08:42:53").getTime());
            s.setTime(s.getTime() + 439385 + 2000);
            /*t.setTime(s.getTime() - t.getTime()- 30000);
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));
            seconds = (int)TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS);
            minuts = seconds / 60;
            seconds = seconds % 60;
            System.out.println(minuts + ":" + seconds);

*/
            System.out.println("___________________\n004");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            t = new Timestamp(sdf.parse("09:38:26").getTime());
            t.setTime(t.getTime()+ 30000 + 217672);//216000 + 2000);//
            s = new Timestamp(sdf.parse("09:34:37").getTime());
            s.setTime(s.getTime() + 474682
                    + 2000);

            System.out.println("____________________\n005");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            t = new Timestamp(sdf.parse("10:12:25.490").getTime());
            t.setTime(t.getTime()+ 30000 + 216000 + 2000);//
            s = new Timestamp(sdf.parse("10:07:34").getTime());
            s.setTime(s.getTime() + 537042

                    + 2000);

            System.out.println("____________________\n006");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            // _______________0003______________________
            t = new Timestamp(sdf.parse("08:11:07").getTime());
            t.setTime(t.getTime()+ 30000 + 216000 + 2000);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 636052 + 2000);

            System.out.println("____________________\n003");
            System.out.println("phase 1:");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            t.setTime(t.getTime()+ 30000 + 217673);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 883876 + 2000);
            System.out.println("phase 2:");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            t.setTime(t.getTime()+ 30000 + 217673);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 1131699 + 2000);
            System.out.println("phase 3:");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            t.setTime(t.getTime()+ 30000 + 217673);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 1379522 + 2000);
            System.out.println("phase 4:");
            System.out.println("eprime time: " + sdf.format(s));
            System.out.println("our time: " + sdf.format(t));

            // 0003 check eprime times
            t = new Timestamp(sdf.parse("08:04:37").getTime());
            t.setTime(t.getTime()+ 430347);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 636052);
            t.setTime(s.getTime() - t.getTime() + 10000);
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            t = new Timestamp(sdf.parse("08:04:37").getTime());
            t.setTime(t.getTime()+ 682203);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 883876);
            t.setTime(s.getTime() - t.getTime() + 14000);
            System.out.println(TimeUnit.MILLISECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            // 0005 check eprime times
            t = new Timestamp(sdf.parse("09:34:37").getTime());
            t.setTime(t.getTime()+ 268976);//
            s = new Timestamp(sdf.parse("09:34:37").getTime());
            s.setTime(s.getTime() + 474682 + 2000);
            t.setTime(s.getTime() - t.getTime() + 10000);
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            t = new Timestamp(sdf.parse("08:04:37").getTime());
            t.setTime(t.getTime()+ 520833);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 722505 + 2000);
            t.setTime(s.getTime() - t.getTime() + 14000);
            System.out.println(TimeUnit.MILLISECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            Timestamp r = new Timestamp(sdf.parse("08:11:07.415").getTime());
            t = new Timestamp(sdf.parse("08:04:37").getTime());
            t.setTime(t.getTime()+ 430347 - 10000);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 722505 + 2000);
            //r.setTime(s.getTime() - r.getTime());
            t.setTime(t.getTime() - r.getTime());
           // t.setTime(r.getTime() - t.getTime());
            //System.out.println(sdf.format(t));
            System.out.println(TimeUnit.SECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            t = new Timestamp(sdf.parse("08:04:37").getTime());
            t.setTime(t.getTime()+ 430347 - 10000 - 30000);//
            //s = new Timestamp(sdf.parse("08:04:37").getTime());
            //s.setTime(s.getTime() + 636052 + 2000);
            //s.setTime(s.getTime() - t.getTime() + 10000);

            //t = new Timestamp(sdf.parse("08:04:37").getTime());
           // t.setTime(t.getTime() + s.getTime() - 30000);

            //t.setTime(s.getTime() - 30000);
            System.out.println(TimeUnit.MILLISECONDS.convert(s.getTime(), TimeUnit.MILLISECONDS));
            System.out.println(sdf.format(t));

            long startRun = sdf.parse("08:04:37").getTime();
            long firstT = 430347 + startRun;
            int wNum = 2;
            long lastT = 636052 + startRun;
            long phase = EprimeTimeCalc_Control.phaseDurationCalc(wNum, firstT, lastT);
            System.out.println(phase);
            long startT = EprimeTimeCalc_Control.startOfTask(wNum, firstT);
            System.out.println(new SimpleDateFormat("hh:mm:ss.SSS").format(startT));

            t = new Timestamp(sdf.parse("08:04:37").getTime());
            t.setTime(t.getTime()+ 430347);//
            s = new Timestamp(sdf.parse("08:04:37").getTime());
            s.setTime(s.getTime() + 636052 + 2000);
            t.setTime(s.getTime() - t.getTime() + 10000);
            System.out.println(TimeUnit.MILLISECONDS.convert(t.getTime(), TimeUnit.MILLISECONDS));

            //t = new Timestamp(sdf.parse("08:11:07").getTime());
            t.setTime(startT + 30000 + 217705);
            System.out.println("-----\n" + t + "\n" + s + "\n-----");
            firstT = 682203 + startRun;
            lastT = 883876 + startRun;
            wNum = 3;
            phase = EprimeTimeCalc_Control.phaseDurationCalc(wNum, firstT, lastT);
            System.out.println(phase);

            String path = args[0];
            ReadFromEprimeXLS readFromEprimeXLS = new ReadFromEprimeXLS(path);

            List<EprimeData_handle> eprimeData_handles = new ArrayList<>();
            readFromEprimeXLS.readEprimeData();
            eprimeData_handles = ReadFromEprimeXLS.getEprimeData_handles();

            phase = EprimeTimeCalc_Control.phaseDurationCalc((int)eprimeData_handles.get(0).getProcedureTrial(), eprimeData_handles.get(0).getT1_OnsetTime(), eprimeData_handles.get(11).getT1_OnsetTime());
            System.out.println("Phase1: " + phase);
            phase = EprimeTimeCalc_Control.phaseDurationCalc((int)eprimeData_handles.get(12).getProcedureTrial(),
                    eprimeData_handles.get(12).getT1_OnsetTime(), eprimeData_handles.get(23).getT1_OnsetTime());
            System.out.println("Phase2: " + phase);
            phase = EprimeTimeCalc_Control.phaseDurationCalc((int)eprimeData_handles.get(24).getProcedureTrial(),
                    eprimeData_handles.get(24).getT1_OnsetTime(), eprimeData_handles.get(35).getT1_OnsetTime());
            System.out.println("Phase3: " + phase);
            phase = EprimeTimeCalc_Control.phaseDurationCalc((int)eprimeData_handles.get(36).getProcedureTrial(),
                    eprimeData_handles.get(36).getT1_OnsetTime(), eprimeData_handles.get(47).getT1_OnsetTime());
            System.out.println("Phase4: " + phase);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
