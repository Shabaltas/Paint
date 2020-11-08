package by.bsuir.oop.readerXML;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

import by.bsuir.oop.readerXML.entity.User;
import by.bsuir.oop.readerXML.handler.XMLHandler;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class Main {

    private static final String PATH = "data\\SAXfiles\\Banks.xml";

    private static final Logger LOGGER = Logger.getLogger(Main.class);
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler();
        parser.parse(new File(PATH), handler);
        List<User> users = handler.getListUsers();
        for (User user : users){
            LOGGER.info(user);
        }
        LOGGER.info("Amount of all users: " + handler.getAmount());
        LOGGER.info("Amount of suitable users: " + users.size());
    }
}
