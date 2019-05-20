package by.bsuir.oop.paint.configuration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConfigController {

    private static final org.apache.log4j.Logger LOGGER = Logger.getLogger(ConfigController.class.getSimpleName());
    private static final String FILENAME = "data\\configuration\\Configuration.xml";
    @FXML
    public MenuButton btnLanguage;
    @FXML
    public TextField fieldExtension;
    @FXML
    public TextField fieldWidth;
    @FXML
    public TextField fieldHeight;

    public void saveConfig() {
        Configuration config = new Configuration();
        config.setExtension(fieldExtension.getText());
        config.setHeight(Integer.valueOf(fieldHeight.getText()));
        config.setWidth(Integer.valueOf(fieldWidth.getText()));
        config.setLanguage(btnLanguage.getText());
        try (FileOutputStream encoder = new FileOutputStream(FILENAME)) {
            JAXBContext context = JAXBContext.newInstance(Configuration.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.marshal(config, encoder);
        } catch (IOException | JAXBException e) {
            LOGGER.warn(e);
        }
    }

    public static Configuration readFromXML(){
        Configuration config;
            try (FileInputStream decoder = new FileInputStream(FILENAME)) {
                JAXBContext context = JAXBContext.newInstance(Configuration.class);
                Unmarshaller um = context.createUnmarshaller();
                config = (Configuration) um.unmarshal(decoder);
                return config;
            } catch (JAXBException | IOException e){
                config = new Configuration();
                config.setLanguage("english");
                config.setWidth(1200);
                config.setHeight(800);
                config.setExtension(".class");
               return config;
            }
    }

    public void changeLanguage(ActionEvent actionEvent) {
        btnLanguage.setText(((MenuItem)actionEvent.getSource()).getText());
        LOGGER.info("SAVED");
    }
}
