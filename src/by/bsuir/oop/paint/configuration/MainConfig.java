package by.bsuir.oop.paint.configuration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import by.bsuir.oop.paint.sample.Main;

public class MainConfig extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Logger.getLogger(Main.class).info("start");
        Parent root = FXMLLoader.load(getClass().getResource("configWindow.fxml"));
        primaryStage.setTitle("Configuration");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(we -> Logger.getLogger(MainConfig.class).info("end"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
