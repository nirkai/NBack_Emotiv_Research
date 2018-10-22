package info_processing;

import com.example.EmotivReciveData;
import e_prime.EprimeData_handle;
import e_prime.EprimeTimeCalc_Control;
import e_prime.ReadFromEmotivFile;
import e_prime.ReadFromEprimeXLS;
import emotive_to_graph.Signals;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AvgTask {

    long startTask = 0;
    private String[] wavesArr = {"Alpha", "Low_beta", "High_beta", "Gamma", "Theta"};
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss");
    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

    AvgTask(String EprimePath, String emotivePath, String outputFile) {
        ReadFromEprimeXLS readFromEprime = new ReadFromEprimeXLS(EprimePath);
        List<EprimeData_handle> eprimeData_handles = new ArrayList<>();
        readFromEprime.readEprimeData();
        eprimeData_handles = ReadFromEprimeXLS.getEprimeData_handles();

        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        startTask = EprimeTimeCalc_Control.startOfTask((int) eprimeData_handles.get(0).getProcedureTrial(), eprimeData_handles.get(0).getT1_OnsetTime());
        //startTask -= 30000;
        System.out.println(sdf.format(startTask));
        ReadFromEmotivFile emotive = new ReadFromEmotivFile(emotivePath, sdf.format(startTask));
        emotive.fromFileToMap();

        for (int channel = 0; channel < emotive.getChannelsNames().length; channel++) {
            System.out.println(emotive.getChannelsNames()[channel]);

            Sheet sheet = workbook.createSheet(emotive.getChannelsNames()[channel]);
//
            makeHeaderXlsx(workbook, sheet);
//
            CalcAccRt(eprimeData_handles, sheet);

            calcWaves(emotive, emotive.getChannelsNames()[channel], sheet);
        }

        FileOutputStream fileOut = null;
        try {
            //String outputFile = absPath + "\\Calc_Output_" + EprimePath.replaceAll(".*\\\\", "");
            fileOut = new FileOutputStream(outputFile);
            workbook.write(fileOut);
            fileOut.close();
            // Closing the workbook
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calcWaves(ReadFromEmotivFile emotive, String channel, Sheet sheet) {
        for (int wave = 0, waveRow = 4; wave < 5; wave++, waveRow++) {
            Row wavesRow = sheet.createRow(waveRow);
            Cell wavesCell = wavesRow.createCell(0);
            wavesCell.setCellValue(wavesArr[wave]);

            System.out.println("\t" + wavesArr[wave]);
            List<Double> waves = emotive.getSignalsOfWaveInChannel(channel, wavesArr[wave]);

            int tempCount = 0;
            int counter = 0;
            double restAvg = 0, taskAvg = 0;
            double[] restList = new double[4];
            double[] taskList = new double[4];
            int restSec = 30;
            int taskSec = 218;


            for (int i = 0, wavePhasesCol = 1; i < 4; i++) {
                System.out.println("\t\tphase" + i+1);
                for (tempCount += restSec, restAvg = 0; counter < tempCount; counter++) {
                    restAvg += waves.get(counter);
                }
                //rest1Avg = restAvg / restSec;
                restList[i] = restAvg / restSec;
                for (tempCount += taskSec, taskAvg = 0; counter < tempCount; counter++) {
                    taskAvg += waves.get(counter);
                }
                //task1Avg = taskAvg / taskSec;
                taskList[i] = taskAvg / taskSec;
                wavesCell = wavesRow.createCell(wavePhasesCol++);
                wavesCell.setCellValue(restList[i]);
                wavesCell = wavesRow.createCell(wavePhasesCol++);
                wavesCell.setCellValue(taskList[i]);
                System.out.println("\t\t\tavg" + i+1 + ": " + restList[i] + " " + taskList[i]);
            }
        }
    }

    private void CalcAccRt(List<EprimeData_handle> eprimeData_handles, Sheet sheet) {
        double [] accList = new double[4];
        long [] rtList = new long[4];
        double rt_duration = 0;
        double acc_success = 0;

        Row accRow = sheet.createRow(2);
        Cell accCell = accRow.createCell(0);
        accCell.setCellValue("Acc");
        Row rtRow = sheet.createRow(3);
        Cell rtCell = rtRow.createCell(0);
        rtCell.setCellValue("Rt");
        for (int i = 1, j = 0, c = 2; i <= 4; i ++, c += 2) {
            acc_success = 0;
            rt_duration = 0;
            System.out.println("phase" + i);
            int rtSuccessCount = 0;
            for (; j < 12 * i; j++) {
                acc_success += eprimeData_handles.get(j).getT1_ACC();
                if (eprimeData_handles.get(j).getT1_ACC() == 1 && eprimeData_handles.get(j).getT1_RT() > 0) {
                    rt_duration += eprimeData_handles.get(j).getT1_RT();
                    rtSuccessCount++;
                }
                //System.out.println(eprimeData_handles.get(j).getT1_RT() + " " +formatInterval(eprimeData_handles.get(j).getT1_RT()));
            }
            accList[i-1] = acc_success / 12;
            rtList[i-1] = round(rt_duration / rtSuccessCount);
            accCell = accRow.createCell(c);
            accCell.setCellValue(accList[i-1]);
            rtCell = rtRow.createCell(c);
            rtCell.setCellValue(Double.parseDouble(formatInterval(rtList[i-1])));
            System.out.println("acc avg: "+accList[i-1]);
            System.out.println("rt duration: " + formatInterval(rtList[i-1]));
            System.out.println();
        }
    }

    private void makeHeaderXlsx(Workbook workbook, Sheet sheet) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        Row headerRow = sheet.createRow((short)0);
        Row row2 = sheet.createRow(1);
        //Row headerRow = sheet.createRow(0);
        for(int i = 0, j = 1; i < 4; i++,  j += 2) {
            Cell cell = headerRow.createCell(2*i+1);
            cell.setCellValue("Phase" + (i+1));
            cell.setCellStyle(headerCellStyle);

            Cell cell2 = row2.createCell(j);
            cell2.setCellValue("Rest");
            cell2 = row2.createCell(j+1);
            cell2.setCellValue("Task");
        }

        sheet.addMergedRegion(new CellRangeAddress(0,0,1,2));
        sheet.addMergedRegion(new CellRangeAddress(0,0,3,4));
        sheet.addMergedRegion(new CellRangeAddress(0,0,5,6));
        sheet.addMergedRegion(new CellRangeAddress(0,0,7,8));
    }

    private double Avg(int currentCounter, int totalCount, List<Double> wave){
        double sum = 0;
        for (; currentCounter < totalCount; currentCounter++){
            sum += wave.get(currentCounter);
        }
        return sum;
    }

//    private String formatInterval(final long l)
//    {
//        final long hr = TimeUnit.MILLISECONDS.toHours(l);
//        final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
//        final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
//        final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
//        return String.format("%02d.%03d", sec, ms);
////        return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
//    }

    private String formatInterval(final long l)
    {
       return String.format("%.03f", l / 1000.0);
    }

    private long round(double num){
        return num - (long)num < 5? (long)num : (long)num+1;
    }

    public static void main(String[] args) {
        String eprimePath = args[0];
        String emotivPath = args[1];
        AvgTask avgTask = new AvgTask(eprimePath, emotivPath, args[2]);

    }
}
