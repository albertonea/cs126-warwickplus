package ui;

import javax.swing.JPanel;

import java.awt.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import stores.CastCredit;
import stores.Credits;

public class CastReel extends AbstractReel {

    private CastCredit cast;

    public CastReel(JPanel screenPanel, CastCredit cast, Credits credits, 
                    String reelTitle, String loadingMessage) {
        super(screenPanel, reelTitle, loadingMessage);
        this.cast = cast;
    }

    @Override
    protected Image getImage(int castId){
        String castImageURL = cast.getProfilePath();
        try{
            Image unknown = ImageIO.read(new File("src/main/resources/img/Person-Unknown.png"));

            if (castImageURL == null || castImageURL.equals("")){
                System.err.println("Cast image URL was null or empty");
                return unknown;
            }

            String completeURL = "https://image.tmdb.org/t/p/w500" + castImageURL;
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
    protected void itemClickAction(int castId){
        //Create a CastPanel with the cast id.
    }
    
}