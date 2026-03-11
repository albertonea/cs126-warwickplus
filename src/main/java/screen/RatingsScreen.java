package screen;

import java.awt.Dimension;
import java.awt.*;
import java.text.DecimalFormat;

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
import ui.MovieReel;
import utils.Constants;

public class RatingsScreen {
    public static void createPanel(JPanel panel, AbstractStores stores) {
        IRatings ratings = stores.getRatings();
        System.out.println("Ratings screen");
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
        
        MovieReel mostRatedMovies = new MostRatedMovieReel(panel, stores);
        mostRatedMovies.setSize(new Dimension(reels.getWidth(), (int) (reels.getHeight() * 0.3)));
        reels.add(mostRatedMovies);
        SwingUtilities.invokeLater(mostRatedMovies);
        
        MovieReel topRatedMovies = new TopRatedMovieReel(panel, stores);
        reels.add(topRatedMovies);
        SwingUtilities.invokeLater(topRatedMovies);


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
        TitledBorder mostRatingsUserBorder;
        mostRatingsUserBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Most Ratings from Users");
        mostRatingsUserBorder.setTitleJustification(TitledBorder.CENTER);
        mostRatingsUserBorder.setTitleColor(Constants.getFontColor());


        JPanel mostRatingsUserInner = new JPanel(); //The panel that is getting scrolled around
        mostRatingsUserInner.setBackground(Constants.getHighlight());
        mostRatingsUserInner.setForeground(Constants.getFontColor());
        JScrollPane mostRatingsUserScrollPane = new JScrollPane(mostRatingsUserInner);
        mostRatingsUserScrollPane.setBackground(Constants.getHighlight());
        mostRatingsUserScrollPane.setForeground(Constants.getFontColor());
        mostRatingsUserScrollPane.getVerticalScrollBar().setUnitIncrement(40);
        mostRatingsUserScrollPane.setBorder(mostRatingsUserBorder);

        MostRatedUsersRunnable mostRatedUsersRunnable = new MostRatedUsersRunnable(panel, mostRatingsUserScrollPane, mostRatingsUserInner, ratings);


        constraints.insets = new Insets(5,5,5,5); //Add spacing around the panel
        //Make the most rated users list one 'grid cell' wide, and in the location after the 3-wide reels
        constraints.weightx = 0.2;
        constraints.gridwidth = 1;
        constraints.gridx = 1; //place this in the grid cell to the right of the reels



        panel.add(mostRatingsUserScrollPane, constraints);

        panel.setVisible(true);

        SwingUtilities.invokeLater(mostRatedUsersRunnable);
    }
}

class MostRatedUsersRunnable implements Runnable {

    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private IRatings ratings;
    private JLabel loadingText;

    public MostRatedUsersRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, IRatings ratings) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.ratings = ratings;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching Top Users...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        System.out.println("Running Most Rated Users");
        int[] userResults = ratings.getMostRatedUsers(Constants.mostUserRatingCount);

        scrollPane.setVisible(false);

        if (userResults == null || userResults.length == 0) {
            loadingText.setText("No user ratings found!");
            System.out.println("\tNo user ratings found");
            scrollPane.setVisible(true);
            return;
        } else {
            loadingText.setText("Processing " + userResults.length + " Users...");
            System.out.println("\t" + userResults.length + " Users found (max: " + Constants.mostUserRatingCount + ")");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setSize(scrollPane.getWidth() - 40, userResults.length * (itemHeight + ((new JSeparator()).getHeight())));

        for (int i = 0; i < userResults.length; i++) {
            JPanel resultItem = new JPanel();
            resultItem.setSize(scrollPane.getWidth() - 40, itemHeight);

            Float userAvgRating = ratings.getUserAverageRating(userResults[i]);
            String formattedUsersAvgRating;
            if (userAvgRating == null){
                loadingText.setText("No user average rating found: Received null! (For user "+ userResults[i] +")");
                System.out.println("\tNo user average rating found: Received null!(For user "+ userResults[i] +")");
                scrollPane.setVisible(true);
                return;
            }
            else{
                formattedUsersAvgRating = new DecimalFormat("0.00").format(userAvgRating);
            }
            
            float[] userRatings = ratings.getUserRatings(userResults[i]);
            if (userRatings == null){
                loadingText.setText("No user ratings found: Received null! (For user "+ userResults[i] +")");
                System.out.println("\tNo user ratings found: Received null!(For user "+ userResults[i] +")");
                scrollPane.setVisible(true);
                return;
            }
            else{
                formattedUsersAvgRating = new DecimalFormat("0.00").format(userAvgRating);
            }

            
            String resultString = "UID: " + userResults[i] + "\t " + formattedUsersAvgRating + "★ (" + userRatings.length + ")";

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

            if (i < userResults.length - 1) {
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPanel.add(sep);
            }
        }
        scrollPane.setVisible(true);
        System.out.println("Finished running Most Rated Users");
    }
}

class MostRatedMovieReel extends MovieReel {
    public MostRatedMovieReel(JPanel screenPanel, AbstractStores stores){
        super(screenPanel, stores, "Most Rated Movies", "Searching Most Rated Movies...");
    }

    public void run(){
        System.out.println("Running Most Rated Movie Reel");
        int length = Constants.topMoviesCount;
        int[] movieResults = stores.getRatings().getMostRatedMovies(length);

        if (movieResults == null || movieResults.length == 0){
            String message = "No most rated movie ratings found";
            System.out.println("\t" + message);
            this.add(new JLabel(message));
        }
        else{
            String[] labels = new String[length];
            for (int i = 0; i < length; i++){
                labels[i] = String.format("%s (%d)", stores.getMovies().getTitle(movieResults[i]),
                                                     stores.getRatings().getNumRatings(movieResults[i]));
            }
            displayItems(movieResults, labels);
            System.out.println("Finished running Most Rated Movie Reel");
        }
    }
}

class TopRatedMovieReel extends MovieReel {
    public TopRatedMovieReel(JPanel screenPanel, AbstractStores stores){
        super(screenPanel, stores, "Top Rated Movies", "Searching Top Rated Movies...");
    }

    public void run(){
        System.out.println("Running Top Rated Movie Reel");
        int length = Constants.topMoviesCount;
        int[] movieResults = stores.getRatings().getTopAverageRatedMovies(length);

        if (movieResults == null || movieResults.length == 0){
            System.out.println("\tNo top rated movie ratings found");
        }
        else{
            String[] labels = new String[length];
            for (int i = 0; i < length; i++){
                labels[i] = String.format("%s (%.2f ★)", stores.getMovies().getTitle(movieResults[i]),
                                                         stores.getRatings().getMovieAverageRating(movieResults[i]));
            }
            displayItems(movieResults, labels);
            System.out.println("Finished running Top Rated Movie Reel");
        }
    }
}