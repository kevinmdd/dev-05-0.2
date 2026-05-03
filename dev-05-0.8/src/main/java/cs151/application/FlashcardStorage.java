package cs151.application;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FlashcardStorage {

    private static final String FILE_NAME = "flashcards.csv";

    public static void save(List<Flashcard> flashcards) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {

            writer.println("Deck,Front,Back,Status,CreationDate,LastReviewDate");

            for (Flashcard flashcard : flashcards) {
                String deckName = flashcard.getDeck().getName().replace(",", " ");
                String front = flashcard.getFrontText().replace(",", " ")
                        .replace("\n", " ")
                        .replace("\r", " ");
                String back = flashcard.getBackText().replace(",", " ")
                        .replace("\n", " ")
                        .replace("\r", " ");
                String status = flashcard.getStatus().replace(",", " ");
                String creationDate = flashcard.getCreationDate().toString();
                String lastReviewDate = flashcard.getLastReviewDate().toString();

                writer.println(deckName + "," + front + "," + back + ","
                        + status + "," + creationDate + "," + lastReviewDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Flashcard> load() {
        List<Flashcard> flashcards = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            List<Deck> decks = DeckStorage.load();

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 6);

                String deckName = parts[0];
                String front = parts[1];
                String back = parts[2];
                String status = parts[3];
                LocalDateTime creationDate = LocalDateTime.parse(parts[4]);
                LocalDateTime lastReviewDate = LocalDateTime.parse(parts[5]);

                Deck matchedDeck = null;
                for (Deck d : decks) {
                    if (d.getName().equalsIgnoreCase(deckName)) {
                        matchedDeck = d;
                        break;
                    }
                }

                if (matchedDeck != null) {
                    Flashcard flashcard = new Flashcard(
                            matchedDeck,
                            front,
                            back,
                            status,
                            creationDate,
                            lastReviewDate
                    );
                    flashcards.add(flashcard);
                }
            }

        } catch (Exception e) {
            // file may not exist yet
        }

        return flashcards;
    }
    
    public static void delete(Flashcard targetFlashcard) throws IOException {
    List<Flashcard> flashcards = load();

    flashcards.removeIf(flashcard ->
            flashcard.getDeck().getName().equalsIgnoreCase(targetFlashcard.getDeck().getName()) &&
            flashcard.getFrontText().equalsIgnoreCase(targetFlashcard.getFrontText()) &&
            flashcard.getBackText().equalsIgnoreCase(targetFlashcard.getBackText()));

    save(flashcards);


}
    public static void deleteByDeck(String deckName) throws IOException {
        List<Flashcard> flashcards = load();

        flashcards.removeIf(flashcard ->
                flashcard.getDeck().getName().equalsIgnoreCase(deckName));

        save(flashcards);
    }

    public static void updateDeckReference(String oldDeckName, Deck updatedDeck) throws IOException {
        List<Flashcard> flashcards = load();

        for (Flashcard flashcard : flashcards) {
            if (flashcard.getDeck().getName().equalsIgnoreCase(oldDeckName)) {
                flashcard.setDeck(updatedDeck);
            }
        }

        save(flashcards);
    }
}