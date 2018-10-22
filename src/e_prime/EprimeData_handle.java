package e_prime;

public class EprimeData_handle {
    private long ERun_start;
    private long T1_OnsetTime; // trigger time from start of trial
    private long T1_RT;        //	reaction from the trigger time
    private long T1_RTT;       // response correct or not
    private int T1_ACC;       //	reaction from start of trial
    private long ProcedureTrial;    // number of words before trigger

    public EprimeData_handle(long ERun_start, long t1_OnsetTime, long t1_RT, long t1_RTT, int t1_ACC, long procedureTrial) {
        this.ERun_start = ERun_start;
        T1_OnsetTime = t1_OnsetTime + ERun_start;
//        T1_RT = t1_RT + T1_OnsetTime;
        T1_RT = t1_RT;
        T1_RTT = t1_RTT + ERun_start;
        T1_ACC = t1_ACC;
        ProcedureTrial = procedureTrial;
    }

    public long getProcedureTrial() {
        return ProcedureTrial;
    }

    public long getERun_start() {
        return ERun_start;
    }

    public long getT1_OnsetTime() {
        return T1_OnsetTime;
    }

    public long getT1_RT() {
        return T1_RT;
    }

    public long getT1_RTT() {
        return T1_RTT;
    }

    public int getT1_ACC() {
        return T1_ACC;
    }

    @Override
    public String toString() {
        return "EprimeData_handle{" +
                "ERun_start=" + ERun_start +
                ", T1_OnsetTime=" + T1_OnsetTime +
                ", T1_RT=" + T1_RT +
                ", T1_RTT=" + T1_RTT +
                ", T1_ACC=" + T1_ACC +
                ", ProcedureTrial=" + ProcedureTrial +
                '}';
    }
}
