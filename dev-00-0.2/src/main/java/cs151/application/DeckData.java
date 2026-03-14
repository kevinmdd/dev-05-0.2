package cs151.application;
import java.util.List;
import java.util.ArrayList;

public class DeckData
{
    // save decks to list
    // placeholder for flat files/sqlite
    private List<Deck> savedDecks = new ArrayList<>();

    // check if name is unique
    public boolean isUnique(String newName)
    {
        String name = newName.trim();
        for (Deck deck : savedDecks)
        {
            if (name.equalsIgnoreCase(deck.getName()))
            {
                return false; // not unique name
            }
        }
        return true; // unique name
    }

    public void addDeck(Deck deck) throws Exception {
        // if empty name throw exception
        if (deck.getName().isEmpty())
        {
            throw new Exception("Name cannot be empty.");
        }

        // if name isn't unique throw exception
        if (!isUnique(deck.getName()))
        {
            throw new Exception("Name has to be unique!");
        }

        // if not empty and unique
        savedDecks.add(deck);
    }

}
