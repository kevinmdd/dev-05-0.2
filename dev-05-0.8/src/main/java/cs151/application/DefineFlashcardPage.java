package cs151.application;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

public class DefineFlashcardPage
{
    private List<Deck> decks = new ArrayList<>();
    private List<Flashcard> flashcards = new ArrayList<>();

    private final BorderPane root = new BorderPane();

    private ComboBox<Deck> deckComboBox;
    private TextArea frontTextArea;
    private TextArea backTextArea;
    private ComboBox<String> statusComboBox;
    private Label errorLabel;
    private Label successLabel;
    private final DeckStorage deckStorage = new DeckStorage();
    private final FlashcardStorage flashcardStorage = new FlashcardStorage();


    public void start(Stage stage) throws IOException {
        decks = deckStorage.load();
        flashcards = flashcardStorage.load();
        showForm();

        Scene scene = new Scene(root, 750, 600);
        stage.setTitle("Define Flashcard");
        stage.setScene(scene);
        stage.show();
    }

    private void showForm()
    {
        Label title = new Label("Define Flashcard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        deckComboBox = new ComboBox<>();
        deckComboBox.setItems(FXCollections.observableArrayList(decks));
        deckComboBox.setPromptText("Select Deck");
        deckComboBox.setMaxWidth(350);

        deckComboBox.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(Deck item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        deckComboBox.setButtonCell(new ListCell<>()
        {
            @Override
            protected void updateItem(Deck item, boolean empty)
            {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        frontTextArea = new TextArea();
        frontTextArea.setPromptText("Front Text");
        frontTextArea.setPrefRowCount(3);
        frontTextArea.setWrapText(true);
        frontTextArea.setMaxWidth(350);

        backTextArea = new TextArea();
        backTextArea.setPromptText("Back Text");
        backTextArea.setPrefRowCount(5);
        backTextArea.setWrapText(true);
        backTextArea.setMaxWidth(350);

        statusComboBox = new ComboBox<>();
        statusComboBox.setItems(FXCollections.observableArrayList(
                Flashcard.STATUS_NEW,
                Flashcard.STATUS_LEARNING,
                Flashcard.STATUS_MASTERED
        ));
        statusComboBox.setValue(Flashcard.STATUS_NEW);
        statusComboBox.setMaxWidth(350);

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: green;");
        successLabel.setVisible(false);
        successLabel.setManaged(false);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(100);
        saveButton.setOnAction(e -> {
            try {
                saveFlashcard();
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Could not save flashcard.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setPrefWidth(100);
        backButton.setOnAction(e -> goBack());

        HBox buttons = new HBox(12, saveButton, backButton);
        buttons.setAlignment(Pos.CENTER);

        VBox form = new VBox(
                12,
                title,
                new Label("Deck *"),
                deckComboBox,
                new Label("Front Text *"),
                frontTextArea,
                new Label("Back Text *"),
                backTextArea,
                new Label("Status *"),
                statusComboBox,
                errorLabel,
                successLabel,
                buttons
        );

        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setMaxWidth(450);

        VBox wrapper = new VBox(form);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(20));

        root.setCenter(wrapper);
    }

    private void saveFlashcard() throws IOException
    {
        hideMessages();

        Deck selectedDeck = deckComboBox.getValue();
        String front = normalize(frontTextArea.getText());
        String back = normalize(backTextArea.getText());
        String status = statusComboBox.getValue();


        if (selectedDeck == null) {
            showError("Deck is required.");
            return;
        }

        if (front.isEmpty()) {
            showError("Front Text cannot be empty.");
            return;
        }

        if (back.isEmpty()) {
            showError("Back Text cannot be empty.");
            return;
        }

        if (!Flashcard.isValidStatus(status)) {
            showError("Status must be New, Learning, or Mastered.");
            return;
        }

        if (isDuplicateFrontTextInDeck(selectedDeck, front)) {
            showError("Within the same deck, Front Text must be unique.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        Flashcard flashcard = new Flashcard(
                selectedDeck,
                front,
                back,
                status,
                now,
                now
        );

        flashcards.add(flashcard);
        flashcardStorage.save(flashcards);

        clearForm();
        showSuccess("Flashcard saved successfully.");
    }

    private boolean isDuplicateFrontTextInDeck(Deck deck, String frontText)
    {
        for (Flashcard flashcard : flashcards) {
            boolean sameDeck = flashcard.getDeck().getName()
                    .equalsIgnoreCase(deck.getName());

            boolean sameFront = normalize(flashcard.getFrontText())
                    .equalsIgnoreCase(frontText);

            if (sameDeck && sameFront) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String text)
    {
        return text == null ? "" : text.trim();
    }

    private void clearForm()
    {
        deckComboBox.setValue(null);
        frontTextArea.clear();
        backTextArea.clear();
        statusComboBox.setValue(Flashcard.STATUS_NEW);
    }

    private void hideMessages()
    {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        successLabel.setVisible(false);
        successLabel.setManaged(false);
    }

    private void showError(String message)
    {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void showSuccess(String message)
    {
        successLabel.setText(message);
        successLabel.setVisible(true);
        successLabel.setManaged(true);
    }

    private void goBack()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            Scene scene = new Scene(loader.load(), 600, 400);

            Stage stage = (Stage) root.getScene().getWindow();
            stage.setScene(scene);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}