package stores;

import interfaces.IKeyword;

public class Keyword implements IKeyword{

    private int id = -1;
    private String name = null;

    public Keyword(int id, String name){
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
    public int compareTo(IKeyword o) {
        return ((Integer) id).compareTo(o.getID());
    }
    
}
