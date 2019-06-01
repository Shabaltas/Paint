package by.bsuir.oop.readerXML.handler;

import by.bsuir.oop.readerXML.entity.Tag;
import by.bsuir.oop.readerXML.entity.User;
import by.bsuir.oop.readerXML.exception.InvalidTagException;
import by.bsuir.oop.readerXML.validator.XMLValidator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

public class XMLHandler extends DefaultHandler {
    private static final String CODEIGNITER = "CODEIGNITER";
    private static final String CSS3 = "CSS3";
    private static final String SINATRA = "SINATRA";
    private boolean hasSkill = false;
    private String skillregex = "^(CODEIGNITER)|(CSS3)|(SINATRA)$";
    private List<User> listUsers = new ArrayList<>();
    private List<String> skills = new ArrayList<>();
    private EnumMap<Tag, String> values = new EnumMap<>(Tag.class);
    private Tag tag;
    private int amount = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        String qNameUp = qName.toUpperCase();
        if (new XMLValidator().isEnumValid(Tag.class, qNameUp)) {
            tag = Tag.valueOf(qNameUp);
            if (Tag.USER.equals(tag)) amount++;
        } else {
            throw new SAXException(new InvalidTagException(qName));
        }
    }

    @Override
    public void characters (char[] ch, int start, int length) throws SAXException {
        String information = new String(ch, start, length);
        information = information.replace("\n", "").trim().toUpperCase();
        if (!"".equals(information)){
            if  (Tag.SKILL == tag) {
                skills.add(information);
                //hasSkill = hasSkill || Pattern.matches(skillregex, information);
                hasSkill = hasSkill || "LOL".equals(information);
                //hasSkill = hasSkill || CODEIGNITER.equals(information) || CSS3.equals(information) || SINATRA.equals(information);
            }
            values.put(tag, information);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
        String qNameUp = qName.toUpperCase();
        if (new XMLValidator().isEnumValid(Tag.class, qNameUp)) {
            if (Tag.USER.toString().equals(qNameUp)) {
                if (hasSkill) {
                    listUsers.add(new User(values.get(Tag.UID), values.get(Tag.NAME), values.get(Tag.EMAIL),
                            values.get(Tag.CITY), values.get(Tag.ADDRESS), values.get(Tag.PHONE),
                            User.Gender.valueOf(values.get(Tag.GENDER)), skills));
                }
                hasSkill = false;
                values.clear();
                skills.clear();
            }
        } else {
            throw new SAXException(new InvalidTagException(qName));
        }
    }

    public List<User> getListUsers(){
        return this.listUsers;
    }

    public int getAmount(){
        return this.amount;
    }
}

