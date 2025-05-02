package org.example.clientsevermsgexample;

/**
 * @author Ahnaf Sindid
 */

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientView implements Initializable {

    @FXML
    private AnchorPane ap_main;

    @FXML
    private Button button_send;

    @FXML
    private TextField tf_message;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private VBox vbox_messages;

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private final String Serv_Address = "localhost";
    private final int Serv_Port = 6789;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vbox_messages.heightProperty().addListener((observable, oldValue, newValue) -> {
            sp_main.setVvalue((Double) newValue);
        });

        button_send.setOnAction(event -> sendMessage());
        tf_message.setOnAction(event -> sendMessage());

        try{
            connect();
        } catch (IOException e) {
            displaySysMessage("Failed to connect to the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void connect() throws IOException {
        socket = new Socket(Serv_Address, Serv_Port);
        displaySysMessage("Connected to server at" + Serv_Address + ":" + Serv_Port);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());

        new Thread(() -> {
            try{
                while(socket.isConnected()){
                    String msgServer = dis.readUTF();
                    Platform.runLater(() -> {
                        displayMsg(msgServer);
                    });
                }
            } catch (IOException e) {
                Platform.runLater(() -> {
                    displaySysMessage("Connect to server Lost: " + e.getMessage());
                });
                closeConnection();
            }
        }).start();
    }
    private void sendMessage() {
        String messageToSend = tf_message.getText().trim();
        if(messageToSend.isEmpty()){
            return;
        }
        try{
            dos.writeUTF(messageToSend);
            dos.flush();

            displaySentMessage(messageToSend);
            tf_message.clear();
        } catch (IOException e) {
            displaySysMessage("Error sending message: " + e.getMessage());
            e.printStackTrace();
            closeConnection();
        }
    }
    private void displaySentMessage(String message) {
        vbox_messages.getChildren().add(new Label(message));
    }
    private void displayMsg(String message) {
        vbox_messages.getChildren().add(new Label(message));
    }
    private void displaySysMessage(String message) {
        vbox_messages.getChildren().add(new Label(message));
    }
    private void closeConnection() {
        try{
            if(socket != null) socket.close();
            if(dos != null) dos.close();
            if(dis != null) dis.close();
        } catch (IOException ignored) {}
    }
}
