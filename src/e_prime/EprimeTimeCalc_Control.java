package e_prime;

public class EprimeTimeCalc_Control {
    private static long restTime = 30000; // 30 seconds
    private static long phaseTimeDuration = 0;
    private static long startTaskTime = 0;

    public static long getRestTime() {
        return restTime;
    }

    public static long getPhaseTimeDuration() {
        return phaseTimeDuration;
    }

    public static void setPhaseTimeDuration(long phaseTimeDuration) {
        EprimeTimeCalc_Control.phaseTimeDuration = phaseTimeDuration;
    }

    public static long getStartTaskTime() {
        return startTaskTime;
    }

    public static void setStartTaskTime(long startTaskTime) {
        EprimeTimeCalc_Control.startTaskTime = startTaskTime;
    }

    /*public static long phaseDurationCalc(long startOfErun, String numOfWordsBeforeTarget, long firstTarget, long lastTargetInFirstPhase){
        long displayTime = 2000; // 2 seconds
        long endOfPhase1 = startOfErun + lastTargetInFirstPhase + displayTime;
        firstTarget += startOfErun;
        int numOfWords = numberFromString(numOfWordsBeforeTarget);
        long startPhaseToFtirsTarget = startFirstTargetDiff_MSec(numOfWords);
        phaseTimeDuration = endOfPhase1 - firstTarget + startPhaseToFtirsTarget;
        return phaseTimeDuration;
    }*/
    public static long phaseDurationCalc(int numOfWordsBeforeTarget, long firstTarget, long lastTargetInFirstPhase){
        long displayTime = 2000; // 2 seconds
        long endOfPhase1 = lastTargetInFirstPhase + displayTime;
        long startPhaseToFtirsTarget = startFirstTargetDiff_MSec(numOfWordsBeforeTarget);
        phaseTimeDuration = endOfPhase1 - firstTarget + startPhaseToFtirsTarget;
        return phaseTimeDuration;
    }

    /*public static long startOfTask(long startOfErun, String numOfWordsBeforeTarget, long firstTarget){
        int numOfWords = numberFromString(numOfWordsBeforeTarget);
        long startPhaseToFtirsTarget = startFirstTargetDiff_MSec(numOfWords);
        startTaskTime = startOfErun + firstTarget - startPhaseToFtirsTarget - restTime;
        return startTaskTime;
    }*/
    public static long startOfTask(int numOfWordsBeforeTarget, long firstTarget){
        long startPhaseToFtirsTarget = startFirstTargetDiff_MSec(numOfWordsBeforeTarget);
        startTaskTime = firstTarget - startPhaseToFtirsTarget - restTime;
        return startTaskTime;
    }

    private static long startFirstTargetDiff_MSec(int numOfWords){
        int displayTime = 2000;
        // We have also Pixations in the task, with number of numOfWords + 1.
        // Total of displays  is numOfWords * 2 + 1.
        // Every Display take 2 seconds.
        return (2 * numOfWords + 1) * displayTime;
    }
    public static int numberFromString(String wordNum){
        switch (wordNum){
            case "One":
                return 1;
            case "Two":
                return 2;
            case "Three":
                return 3;
            case "Four":
                return 4;
            case "Five":
                return 5;
            case "Six":
                return 6;
            case "Seven":
                return 7;
            case "Eight":
                return 8;
        }
        return -1;
    }
}
