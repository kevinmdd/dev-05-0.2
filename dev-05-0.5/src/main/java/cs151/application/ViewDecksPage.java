package cs151.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class ViewDecksPage {

    private TableView<Deck> table = new TableView<>();

    public void start(Stage stage) {
        List<Deck> decks = DeckStorage.load();

        decks.sort(Comparator.comparing(d -> d.getName().toLowerCase()));

        ObservableList<Deck> data = FXCollections.observableArrayList(decks);

        TableColumn<Deck, String> nameCol = new TableColumn<>("Deck Name");
        nameCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Deck, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));

        table.setItems(data);
        table.getColumns().addAll(nameCol, descCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label title = new Label("All Decks");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> goBack(stage));

        VBox layout = new VBox(15, title, table, backBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
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