package screen;

import java.awt.*;
import java.awt.event.*;

import java.time.LocalDate;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MouseInputListener;

import stores.*;
import ui.MovieReel;
import utils.*;
import interfaces.AbstractStores;


public class CrewScreen {
    public static void createPanel(JPanel panel, AbstractStores stores) {
        System.out.println("Crew screen");
        panel.setVisible(false);
        panel.setLayout(null);
        panel.removeAll();

        // Add the border for the crew from the X top rated movies
        TitledBorder topRatedCrewBorder;
        topRatedCrewBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Crew from top rated movies");
        topRatedCrewBorder.setTitleJustification(TitledBorder.CENTER);
        topRatedCrewBorder.setTitleColor(Constants.getFontColor());

        JPanel topRatedCrewOuter = new JPanel();
        topRatedCrewOuter.setBounds(5, 225, (int) (panel.getWidth()*0.48)+10, (int) (panel.getHeight()*0.60)+20);
        topRatedCrewOuter.setBorder(topRatedCrewBorder);
        topRatedCrewOuter.setForeground(Constants.getFontColor());
        topRatedCrewOuter.setBackground(Constants.getHighlight());
        topRatedCrewOuter.setLayout(new GridBagLayout());

        JPanel topRatedCrewInner = new JPanel();
        topRatedCrewInner.setBackground(Constants.getHighlight());
        topRatedCrewInner.setForeground(Constants.getFontColor());
        JScrollPane topRatedCrew = new JScrollPane(topRatedCrewInner);
        topRatedCrew.setBounds(10, 230, 510, 360);
        topRatedCrew.setMinimumSize(new Dimension(510, 360));
        topRatedCrew.setPreferredSize(new Dimension(510, 360));
        topRatedCrew.getViewport().setMinimumSize(new Dimension(510, 360));
        topRatedCrew.getViewport().setPreferredSize(new Dimension(510, 360));
        topRatedCrew.setBackground(Constants.getHighlight());
        topRatedCrew.setForeground(Constants.getFontColor());
        topRatedCrewOuter.add(topRatedCrew);

        TopRatedCrewRunnable topRatedCrewRunnable = new TopRatedCrewRunnable(panel, topRatedCrew, topRatedCrewInner, stores);

        // Add the border for the crew from recent movies
        TitledBorder recentCrewBorder;
        recentCrewBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Crew from movies released in the Naughties (2000 to 2010)");
        recentCrewBorder.setTitleJustification(TitledBorder.CENTER);
        recentCrewBorder.setTitleColor(Constants.getFontColor());

        JPanel recentCrewOuter = new JPanel();
        recentCrewOuter.setBounds((int) (panel.getWidth()*0.48)+15, 225, (int) (panel.getWidth()*0.48)+10, (int) (panel.getHeight()*0.60)+20);
        recentCrewOuter.setBorder(recentCrewBorder);
        recentCrewOuter.setForeground(Constants.getFontColor());
        recentCrewOuter.setBackground(Constants.getHighlight());
        recentCrewOuter.setLayout(new GridBagLayout());

        JPanel recentCrewInner = new JPanel();
        recentCrewInner.setBackground(Constants.getHighlight());
        recentCrewInner.setForeground(Constants.getFontColor());
        JScrollPane recentCrew = new JScrollPane(recentCrewInner);
        recentCrew.setBounds((int) (panel.getWidth()*0.48)+20, 230, 510, 360);
        recentCrew.setMinimumSize(new Dimension(510, 360));
        recentCrew.setPreferredSize(new Dimension(510, 360));
        recentCrew.getViewport().setMinimumSize(new Dimension(510, 360));
        recentCrew.getViewport().setPreferredSize(new Dimension(510, 360));
        recentCrew.setBackground(Constants.getHighlight());
        recentCrew.setForeground(Constants.getFontColor());
        recentCrewOuter.add(recentCrew);

        RecentMoviesCrewsRunnable recentMoviesCrewsRunnable = new RecentMoviesCrewsRunnable(panel, recentCrew, recentCrewInner, stores);

        JPanel famousCrewMovieOuter = new JPanel();
        famousCrewMovieOuter.setBounds(5, 5, 1070, 218);
        famousCrewMovieOuter.setForeground(Constants.getFontColor());
        famousCrewMovieOuter.setBackground(Constants.getHighlight());
        famousCrewMovieOuter.setLayout(new GridBagLayout());

        MovieReel featuredCrewMovieReel = new FeaturedCrewMovieReel(panel, stores, "Edgar Wright");
        featuredCrewMovieReel.setBounds(5, 5, 1070, 218);
        featuredCrewMovieReel.setMinimumSize(new Dimension(1065, 215));
        featuredCrewMovieReel.setPreferredSize(new Dimension(1065, 215));
        featuredCrewMovieReel.getViewport().setMinimumSize(new Dimension(1065, 215));
        featuredCrewMovieReel.getViewport().setPreferredSize(new Dimension(1065, 215));
        famousCrewMovieOuter.add(featuredCrewMovieReel);

        panel.add(topRatedCrewOuter);
        panel.add(recentCrewOuter);
        panel.add(famousCrewMovieOuter);

        panel.setVisible(true);

        SwingUtilities.invokeLater(topRatedCrewRunnable);
        SwingUtilities.invokeLater(recentMoviesCrewsRunnable);
        SwingUtilities.invokeLater(featuredCrewMovieReel);
    }
}

class TopRatedCrewRunnable implements Runnable {
    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private JLabel loadingText;
    private int[] topMovies; // store IDs of 20 top movies

    public TopRatedCrewRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching for crew from top rated movies...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        topMovies = stores.getRatings().getTopAverageRatedMovies(Constants.topMoviesCount); // ids of top 20 movies

        scrollPane.setVisible(false);

        if (topMovies == null || topMovies.length == 0) {
            loadingText.setText("No crew that featured in top movies were found!");
            System.out.println("\tNo crew that featured in top movies were found");
            scrollPane.setVisible(true);
            return;
        }
        else {
            loadingText.setText("Processing " + topMovies.length + " crews that featured in top movies...");
            System.out.println("\t" + topMovies.length + " of the top crews found (max: " + Constants.topMoviesCount + ")");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setSize(scrollPane.getWidth() - 40, topMovies.length * (itemHeight + ((new JSeparator()).getHeight())));
    
        for (int i = 0; i < topMovies.length; i ++) {
            JPanel resultItem = new JPanel();
            resultItem.setSize(scrollPane.getWidth(), itemHeight);

            String resultString = "";
            String currentTitle = stores.getMovies().getTitle(topMovies[i]); // title of the current movie
            CrewCredit[] crew = stores.getCredits().getFilmCrew(topMovies[i]); // current cast

            int movieID = topMovies[i];
            // create a clickable button
            JPanel titlePanel = new JPanel();
            JTextArea titleLabel = new JTextArea(currentTitle);
            titlePanel.setBackground(Constants.getBackground());
            titleLabel.setBackground(Constants.getBackground());
            titleLabel.setForeground(Constants.getFontColor());
            titleLabel.setBounds(0, (itemHeight * i), resultsPanel.getWidth(), itemHeight);
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
            for (int j = 0; j < crew.length; j ++) {
                resultString += crew[j].getName() + " (" + crew[j].getJob() + ")";
                if (j < crew.length-1) { // add '|' only if not last cast member reached
                    resultString += " | ";
                }
            }
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

class RecentMoviesCrewsRunnable implements Runnable {
    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private JLabel loadingText;
    private LocalDate endDate = LocalDate.now(); // store the date to be compared
    private LocalDate startDate = LocalDate.now(); // store the date to be compared
    int[] newMovies; // IDs of movies released in the naughties

    public RecentMoviesCrewsRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching for crew in Movies released in the Naughties (2000 to 2010)...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);

        endDate = LocalDate.of(2010, 1, 1); // date is 1/1/2010 00:00:00
        startDate = LocalDate.of(2000, 1, 1); // date is 01/11/2000 00:00:00
    }

    @Override
    public void run() {
        newMovies = stores.getMovies().getAllIDsReleasedInRange(startDate, endDate);

        scrollPane.setVisible(false);

        if (newMovies == null || newMovies.length == 0) {
            loadingText.setText("No crew that played in movies released in the Naughties (2000 to 2010) were found!");
            System.out.println("\tNo crew that played in movies released in the Naughties (2000 to 2010) were found");
            scrollPane.setVisible(true);
            return;
        }
        else {
            loadingText.setText("Processing crews featured in movies released in Naughties (2000 to 2010).");
            System.out.println("\t" + newMovies.length + " crews were found");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setSize(scrollPane.getWidth() - 60, newMovies.length * (itemHeight + ((new JSeparator()).getHeight())));
   
        for (int i = 0; i < newMovies.length; i ++) {
            JPanel resultItem = new JPanel();
            resultItem.setSize(scrollPane.getWidth(), itemHeight);

            String resultString = "";
            String currentTitle = stores.getMovies().getTitle(newMovies[i]);
            CrewCredit[] crew = stores.getCredits().getFilmCrew(newMovies[i]); // current cast

            int movieID = newMovies[i];
            // create a clickable button
            JPanel titlePanel = new JPanel();
            JTextArea titleLabel = new JTextArea(currentTitle);
            titlePanel.setBackground(Constants.getBackground());
            titleLabel.setBackground(Constants.getBackground());
            titleLabel.setForeground(Constants.getFontColor());
            titleLabel.setBounds(0, (itemHeight * i), resultsPanel.getWidth(), itemHeight);
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

            for (int j = 0; j < crew.length; j ++) {
                resultString += crew[j].getName() + " (" + crew[j].getJob() + ")";
                if (j < crew.length-1) { // add '|' only if not last cast member reached
                    resultString += " | ";
                }
            }
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

            if (i < newMovies.length-1) {
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPanel.add(sep);
            }
        }
        scrollPane.setVisible(true);
    }
}

class FeaturedCrewMovieReel extends MovieReel{
    private String crewName = "";
    public FeaturedCrewMovieReel(JPanel screenPanel, AbstractStores stores, String crewName) {
        super(screenPanel, stores, crewName + " Movies", "Searching for films with " + crewName + "...");
        this.crewName = crewName;
    }

    public void run(){
        System.out.println("Running Featured Crew Movie Reel --> " + crewName);

        Person[] foundCrew = stores.getCredits().findCrew(crewName);

        if (foundCrew == null || foundCrew.length == 0) {
            System.out.println("    No crew members found with the name \"" + crewName + "\"");
            return;
        }

        int[] featuredCrewMovies = stores.getCredits().getCrewFilms(foundCrew[0].getID());

        if (featuredCrewMovies == null || featuredCrewMovies.length == 0) {
            System.out.println("    No films with " + crewName + " were found");
        } else {
            String[] labels = new String[featuredCrewMovies.length];
            for (int i = 0; i < featuredCrewMovies.length; i++) {
                labels[i] = String.format("%s", stores.getMovies().getTitle(featuredCrewMovies[i]));
            }
            displayItems(featuredCrewMovies, labels);
            System.out.println("Finished running Featured Crew Movie Reel");
        }
    }
}
