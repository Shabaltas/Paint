package by.bsuir.oop.readerXML;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class XMLHandler extends DefaultHandler {
    private boolean hasSkill = false;
    private String skillregex = "^(CodeIgniter)|(CSS3)|(Sinatra)$";
    private List<User> listUsers = new ArrayList<>();
    private List<String> skills = new ArrayList<>();
    private HashMap<String, String> values = new HashMap<>();
    private String tag;
    private int amount = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tag = qName;
        if ("user".equals(tag)) amount++;
    }

    @Override
    public void characters (char[] ch, int start, int length) throws SAXException {
        String information = new String(ch, start, length);
        information = information.replace("\n", "").trim();
        if (!"".equals(information)){
            if  (("skill".equals(tag))) {
                skills.add(information);
                //hasSkill = hasSkill || Pattern.matches(skillregex, information);
                hasSkill = hasSkill || "CodeIgniter".equals(information) || "CSS3".equals(information) || "Sinatra".equals(information);
            }
            values.put(tag, information);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("user".equals(qName) ){
            if ( hasSkill) {
                listUsers.add(new User(values.get("uid"), values.get("name"), values.get("email"),
                        values.get("city"), values.get("address"), values.get("phone"), User.Gender.valueOf(values.get("gender")), skills));
            }
            hasSkill = false;
            values.clear();
            skills.clear();
        }
    }

    public List<User> getListUsers(){
        return this.listUsers;
    }

    public int getAmount(){
        return this.amount;
    }
}

