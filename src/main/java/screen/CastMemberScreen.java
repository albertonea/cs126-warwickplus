package screen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import interfaces.AbstractStores;
import interfaces.IPerson;
import ui.MovieReel;
import utils.Constants;
import utils.DisplayImage;
import utils.DisplayImage.ImageType;



//A Screen for information about a specific cast member, like film screen is for Movies
public class CastMemberScreen {
    public static void createPanel(JPanel panel, int castID, AbstractStores stores) {

        IPerson castMember = stores.getCredits().getCast(castID);
        if (castMember == null){
        }
        System.out.println("Cast Member screen --> ID: " + castID);
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Constants.getHighlight());
        panel.setForeground(Constants.getFontColor());
        GridBagConstraints topConstraints = new GridBagConstraints();
        topConstraints.insets = new Insets(5,5,5,5); //Add space between areas

        topConstraints.weightx = 1.0;
        topConstraints.weighty = 1.0;
        topConstraints.fill = GridBagConstraints.BOTH;

        topConstraints.gridx = 0;
        topConstraints.gridy = 0;
        JPanel leftSide = new JPanel();
        leftSide.setBackground(Constants.getHighlight());
        leftSide.setForeground(Constants.getFontColor());
        leftSide.setPreferredSize(new Dimension(100,100));
        panel.add(leftSide, topConstraints);

        topConstraints.gridx = 1;
        topConstraints.weightx = 0.2;
        JPanel profileInfo = new JPanel();
        profileInfo.setPreferredSize(new Dimension(100,100));
        profileInfo.setBackground(Constants.getHighlight());
        profileInfo.setForeground(Constants.getFontColor());
        panel.add(profileInfo, topConstraints);


        leftSide.setLayout(new GridBagLayout());
        GridBagConstraints leftConstraints = new GridBagConstraints();
        //All components should fill the left side horizontally
        leftConstraints.anchor = GridBagConstraints.NORTH;
        leftConstraints.gridy = 0;
        leftConstraints.weightx = 1.0;
        leftConstraints.fill = GridBagConstraints.HORIZONTAL;
        //Create Cast title
        JTextPane title = new JTextPane();
        Document titleDoc = title.getStyledDocument();

        try{
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            StyleConstants.setFontSize(attributeSet, 32);
            StyleConstants.setBold(attributeSet, true);
            titleDoc.insertString(titleDoc.getLength(), castMember.getName() + " ", attributeSet);
        } catch(BadLocationException e) {

        } catch(NullPointerException e) {
            System.err.println("===== Cast Member is null! =====");
            e.printStackTrace();
        }

        title.setBounds(5,5,(int)(panel.getWidth()*0.8)-10, 40);
        title.setForeground(Constants.getFontColor());
        title.setBackground(Constants.getHighlight());
        title.setEditable(false);
        leftSide.add(title, leftConstraints);
        System.out.println("\tName built");


        leftConstraints.gridy = 1;
        MovieReel appearsInReel = new CastMemberAppearsInReel(panel, stores, castID);
        appearsInReel.setSize(new Dimension(100, 300)); 
        appearsInReel.setMinimumSize(new Dimension(1,300));
        SwingUtilities.invokeLater(appearsInReel);
        leftSide.add(appearsInReel, leftConstraints);

        MovieReel starsInReel = new CastMemberStarsInReel(panel, stores, castID);
        starsInReel.setMinimumSize(new Dimension(100,300));
        starsInReel.setSize(new Dimension(100,300));
        SwingUtilities.invokeLater(starsInReel);
        leftConstraints.gridy = 2;
        leftSide.add(starsInReel, leftConstraints);

        profileInfo.setLayout(new GridBagLayout());
        GridBagConstraints profileInfoConstraints = new GridBagConstraints();
        profileInfoConstraints.weightx = 1.0;
        profileInfoConstraints.fill = GridBagConstraints.HORIZONTAL;
        profileInfoConstraints.gridx = 0;
        profileInfoConstraints.gridy = 0;
        profileInfoConstraints.anchor = GridBagConstraints.NORTH;

        DisplayImage castProfile = new DisplayImage(castMember.getProfilePath(), ImageType.Person);

        if (castProfile != null) {
            castProfile.setPreferredSize(new Dimension(1000,1000));
            castProfile.setMinimumSize(new Dimension(350,350));
            if (castProfile.isUnknown()) {
                castProfile.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            } 
            profileInfo.add(castProfile, profileInfoConstraints);
        }
        System.out.println("\tProfile picture built");

        //Add overview
        TitledBorder keyFactsBorder;
        keyFactsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Key Facts");
        keyFactsBorder.setTitleJustification(TitledBorder.LEFT);
        keyFactsBorder.setTitleColor(Constants.getFontColor());
        int id = castMember.getID();
        String keyFactsText = 
        "CastID: " + id + "\n" +
        "Appears in " + stores.getCredits().getCastFilms(id).length + " films. \n";
        
        JTextArea keyFacts = new JTextArea(keyFactsText);
        keyFacts.setForeground(Constants.getFontColor());
        keyFacts.setBackground(Constants.getHighlight());
        keyFacts.setLineWrap(true);
        keyFacts.setWrapStyleWord(true);
        keyFacts.setBounds(5, 55, (int) (panel.getWidth() * 0.8) - 10, (int) (panel.getHeight() * 0.2));
        keyFacts.setBorder(keyFactsBorder);
        keyFacts.setEditable(false);
        System.out.println("\tKey Facts built");


        profileInfoConstraints.gridy = 1;
        profileInfoConstraints.weighty = 1.0;
        profileInfo.add(keyFacts, profileInfoConstraints);

    }
}

class CastMemberAppearsInReel extends MovieReel {
    int castID;
    public CastMemberAppearsInReel(JPanel screenPanel, AbstractStores stores, int castID){
        super(screenPanel, stores, "Appears In", "Searching For Movies that castID "+ castID +" appears in...");
        this.castID = castID;
    }

    public void run(){
        System.out.println("Running 'cast appears in' reel");
        int[] movieResults = stores.getCredits().getCastFilms(castID);
        int length = movieResults.length;

        if (movieResults == null || length == 0){
            System.out.println("\tNo movies found");
        }

        String[] labels = new String[length];
        for (int i = 0; i < length; i++){
            labels[i] = stores.getMovies().getTitle(movieResults[i]);
        }
        displayItems(movieResults, labels);
        System.out.println("Finished running Appears In");
    }
}

class CastMemberStarsInReel extends MovieReel {
    int castID;
    public CastMemberStarsInReel(JPanel screenPanel, AbstractStores stores, int castID){
        super(screenPanel, stores, "Stars In", "Searching For Movies that castID "+ castID +" stars in...");
        this.castID = castID;
    }

    public void run(){
        System.out.println("Running 'cast stars in' reel for castID: " + castID);
        int[] movieResults = stores.getCredits().getCastStarsInFilms(castID);
        int length = movieResults.length;

        if (movieResults == null || length == 0){
            System.out.println("\tNo movies found");
        }
        String[] labels = new String[length];
        for (int i = 0; i < length; i++){
            labels[i] = stores.getMovies().getTitle(movieResults[i]);
        }
        displayItems(movieResults, labels);
        System.out.println("Finished running Stars In");
    }
}