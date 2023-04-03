package softwaredesign;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    // Current clicks
    int curr_clicks = 0;
    // Max amount of clicks to pass
    int max_clicks = 100;
    // Time it will take for the game to finish
    int timeout_duration = 60;

    // Sound Logic
    int prev_index = 0;

    // Init UI elements
    Arc progressArc;
    Timeline timeline;

    // Init FXML elements
    @FXML
    private Group progressBar;
    @FXML
    private ImageView CookieID;
    @FXML
    private Text StatusText;
    @FXML
    private MediaView mediaView;

    private ArrayList<String> CookieImages = new ArrayList<String>(Arrays.asList(
            "Cookie_TwentyFive.png",
            "Cookie_Fifty.png",
            "Cookie_SeventyFive.png",
            "Cookie_Full.png"
    ));

    @FXML private void handleOnMouseClicked(MouseEvent event)
    {
        if(((ImageView)event.getSource()).getId().equals("CookieID")){
            curr_clicks++;
            processClicks();
        }
    }

    private void processClicks(){
        int image_index = Math.max(0, (int)Math.ceil((max_clicks - curr_clicks) / (double)(max_clicks / 4)) - 1);

        if (image_index != prev_index){
            prev_index = image_index;
            PlayEatSound();
        }
        Image newImage = new Image(CookieImages.get(image_index));

        // Start the timer on the first click
        if(curr_clicks == 1){
            StatusText.setText("KEEP CLICKING TILL THE TIME RUNS OUT");
            startTimer();
        }
        else if (curr_clicks == max_clicks){
            CookieID.setVisible(false);
            timeline.stop();
            showEndScreen();
        }else{
            CookieID.setImage(newImage);
        }


    }

    private void PlayEatSound(){
        AudioClip clip = null;
        try {
            clip = new AudioClip(getClass().getResource("/Music/SoftEat.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        clip.play();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            //Note: The song is https://youtu.be/xc_0wfIuuzw
            String fileName = getClass().getResource("/music/Music.mp3").toURI().toString();
            Media media = new Media(fileName);
            MediaPlayer player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.2);
            mediaView.setMediaPlayer(player);

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mediaView.getMediaPlayer().play();

        double centerX = 250;
        double centerY = 145;
        double radius = 180;
        double strokeWidth = 15;
        Color backgroundColor = Color.LIGHTGRAY;
        Color progressColor = Color.BLUE;

        Circle background = new Circle(centerX, centerY, radius);
        background.setStrokeWidth(strokeWidth);
        background.setStroke(backgroundColor);
        background.setFill(null);

        double startAngle = 90;
        double angleExtent = 0;
        progressArc = new Arc(centerX, centerY, radius, radius, startAngle, angleExtent);
        progressArc.setStrokeWidth(strokeWidth);
        progressArc.setStroke(progressColor);
        progressArc.setFill(null);

        progressBar.getChildren().addAll(background, progressArc);
    }

    private void startTimer() {
        double angleExtent = 360;
        Duration duration = Duration.seconds(timeout_duration);

        KeyValue keyValue = new KeyValue(progressArc.lengthProperty(), angleExtent, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(duration, keyValue);

        timeline = new Timeline(keyFrame);
        timeline.setOnFinished(event -> {
            showEndScreen();
        });

        timeline.play();
    }

    private void showEndScreen(){
        String finalMessage = "Sorry, gotta click faster";
        if (curr_clicks == max_clicks){
            finalMessage = "You won, you click fast";
        }

        // show dialog when timeline is finished
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Game Finished");
        dialog.setHeaderText(finalMessage + "\nWould you like to play again or go back to the previous screen?");
        ButtonType buttonRetry = new ButtonType("Retry", ButtonBar.ButtonData.YES);

        //Initially I planned to include a going back to the menu button but since we couldn't merge
        //my project with my group-mates' codes I eliminated it.

        //ButtonType buttonBack = new ButtonType("Go Back", ButtonBar.ButtonData.NO);
        //dialog.getDialogPane().getButtonTypes().addAll(buttonRetry, buttonBack);
        dialog.getDialogPane().getButtonTypes().addAll(buttonRetry);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonRetry) {
                restartGame();
            }
            //else if (dialogButton == buttonBack) {
              //  goBackToMenu();
            //}
            return null;
        });
        dialog.show();
    }

    private void restartGame() {
        curr_clicks = 0;
        progressArc.setLength(0);
        Image newImage = new Image(CookieImages.get(3));
        StatusText.setText("CLICK ON THE COOKIE TO START");
        CookieID.setImage(newImage);
        CookieID.setVisible(true);
        timeline.getKeyFrames().clear();
    }

    private void goBackToMenu() {
        // handle "No" button press event
    }
}