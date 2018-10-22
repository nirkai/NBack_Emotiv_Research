package info_processing;

import jdk.nashorn.internal.ir.IfNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class NBackDataCalc {
    public static void main(String[] args) {
        String currentDir = System.getProperty("user.dir");
        File folder = new File(currentDir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".xlsx")) {
                String fileName = file.getName();
                String eprimePath = currentDir + "\\" + fileName;
                fileName = fileName.replace(".xlsx",".csv");
                String emotivPath = currentDir + "\\" + fileName;
                File emotivFile = new File(emotivPath);
                if (emotivFile.isFile() && emotivFile.exists()) {
                    String outputFile = file.getParent() + "\\Calc_Output_" + eprimePath.replaceAll(".*\\\\", "");
                    new AvgTask(eprimePath, emotivPath, outputFile);
                    System.out.println(outputFile);
                    System.out.println(eprimePath + "\n" + emotivPath);
                }
            }
        }
//        try (Stream<Path> paths = Files.walk(Paths.get(currentDir))) {
//            paths
//                    .filter(Files::isRegularFile)
//                    .forEach(file -> {
//                        if(file.getFileName().endsWith(".xlsx"))
//                            System.out.println(file.getFileName());
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
