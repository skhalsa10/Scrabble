package me.snizzle.gamelauncher;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import me.snizzle.game.Game;
import me.snizzle.game.GameFactory;
import me.snizzle.game.GameType;

/**
 * this game launcher is a gui application that launches games.
 * the goal is to have the ability to launch all games made in this class this can be reused in the future apps
 */
public class GameLauncher extends Application {
    //We will start by declaring buttons here that will launch a specific game
    private Button scrabbleBtn;
    //add more buttons here for any new games

    //here we have the declaration of the objects needed for the Game launcher Window
    //lets make a grid
    private Scene launcherScene;
    private VBox root;
    private Label title;
    private HBox row1;

    //declare the game  that will be launched
    private Game game;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("CS351 Game Launcher");
        //this will be rows of stuff
        root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        //create the scene
        launcherScene = new Scene(root, 500, 300);
        //add css for styling
        launcherScene.getStylesheets().add("me/snizzle/gamelauncher/GameLauncher.css");

        //creat the title
        title = new Label("The Game Launcher");
        title.setAlignment(Pos.CENTER);
        title.setId("title");

        //add the title
        root.getChildren().add(title);
        root.setSpacing(10);

        //create the button
        scrabbleBtn = new Button("Scrabble");
        scrabbleBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                launchScrabble();
            }
        });

        //creat row1 of buttons
        row1 = new HBox();
        //allign all children to center
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().add(scrabbleBtn);

        //add the the row to root
        root.getChildren().add(row1);


        primaryStage.setScene(launcherScene);
        primaryStage.show();
    }

    private void launchScrabble() {
        Stage scrabbleStage = new Stage();
        disableButtons();
        game = GameFactory.getGame(GameType.SCRABBLE, scrabbleStage);
        scrabbleStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                enableButtons();
                //game.end TODO
            }
        });


        game.play();

        scrabbleStage.show();
    }

    private void enableButtons() {
        scrabbleBtn.setDisable(false);
    }

    //this should be rotating through a collection if I have a huge game launcher for now its not needed
    private void disableButtons() {
        scrabbleBtn.setDisable(true);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
