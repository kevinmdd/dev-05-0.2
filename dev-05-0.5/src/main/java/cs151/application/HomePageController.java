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
    private void handleViewDecks(ActionEvent event)
    {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            ViewDecksPage viewDecksPage = new ViewDecksPage();
            viewDecksPage.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateFlashcard(ActionEvent event)
    {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            DefineFlashcardPage defineFlashcardPage = new DefineFlashcardPage();
            defineFlashcardPage.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewFlashcards(ActionEvent event)
    {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            ViewFlashcardsPage viewFlashcardsPage = new ViewFlashcardsPage();
            viewFlashcardsPage.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchFlashcards(ActionEvent event)
    {
        try {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

            SearchFlashcardsPage searchFlashcardsPage = new SearchFlashcardsPage();
            searchFlashcardsPage.start(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}