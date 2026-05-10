package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;

public class SearchFlashcardsPage
{
    private final TableView<Flashcard> table = new TableView<>();
    private ObservableList<Flashcard> data;
    private final FlashcardStorage flashcardStorage = new FlashcardStorage();


    public void start(Stage stage)
    {
        List<Flashcard> flashcards = flashcardStorage.load();
        data = FXCollections.observableArrayList(flashcards);

        TextField searchField = new TextField();
        searchField.setPromptText("Search flashcards...");
        searchField.setMaxWidth(400);

        // FILTER
        FilteredList<Flashcard> filteredData = new FilteredList<>(data, f -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String text = newVal == null ? "" : newVal.trim().toLowerCase();

            filteredData.setPredicate(f -> {
                if (text.isEmpty()) return true;

                return f.getDeck().getName().toLowerCase().contains(text)
                        || f.getFrontText().toLowerCase().contains(text)
                        || f.getBackText().toLowerCase().contains(text)
                        || f.getStatus().toLowerCase().contains(text)
                        || f.getCreationDate().toString().toLowerCase().contains(text)
                        || f.getLastReviewDate().toString().toLowerCase().contains(text);
            });
        });

        // COLUMNS
        TableColumn<Flashcard, String> deckCol = new TableColumn<>("Deck");
        deckCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDeck().getName()));

        TableColumn<Flashcard, String> frontCol = new TableColumn<>("Front Text");
        frontCol.setCellValueFactory(c ->
                new SimpleStringProperty(firstLine(c.getValue().getFrontText())));

        TableColumn<Flashcard, String> backCol = new TableColumn<>("Back Text");
        backCol.setCellValueFactory(c ->
                new SimpleStringProperty(firstLine(c.getValue().getBackText())));

        TableColumn<Flashcard, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));

        TableColumn<Flashcard, String> creationCol = new TableColumn<>("Creation Date");
        creationCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCreationDate().toString()));

        TableColumn<Flashcard, String> reviewCol = new TableColumn<>("Last Review Date");
        reviewCol.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getLastReviewDate().toString()));

        table.setItems(filteredData);
        table.getColumns().addAll(deckCol, frontCol, backCol, statusCol, creationCol, reviewCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // BUTTONS
        Button editBtn = new Button("Edit Flashcard");
        Button deleteBtn = new Button("Delete Flashcard");
        Button backBtn = new Button("Back");

        // EDIT BUTTON
        editBtn.setOnAction(e -> {
            Flashcard selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showAlert("Please select a flashcard to edit.");
                return;
            }

            showEditDialog(selected);
        });

        // DELETE BUTTON (FIXED)
        deleteBtn.setOnAction(e -> {
            Flashcard selected = table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showAlert("Please select a flashcard to delete.");
                return;
            }

            try {
                flashcardStorage.delete(selected);
                data.remove(selected);
            } catch (IOException ex) {
                showAlert("Could not delete flashcard.");
                ex.printStackTrace();
            }
        });

        // BACK
        backBtn.setOnAction(e -> goBack(stage));

        HBox actionButtons = new HBox(10, editBtn, deleteBtn);
        actionButtons.setAlignment(Pos.CENTER);

        Label title = new Label("Search Flashcards");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        VBox layout = new VBox(15, title, searchField, table, actionButtons, backBtn);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1000, 500);
        stage.setScene(scene);
        stage.setTitle("Search Flashcards");
        stage.show();
    }

    // EDIT DIALOG
    private void showEditDialog(Flashcard flashcard)
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Flashcard");

        TextArea frontField = new TextArea(flashcard.getFrontText());
        TextArea backField = new TextArea(flashcard.getBackText());

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(
                Flashcard.STATUS_NEW,
                Flashcard.STATUS_LEARNING,
                Flashcard.STATUS_MASTERED
        );
        statusBox.setValue(flashcard.getStatus());

        VBox content = new VBox(10,
                new Label("Front Text"),
                frontField,
                new Label("Back Text"),
                backField,
                new Label("Status"),
                statusBox
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                updateFlashcard(flashcard,
                        frontField.getText(),
                        backField.getText(),
                        statusBox.getValue());
            }
        });
    }

    // UPDATE LOGIC
    private void updateFlashcard(Flashcard flashcard, String newFront, String newBack, String newStatus)
    {
        newFront = newFront == null ? "" : newFront.trim();
        newBack = newBack == null ? "" : newBack.trim();

        if (newFront.isEmpty() || newBack.isEmpty()) {
            showAlert("Front and Back cannot be empty.");
            return;
        }

        // DUPLICATE CHECK
        for (Flashcard f : data) {
            if (f != flashcard &&
                    f.getDeck().getName().equalsIgnoreCase(flashcard.getDeck().getName()) &&
                    f.getFrontText().trim().equalsIgnoreCase(newFront)) {

                showAlert("Front Text must be unique within the same deck.");
                return;
            }
        }

        flashcard.setFrontText(newFront);
        flashcard.setBackText(newBack);
        flashcard.setStatus(newStatus);
        flashcard.updateLastReviewDate();

        try {
            flashcardStorage.save(data);
            table.refresh();
        } catch (IOException e) {
            showAlert("Failed to save changes.");
            e.printStackTrace();
        }
    }

    // FIRST LINE ONLY
    private String firstLine(String text)
    {
        if (text == null || text.isBlank()) return "";
        return text.split("\\R", 2)[0];
    }

    private void showAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goBack(Stage stage)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}