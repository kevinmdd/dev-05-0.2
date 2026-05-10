package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ViewDecksPage {

    private final TableView<Deck> table = new TableView<>();
    private ObservableList<Deck> data;
    private final DeckStorage deckStorage = new DeckStorage();
    private final FlashcardStorage flashcardStorage = new FlashcardStorage();

    public void start(Stage stage) {
        List<Deck> decks = deckStorage.load();
        decks.sort(Comparator.comparing(d -> d.getName().toLowerCase()));

        data = FXCollections.observableArrayList(decks);

        TableColumn<Deck, String> nameCol = new TableColumn<>("Deck Name");
        nameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Deck, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(firstLine(cellData.getValue().getDescription())));

        table.setItems(data);
        table.getColumns().clear();
        table.getColumns().addAll(nameCol, descCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(300);

        // Double-click to edit
        table.setRowFactory(tv -> {
            TableRow<Deck> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    editDeck(row.getItem());
                }
            });
            return row;
        });

        Label title = new Label("All Decks");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label instruction = new Label("Select a deck to delete, or double-click a row to edit.");

        Button editBtn = new Button("Edit Selected Deck");
        editBtn.setOnAction(e -> {
            Deck selectedDeck = table.getSelectionModel().getSelectedItem();
            if (selectedDeck == null) {
                showWarning("Please select a deck to edit.");
                return;
            }
            editDeck(selectedDeck);
        });

        Button deleteBtn = new Button("Delete Selected Deck");
        deleteBtn.setOnAction(e -> {
            Deck selectedDeck = table.getSelectionModel().getSelectedItem();
            if (selectedDeck == null) {
                showWarning("Please select a deck to delete.");
                return;
            }
            deleteDeck(selectedDeck);
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> goBack(stage));

        HBox buttons = new HBox(12, editBtn, deleteBtn, backBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, title, instruction, table, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 700, 450);
        stage.setTitle("View Decks");
        stage.setScene(scene);
        stage.show();
    }

    private void editDeck(Deck selectedDeck) {
        String oldName = selectedDeck.getName();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Deck");
        dialog.setHeaderText("Edit selected deck information");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField nameField = new TextField(selectedDeck.getName());
        TextArea descriptionArea = new TextArea(selectedDeck.getDescription());
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setWrapText(true);

        VBox content = new VBox(10,
                new Label("Deck Name *"),
                nameField,
                new Label("Description"),
                descriptionArea
        );
        content.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(content);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == saveButtonType) {
            String newName = nameField.getText() == null ? "" : nameField.getText().trim();
            String newDescription = descriptionArea.getText() == null ? "" : descriptionArea.getText().trim();

            if (newName.isEmpty()) {
                showError("Deck name cannot be empty.");
                return;
            }

            if (isDuplicateName(newName, selectedDeck)) {
                showError("Deck name must be unique.");
                return;
            }

            selectedDeck.setName(newName);
            selectedDeck.setDescription(newDescription);

            try {
                // Update linked flashcards first, while old deck name still exists in storage
                flashcardStorage.updateDeckReference(oldName, selectedDeck);

                // Then save decks permanently
                deckStorage.save(data);

                sortAndRefresh();
                table.refresh();

                showInfo("Deck updated successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Could not save the modified deck.");
            }
        }
    }

    private void deleteDeck(Deck selectedDeck) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Deck");
        confirm.setHeaderText("Delete selected deck?");
        confirm.setContentText("All flashcards linked to this deck will also be deleted permanently.");

        Optional<ButtonType> result = confirm.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete linked flashcards first, before the deck disappears from deck storage
                flashcardStorage.deleteByDeck(selectedDeck.getName());

                data.remove(selectedDeck);
                deckStorage.save(data);

                table.refresh();
                showInfo("Deck and linked flashcards deleted successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Could not delete the deck.");
            }
        }
    }

    private boolean isDuplicateName(String name, Deck currentDeck) {
        for (Deck deck : data) {
            if (deck != currentDeck && deck.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private String firstLine(String text) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String[] lines = text.split("\\R", 2);
        return lines[0];
    }

    private void sortAndRefresh() {
        FXCollections.sort(data, Comparator.comparing(d -> d.getName().toLowerCase()));
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void goBack(Stage stage) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("HomePage.fxml"));
            stage.setScene(new Scene(loader.load(), 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}