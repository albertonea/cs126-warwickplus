package screen;

import java.awt.Dimension;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import interfaces.AbstractStores;
import ui.MovieReel;
import utils.Constants;



//A Screen for information about a specific cast member, like film screen is for Movies
public class CollectionScreen {
    public static void createPanel(JPanel panel, int collectionID, AbstractStores stores) {
        panel.removeAll();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Constants.getHighlight());
        panel.setForeground(Constants.getFontColor());
        System.out.println("Collection screen --> ID: " + collectionID);

        MovieReel collectionReel = new CollectionReel(panel, stores, collectionID);
        collectionReel.setPreferredSize(new Dimension(1,300));
        collectionReel.setSize(new Dimension(1,300));
        collectionReel.setMinimumSize(new Dimension(1,300));
        SwingUtilities.invokeLater(collectionReel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;

        //Create Cast title
        JTextPane title = new JTextPane();
        Document titleDoc = title.getStyledDocument();

        try{
            SimpleAttributeSet attributeSet = new SimpleAttributeSet();
            StyleConstants.setFontSize(attributeSet, 32);
            StyleConstants.setBold(attributeSet, true);
            titleDoc.insertString(titleDoc.getLength(), stores.getMovies().getCollectionName(collectionID) + " ", attributeSet);
        } catch(BadLocationException e) {

        } catch(NullPointerException e) {
            System.err.println("===== Collection Member is null! =====");
            e.printStackTrace();
        }

        title.setBounds(5,5,(int)(panel.getWidth()*0.8)-10, 40);
        title.setForeground(Constants.getFontColor());
        title.setBackground(Constants.getHighlight());
        title.setEditable(false);
        panel.add(title, constraints);

        constraints.gridy = 1;
        panel.add(collectionReel, constraints);

    }
}

class CollectionReel extends MovieReel {
    int collectionID;
    public CollectionReel(JPanel screenPanel, AbstractStores stores, int collectionID){
        super(screenPanel, stores, "Collection", "Searching for movies in collection " + collectionID + "...");
        this.collectionID = collectionID;
    }

    public void run(){
        System.out.println("Running Collection Reel");
        int[] moviesInCollection = stores.getMovies().getFilmsInCollection(collectionID);
        int length = moviesInCollection.length;

        if (moviesInCollection == null || moviesInCollection.length == 0){
            System.out.println("\tNo movies found in the collection");
        }
        String[] labels = new String[length];
        for (int i = 0; i < length; i++){
            labels[i] = String.format("%s (%.2f ★)", stores.getMovies().getTitle(moviesInCollection[i]),
                                                     stores.getRatings().getMovieAverageRating(moviesInCollection[i]));
        }
        displayItems(moviesInCollection, labels);
        System.out.println("Finished running Collection Reel");
    }
}