package sk.upjs.ics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.upjs.ics.attendances.Attendance;
import sk.upjs.ics.attendances.AttendanceDao;
import sk.upjs.ics.attendances.AttendanceService;
import sk.upjs.ics.subjects.SubjectDao;
import sk.upjs.ics.subjects.SubjectService;
import sk.upjs.ics.users.UserDao;
import sk.upjs.ics.users.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AttenderController {

    @FXML
    private ListView<Attendance> attendancesListView;

    @FXML
    private Button newAttendanceButton;

    private final UserDao userDao = Factory.INSTANCE.getUserDao();

    private final SubjectDao subjectDao = Factory.INSTANCE.getSubjectDao();

    private final AttendanceDao attendanceDao = Factory.INSTANCE.getAttendanceDao();

    @FXML
    void attendancesListViewOnMouseClicked(MouseEvent event) {

    }

    @FXML
    void newAttendanceButtonOnAction(ActionEvent event) throws IOException {
        var loader = new FXMLLoader(getClass().getResource("attendances/AttendanceEdit.fxml"));
        Parent rootPane = loader.load();

        var scene = new Scene(rootPane);
        var stage = new Stage();

        stage.setTitle("Nová prezenčka");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);

        // otvor okno a cakaj kym ho niekto zavrie
        stage.showAndWait();
        // potom pokracuj

        attendancesListView.getItems().clear();
        attendancesListView.getItems().addAll(attendanceDao.findAllSortedByDate());

    }

    @FXML
    void initialize() {
        // load test data

        // CSV does have ids, but DAO does not insert an entity with ID.
        // It will generate its own, so need to map CSV id -> DAO id.
        var userIds = new HashMap<Long, Long>();
        for (var u: UserService.loadFromCsv()) {
            userIds.put(u.id(), userDao.create(u.withId(null)).id());
        }

        var subjectIds = new HashMap<Long, Long>();
        for (var subject: SubjectService.loadFromCsv()) {
            subject = subject.withId(null);

            var teacher = subject.teacher();

            // replace the teacher id from CSV with DAO id
            subject = subject.withTeacher(teacher.withId(userIds.get(teacher.id())));

            // replace the students of subjects having CSV ids, with the DAO ids
            subject = subject.withStudents(
                    subject.students()
                            .stream()
                            .map(st -> st.withId(userIds.get(st.id())))
                            .collect(Collectors.toSet())
            );

            subjectIds.put(subject.id(), subjectDao.create(subject).id());
        }

        for (var attendance: AttendanceService.loadFromCsv()) {
            attendance = attendance.withId(null);

            var subject = attendance.subject();

            attendance = attendance.withSubject(subject.withId(subjectIds.get(subject.id())));

            attendance = attendance.withAttendees(
                    attendance.attendees()
                            .stream()
                            .map(a -> a.withId(userIds.get(a.id())))
                            .collect(Collectors.toSet())
            );

            attendanceDao.create(attendance);
        }

        // ListView cell factory
        attendancesListView.setCellFactory(param -> new ListCell<>(){
            @Override
            protected void updateItem(Attendance item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    var sb = new StringBuilder();
                    sb.append(item.datetime().toLocalDate());
                    sb.append(" ");
                    sb.append(item.subject().name());
                    sb.append(" (");
                    sb.append("účasť");
                    sb.append(" ");
                    sb.append(item.attendees().size());
                    sb.append("/");
                    sb.append(item.subject().students().size());
                    sb.append(")");

                    setText(sb.toString());
                }
            }
        });


        // init data
        attendancesListView.getItems().addAll(attendanceDao.findAllSortedByDate());

    }

}
