package cs151.application;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
/**
 * Controller for the Home Page of the Flash Master application.
 *
 * Handles user interactions like creating
 * a new flashcard deck or viewing existing decks.
 */
public class HomePageController
{
    /**
     * Handles the Create Deck button click.
     *
     * This method switches the current stage to the DefineDeckPage
     * where the user can create a new deck.
     *
     * @param event the ActionEvent triggered when the button is clicked
     */
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
    /**
     * Handles the View Decks button click.
     */
    @FXML
    private void handleViewDecks()
    {
        System.out.println("View existing decks clicked");
    }
}