package cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
// hi
import java.io.IOException;

public class Main extends Application
{
    // Hi
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("CS151 JavaFX App");
        stage.setScene(scene);
        stage.show();

        DefineDeckPage defineDeckPage = new DefineDeckPage();
        defineDeckPage.start(stage);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private static void launch()
    {

    }
}