package cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * Main entry point for the Flash Master application.
 *
 * This class launches the JavaFX application
 */
public class Main extends Application
{
    /**
     * Starts the JavaFX application.
     *
     * Loads the HomePage.fxml layout and displays the main window.
     *
     * @param stage the primary stage provided by JavaFX
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("HomePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Flash Master");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Launches the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}