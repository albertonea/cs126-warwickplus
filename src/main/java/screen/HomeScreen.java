package screen;

import java.awt.Dimension;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import interfaces.*;
import stores.Person;
import ui.MovieReel;
import utils.Constants;

public class HomeScreen {
    public static void createPanel(JPanel panel, AbstractStores stores) {
        System.out.println("Home screen");
        panel.setVisible(false);
        panel.removeAll();

        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        
        //Fill space, and use non-zero weights so that fill works
        constraints.weightx = 2.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        Insets insets = new Insets(5,5,5,5); //Add space around reels
        constraints.insets = insets;

        //Make the reels 3 grid units wide, in the leftmost position
        int reelsWidth = 1;
        constraints.gridwidth = reelsWidth;
        constraints.gridx = 0;
        constraints.gridy = 0;

        //The reels that will be scrolled
        JPanel reels = new JPanel();
        reels.setBorder(BorderFactory.createEmptyBorder());
        
        //Make layout vertically stacked
        reels.setLayout(new BoxLayout(reels, BoxLayout.Y_AXIS));
        
        MovieReel topAverageRatedMovies = new TopAverageRatedMovies(panel, stores);
        topAverageRatedMovies.setSize(new Dimension(reels.getWidth(), (int) (reels.getHeight() * 0.3)));
        reels.add(topAverageRatedMovies);
        SwingUtilities.invokeLater(topAverageRatedMovies);
        
        MovieReel mostKeywordMovies = new MostKeywordMovies(panel, stores);
        reels.add(mostKeywordMovies);
        SwingUtilities.invokeLater(mostKeywordMovies);


        JScrollPane reelsScrollPane = new JScrollPane(reels);
        reelsScrollPane.setBackground(Constants.getHighlight());
        reelsScrollPane.setForeground(Constants.getFontColor());
        reelsScrollPane.setBorder(BorderFactory.createEmptyBorder());

        reels.setSize(new Dimension(reelsScrollPane.getWidth(), reelsScrollPane.getHeight()));

        //Restrict the reels pane (which has a vertical stack of reels) to be the width of the pane scrolling it
        //This stops the reels scrollpane within it from just being really wide and not having a scroll bar,
        //where the reelsScrollPane just started scrolling everything
        reels.setPreferredSize(new Dimension(reelsScrollPane.getWidth(), reelsScrollPane.getHeight()));

        panel.add(reelsScrollPane, constraints);


        // Get the top 100 users with the most ratings & display their UID, average rating and number of ratings. Ordered by number of reviews, descending.
        TitledBorder mostCastCreditsBorder;
        mostCastCreditsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Most Cast Credits");
        mostCastCreditsBorder.setTitleJustification(TitledBorder.CENTER);
        mostCastCreditsBorder.setTitleColor(Constants.getFontColor());


        JPanel mostCastCreditsInner = new JPanel(); //The panel that is getting scrolled around
        mostCastCreditsInner.setBackground(Constants.getHighlight());
        mostCastCreditsInner.setForeground(Constants.getFontColor());
        JScrollPane mostCastCreditsScrollPane = new JScrollPane(mostCastCreditsInner);
        mostCastCreditsScrollPane.setBackground(Constants.getHighlight());
        mostCastCreditsScrollPane.setForeground(Constants.getFontColor());
        mostCastCreditsScrollPane.getVerticalScrollBar().setUnitIncrement(40);
        mostCastCreditsScrollPane.setBorder(mostCastCreditsBorder);

        MostCastCredits mostCastCreditsRunnable = new MostCastCredits(panel, mostCastCreditsScrollPane, mostCastCreditsInner, stores);


        constraints.insets = new Insets(5,5,5,5); //Add spacing around the panel
        //Make the most rated users list one 'grid cell' wide, and in the location after the 3-wide reels
        constraints.weightx = 0.2;
        constraints.gridwidth = 1;
        constraints.gridx = 1; //place this in the grid cell to the right of the reels



        panel.add(mostCastCreditsScrollPane, constraints);

        panel.setVisible(true);

        SwingUtilities.invokeLater(mostCastCreditsRunnable);
    }
}

class MostCastCredits implements Runnable {

    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private JLabel loadingText;

    public MostCastCredits(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching Most Cast Credits...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        System.out.println("Running Most Cast Credits Users");
        Person[] mostCastCredits = stores.getCredits().getMostCastCredits(Constants.mostUserRatingCount);

        scrollPane.setVisible(false);

        if (mostCastCredits == null || mostCastCredits.length == 0) {
            loadingText.setText("No cast credits found!");
            System.out.println("\tNo cast credits found");
            scrollPane.setVisible(true);
            return;
        } else {
            loadingText.setText("Processing " + mostCastCredits.length + " Cast Credits...");
            System.out.println("\t" + mostCastCredits.length + " Cast Credits found (max: " + Constants.mostUserRatingCount + ")");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setSize(scrollPane.getWidth() - 40, mostCastCredits.length * (itemHeight + ((new JSeparator()).getHeight())));

        for (int i = 0; i < mostCastCredits.length; i++) {
            JPanel resultItem = new JPanel();
            resultItem.setSize(scrollPane.getWidth() - 40, itemHeight);

            int numCredits = stores.getCredits().getNumCastCredits(mostCastCredits[i].getID());
            String personNameAndID = "ID: "+ mostCastCredits[i].getID() +"\t Name: " + mostCastCredits[i].getName();
            if (numCredits < 0){
                loadingText.setText("No number of cast credits found: Received number less than 0! (For Person "+ personNameAndID);
                System.out.println("\tNo number of cast credits found: Received number less than 0!(For Person "+ personNameAndID);
                scrollPane.setVisible(true);
                return;
            }
            
            String resultString = personNameAndID + " → " + numCredits;

            JTextArea title = new JTextArea(resultString);
            title.setBounds(0, (itemHeight * i), resultsPanel.getWidth(), itemHeight);
            title.setForeground(Constants.getFontColor());
            title.setBackground(Constants.getHighlight());
            title.setEditable(false);
            title.setLineWrap(true);
            title.setWrapStyleWord(true);

            resultItem.add(title);
            resultItem.setBackground(Constants.getHighlight());
            resultItem.setForeground(Constants.getFontColor());

            resultsPanel.add(resultItem);

            if (i < mostCastCredits.length - 1) {
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPanel.add(sep);
            }
        }
        scrollPane.setVisible(true);
        System.out.println("Finished running Most Cast Credits Users");
    }
}

class TopAverageRatedMovies extends MovieReel {
    public TopAverageRatedMovies(JPanel screenPanel, AbstractStores stores){
        super(screenPanel, stores, "Top Average Rated Movies", "Searching Top Average Rated Movies...");
    }

    public void run(){
        System.out.println("Running Top Average Rated Movie Reel");
        int length = Constants.topMoviesCount;
        int[] movieResults = stores.getRatings().getTopAverageRatedMovies(length);

        if (movieResults == null || movieResults.length == 0){
            String message = "No top average rated movie ratings found";
            System.out.println("\t" + message);
            this.add(new JLabel(message));
        }
        else{
            String[] labels = new String[length];
            for (int i = 0; i < length; i++){
                labels[i] = String.format("%s (%.2f ★)", stores.getMovies().getTitle(movieResults[i]),
                                                     stores.getRatings().getMovieAverageRating(movieResults[i]));
            }
            displayItems(movieResults, labels);
            System.out.println("Finished running Top Average Rated Movie Reel");
        }
    }
}

class MostKeywordMovies extends MovieReel {
    public MostKeywordMovies(JPanel screenPanel, AbstractStores stores){
        super(screenPanel, stores, "Most Keywords Movies", "Searching Most Keywords Movies...");
    }

    public void run(){
        System.out.println("Running Most Keywords Movie Reel");
        int length = Constants.topMoviesCount;
        int[] movieResults = stores.getKeywords().getMostKeywordFilms(length);

        if (movieResults == null || movieResults.length == 0){
            System.out.println("\tNo most keyword movie ratings found");
        }
        else{
            String[] labels = new String[length];
            for (int i = 0; i < length; i++){
                labels[i] = String.format("%s (%d)", stores.getMovies().getTitle(movieResults[i]),
                                                         stores.getKeywords().getKeywordsForFilm(movieResults[i]).length);
            }
            displayItems(movieResults, labels);
            System.out.println("Finished running Top Rated Movie Reel");
        }
    }
}
