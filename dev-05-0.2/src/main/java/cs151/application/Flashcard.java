package cs151.application;

import java.time.LocalDateTime;

public class Flashcard
{
    public static final String STATUS_NEW = "New";
    public static final String STATUS_LEARNING = "Learning";
    public static final String STATUS_MASTERED = "Mastered";

    private Deck deck;
    private String frontText;
    private String backText;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime reviewDate;

    public Flashcard(Deck deck, String frontText, String backText,
                     String status, LocalDateTime creationDate, LocalDateTime reviewDate)
    {
        this.deck = deck;
        this.frontText = frontText;
        this.backText = backText;
        this.status = status;
        this.creationDate = creationDate;
        this.reviewDate = reviewDate;
    }

    public String getFrontText()
    {
        return frontText;
    }

    public String getBackText()
    {
        return backText;
    }

    public String getStatus()
    {
        return status;
    }

    public Deck getDeck()
    {
        return deck;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public LocalDateTime getLastReviewDate()
    {
        return reviewDate;
    }

    public void setFrontText(String frontText)
    {
        this.frontText = frontText;
    }

    public void setBackText(String backText)
    {
        this.backText = backText;
    }

    public void setStatus(String status)
    {
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = status;
    }

    public void setDeck(Deck deck)
    {
        this.deck = deck;
    }

    public void updateLastReviewDate()
    {
        this.reviewDate = LocalDateTime.now();
    }

    public static boolean isValidStatus(String status)
    {
        return STATUS_NEW.equals(status)
                || STATUS_LEARNING.equals(status)
                || STATUS_MASTERED.equals(status);
    }
}