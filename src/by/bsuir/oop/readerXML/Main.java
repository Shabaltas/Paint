package by.bsuir.oop.readerXML;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler();
        parser.parse(new File("data\\SAXfiles\\feed.xml"), handler);
        List<User> users = handler.getListUsers();
        for (User user : users){
            System.out.println(user);
        }
        System.out.println("Amount of all users: " + handler.getAmount());
        System.out.println("Amount of suitable users: " + users.size());
    }
}
