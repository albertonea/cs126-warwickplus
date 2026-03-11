package stores;

import interfaces.IPerson;

public class Person implements IPerson{
    private int id;
    private String name;
    private String profilePath;
    public Person(int id, String name, String profilePath){
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getProfilePath() {
        return profilePath;
    }
}
