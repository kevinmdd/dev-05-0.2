package cs151.application;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewFlashcardPage {

    private List<Deck> decks;
    private List<Flashcard> allFlashcards;

    private List<Flashcard> filteredFlashcards = new ArrayList<>();
    private int currentIndex = 0;

    public void start(Stage stage) {
        decks = DeckStorage.load();
        allFlashcards = FlashcardStorage.load();

        showReviewPage(stage);
    }

    private void showReviewPage(Stage stage) {

        TextField searchField = new TextField();
        searchField.setPromptText("Search Deck");

        ListView<Deck> deckList = new ListView<>();
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
                Scene scene = new Scene(loader.load(), 600, 400);

                Stage currentStage = (Stage) backBtn.getScene().getWindow();
                currentStage.setScene(scene);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        deckList.setItems(FXCollections.observableArrayList(decks));

        deckList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Deck item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        // SEARCH
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String text = newVal.toLowerCase();
            deckList.setItems(FXCollections.observableArrayList(
                    decks.stream()
                            .filter(d -> d.getName().toLowerCase().contains(text))
                            .toList()
            ));
        });

        // CLICK ROW → GO TO REVIEW
        deckList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                Deck selected = deckList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openDeckReview(stage, selected);
                }
            }
        });

        VBox layout = new VBox(10, searchField, deckList, backBtn);        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 600, 400));
    }

    private void openDeckReview(Stage stage, Deck deck) {

        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "New", "Learning", "Mastered");
        filterBox.setValue("All");

        TextArea front = new TextArea();
        TextArea back = new TextArea();

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("New", "Learning", "Mastered");

        Label deckName = new Label(deck.getName());
        Label creationDate = new Label();
        Label reviewDate = new Label();

        Button nextBtn = new Button("Next");
        Button prevBtn = new Button("Previous");
        Button saveBtn = new Button("Save");
        Button backBtn = new Button("Back");

        // FILTER LOGIC
        Runnable applyFilter = () -> {
            String filter = filterBox.getValue();

            filteredFlashcards = allFlashcards.stream()
                    .filter(f -> f.getDeck().getName().equalsIgnoreCase(deck.getName()))
                    .filter(f -> filter.equals("All") || f.getStatus().equals(filter))
                    .collect(Collectors.toList());

            currentIndex = 0;
            showCard(front, back, statusBox, creationDate, reviewDate, nextBtn, prevBtn);
        };

        filterBox.setOnAction(e -> applyFilter.run());

        // NAVIGATION
        nextBtn.setOnAction(e -> {
            if (currentIndex < filteredFlashcards.size() - 1) {
                currentIndex++;
                showCard(front, back, statusBox, creationDate, reviewDate, nextBtn, prevBtn);
            }
        });

        prevBtn.setOnAction(e -> {
            if (currentIndex > 0) {
                currentIndex--;
                showCard(front, back, statusBox, creationDate, reviewDate, nextBtn, prevBtn);
            }
        });

        // SAVE
        saveBtn.setOnAction(e -> {
            if (filteredFlashcards.isEmpty()) return;

            Flashcard f = filteredFlashcards.get(currentIndex);
            f.setFrontText(front.getText());
            f.setBackText(back.getText());
            f.setStatus(statusBox.getValue());
            f.updateLastReviewDate();

            try {
                FlashcardStorage.save(allFlashcards);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        backBtn.setOnAction(e -> start(stage));

        VBox layout = new VBox(10,
                deckName,
                filterBox,
                front,
                back,
                statusBox,
                creationDate,
                reviewDate,
                new HBox(10, prevBtn, nextBtn),
                saveBtn,
                backBtn
        );

        layout.setPadding(new Insets(20));
        applyFilter.run();

        stage.setScene(new Scene(layout, 600, 500));
    }

    private void showCard(TextArea front, TextArea back,
                          ComboBox<String> statusBox,
                          Label creationDate, Label reviewDate,
                          Button nextBtn, Button prevBtn) {

        if (filteredFlashcards.isEmpty()) {
            front.clear();
            back.clear();
            return;
        }

        Flashcard f = filteredFlashcards.get(currentIndex);

        front.setText(f.getFrontText());
        back.setText(f.getBackText());
        statusBox.setValue(f.getStatus());

        creationDate.setText("Created: " + f.getCreationDate().toLocalDate());
        reviewDate.setText("Last Reviewed: " + LocalDate.now());

        prevBtn.setDisable(currentIndex == 0);
        nextBtn.setDisable(currentIndex == filteredFlashcards.size() - 1);
    }
}