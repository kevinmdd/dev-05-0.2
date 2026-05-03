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

import java.util.Comparator;
import java.util.List;

public class ViewFlashcardsPage
{
    private final TableView<Flashcard> table = new TableView<>();

    public void start(Stage stage)
    {
        List<Flashcard> flashcards = FlashcardStorage.load();

        flashcards.sort(Comparator.comparing(Flashcard::getCreationDate).reversed());

        ObservableList<Flashcard> data = FXCollections.observableArrayList(flashcards);

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

        table.setItems(data);
        table.getColumns().addAll(deckCol, frontCol, backCol, statusCol, creationCol, reviewCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        Label title = new Label("All Flashcards");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label sortNote = new Label("Sorted by Creation Date: Newest First");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> goBack(stage));

        VBox layout = new VBox(15, title, sortNote, table, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 1000, 500);
        stage.setTitle("View Flashcards");
        stage.setScene(scene);
        stage.show();

        flashcards.sort(Comparator.comparing(Flashcard::getCreationDate).reversed());
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