package cs151.application;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

public class HomePageController
{

    @FXML
    private void handleCreateDeck(ActionEvent event)
    {
        try
        {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            DefineDeckPage defineDeckPage = new DefineDeckPage();
            defineDeckPage.start(stage);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewDecks()
    {
        System.out.println("View existing decks clicked");
    }
}