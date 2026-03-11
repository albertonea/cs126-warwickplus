package stores;

import interfaces.*;

public class Stores extends AbstractStores{
    public Stores(){
        credits  = new Credits(this);
        keywords = new Keywords(this);
        movies   = new Movies(this);
        ratings  = new Ratings(this);
    }
}
