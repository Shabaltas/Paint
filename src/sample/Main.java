package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Logger.getLogger(Main.class).info("start");
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("LinaPaint");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.setOnCloseRequest(we -> Logger.getLogger(Main.class).info("end"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
