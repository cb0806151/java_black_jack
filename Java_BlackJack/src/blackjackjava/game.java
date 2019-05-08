
package blackjackjava;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Christopher Baunach
 */
public class game extends user_interface {

    @Override
    public void start(Stage stage) {
        
        // set the scene and start the stage
        Scene scene = new Scene(createGameScene(), 750, 350);
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
