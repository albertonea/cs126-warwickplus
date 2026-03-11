package stores;

import interfaces.ICastCredit;

public class CastCredit implements interfaces.ICastCredit  {

    private int elementID = -1;
    private String character = null;
    private String creditID = null;
    private int id = -1;
    private String name = null;
    private int order = -1;
    private String profilePath = null;

    public CastCredit (int elementID, String character, String creditID, int id, String name, int order, String profilePath) {
        this.elementID = elementID;
        this.character = character;
        this.creditID = creditID;
        this.id = id;
        this.name = name;
        this.order = order;
        this.profilePath = profilePath;
    }

    @Override
    public int getElementID() {
        return elementID;
    }

    @Override
    public String getCharacter() {
        return character;
    }

    @Override
    public String getCreditID() {
        return creditID;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String getProfilePath() {
        return profilePath;
    }

    @Override
    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;        
    }

    @Override
    public int compareTo(ICastCredit o) {
        return ((Integer) id).compareTo(o.getID());
    }

    @Override
    public String toString() {
        return "Element ID: " + elementID + "\tName: " + name + "\tCharacter: " + character + "\tCredit ID" + creditID + "\tID: " + id + "\tOrder: " + order + "\tProfile Path: " + profilePath;
    }
    
}
