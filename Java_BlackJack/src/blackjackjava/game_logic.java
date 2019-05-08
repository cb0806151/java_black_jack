
package blackjackjava;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Christopher Baunach
 */
public class game_logic {
    public IntegerProperty playerWins = new SimpleIntegerProperty(0);
    public IntegerProperty dealerWins = new SimpleIntegerProperty(0);
    public int numberOfExtraDecks = -1;
    public int playersHandMax = 0;
    public int playersHandMin = 0;
    public int dealersHandMax = 0;
    public int dealersHandMin = 0;
    public boolean playerStanding = false;
    public BooleanProperty hitAndStandButtonsDisabled = new SimpleBooleanProperty(true);
    public StringProperty messageForPlayer = new SimpleStringProperty("Welcome to BlackJackJava!\nPress shuffle to start.");
    public String cardImageFolderPath = "src/cardImages";
    public String cardImageFolderName = "cardImages";
    public ArrayList<String> playersHand = new ArrayList<String>();
    public ArrayList<String> dealersHand = new ArrayList<String>();
    public ArrayList<String> currentDeck = new ArrayList<String>();
    
    public void user_interface() {
        
    }
    
    public void retrieveDeck(int deckMultiplier) {
        // retrieves a copy of the deck plus x additional decks
        File cardFolder = new File(cardImageFolderPath);
        currentDeck = new ArrayList<String>(Arrays.asList(cardFolder.list()));
        while (deckMultiplier > 0) {
            currentDeck.addAll(currentDeck);
            deckMultiplier -= 1;
        }
        Collections.shuffle(currentDeck);
    }
    
    public boolean haltGame(boolean winLossGame, String winner, String loser) {
        // ends the game, displays the win message, gives a point to the winner, and disabled the stand and hit buttons
        if (winLossGame) {
            if ("The House".equals(winner)) {
                dealerWins.set(dealerWins.get()+1);
            } else {
                playerWins.set(playerWins.get()+1);
            }
            messageForPlayer.setValue(winner + " won! " + loser + " lost!\n\nClick Shuffle to start a new game.");
        } else {
            messageForPlayer.setValue("Its a draw!\n\nClick Shuffle to start a new game.");
        }
        hitAndStandButtonsDisabled.set(true);
        return false;
    }
    
    public boolean checkForWinner(ArrayList<String> hand) {
        // checks for the winner and ends the game if someone has won
        int mainHandMax = dealersHandMax;
        int otherHandMax = playersHandMax;
        int mainHandMin = dealersHandMin;
        int otherHandMin = playersHandMin;
        String mainHandName = "The House";
        String otherHandName = "You";
        
        if (hand == playersHand) {
            mainHandName = "You";
            otherHandName = "The House";
            mainHandMax = playersHandMax;
            otherHandMax = dealersHandMax;
            mainHandMin = playersHandMin;
            otherHandMin = dealersHandMin;
        }
        
        if (mainHandMin == 21) {
            if (otherHandMin == mainHandMin) {
                return haltGame(false, "", "");
            } else {
                return haltGame(true, mainHandName, otherHandName);
            }
        } else if (mainHandMax == 21) {
            if (otherHandMax == mainHandMax) {
                return haltGame(false, "", "");
            } else {
                return haltGame(true, mainHandName, otherHandName);
            }
        } else if (mainHandMin > 21) {
            if (otherHandMin > mainHandMin) {
                return haltGame(true, mainHandName, otherHandName);
            } else {
                return haltGame(true, otherHandName, mainHandName);
            }
        } else if ("The House".equals(mainHandName) && playerStanding == true) {
            if (mainHandMin > otherHandMin) {
                if (mainHandMax > otherHandMax) {
                    return haltGame(true, mainHandName, otherHandName);
                } else {
                    return haltGame(true, otherHandName, mainHandName);
                }
            }
        }
        return true;
    }
    
    public void increaseHandValue(ArrayList<String> hand, String topCard) {
        // increases the hand max and min values
        Integer valueOfHandsLastCard = Integer.valueOf(topCard.substring(0, 2));
        if (hand == playersHand) {
            if (valueOfHandsLastCard == 1) {
                playersHandMax += 11;
                playersHandMin += 1;
            } else {
                playersHandMax += valueOfHandsLastCard;
                playersHandMin += valueOfHandsLastCard;
            }
        } else {
            if (valueOfHandsLastCard == 1) {
                dealersHandMax += 11;
                dealersHandMin += 1;
            } else {
                dealersHandMax += valueOfHandsLastCard;
                dealersHandMin += valueOfHandsLastCard;
            }
        }
    }
}
