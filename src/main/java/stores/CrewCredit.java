package stores;

import interfaces.ICrewCredit;

public class CrewCredit implements interfaces.ICrewCredit {

    private String elementID = null;
    private String department = null;
    private int id = -1;
    private String job = null;
    private String name = null;
    private String profilePath = null;

    public CrewCredit(String elementID, String department, int id, String job, String name, String profilePath) {
        this.elementID = elementID;
        this.department = department;
        this.id = id;
        this.job = job;
        this.name = name;
        this.profilePath = profilePath;
    }

    @Override
    public String getElementID() {
        return elementID;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getJob() {
        return job;
    }

    @Override
    public String getName() {
        return name;
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
    public int compareTo(ICrewCredit o) {
        return ((Integer) id).compareTo(o.getID());
    }

    @Override
    public String toString() {
        return "Element ID: " + elementID + "\tName: " + name + "\tDepartment : " + department + "\tJob: " + job + "\tID: " + id + "\tProfile Path: " + profilePath;
    }
    
}
