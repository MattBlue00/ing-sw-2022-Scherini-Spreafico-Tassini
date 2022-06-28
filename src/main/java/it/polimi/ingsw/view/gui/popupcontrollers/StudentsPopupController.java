package it.polimi.ingsw.view.gui.popupcontrollers;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Student;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Objects;

public class StudentsPopupController extends PopupController{

    @FXML
    private GridPane studentsBox;
    private String chosenStudent;
    private List<Student> students;

    public StudentsPopupController() {
        super();
    }

    public void chooseStudent(Event e){
        ImageView node = (ImageView) e.getTarget();
        Image img = node.getImage();
        for(Color color : Color.values()){
            String c = color.toString().toLowerCase();
            if(img.getUrl().toLowerCase().contains(c))
                chosenStudent = color.toString();
        }
        getWindow().close();
    }

    @Override
    public String display() {

        getWindow().setTitle("Choose a student");
        getWindow().setHeight(100);
        getWindow().setWidth(288);

        Parent rootLayout = load("/fxml/students_popup.fxml");
        Scene scene = new Scene(rootLayout, 288, 70);
        scene.getStylesheets().add
                (Objects.requireNonNull(getClass().getResource("/css/popup_style.css")).toExternalForm());
        getWindow().setScene(scene);

        studentsBox = (GridPane) rootLayout;
        int i = 0;
        for(Student student : students){
            ImageView studentImage = new ImageView(new Image("img/student_"+student.getColor().toString()+".png"));
            studentImage.setFitWidth(70);
            studentImage.setPreserveRatio(true);
            studentImage.setDisable(false);
            studentImage.getStyleClass().add("popupClickable");
            studentsBox.add(studentImage, i, 0);
            i++;
        }
        studentsBox.addEventHandler(MouseEvent.MOUSE_CLICKED, this::chooseStudent);

        getWindow().showAndWait();

        return chosenStudent;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
