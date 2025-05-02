package org.example.clientsevermsgexample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class MainController implements Initializable {
    @FXML
    private ComboBox dropdownPort;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropdownPort.getItems().addAll("7",     // ping
                "13",     // daytime
                "21",     // ftp
                "23",     // telnet
                "71",     // finger
                "80",     // http
                "119",     // nntp (news)
                "161"      // snmp);
        );
        user1_client.setOnAction(this::startClient);
        user2_server.setOnAction(this::startServer);
    }

    @FXML
    private Button clearBtn;

    @FXML
    private Button user1_client;

    @FXML
    private Button user2_server;

    @FXML
    private TextArea resultArea;

    @FXML
    private Label server_lbl;

    @FXML
    private Button testBtn;

    @FXML
    private Label test_lbl;

    @FXML
    private TextField urlName;

    Socket socket1;

    Label lb122, lb12;
    TextField msgText;

    @FXML
    void checkConnection(ActionEvent event) {

        String host = urlName.getText();
        int port = Integer.parseInt(dropdownPort.getValue().toString());

        try {
            Socket sock = new Socket(host, port);
            resultArea.appendText(host + " listening on port " + port + "\n");
            sock.close();
        } catch (UnknownHostException e) {
            resultArea.setText(String.valueOf(e) + "\n");
            return;
        } catch (Exception e) {
            resultArea.appendText(host + " not listening on port "
                    + port + "\n");
        }


    }

    @FXML
    void clearBtn(ActionEvent event) {
        resultArea.setText("");
        urlName.setText("");

    }


    @FXML
    void startServer(ActionEvent event) {
        Stage stage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("server-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Server");
            stage.setScene(scene);
            stage.show();
            resultArea.appendText("Server started\n");
        } catch (IOException e) {
            resultArea.appendText("Error starting Server: " + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

    @FXML
    void startClient(ActionEvent event) {
        Stage stage = new Stage();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client-view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Client");
            stage.show();
            resultArea.appendText("Client started\n");
        }catch (IOException e){
            resultArea.appendText("Error starting Client: " + e.getMessage() + "\n");
            e.printStackTrace();
        }

    }

}
