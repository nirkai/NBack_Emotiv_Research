package e_prime;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class get the output files of the E-Prime as an xls files, and collect the relevant data.
 */
public class ReadFromEprimeXLS implements IReadFromEprimeEdat{

    int TIME = 0,
            SPACE = 0,
            SessionTime = 12,           // E-run start time
            ProcedureTrial = 62,        //  number of pair words before target ('target' refer to one word display).
            T1_OnsetTime_Trial = 69, // trigger time from start of trial
            T1_RT_Trial = 71,			//	reaction from the trigger time
            T1_ACC_Trial = 65,		// response correct or not
            T1_RTT_Trial = 72;		//	reaction from start of trial

    private static List<EPrime_Triggered> ePrime_triggereds = new ArrayList<>();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private static List<Timestamp> T1_Onset_List = new ArrayList<>();

    private static List<EprimeData_handle> eprimeData_handles = new ArrayList<>();

    String xlsPath;
    public ReadFromEprimeXLS(String path) {
        xlsPath = path;
    }

    public void readEprimeData(){
        File file = new File(xlsPath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            // Finds the workbook instance for XLSX file
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();

            for (int i = 0; i < 8 && rowIterator.hasNext(); i++){
                rowIterator.next();
            }
            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Cell cell = row.getCell(T1_OnsetTime_Trial);
                long onset_long = getCellValue(cell);

                cell = row.getCell(ProcedureTrial);
                long number = getCellValue(cell);

                cell = row.getCell(SessionTime);
                long sTime = getCellValue(cell);

                cell = row.getCell(T1_RT_Trial);
                long rt_long = getCellValue(cell);

                cell = row.getCell(T1_RTT_Trial);
                long rtt_long = getCellValue(cell);

                cell = row.getCell(T1_ACC_Trial);
                int acc_int = (int)getCellValue(cell);
                //boolean acc_bool = acc_int == 1? true : false;
                EprimeData_handle eprimeData_handle = new EprimeData_handle(sTime, onset_long, rt_long, rtt_long, acc_int, number);
                eprimeData_handles.add(eprimeData_handle);

                /*EPrime_Triggered ePrimeTrigger = new EPrime_Triggered(0);
                ePrimeTrigger.setT1_OnsetTime(onset_long);
                ePrimeTrigger.setT1_RT(rt_long);
                ePrimeTrigger.setT1_RTT(rtt_long);
                ePrimeTrigger.setT1_ACC(acc_int);
                ePrime_triggereds.add(ePrimeTrigger);
                */
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }


    }

    private long getCellValue(Cell cell) {
        long cellValue = -1;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                String data = cell.getStringCellValue();
                int num = EprimeTimeCalc_Control.numberFromString(data);
                if (num > -1)
                    cellValue = num;

                //cellValue = Long.parseLong(cell.getStringCellValue());
                //System.out.print(cell.getStringCellValue() + "\t" + "String");
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    try {
                        String s = getCellValueAsString(cell);

                        cellValue = sdf.parse(s).getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                    cellValue = (long)cell.getNumericCellValue();
                //System.out.print((long)cell.getNumericCellValue() + "\t" + "Numeric");
                break;
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue() + "\t" + "Boolean");
                break;
            default :

        }
        return cellValue;
    }

    private String getCellValueAsString(Cell poiCell){
        if (poiCell.getCellTypeEnum()== CellType.NUMERIC && DateUtil.isCellDateFormatted(poiCell)) {
            //get date
            Date date = poiCell.getDateCellValue();

            //set up formatters that will be used below
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat formatYearOnly = new SimpleDateFormat("yyyy");

        /*get date year.
        *"Time-only" values have date set to 31-Dec-1899 so if year is "1899"
        * you can assume it is a "time-only" value
        */
            String dateStamp = formatYearOnly.format(date);

            if (dateStamp.equals("1899")){
                //Return "Time-only" value as String HH:mm:ss
                return formatTime.format(date);
            } else {
                //here you may have a date-only or date-time value

                //get time as String HH:mm:ss
                String timeStamp =formatTime.format(date);

                if (timeStamp.equals("00:00:00")){
                    //if time is 00:00:00 you can assume it is a date only value (but it could be midnight)
                    //In this case I'm fine with the default Cell.toString method (returning dd-MMM-yyyy in case of a date value)
                    return poiCell.toString();
                } else {
                    //return date-time value as "dd-MMM-yyyy HH:mm:ss"
                    return poiCell.toString()+" "+timeStamp;
                }
            }
        }

        //use the default Cell.toString method (returning "dd-MMM-yyyy" in case of a date value)
        return poiCell.toString();
    }

    public static List<EprimeData_handle> getEprimeData_handles() {
        //return new ArrayList<>(eprimeData_handles);
        return eprimeData_handles;
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
