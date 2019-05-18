package by.bsuir.oop.paint.sample;

import by.bsuir.oop.paint.configuration.*;
import by.bsuir.oop.paint.configuration.language.English;
import by.bsuir.oop.paint.configuration.language.Language;
import by.bsuir.oop.paint.configuration.language.Russian;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

public class Main extends Application {

    Configuration config ;
    static String extension;
    static Language language;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Logger.getLogger(Main.class).info("start");
        config = ConfigController.readFromXML();
        System.out.println(config);
        extension = config.getExtension();
        if ("english".equals(config.getLanguage())){
            language = new English();
        }else{
            language = new Russian();
        }
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("LinaPaint");
        primaryStage.setScene(new Scene(root, config.getWidth(), config.getHeight()));
        primaryStage.setOnCloseRequest(we -> Logger.getLogger(Main.class).info("end"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
