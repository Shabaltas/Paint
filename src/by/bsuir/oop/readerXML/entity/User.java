package by.bsuir.oop.readerXML.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String uid;
    private String name;
    private String email;
    private String city;
    private String address;
    private String phone;
    private Gender male;
    private List<String> skills;

    public User(String uid, String name, String email, String city, String address, String phone, Gender male, List<String> skills) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.city = city;
        this.address = address;
        this.phone = phone;
        this.male = male;
        this.skills = new ArrayList<>();
        this.skills.addAll(skills);
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public Gender getMale() {
        return male;
    }

    public List<String> getSkills() {
        return skills;
    }

    public enum Gender{FEMALE, MALE}

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", male=" + male +
                ", skills=" + skills +
                '}';
    }
}
