package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {

            InputStream in = EmotivDetails.class.getResourceAsStream("/edk.dll");
            String dir = System.getProperty("user.dir");
            String name = "\\edk.dll";
            Path p = Paths.get(dir + name);

            boolean exists = Files.exists(p);
            if (!exists) {
                File fileOut = new File(dir + name);
                DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileOut));
                long oneChar = 0;
                while ((oneChar = in.read()) != -1) {
                    writer.write((int) oneChar);
                }

                in.close();
                writer.close();
            }
            Runtime.getRuntime().load(dir + name);
        }
        catch (Exception e){
            System.out.println("You can not load the system without 'edk.dll'");
        }
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("N_Back with Emotiv");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
