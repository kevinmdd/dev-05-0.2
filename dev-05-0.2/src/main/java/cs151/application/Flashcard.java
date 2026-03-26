package cs151.application;

import java.util.Date;
import java.time.LocalDate;


public class Flashcard
{
    private String frontText;
    private String backText;
    private String status;
    private Deck deck;
    private LocalDate creationDate;
    private LocalDate reviewDate;


    public Flashcard(Deck deck, String frontText, String backText,
                     String status, LocalDate creationDate, LocalDate reviewDate)
    {
        this.deck = deck;
        this.frontText = frontText;
        this.backText = backText;
        this.status = status;
        this.creationDate = creationDate;
        this.reviewDate = reviewDate;
    }

    public String getFrontText()
    { return frontText; }

    public String getBackText()
    { return backText; }

    public String getStatus()
    { return status; }

    public Deck getDeck()
    { return deck; }

    public LocalDate getCreationDate()
    { return creationDate; }

    public LocalDate getLastReviewDate()
    { return reviewDate; }


}
