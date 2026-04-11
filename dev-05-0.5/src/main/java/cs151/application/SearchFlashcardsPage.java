package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.List;

public class SearchFlashcardsPage
{
    private final TableView<Flashcard> table = new TableView<>();
    private ObservableList<Flashcard> data;

    public void start(Stage stage)
    {
        // Load all flashcards
        List<Flashcard> flashcards = FlashcardStorage.load();
        data = FXCollections.observableArrayList(flashcards);

        // Search field (just made the UI)
        TextField searchField = new TextField();
        searchField.setPromptText("Search flashcards...");
        searchField.setMaxWidth(400);

        // add filter logic
        // add listener, filter flashcards, case insensitive, update dynamically

        // Table Columns
        TableColumn<Flashcard, String> deckCol = new TableColumn<>("Deck");
        deckCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDeck().getName()));

        TableColumn<Flashcard, String> frontCol = new TableColumn<>("Front Text");
        frontCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFrontText()));

        TableColumn<Flashcard, String> backCol = new TableColumn<>("Back Text");
        backCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBackText()));

        TableColumn<Flashcard, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus()));

        TableColumn<Flashcard, String> creationCol = new TableColumn<>("Creation Date");
        creationCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate().toString()));

        TableColumn<Flashcard, String> reviewCol = new TableColumn<>("Last Review Date");
        reviewCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLastReviewDate().toString()));

        // format text, show just first line of front and back text
        // sorting and maybe a message saying it doesnt work

        // Set data (no filtering yet)
        table.setItems(data);
        table.getColumns().addAll(deckCol, frontCol, backCol, statusCol, creationCol, reviewCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label title = new Label("Search Flashcards");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> goBack(stage));

        VBox layout = new VBox(15, title, searchField, table, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1000, 500);
        stage.setScene(scene);
        stage.setTitle("Search Flashcards");
        stage.show();
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