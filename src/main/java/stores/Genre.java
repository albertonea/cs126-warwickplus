package stores;

import interfaces.IGenre;

public class Genre implements IGenre{

    private int id;
    private String name;

    public Genre(int id, String name){
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
    public int compareTo(IGenre genre) {
        return ((Integer) id).compareTo(genre.getID());
    }
    
}
