package gui;

import emotivPTask.ReciveDataTry;
import emotive_to_graph.GraphOfchannels;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
//import javafx.scene.control.TextField;

public class Controller extends Component implements Initializable, ReceivingEmotivDataListener, NativeKeyListener{
    @FXML
    TextField fileNameText;
    @FXML
    Label filePathLabel;
    @FXML
    Label fileNameNotChosenLabel;
    @FXML
    Label filePathNotChooseLabel;
    @FXML
    Button filePathButton;
    @FXML
    Button startButton;
    @FXML
    Button stopButton;
    @FXML
    Button plotButton;
    @FXML
    VBox chartPane;
    @FXML
    ComboBox channelCombobox;
    @FXML
    ComboBox wavesCombobox;
    @FXML
    Label channelLabel;
    @FXML
    Label wavesLabel;
    @FXML
    Sphere readyLed;
    @FXML
    Label dataErrorLabel;

    private String fullPathName = "";
    private String fileName = "";
    private String filePath = "";
    private String channel = "";
    private String wave = "";

    private StartAndFinishListener listener;
    private boolean active = false;
    private void notifyStartListener() {
        listener.isStart(active);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("HH.mm.ss.SSS");

    private int isReceiveData = 0;


    private static class TimeKeeper {
        static String startOfEprimeTrial_Pressed = "";
        static String startOfEprimeTrial_Released = "";
    }

    public void fileNameChosen(ActionEvent e){
      fileName = fileNameText.getText();
      if (fileName.equals("")){
          fileNameNotChosenLabel.setText("Choose name please");
          startButton.setDisable(true);
      }
      else{
              fileNameNotChosenLabel.setText("");
              if (!filePath.equals(""))
                  startButton.setDisable(false);
      }
    }

    public void filePathChosen(){
        JFileChooser c = new JFileChooser();
        c.setCurrentDirectory(new File("."));
        c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        // Demonstrate "Open" dialog:
        int rVal = c.showOpenDialog(Controller.this);
        fileName = fileNameText.getText();
        if (rVal == JFileChooser.APPROVE_OPTION) {
            //filePathLabel.setText(c.getSelectedFile().getName());
            filePath = c.getCurrentDirectory().toString() +"\\" + c.getSelectedFile().getName();
            filePathLabel.setText(filePath);
            if (!fileName.equals("")) {
                fileNameNotChosenLabel.setText("");
                filePathNotChooseLabel.setText("");
                startButton.setDisable(false);
            }
            else
                fileNameNotChosenLabel.setText("Choose name please");
        }
        if (rVal == JFileChooser.CANCEL_OPTION) {
            //filename1.setText("No File Choosed");
            startButton.setDisable(true);
            filePathLabel.setText("");
            filePathNotChooseLabel.setText("Choose path please");

            if (fileName.equals(""))
                fileNameNotChosenLabel.setText("Choose name please");

         //   filePathNotChooseLabel.setVisible(true);
        }
    }

    public void startReciveEmotivData(ActionEvent e){
        if (!fileName.equals("") && !filePath.equals("")) {

            fullPathName = filePath + "//" + fileName + ".csv";
            if (ifNotExist(fullPathName)) {
                stopButton.setDisable(false);
                ReciveDataTry reciveDataTry = new ReciveDataTry(fullPathName);
                reciveDataTry.setClass(this);
                listener = reciveDataTry;
                try {
                    new Thread(reciveDataTry).start();
                    active = true;
                    notifyStartListener();

                    initNativeKeyListener();
                }
                catch (Exception ex){

                }
            }
            else {
                fileName = "";
                fileNameNotChosenLabel.setText("This file already exist");
            }
        }
    }

    private void initNativeKeyListener() {
        try {

            LogManager.getLogManager().reset();

            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            ex.printStackTrace();

        }
        GlobalScreen.addNativeKeyListener(new Controller());
    }

    public void stopReciveEmotivData(ActionEvent e){
        active = false;
        notifyStartListener();
        chartPane.setVisible(true);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.RED);
        material.setSpecularColor(Color.WHITE);
        System.out.println("Enter pressed in " + TimeKeeper.startOfEprimeTrial_Pressed);
        System.out.println("Enter released in " + TimeKeeper.startOfEprimeTrial_Released);
        readyLed.setMaterial(material);

        /*try {

            String pressTime = String.format("Pressed, %s, %s", TimeKeeper.startOfEprimeTrial_Pressed, TimeKeeper.startOfEprimeTrial_Released);
            Files.write(Paths.get(fullPathName), pressTime.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException ex) {
            //exception handling left as an exercise for the reader
        }*/
        File file = new File(filePath + "/" + fileName + ".txt");
        file.getParentFile().mkdirs();
        PrintWriter fout     = null;

        try {
            fout= new PrintWriter(file);
            fout.printf("Pressed, %s, %s", TimeKeeper.startOfEprimeTrial_Pressed, TimeKeeper.startOfEprimeTrial_Released);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e1) { ;
            e1.printStackTrace();
        }
    }

    public void plotGraph(ActionEvent e){
        if (channel.equals("") || wave.equals("")) {
            if (channel.equals("")){
                channelLabel.setText("You must choose channel first");
            }
            else
                channelLabel.setText("");
            if (wave.equals("")){
                wavesLabel.setText("You must choose wave first");
            }
            else
                wavesLabel.setText("");

        }
        else{
            wavesLabel.setText("");
            channelLabel.setText("");
            String title = "N_Back&Emotiv";
            fullPathName = filePath + "\\" + fileName + ".csv";
            String[] args = {channel, fullPathName, channel, wave};
            GraphOfchannels graphOfchannels = new GraphOfchannels(args[0], args[1], args[2], args[3]);
          //  graphOfchannels.main(args);
            graphOfchannels.pack();
            graphOfchannels.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            graphOfchannels.setVisible(true);


        }
    //    GraphOfchannels.main(null);

    }

    public void channelComboboxChoose(ActionEvent e){
        channel = channelCombobox.getValue().toString();
    }

    public void wavesComboboxChoose(ActionEvent e){
        wave = wavesCombobox.getValue().toString();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        startButton.setDisable(true);
        stopButton.setDisable(true);
        //chartPane.setVisible(false);
        channelCombobox.getItems().addAll(EmotivDetails.channelsNames);
        for (String s : EmotivDetails.waves) {
            wavesCombobox.getItems().add(s);
        }
        wavesCombobox.getItems().add("all");
    }

    private boolean ifNotExist(String path){
        if (filePath.equals(""))
            return false;
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String name = listOfFile.getName().toString();
                if (listOfFile.getName().equals(fileName+".csv"))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void isReceive(int receiveData) {
        if (receiveData < 0){
            dataErrorLabel.setText("Can't receive data");
        }
        else{
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseColor(Color.GREENYELLOW);
            material.setSpecularColor(Color.WHITE);

            readyLed.setMaterial(material);
        }
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

        if (NativeKeyEvent.VC_RIGHT == nativeKeyEvent.getKeyCode())
            System.out.println("Right Key pressed");
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        if (NativeKeyEvent.VC_LEFT == nativeKeyEvent.getKeyCode() || NativeKeyEvent.VC_RIGHT == nativeKeyEvent.getKeyCode()
                || NativeKeyEvent.VC_UP == nativeKeyEvent.getKeyCode() || NativeKeyEvent.VC_DOWN == nativeKeyEvent.getKeyCode()) {
            TimeKeeper.startOfEprimeTrial_Pressed = sdf.format(t);
            System.out.println(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()) + " pressed in " + TimeKeeper.startOfEprimeTrial_Pressed);

        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        Timestamp t = new Timestamp(System.currentTimeMillis());
        if (NativeKeyEvent.VC_LEFT == nativeKeyEvent.getKeyCode() || NativeKeyEvent.VC_RIGHT == nativeKeyEvent.getKeyCode()
                || NativeKeyEvent.VC_UP == nativeKeyEvent.getKeyCode() || NativeKeyEvent.VC_DOWN == nativeKeyEvent.getKeyCode()) {

            TimeKeeper.startOfEprimeTrial_Released = sdf.format(t);
            System.out.println(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()) + " released in " + TimeKeeper.startOfEprimeTrial_Released);

            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
            }
        }
    }
}
