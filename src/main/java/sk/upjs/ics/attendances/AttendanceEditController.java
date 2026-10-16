package sk.upjs.ics.attendances;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sk.upjs.ics.BadRequestException;
import sk.upjs.ics.Factory;
import sk.upjs.ics.NotFoundException;
import sk.upjs.ics.subjects.Subject;
import sk.upjs.ics.subjects.SubjectDao;
import sk.upjs.ics.users.User;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AttendanceEditController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button saveAttendanceButton;

    @FXML
    private ComboBox<Subject> subjectComboBox;

    @FXML
    private VBox studentsVBox;

    private final SubjectDao subjectDao = Factory.INSTANCE.getSubjectDao();

    private final AttendanceDao attendanceDao = Factory.INSTANCE.getAttendanceDao();

    private final Map<User, CheckBox> checkBoxes = new HashMap<>();

    private Long editedAttendanceId;

    public void setExistingAttendance(Attendance attendance) {
        editedAttendanceId = attendance.id();
        datePicker.setValue(attendance.datetime().toLocalDate());
        datePicker.setDisable(true);
        subjectComboBox.setValue(attendance.subject());
        subjectComboBox.setDisable(true);

        showStudents(attendance.subject());
        attendance.attendees().stream()
                .map(checkBoxes::get)
                .filter(cb -> cb != null)
                .forEach(cb -> cb.setSelected(true));
    }

    @FXML
    void subjectComboBoxOnAction(ActionEvent event) {
        showStudents(subjectComboBox.getValue());
    }

    private void showStudents(Subject subject) {
        studentsVBox.getChildren().clear();
        checkBoxes.clear();
        if (subject == null) {
            return;
        }

        subject.students().stream()
                .sorted(Comparator.comparing(User::surname)
                        .thenComparing(User::name)
                        .thenComparing(User::id))
                .forEach(student -> {
                    var cb = new CheckBox(student.name() + " " + student.surname() + ", " + student.email());
                    studentsVBox.getChildren().add(cb);
                    checkBoxes.put(student, cb);
                });
    }

    @FXML
    void saveAttendanceButton(ActionEvent event) throws BadRequestException, NotFoundException {
        // vytvor entitu z GUI komponentov

        Set<User> attendees = checkBoxes.entrySet()
                .stream()
                .filter(e -> e.getValue().isSelected())
                .map(e -> e.getKey())
                .collect(Collectors.toSet());

        var attendance = new Attendance(
                editedAttendanceId,
                datePicker.getValue().atStartOfDay(),
                subjectComboBox.getValue(),
                attendees
        );

        // uloz entitu do DAO

        if (editedAttendanceId == null) {
            attendanceDao.create(attendance);
        } else {
            attendanceDao.update(attendance);
        }

        // zavri okno

        var stage = (Stage) saveAttendanceButton.getScene().getWindow();
        stage.close();
    }


    @FXML
    void initialize(){
        subjectComboBox.setConverter(new StringConverter<Subject>() {
            @Override
            public String toString(Subject object) {
                if (object == null) {
                    return "";
                }

                return object.name();
            }

            @Override
            public Subject fromString(String string) {
                return subjectComboBox.getItems()
                        .stream()
                        .filter(s -> s.name().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        var subjects = subjectDao.findAll();
        subjectComboBox.setItems(FXCollections.observableList(subjects));
    }
}
