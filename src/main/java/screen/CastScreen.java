package screen;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import java.time.LocalDate;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MouseInputListener;

import stores.*;
import ui.MovieReel;
import utils.*;
import interfaces.AbstractStores;
import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;

public class CastScreen {
    public static void createPanel(JPanel panel, AbstractStores stores) {
        System.out.println("Cast screen");
        panel.setVisible(false);
        panel.setLayout(null);
        panel.removeAll();

        // Add the border for the cast from the 100 top rated movies
        TitledBorder topRatedCastBorder;
        topRatedCastBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Cast from Top " + Constants.topMoviesCount + " Rated Movies");
        topRatedCastBorder.setTitleJustification(TitledBorder.CENTER);
        topRatedCastBorder.setTitleColor(Constants.getFontColor());

        JPanel topRatedCastOuter = new JPanel();
        topRatedCastOuter.setBounds(5, 5, (int) (panel.getWidth()*0.48)+10, (int) (panel.getHeight()*0.60)+20);
        topRatedCastOuter.setBorder(topRatedCastBorder);
        topRatedCastOuter.setForeground(Constants.getFontColor());
        topRatedCastOuter.setBackground(Constants.getHighlight());
        topRatedCastOuter.setLayout(new GridBagLayout());

        JPanel topRatedCastInner = new JPanel();
        topRatedCastInner.setBackground(Constants.getHighlight());
        topRatedCastInner.setForeground(Constants.getFontColor());
        topRatedCastInner.setSize(new Dimension((int) 470, 360));
        JScrollPane topRatedCast = new JScrollPane(topRatedCastInner);
        topRatedCast.setBounds(10, 10, 510, 360);
        topRatedCast.setMinimumSize(new Dimension(510, 360));
        topRatedCast.setPreferredSize(new Dimension(510, 360));
        topRatedCast.getViewport().setMinimumSize(new Dimension(510, 360));
        topRatedCast.getViewport().setPreferredSize(new Dimension(510, 360));
        topRatedCast.setBackground(Constants.getHighlight());
        topRatedCast.setForeground(Constants.getFontColor());
        topRatedCastOuter.add(topRatedCast);

        TopRatedCastRunnable topRatedCastRunnable = new TopRatedCastRunnable(panel, topRatedCast, topRatedCastInner, stores);

        // Add the border for the cast from movies released in the 90's
        TitledBorder oldMoviesCastBorder;
        oldMoviesCastBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Cast from Movies in the 90's");
        oldMoviesCastBorder.setTitleJustification(TitledBorder.CENTER);
        oldMoviesCastBorder.setTitleColor(Constants.getFontColor());

        JPanel oldMoviesCastOuter = new JPanel();
        oldMoviesCastOuter.setBounds(540, 5, (int) (panel.getWidth()*0.48)+10, (int) (panel.getHeight()*0.60)+20);
        oldMoviesCastOuter.setBorder(oldMoviesCastBorder);
        oldMoviesCastOuter.setForeground(Constants.getFontColor());
        oldMoviesCastOuter.setBackground(Constants.getHighlight());
        oldMoviesCastOuter.setLayout(new GridBagLayout());

        JPanel oldMoviesCastInner = new JPanel();
        oldMoviesCastInner.setBackground(Constants.getHighlight());
        oldMoviesCastInner.setForeground(Constants.getFontColor());
        oldMoviesCastInner.setSize(new Dimension((int) 470, 360));
        JScrollPane oldMoviesCast = new JScrollPane(oldMoviesCastInner);
        oldMoviesCast.setBounds(10, 10, 510, 360);
        oldMoviesCast.setMinimumSize(new Dimension(510, 360));
        oldMoviesCast.setPreferredSize(new Dimension(510, 360));
        oldMoviesCast.getViewport().setMinimumSize(new Dimension(510, 360));
        oldMoviesCast.getViewport().setPreferredSize(new Dimension(510, 360));
        oldMoviesCast.setBackground(Constants.getHighlight());
        oldMoviesCast.setForeground(Constants.getFontColor());
        oldMoviesCastOuter.add(oldMoviesCast);

        OldMoviesCastRunnable oldMoviesCastRunnable = new OldMoviesCastRunnable(panel, oldMoviesCast, oldMoviesCastInner, stores);

        JPanel featuredCastMoviesOuter = new JPanel();
        featuredCastMoviesOuter.setBounds(5, 410, 1070, 218);
        featuredCastMoviesOuter.setForeground(Constants.getFontColor());
        featuredCastMoviesOuter.setBackground(Constants.getHighlight());
        featuredCastMoviesOuter.setLayout(new GridBagLayout());

        MovieReel featuredCastMovieReel = new FeaturedCastMovieReel(panel, stores, "Tom Hanks");
        featuredCastMoviesOuter.add(featuredCastMovieReel);
        featuredCastMovieReel.setBounds(5, 410, 1070, 218);
        featuredCastMovieReel.setMinimumSize(new Dimension(1065, 215));
        featuredCastMovieReel.setPreferredSize(new Dimension(1065, 215));
        featuredCastMovieReel.getViewport().setMinimumSize(new Dimension(1065, 215));
        featuredCastMovieReel.getViewport().setPreferredSize(new Dimension(1065, 215));

        panel.add(topRatedCastOuter);
        panel.add(oldMoviesCastOuter);
        panel.add(featuredCastMoviesOuter);

        panel.setVisible(true);

        SwingUtilities.invokeLater(topRatedCastRunnable);
        SwingUtilities.invokeLater(oldMoviesCastRunnable);
        SwingUtilities.invokeLater(featuredCastMovieReel);
    }
}

class TopRatedCastRunnable implements Runnable {
    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private JLabel loadingText;
    private int[] topMovies; // store IDs of 20 top movies

    public TopRatedCastRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching for cast from top rated movies...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {

        topMovies = stores.getRatings().getMostRatedMovies(Constants.topMoviesCount); // ids of top 20 movies

        scrollPane.setVisible(false);

        if (topMovies == null || topMovies.length == 0) {
            loadingText.setText("No casts that featured in top movies were found!");
            System.out.println("\tNo casts that featured in top movies were found");
            scrollPane.setVisible(true);
            return;
        }
        else {
            loadingText.setText("Processing " + topMovies.length + " casts that featured in top movies...");
            System.out.println("\t" + topMovies.length + " of the top cast found (max: " + Constants.topMoviesCount + ")");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setSize(scrollPane.getWidth(), topMovies.length * (itemHeight + ((new JSeparator()).getHeight())));

        for (int i = 0; i < topMovies.length; i ++) {
            JPanel resultItem = new JPanel();
            resultItem.setBackground(Constants.getBackground());
            resultItem.setSize(scrollPane.getWidth(), itemHeight);

            String resultString = "";
            int movieID = topMovies[i];
            String currentTitle = stores.getMovies().getTitle(topMovies[i]); // title of the current movie
            CastCredit[] cast = stores.getCredits().getFilmCast(topMovies[i]); // current cast

            // create a clickable button
            JPanel titlePanel = new JPanel();
            JTextArea titleLabel = new JTextArea(currentTitle);
            titlePanel.setBackground(Constants.getBackground());
            titleLabel.setBackground(Constants.getBackground());
            titleLabel.setForeground(Constants.getFontColor());
            titleLabel.setBounds(0, (itemHeight * i), resultsPanel.getWidth()-40, itemHeight);
            titleLabel.setEditable(false);
            titleLabel.setLineWrap(true);
            titleLabel.setWrapStyleWord(true);
            titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, titleLabel.getFont().getSize()));
            titlePanel.add(titleLabel);

            titleLabel.addMouseListener(new MouseInputListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            FilmScreen.createPanel(masterPanel, movieID, stores);                            
                        }
                    });
                }
                @Override
                public void mousePressed(MouseEvent e) {}

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}

                @Override
                public void mouseDragged(MouseEvent e) {}

                @Override
                public void mouseMoved(MouseEvent e) {}
            });

            resultsPanel.add(titlePanel);

            for (int j = 0; j < cast.length; j ++) {
                resultString += cast[j].getName() + " (" + cast[j].getCharacter() + ")";
                if (j < cast.length-1) { // add '|' only if not last cast member reached
                    resultString += " | ";
                }
            }
            JTextArea title = new JTextArea(resultString);
            title.setBounds(0, (itemHeight * i), resultsPanel.getWidth()-40, itemHeight);
            title.setForeground(Constants.getFontColor());
            title.setBackground(Constants.getHighlight());
            title.setEditable(false);
            title.setLineWrap(true);
            title.setWrapStyleWord(true);

            resultItem.add(title);
            resultItem.setBackground(Constants.getHighlight());
            resultItem.setForeground(Constants.getFontColor());

            resultsPanel.add(resultItem);

            if (i < topMovies.length-1) {
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPanel.add(sep);
            }
        }
        scrollPane.setVisible(true);
    }
}

class OldMoviesCastRunnable implements Runnable {

    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private JLabel loadingText;
    private LocalDate endDate = LocalDate.now(); // store the date to be compared
    private LocalDate startDate = LocalDate.now(); // store the date to be compared
    int[] oldMovies; // IDs of movies released before a predefined date

    public OldMoviesCastRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching for cast in Movies released in the 90's...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);

        endDate = LocalDate.of(2000, 1, 1); // date is 01/01/2000 00:00:00
        startDate = LocalDate.of(1990, 1, 1); // date is EpOCH 0, 01/01/1990 00:00:00
    }

    @Override
    public void run() {
        oldMovies = stores.getMovies().getAllIDsReleasedInRange(startDate, endDate); // IDs of movies released before a pre-defined date

        scrollPane.setVisible(false);

        if (oldMovies == null || oldMovies.length == 0) {
            loadingText.setText("No casts that played in movies released in the 90's were found!");
            System.out.println("\tNo casts that played in movies released in the 90's were found");
            scrollPane.setVisible(true);
            return;
        }
        else {
            loadingText.setText("Processing casts featured in movies released in the 90's.");
            System.out.println("\t" + oldMovies.length + " casts were found");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setSize(scrollPane.getWidth(), oldMovies.length * (itemHeight + ((new JSeparator()).getHeight())));

        for (int i = 0; i < oldMovies.length; i ++) {
            JPanel resultItem = new JPanel();
            resultItem.setSize(scrollPane.getWidth(), itemHeight);

            String resultString = "";
            int movieID = oldMovies[i];
            String currentTitle = stores.getMovies().getTitle(oldMovies[i]);
            CastCredit[] cast = stores.getCredits().getFilmCast(oldMovies[i]); // current cast

            // create a clickable button
            JPanel titlePanel = new JPanel();
            JTextArea titleLabel = new JTextArea(currentTitle);
            titlePanel.setBackground(Constants.getBackground());
            titleLabel.setBackground(Constants.getBackground());
            titleLabel.setForeground(Constants.getFontColor());
            titleLabel.setBounds(0, (itemHeight * i), resultsPanel.getWidth()-40, itemHeight);
            titleLabel.setEditable(false);
            titleLabel.setLineWrap(true);
            titleLabel.setWrapStyleWord(true);
            titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, titleLabel.getFont().getSize()));
            titlePanel.add(titleLabel);

            titleLabel.addMouseListener(new MouseInputListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            FilmScreen.createPanel(masterPanel, movieID, stores);
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });

            resultsPanel.add(titlePanel);

            for (int j = 0; j < cast.length; j ++) {
                resultString += cast[j].getName() + " (" + cast[j].getCharacter() + ")";
                if (j < cast.length-1) { // add '|' only if not last cast member reached
                    resultString += " | ";
                }
            }
            JTextArea detail = new JTextArea(resultString);
            detail.setBounds(0, (itemHeight * i), resultsPanel.getWidth()-40, itemHeight);
            detail.setForeground(Constants.getFontColor());
            detail.setBackground(Constants.getHighlight());
            detail.setEditable(false);
            detail.setLineWrap(true);
            detail.setWrapStyleWord(true);

            resultItem.add(detail);
            resultItem.setBackground(Constants.getHighlight());
            resultItem.setForeground(Constants.getFontColor());

            resultsPanel.add(resultItem);

            if (i < oldMovies.length-1) {
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPanel.add(sep);
            }
        }
        scrollPane.setVisible(true);
    }
}

class FeaturedCastMovieReel extends MovieReel {
    private String castName = "";
    public FeaturedCastMovieReel(JPanel screenPanel, AbstractStores stores, String castName) {
        super(screenPanel, stores, castName + " Movies", "Searching for films with " + castName + "...");
        this.castName = castName;
    }

    public void run(){
        System.out.println("Running Featured Cast Movie Reel --> " + castName);

        Person[] foundCast = stores.getCredits().findCast(castName);

        if (foundCast == null || foundCast.length == 0) {
            System.out.println("    No cast members found with the name \"" + castName + "\"");
            return;
        }

        int[] featuredCastMovies = stores.getCredits().getCastFilms(foundCast[0].getID());

        if (featuredCastMovies == null || featuredCastMovies.length == 0) {
            System.out.println("    No films with " + castName + " were found");
        } else {
            String[] labels = new String[featuredCastMovies.length];
            for (int i = 0; i < featuredCastMovies.length; i++) {
                labels[i] = String.format("%s", stores.getMovies().getTitle(featuredCastMovies[i]));
            }
            displayItems(featuredCastMovies, labels);
            System.out.println("Finished running Featured Cast Movie Reel");
        }

    }
}