package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
    @FXML
    public Button fxButton;
    public void click(ActionEvent actionEvent) {
        System.out.println("Hello World");
        fxButton.setText("Hey!");
    }
}
