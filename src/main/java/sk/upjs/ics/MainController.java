package sk.upjs.ics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    // Сюда JavaFX сам подставит Label с fx:id="resultLabel" из FXML
    @FXML
    private Label resultLabel;

    // Вызывается при нажатии на кнопку (onAction="#onButtonClick")
    @FXML
    private void onButtonClick() {
        resultLabel.setText(String.valueOf(Math.random()));
    }
}