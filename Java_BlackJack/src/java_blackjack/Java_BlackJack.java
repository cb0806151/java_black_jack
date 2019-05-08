/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_blackjack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Raxsus
 */
public class Java_BlackJack extends Application {
    public int walletCount = 100;
    public int playersHandMax = 0;
    public int playersHandMin = 0;
    public int dealersHandMax = 0;
    public int dealersHandMin = 0;
    public String cardImageFolderPath = "src/cardImages";
    public String cardImageFolderName = "cardImages";
    public ArrayList<String> playersHand = new ArrayList<String>();
    public ArrayList<String> dealersHand = new ArrayList<String>();
    public ArrayList<String> currentDeck = new ArrayList<String>();
    public StackPane playersCards = new StackPane();
    public StackPane dealersCards = new StackPane();
    public Text playerTitle = new Text("You");
    Text dealersTitle = new Text("The Dealer");
    public Button hitMeButton = new Button("Hit Me");
    public Button standButton = new Button("Stand");
    public Button shuffleDeckButton = new Button("Shuffle");
    public Text message = new Text("Welcome to BlackJackJava!\nClick shuffle to start a game!");
    
    public void retrieveDeck() {
        File cardFolder = new File(cardImageFolderPath);
        currentDeck = new ArrayList<String>(Arrays.asList(cardFolder.list()));
        Collections.shuffle(currentDeck);
    }
    
    public void showDealersHand() {
        dealersCards.getChildren().clear();
        for (int i=0; i<dealersHand.size(); i++) {
            ImageView tempCard = new ImageView(cardImageFolderName + "/" + dealersHand.get(i));
            tempCard.setTranslateX(30*i);
            dealersCards.getChildren().add(tempCard);
            dealersCards.setTranslateX(-15*i);
        }
    }
    
    public void haltGame() {
        showDealersHand();
        standButton.setDisable(true);
        hitMeButton.setDisable(true);
    }
    
    public void checkForWinner(ArrayList<String> hand) {
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
        
        if (mainHandMin >= 21) {
            if (otherHandMin == mainHandMin) {
                message.setText("Its a draw!\n\nClick Shuffle to start a new game.");
                haltGame();
            } else if (otherHandMin > mainHandMin) {
                message.setText(mainHandName + " won! " + otherHandName + " lost!\n\nClick Shuffle to start a new game.");
                haltGame();
            } else {
                message.setText(mainHandName + " lost! " + otherHandName + " won!\n\nClick Shuffle to start a new game.");
                haltGame();
            }
        } else if (mainHandMax == 21) {
            if (otherHandMax == mainHandMax) {
                message.setText("Its a draw!\n\nClick Shuffle to start a new game.");
                haltGame();
            } else {
                message.setText(mainHandName + " won! " + otherHandName + " lost!\n\nClick Shuffle to start a new game.");
                haltGame();
            }
        }
    }
    
    public void increaseHandValue(ArrayList<String> hand, String topCard) {
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
    
    public void dealCard(ArrayList<String> hand, StackPane handContainer) {
        String topCard = currentDeck.get(0);
        hand.add(topCard);
        currentDeck.remove(0);
        
        ImageView tempCard = new ImageView(cardImageFolderName + "/" + hand.get(hand.size()-1));
        if (hand == dealersHand && dealersHand.size() == 1) {
            tempCard = new ImageView("otherImages/cardBack.png");
            handContainer.getChildren().add(tempCard);
        } else {
            handContainer.getChildren().add(tempCard);
        }
        tempCard.setTranslateX(30*hand.size()-1);
        handContainer.setTranslateX(-15*hand.size()-1);
        
        increaseHandValue(hand, topCard);
        checkForWinner(hand);
    }
    
    public void dealersTurn() {
        if (message.getText() == "") {
            int pickPercentage = 21 - dealersHandMin;
            dealCard(dealersHand, dealersCards);
        }
    }


    @Override
    public void start(Stage stage) {
        
        
        GridPane containerHolder = new GridPane();
        VBox optionMenu = new VBox();
        
        Text currentCash = new Text("Wallet: $" + walletCount);
        TextField betInputField = new TextField();
        Label betInputLabel = new Label("Current Bet: $");
        
        shuffleDeckButton.setOnAction(e -> {
            retrieveDeck();
            playersHand.clear();
            dealersHand.clear();
            playersHandMax = 0;
            playersHandMin = 0;
            dealersHandMax = 0;
            dealersHandMin = 0;
            dealersCards.getChildren().clear();
            playersCards.getChildren().clear();
            standButton.setDisable(false);
            hitMeButton.setDisable(false);
            playerTitle.setVisible(true);
            dealersTitle.setVisible(true);
            message.setText("");
            dealCard(playersHand, playersCards);
            dealCard(dealersHand, dealersCards);
            dealCard(playersHand, playersCards);
            dealCard(dealersHand, dealersCards);
        });
        
        
        hitMeButton.setOnAction(e -> {
            dealCard(playersHand, playersCards);
            dealersTurn();
        });
        
        standButton.setOnAction(e -> {
            dealersTurn();
        });
        
        hitMeButton.setDisable(true);
        standButton.setDisable(true);
        
        
        
        optionMenu.getChildren().addAll(Arrays.asList(currentCash,betInputLabel,
                betInputField,shuffleDeckButton,hitMeButton,standButton,message));
        optionMenu.setSpacing(20);
        optionMenu.setMinWidth(150);
        optionMenu.setMinHeight(350);
        optionMenu.setAlignment(Pos.CENTER);
        
        
        VBox playersCardsContainer = new VBox();
        playerTitle.setVisible(false);
        playersCardsContainer.getChildren().addAll(Arrays.asList(playerTitle,
                                                                 playersCards));
        playersCardsContainer.setMinWidth(300);
        playersCardsContainer.setAlignment(Pos.CENTER);
        
        
        VBox dealersCardsContainer = new VBox();
        dealersTitle.setVisible(false);
        dealersCardsContainer.getChildren().addAll(Arrays.asList(dealersTitle,
                                                                dealersCards));
        dealersCardsContainer.setMinWidth(300);
        dealersCardsContainer.setAlignment(Pos.CENTER);
        
        
        containerHolder.add(playersCardsContainer, 0, 0);
        containerHolder.add(optionMenu, 1, 0);
        containerHolder.add(dealersCardsContainer, 2, 0);
        
        
        Scene scene = new Scene(containerHolder, 750, 350);
        stage.setTitle("Black Jack Java");
        stage.setScene(scene);
        stage.show();
        
    }    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
