package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
// hi
public class MainController
{
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}