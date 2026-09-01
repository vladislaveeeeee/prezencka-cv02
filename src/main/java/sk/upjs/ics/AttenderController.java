package sk.upjs.ics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AttenderController {

    @FXML
    private Label niecoLabel;

    @FXML
    private Button stlacmaButton;

    @FXML
    void stlacmaButtonOnAction(ActionEvent event) {
        niecoLabel.setText(String.valueOf(Math.random()));
    }

}
