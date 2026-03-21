package cs151.application;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeckStorage
{
    private static final String FILE_NAME = "decks.csv";

    public static void save(List<Deck> decks) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME)))
        {
            writer.println("DeckName,Description");

            for (Deck deck: decks)
            {
                String name = deck.getName().replace(",", " ");
                String description = deck.getDescription().replace(",", " ");
                writer.println(name + "," + description);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List<Deck> load() {
        List<Deck> decks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(",", 2);
                String name = parts.length > 0 ? parts[0] : "";
                String desc = parts.length > 1 ? parts[1] : "";

                decks.add(new Deck(name, desc));
            }

        } catch (Exception e) {
            // file could not exist
        }

        return decks;
    }

}
