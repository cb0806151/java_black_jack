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
        
        increaseHandValue(hand, topCard);
        
        currentDeck.remove(0);
        ImageView tempCard = new ImageView(cardImageFolderName + "/" + hand.get(hand.size()-1));
        tempCard.setTranslateX(30*hand.size()-1);
        handContainer.setTranslateX(-15*hand.size()-1);
        if (hand == dealersHand && dealersHand.size() == 1) {
            handContainer.getChildren().add(new ImageView("otherImages/cardBack.png"));
        } else {
            handContainer.getChildren().add(tempCard);
        }
    }
    

    @Override
    public void start(Stage stage) {
        File cardFolder = new File(cardImageFolderPath);
        ArrayList<String> cardImageArray = new ArrayList<String>(Arrays.asList(cardFolder.list()));
        currentDeck = cardImageArray;
        Collections.shuffle(currentDeck);
        
        GridPane containerHolder = new GridPane();
        VBox optionMenu = new VBox();
        
        Text currentCash = new Text("Wallet: $" + walletCount);
        TextField betInputField = new TextField();
        Label betInputLabel = new Label("Current Bet: $");
        Button shuffleDeckButton = new Button("Shuffle");
        
        shuffleDeckButton.setOnAction(e -> {
            playersHand.clear();
            dealersHand.clear();
            playersHandMax = 0;
            playersHandMin = 0;
            dealersHandMax = 0;
            dealersHandMin = 0;
            dealCard(playersHand, playersCards);
            dealCard(dealersHand, dealersCards);
            dealCard(playersHand, playersCards);
            dealCard(dealersHand, dealersCards);
            System.out.println("players max hand" + String.valueOf(playersHandMax));
            System.out.println("players min hand" + String.valueOf(playersHandMin));
            System.out.println("dealers max hand" + String.valueOf(dealersHandMax));
            System.out.println("dealers min hand" + String.valueOf(dealersHandMin));
        });
        
        Button hitMeButton = new Button("Hit Me");
        
        hitMeButton.setOnAction(e -> {
            dealCard(playersHand, playersCards);
            //System.out.println(playersHandMax);
            //dealersTurn();
        });
        
        Button standButton = new Button("Stand");
        
        
        
        Button newGameButton = new Button("New Game");
        
        
        
        optionMenu.getChildren().addAll(Arrays.asList(currentCash,betInputLabel,
                betInputField,shuffleDeckButton,hitMeButton,standButton,
                newGameButton));
        optionMenu.setSpacing(20);
        optionMenu.setMinWidth(150);
        optionMenu.setAlignment(Pos.CENTER);
        
        
        VBox playersCardsContainer = new VBox();
        Text playerTitle = new Text("You");
        
        playersCardsContainer.getChildren().addAll(Arrays.asList(playerTitle,
                                                                 playersCards));
        playersCardsContainer.setMinWidth(300);
        playersCardsContainer.setAlignment(Pos.CENTER);
        
        
        VBox dealersCardsContainer = new VBox();
        Text dealersTitle = new Text("The Dealer");
     
        dealersCardsContainer.getChildren().addAll(Arrays.asList(dealersTitle,
                                                                dealersCards));
        dealersCardsContainer.setMinWidth(300);
        dealersCardsContainer.setAlignment(Pos.CENTER);
        
        
        containerHolder.add(playersCardsContainer, 0, 0);
        containerHolder.add(optionMenu, 1, 0);
        containerHolder.add(dealersCardsContainer, 2, 0);
        
        
        Scene scene = new Scene(containerHolder, 750, 350);
        stage.setTitle("Java Black Jack");
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
