/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjackjava;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Raxsus
 */
public class user_interface extends Application {
    public game_logic logic = new game_logic();
    
    public StackPane playersCards = new StackPane();
    public StackPane dealersCards = new StackPane();
    public Text playerTitle = new Text("You: ");
    public Text dealersTitle = new Text("The Dealer: ");
    public Text playerWinsText = new Text("");
    public Text dealerWinsText = new Text("");
    public Button hitMeButton = new Button("Hit Me");
    public Button standButton = new Button("Stand");
    public Button shuffleDeckButton = new Button("Shuffle");
    public Text message = new Text("");
    
    protected GridPane createGameScene() {
        GridPane containerHolder = new GridPane();
        VBox optionMenu = new VBox();
        
        // bind text properties to their perspective variables
        message.textProperty().bind(logic.messageForPlayer);
        dealerWinsText.textProperty().bind(logic.dealerWins.asString());
        playerWinsText.textProperty().bind(logic.playerWins.asString());
        standButton.disableProperty().bind(logic.hitAndStandButtonsDisabled);
        hitMeButton.disableProperty().bind(logic.hitAndStandButtonsDisabled);

        // load up and style the option menu
        optionMenu.getChildren().addAll(Arrays.asList(shuffleDeckButton,
                hitMeButton,standButton,message));
        optionMenu.setSpacing(20);
        optionMenu.setMinWidth(150);
        optionMenu.setMinHeight(350);
        optionMenu.setAlignment(Pos.CENTER);
        
        // create the players card, title, and win count 
        VBox playersCardsContainer = createCardContainer(playerTitle, playersCards, playerWinsText);
        
        // create the dealers card, title, and win count 
        VBox dealersCardsContainer = createCardContainer(dealersTitle, dealersCards, dealerWinsText);
        
        // add in the card holders and menu into a grid
        containerHolder.add(playersCardsContainer, 0, 0);
        containerHolder.add(optionMenu, 1, 0);
        containerHolder.add(dealersCardsContainer, 2, 0);
        
        promptUserForNumberOfDecks();
        setButtonFunctionality();
        setListenerForEndOfGame();
        
        return containerHolder;
    }
    
    public void setListenerForEndOfGame() {
        logic.hitAndStandButtonsDisabled.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showDealersHand();
                }
            }
        });
    }
    
    public void showDealersHand() {
        dealersCards.getChildren().clear();
        for (int i=0; i<logic.dealersHand.size(); i++) {
            ImageView tempCard = new ImageView(logic.cardImageFolderName + "/" + logic.dealersHand.get(i));
            tempCard.setTranslateX(30*i);
            dealersCards.getChildren().add(tempCard);
            dealersCards.setTranslateX(-15*i);
        }
    }
    
    public boolean dealCard(ArrayList<String> hand, StackPane handContainer) {
        String topCard = logic.currentDeck.get(0);
        hand.add(topCard);
        logic.currentDeck.remove(0);
        
        ImageView tempCard = new ImageView(logic.cardImageFolderName + "/" + hand.get(hand.size()-1));
        if (hand == logic.dealersHand && logic.dealersHand.size() == 1) {
            tempCard = new ImageView("otherImages/cardBack.png");
            handContainer.getChildren().add(tempCard);
        } else {
            handContainer.getChildren().add(tempCard);
        }
        tempCard.setTranslateX(30*hand.size()-1);
        handContainer.setTranslateX(-20*hand.size()-1);
        
        logic.increaseHandValue(hand, topCard);
        return logic.checkForWinner(hand);
    }
    
    public void dealersTurn() {
        if (!standButton.isDisabled()) {
            dealCard(logic.dealersHand, dealersCards);
            dealersTurn();
        }
    }
    
    public void startGame() {
        logic.retrieveDeck(logic.numberOfExtraDecks);
        logic.playerStanding = false;
        logic.hitAndStandButtonsDisabled.set(false);
        logic.playersHand.clear();
        logic.dealersHand.clear();
        logic.playersHandMax = 0;
        logic.playersHandMin = 0;
        logic.dealersHandMax = 0;
        logic.dealersHandMin = 0;
        dealersCards.getChildren().clear();
        playersCards.getChildren().clear();
        playerTitle.setVisible(true);
        dealersTitle.setVisible(true);
        dealerWinsText.setVisible(true);
        playerWinsText.setVisible(true);
        logic.messageForPlayer.setValue("");
        dealCard(logic.playersHand, playersCards);
        dealCard(logic.dealersHand, dealersCards);
        if (dealCard(logic.playersHand, playersCards)) {
            dealCard(logic.dealersHand, dealersCards);
        }
    }
    
    public VBox createCardContainer(Text containerTitle, StackPane cardPane, Text winCount) {
        VBox cardContainer = new VBox();
        HBox titleContainer = new HBox();
        titleContainer.getChildren().addAll(Arrays.asList(containerTitle, winCount));
        titleContainer.setAlignment(Pos.CENTER);
        containerTitle.setVisible(false);
        winCount.setVisible(false);
        cardContainer.getChildren().addAll(Arrays.asList(titleContainer,
                                                        cardPane));
        cardContainer.setMinWidth(300);
        cardContainer.setAlignment(Pos.CENTER);
        return cardContainer;
    }
    
    public void promptUserForNumberOfDecks() {
        while (logic.numberOfExtraDecks < 0) {
            String numberOfDecks = JOptionPane.showInputDialog("How many extra decks would you like for these games of blackjack?\n(you may enter zero for just one deck)");
            try {
                logic.numberOfExtraDecks = Integer.valueOf(numberOfDecks);
            } catch(Exception err) {
                System.out.println(err);
            }
        }
    }
    
    public void setButtonFunctionality() {
        // set functionality for the shuffle button
        shuffleDeckButton.setOnAction(e -> {
            startGame();
        });
        
        // set functionality for the hit me button
        hitMeButton.setOnAction(e -> {
            dealCard(logic.playersHand, playersCards);
        });
        
        // set the functionality for the stand button
        standButton.setOnAction(e -> {
            logic.playerStanding = true;
            dealersTurn();
        });
    }
    
    @Override
    public void start(Stage stage) {
        
    }
}
