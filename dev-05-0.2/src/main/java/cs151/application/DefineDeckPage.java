package cs151.application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DefineDeckPage extends Application {

    private final List<Deck> decks = new ArrayList<>();
    private final ListView<String> deckListView = new ListView<>();
    private final BorderPane root = new BorderPane();

    private TextField deckNameField;
    private TextArea descriptionArea;
    private Label errorLabel;

    @Override
    public void start(Stage stage) {
        showDeckList();

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Define Deck");
        stage.setScene(scene);
        stage.show();
    }

    private void showDeckList() {
        Label title = new Label("Define Deck");
        Button createButton = new Button("Create");
        createButton.setOnAction(e -> showForm());

        HBox top = new HBox(10, title, createButton);
        top.setPadding(new Insets(10));

        refreshDeckList();

        root.setTop(top);
        root.setCenter(deckListView);
    }

    private void refreshDeckList() {
        deckListView.getItems().clear();
        for (Deck deck : decks) {
            deckListView.getItems().add(deck.name + (deck.description.isBlank() ? "" : " - " + deck.description));
        }
    }

    private void showForm() {
        Label title = new Label("Create New Deck");

        deckNameField = new TextField();
        deckNameField.setPromptText("Deck Name (required)");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description (optional)");
        descriptionArea.setPrefRowCount(4);

        errorLabel = new Label("Deck Name must be unique and cannot be empty.");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> saveDeck());

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            deckNameField.clear();
            descriptionArea.clear();
            showDeckList();
        });

        HBox buttons = new HBox(10, saveButton, cancelButton);

        VBox form = new VBox(10,
                title,
                new Label("Deck Name *"),
                deckNameField,
                new Label("Description"),
                descriptionArea,
                errorLabel,
                buttons
        );
        form.setPadding(new Insets(10));

        root.setTop(null);
        root.setCenter(form);
    }

    private void saveDeck() {
        String name = deckNameField.getText() == null ? "" : deckNameField.getText().trim();
        String description = descriptionArea.getText() == null ? "" : descriptionArea.getText().trim();

        if (name.isEmpty() || isDuplicate(name)) {
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            return;
        }

        decks.add(new Deck(name, description));
        showDeckList();
    }

    private boolean isDuplicate(String name) {
        for (Deck deck : decks) {
            if (deck.name.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }

    static class Deck {
        String name;
        String description;

        Deck(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
