package e_prime;

import java.sql.Timestamp;

public class EPrime_Triggered {
    private long start;
    private Timestamp T1_OnsetTime; // trigger time from start of trial
    private Timestamp T1_RT;        //	reaction from the trigger time
    private Timestamp T1_RTT;       // response correct or not
    private boolean T1_ACC;       //	reaction from start of trial

    /*public EPrime_Triggered(Timestamp t1_OnsetTime, Timestamp t1_RT, Timestamp t1_RTT, int t1_ACC) {
        T1_OnsetTime = t1_OnsetTime;
        T1_RT = t1_RT;
        T1_RTT = t1_RTT;
        T1_ACC = t1_ACC == 1? true : false;
    }

    public EPrime_Triggered(Timestamp t1_OnsetTime, Timestamp t1_RTT) {
        T1_OnsetTime = t1_OnsetTime;
        T1_RTT = t1_RTT;
    }
*/
    public EPrime_Triggered() {
        start = 0;
        T1_OnsetTime = new Timestamp(0);
        T1_RT= new Timestamp(0);
        T1_RTT = new Timestamp(0);
        T1_ACC = false;
    }

    public EPrime_Triggered(long l_start) {
        start = l_start;
        T1_OnsetTime = new Timestamp(0);
        T1_RT= new Timestamp(0);
        T1_RTT = new Timestamp(0);
        T1_ACC = false;
    }

    public void setStart(long l_start) {
        start = l_start;
    }

    public void setT1_OnsetTime(long t1_OnsetTime) {
        T1_OnsetTime.setTime(start + t1_OnsetTime);
    }

    public void setT1_RT(long t1_RT) {
        T1_RT.setTime(T1_OnsetTime.getTime() + t1_RT);
    }

    public void setT1_RTT(long t1_RTT) {
        T1_RTT.setTime(t1_RTT);
    }

    public void setT1_ACC(int t1_ACC) {
        T1_ACC = t1_ACC == 1 ? true : false;
    }

    public Timestamp getT1_OnsetTime() {
        return T1_OnsetTime;
    }

    public Timestamp getT1_RT() {
        return T1_RT;
    }

    public Timestamp getT1_RTT() {
        return T1_RTT;
    }

    public boolean getT1_ACC() {
        return T1_ACC;
    }


}
