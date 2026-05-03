package cs151.application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the page used to create and manage flashcard decks.
 *
 * This class allows the user to define new decks by entering a
 * deck name and optional description.
 */
public class DefineDeckPage {

    private List<Deck> decks = new ArrayList<>();
    private final ListView<String> deckListView = new ListView<>();
    private final BorderPane root = new BorderPane();

    private TextField deckNameField;
    private TextArea descriptionArea;
    private Label errorLabel;
    /**
     * Starts the Define Deck page and displays it on the provided stage.
     *
     * @param stage the stage used to display the page
     */
    public void start(Stage stage) {
        decks = DeckStorage.load();
        showForm();

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Define Deck");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Displays the deck list interface showing all created decks.
     */
    private void showDeckList() {
        Label title = new Label("Define Deck");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button createButton = new Button("Create Deck");
        createButton.setPrefWidth(120);
        createButton.setOnAction(e -> showForm());

        VBox header = new VBox(12, title, createButton);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(25, 20, 15, 20));

        refreshDeckList();
        deckListView.setMaxWidth(450);
        deckListView.setPrefHeight(260);

        VBox centerBox = new VBox(deckListView);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(10, 20, 30, 20));

        root.setTop(header);
        root.setCenter(centerBox);
    }

    /**
     * Refreshes the deck list display to reflect the current decks.
     */
    private void refreshDeckList() {
        deckListView.getItems().clear();
        for (Deck deck : decks) {
            String name = deck.getName();
            String description = deck.getDescription();
            deckListView.getItems().add(name + (description.isBlank() ? "" : " - " + description));
        }
    }

    /**
     * Displays the form used to create a new deck.
     */
    private void showForm() {
        Label title = new Label("Create New Deck");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        deckNameField = new TextField();
        deckNameField.setPromptText("Deck Name (required)");
        deckNameField.setMaxWidth(320);

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description (optional)");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setMaxWidth(320);
        descriptionArea.setWrapText(true);

        errorLabel = new Label("Deck name must be unique and cannot be empty.");
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(90);
        saveButton.setOnAction(e -> {
            try {
                saveDeck();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button BackButton = new Button("Back");
        BackButton.setPrefWidth(90);
        BackButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
                Scene scene = new Scene(loader.load(), 600, 400);

                Stage stage = (Stage) root.getScene().getWindow();
                stage.setScene(scene);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttons = new HBox(12, saveButton, BackButton);
        buttons.setAlignment(Pos.CENTER);

        VBox form = new VBox(12,
                title,
                new Label("Deck Name *"),
                deckNameField,
                new Label("Description"),
                descriptionArea,
                errorLabel,
                buttons
        );
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setMaxWidth(420);

        VBox wrapper = new VBox(form);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(20));

        root.setTop(null);
        root.setCenter(wrapper);

    }

    /**
     * Saves a newly created deck if the name is valid and not duplicated.
     */
    private void saveDeck() throws IOException {
        String name = deckNameField.getText() == null ? "" : deckNameField.getText().trim();
        String description = descriptionArea.getText() == null ? "" : descriptionArea.getText().trim();

        if (name.isEmpty() || isDuplicate(name)) {
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        decks.add(new Deck(name, description));

        DeckStorage.save(decks); // ← clean separation

        deckNameField.clear();
        descriptionArea.clear();
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }

    /**
     * Checks if a deck with the same name already exists.
     *
     * @param name the deck name to check
     * @return true if the deck name already exists, false otherwise
     */
    private boolean isDuplicate(String name) {
        for (Deck deck : decks) {
            if (deck.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
