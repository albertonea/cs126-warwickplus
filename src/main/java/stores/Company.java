package stores;

import interfaces.ICompany;

public class Company implements ICompany {

    private int id;
    private String name;

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
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
    public int compareTo(ICompany company) {
        return ((Integer) id).compareTo(company.getID());
    }
    
}
