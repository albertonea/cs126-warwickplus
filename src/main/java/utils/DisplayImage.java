package utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class DisplayImage extends JPanel{
    private BufferedImage rawImage;
    private Image image;
    private int boundary = 0;
    public enum ImageType {Movie, Person};
    private boolean isUnknown = false;

    public DisplayImage(String urlEnding, ImageType type){
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
        }
        catch (IOException e){
            //Unable to find the unknown file
            System.err.println("Unable to find the appropriate 'unknown' image for " + type);
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }
    }

    public DisplayImage(String fileName) throws IOException {
        rawImage = ImageIO.read(new File(fileName));
    }

    public DisplayImage(String fileName, int boundary, boolean URL) throws IOException {
        if (URL) {
            rawImage = ImageIO.read(new URL(fileName));
        } else {
            rawImage = ImageIO.read(new File(fileName));
        }
        this.boundary = boundary;
    }

    public boolean isUnknown(){ return isUnknown; }

    @Override
    protected void paintComponent(Graphics g) {
        if (this.getWidth() > this.getHeight()) {
            image = rawImage.getScaledInstance(-1, this.getHeight(), Image.SCALE_SMOOTH);
        } else {
            image = rawImage.getScaledInstance(this.getWidth(), -1, Image.SCALE_SMOOTH);
        }

        if (image.getWidth(this.getParent()) > this.getWidth()) {
            image = rawImage.getScaledInstance(this.getWidth(), -1, Image.SCALE_SMOOTH);
        }

        if (image.getHeight(this.getParent()) > this.getHeight()) {
            image = rawImage.getScaledInstance(-1, this.getHeight(), Image.SCALE_SMOOTH);
        }

        image = rawImage.getScaledInstance(image.getWidth(this.getParent())-boundary, image.getHeight(this.getParent())-boundary, Image.SCALE_SMOOTH);

        int xPos = (this.getWidth() / 2) - (image.getWidth(this.getParent()) / 2);
        int yPos = (this.getHeight() / 2) - (image.getHeight(this.getParent()) / 2);
        g.drawImage(image, xPos, yPos, this);
    }
}
