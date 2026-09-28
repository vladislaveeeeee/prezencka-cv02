package sk.upjs.ics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.w3c.dom.ls.LSOutput;
import sk.upjs.ics.users.User;
import sk.upjs.ics.users.UserService;

import java.text.MessageFormat;

public class AttenderController {

    @FXML
    private Label niecoLabel;

    @FXML
    private Button stlacmaButton;

    @FXML
    void stlacmaButtonOnAction(ActionEvent event) {
        var users = UserService.loadFromCsv();
        var service = new UserService(users);
        var data = service.genderRatios();
        var boysRatio = data.get(User.Gender.MALE);
        var girlsRation = data.get(User.Gender.FEMALE);
        niecoLabel.setText(MessageFormat.format("CH: {0}; D: {1}", boysRatio, girlsRation));
    }

}
