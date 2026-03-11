package ui;

import javax.swing.JPanel;

import screen.FilmScreen;

import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import interfaces.AbstractStores;

public class MovieReel extends AbstractReel{
    protected AbstractStores stores;

    public MovieReel(JPanel screenPanel, AbstractStores stores,
                     String reelTitle, String loadingMessage)
    {
        super(screenPanel, reelTitle, loadingMessage);
        this.stores = stores;

    }
    
    public MovieReel(JPanel screenPanel, AbstractStores stores)
    {
        super(screenPanel, "Movie Reel", "Loading Movie Reel");
        this.stores = stores;
    }
    
    // REFERENCE: https://stackoverflow.com/questions/21587309/positioning-jlabel-in-jpanel-below-the-image
    // StackOverFlow, user 'MadProgrammer'
    // date: 5/2/2014
    // Date accessed ~ January 25th 2023
    @Override
    protected Image getImage(int movieId){
        try{
            String posterEndURL = stores.getMovies().getPoster(movieId);
            Image unknown = ImageIO.read(new File("src/main/resources/img/Movie-Unknown-poster.png"));
            if (posterEndURL == null || posterEndURL.equals("")){
                return unknown;
            }

            String completeURL = "https://image.tmdb.org/t/p/w342" + posterEndURL;
            Image im;
            try{
                im = ImageIO.read(new URL(completeURL));
                if (im == null){ // File not found at URL
                    im = unknown;
                }
            }
            catch (IOException e){
                im = unknown;
            }

            return im;

        }
        catch (IOException e){
            return null;

        }
        
    }

    @Override
    protected void itemClickAction(int movieId) {
        FilmScreen.createPanel(super.screenPanel, movieId, stores);
    }
}
