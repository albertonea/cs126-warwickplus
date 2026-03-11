package ui;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.imageio.ImageIO;

import java.io.*;
import java.net.URL;
import javax.swing.*;

public class PathImage extends JLabel {
    private BufferedImage rawImage;
    private Image image;
    private int boundary = 0;
    public enum ImageType {Movie, Person};
    private boolean isUnknown = false;

    public PathImage(String urlEnding, ImageType type){
        BufferedImage unknown;
        try{
            switch (type){
                case Movie:
                    unknown = ImageIO.read(new File("src/main/resources/img/Movie-Unknown-poster.png"));
                    break;
                case Person:
                    unknown = ImageIO.read(new File("src/main/resources/img/Person-Unknown.png"));
                    break;
                default:
                    unknown = null;
            }

            if (urlEnding == null || urlEnding.equals("")){
                System.err.println("Unable to load image, ending for image url was null or empty");
                rawImage = unknown;
            }

            //Try and get the image from the url
            String completeURL = "https://image.tmdb.org/t/p/w500" + urlEnding;
            BufferedImage im;
            try {
                im = ImageIO.read(new URL(completeURL));
                if (im == null){
                    im = unknown;
                }
            }
            catch (IOException e){
                im = unknown;
            }

            rawImage = im;

            if (rawImage == unknown){
                boundary = 10;
                isUnknown = true;
                this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
            else {
                boundary = 0;
            }

            setIcon(new ImageIcon(rawImage));
        }
        catch (IOException e){
            //Unable to find the unknown file
            System.err.println("Unable to find the appropriate 'unknown' image for " + type);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }
    }

}
