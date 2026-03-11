package screen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import interfaces.*;

import java.awt.*;
import java.awt.event.MouseEvent;

import utils.Constants;

public class SearchScreen {
    
    public static void createPanel(JPanel panel, String searchTerm, AbstractStores stores) {
        IKeywords keywords = stores.getKeywords();
        System.out.println("Search screen --> \"" + searchTerm + "\"");
        if (searchTerm == "" || searchTerm.isEmpty()) {
            System.err.println("\tCan't search for nothing...");
            return;
        }
        panel.removeAll();
        panel.setLayout(null);
        panel.setVisible(false);
        
        JLabel title = new JLabel("Searching for \"" + searchTerm + "\"");
        title.setForeground(Constants.getFontColor());
        title.setBounds(0,0,panel.getWidth(),35);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);

        TitledBorder movieResultsBorder;
        movieResultsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Movies");
        movieResultsBorder.setTitleJustification(TitledBorder.CENTER);
        movieResultsBorder.setTitleColor(Constants.getFontColor());
        JPanel movieResultsOuter = new JPanel();
        movieResultsOuter.setBackground(Constants.getHighlight());
        movieResultsOuter.setForeground(Constants.getFontColor());
        movieResultsOuter.setBounds(0, title.getHeight(), (int) (panel.getWidth() / 4), (panel.getHeight() - 2 * title.getHeight()));
        movieResultsOuter.setBorder(movieResultsBorder);
        movieResultsOuter.setLayout(new GridBagLayout());

        JPanel movieResults = new JPanel();
        movieResults.setBackground(Constants.getHighlight());
        movieResults.setForeground(Constants.getFontColor());
        movieResults.setLayout(new GridBagLayout());

        JScrollPane movieSearch = new JScrollPane(movieResults);
        movieSearch.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        movieSearch.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        movieSearch.setBounds(5, title.getHeight()+10, (int) (panel.getWidth() / 4)-20, (panel.getHeight()-2 * title.getHeight()-30));
        movieSearch.setMinimumSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        movieSearch.setPreferredSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        movieSearch.getViewport().setMinimumSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        movieSearch.getViewport().setPreferredSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));

        movieSearch.setAutoscrolls(false);
        movieResultsOuter.add(movieSearch);

        MovieSearchRunnable movieSearchRunnable = new MovieSearchRunnable(panel, movieSearch, movieResults, stores, searchTerm);

        TitledBorder castResultsBorder;
        castResultsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()),
                "Cast");
        castResultsBorder.setTitleJustification(TitledBorder.CENTER);
        castResultsBorder.setTitleColor(Constants.getFontColor());
        JPanel castResultsOuter = new JPanel();
        castResultsOuter.setBackground(Constants.getHighlight());
        castResultsOuter.setForeground(Constants.getFontColor());
        castResultsOuter.setBounds((int) (panel.getWidth() / 4), title.getHeight(), (int) (panel.getWidth() / 4), (panel.getHeight() - 2 * title.getHeight()));
        castResultsOuter.setLayout(new GridBagLayout());
        castResultsOuter.setBorder(castResultsBorder);

        JPanel castResults = new JPanel();
        castResults.setBackground(Constants.getHighlight());
        castResults.setForeground(Constants.getFontColor());
        castResults.setLayout(new GridBagLayout());

        JScrollPane castSearch = new JScrollPane(castResults);
        castSearch.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        castSearch.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        castSearch.setBounds((int) (panel.getWidth() / 4)+5, title.getHeight()+10, (int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2*title.getHeight())-30);
        castSearch.setMinimumSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2*title.getHeight())-30));
        castSearch.setPreferredSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2*title.getHeight())-30));
        castSearch.getViewport().setMinimumSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2*title.getHeight())-30));
        castSearch.getViewport().setPreferredSize(new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2*title.getHeight())-30));

        castSearch.setAutoscrolls(false);
        castResultsOuter.add(castSearch);

        CreditsSearchRunnable castSearchRunnable = new CreditsSearchRunnable(panel, castSearch, castResults, stores, searchTerm, true);

        TitledBorder crewResultsBorder;
        crewResultsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()),
                "Crew");
        crewResultsBorder.setTitleJustification(TitledBorder.CENTER);
        crewResultsBorder.setTitleColor(Constants.getFontColor());
        JPanel crewResultsOuter = new JPanel();
        crewResultsOuter.setBackground(Constants.getHighlight());
        crewResultsOuter.setForeground(Constants.getFontColor());
        crewResultsOuter.setBounds((int) (panel.getWidth() / 2), title.getHeight(), (int) (panel.getWidth() / 4),
                (panel.getHeight() - 2 * title.getHeight()));
        crewResultsOuter.setLayout(new GridBagLayout());
        crewResultsOuter.setBorder(crewResultsBorder);

        JPanel crewResults = new JPanel();
        crewResults.setBackground(Constants.getHighlight());
        crewResults.setForeground(Constants.getFontColor());
        crewResults.setLayout(new GridBagLayout());

        JScrollPane crewSearch = new JScrollPane(crewResults);
        crewSearch.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        crewSearch.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        crewSearch.setBounds((int) (panel.getWidth() / 2)+5, title.getHeight()+10, (int) (panel.getWidth() / 4)-20,
                (panel.getHeight() - 2*title.getHeight())-30);
        crewSearch.setMinimumSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        crewSearch.setPreferredSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        crewSearch.getViewport().setMinimumSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        crewSearch.getViewport().setPreferredSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));

        crewSearch.setAutoscrolls(false);
        crewResultsOuter.add(crewSearch);

        CreditsSearchRunnable crewSearchRunnable = new CreditsSearchRunnable(panel, crewSearch, crewResults, stores, searchTerm, false);

        TitledBorder keywordsResultsBorder;
        keywordsResultsBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()),
                "Keywords");
        keywordsResultsBorder.setTitleJustification(TitledBorder.CENTER);
        keywordsResultsBorder.setTitleColor(Constants.getFontColor());
        JPanel keywordsResultsOuter = new JPanel();
        keywordsResultsOuter.setBackground(Constants.getHighlight());
        keywordsResultsOuter.setForeground(Constants.getFontColor());
        keywordsResultsOuter.setBounds((int) 3 * (panel.getWidth() / 4), title.getHeight(),
                (int) (panel.getWidth() / 4),
                (panel.getHeight() - 2 * title.getHeight()));
        keywordsResultsOuter.setLayout(new GridBagLayout());
        keywordsResultsOuter.setBorder(keywordsResultsBorder);

        JPanel keywordResults = new JPanel();
        keywordResults.setBackground(Constants.getHighlight());
        keywordResults.setForeground(Constants.getFontColor());
        keywordResults.setLayout(new GridBagLayout());

        JScrollPane keywordSearch = new JScrollPane(keywordResults);
        keywordSearch.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        keywordSearch.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        keywordSearch.setBounds((int) 3 * (panel.getWidth() / 4)+5, title.getHeight()+10, (int) (panel.getWidth() / 4)-20,
                (panel.getHeight() - 2 * title.getHeight())-30);
        keywordSearch.setMinimumSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        keywordSearch.setPreferredSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        keywordSearch.getViewport().setMinimumSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));
        keywordSearch.getViewport().setPreferredSize(
                new Dimension((int) (panel.getWidth() / 4)-20, (panel.getHeight() - 2 * title.getHeight())-30));

        // keywordSearch.setBorder(null);
        keywordSearch.setAutoscrolls(false);
        keywordsResultsOuter.add(keywordSearch);

        KeywordSearchRunnable keywordSearchRunnable = new KeywordSearchRunnable(keywordSearch, keywordResults, keywords, searchTerm);

        panel.add(title);
        panel.add(movieResultsOuter);
        panel.add(castResultsOuter);
        panel.add(crewResultsOuter);
        panel.add(keywordsResultsOuter);

        panel.setVisible(true);

        SwingUtilities.invokeLater(movieSearchRunnable);
        SwingUtilities.invokeLater(castSearchRunnable);
        SwingUtilities.invokeLater(crewSearchRunnable);
        SwingUtilities.invokeLater(keywordSearchRunnable);
    }
}

class MovieSearchRunnable implements Runnable {
    private JPanel masterPane;
    private JScrollPane scrollPane;
    private JPanel resultsPane;
    private JLabel loadingText;
    private String searchTerm;
    private AbstractStores stores;

    public MovieSearchRunnable(JPanel masterPane, JScrollPane scrollPane, JPanel resultsPane, AbstractStores stores, String searchTerm) {
        this.masterPane = masterPane;
        this.scrollPane = scrollPane;
        this.resultsPane = resultsPane;
        this.stores = stores;
        this.searchTerm = searchTerm;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching Films...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPane.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        int[] idResults = stores.getMovies().findFilms(searchTerm);

        scrollPane.setVisible(false);

        if (idResults == null || idResults.length == 0) {
            loadingText.setText("No films found for \""+searchTerm+"\"!");
            System.out.println("\t0 Films found");
            return;
        } else {
            loadingText.setText("Processing " + idResults.length + " Films...");
            System.out.println("\t" + idResults.length + " Films found");
        }

        scrollPane.setVisible(true);

        final int itemHeight = 50;

        resultsPane.removeAll();
        resultsPane.setLayout(new BoxLayout(resultsPane, BoxLayout.Y_AXIS));
        resultsPane.setSize(scrollPane.getWidth()-40, idResults.length*(itemHeight+((new JSeparator()).getHeight())));


        for (int i = 0; i < idResults.length; i++) {
            JPanel resultItem = new JPanel();
            resultItem.setSize(scrollPane.getWidth()-40, itemHeight);
            int filmID = idResults[i];

            JTextArea title = new JTextArea(stores.getMovies().getTitle(idResults[i]));
            title.setBounds(0, (itemHeight * i), resultsPane.getWidth(), itemHeight);
            title.setForeground(Constants.getFontColor());
            title.setBackground(Constants.getBackground());
            title.setEditable(false);
            title.setLineWrap(true);
            title.setWrapStyleWord(true);
            title.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            FilmScreen.createPanel(masterPane, filmID, stores);                            
                        }
                    });
                }
            });

            resultItem.add(title);
            resultItem.setBackground(Constants.getBackground());
            resultItem.setForeground(Constants.getFontColor());
            resultItem.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            FilmScreen.createPanel(masterPane, filmID, stores);
                        }
                    });
                }
            });

            resultsPane.add(resultItem);

            if (i < idResults.length-1){
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPane.add(sep);
            }
        }
        scrollPane.setVisible(true);
    }
}

class CreditsSearchRunnable implements Runnable {
    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPane;
    private AbstractStores stores;
    private JLabel loadingText;
    private String searchTerm;
    private boolean castFlag;

    public CreditsSearchRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPane, AbstractStores stores, String searchTerm, boolean castFlag) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPane = resultsPane;
        this.stores = stores;
        this.searchTerm = searchTerm;
        this.castFlag = castFlag;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        if (castFlag) {
            loadingText.setText("Searching Cast Members...");
        } else {
            loadingText.setText("Searching Crew Members...");
        }

        resultsPane.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        ICredits credits = stores.getCredits();
        if (castFlag) {
            IPerson[] castResults = credits.findCast(searchTerm);

            if (castResults == null || castResults.length == 0) {
                loadingText.setText("No Cast Members found for \"" + searchTerm + "\"!");
                System.out.println("\t0 Cast Members found");
                return;
            } else {
                loadingText.setText("Processing " + castResults.length + " Cast Members...");
                System.out.println("\t" + castResults.length + " Cast Members found");
            }

            scrollPane.setVisible(false);

            final int itemHeight = 50;

            resultsPane.removeAll();
            resultsPane.setLayout(new BoxLayout(resultsPane, BoxLayout.Y_AXIS));
            resultsPane.setSize(scrollPane.getWidth()-40, castResults.length * (itemHeight + ((new JSeparator()).getHeight())));

            for (int i = 0; i < castResults.length; i++) {
                JPanel resultItem = new JPanel();
                resultItem.setBounds(0, (itemHeight * i), resultsPane.getWidth(), itemHeight);

                JTextArea title = new JTextArea(castResults[i].getName());
                title.setBounds(0, (itemHeight * i), resultsPane.getWidth(), itemHeight);
                title.setForeground(Constants.getFontColor());
                title.setBackground(Constants.getBackground());
                title.setEditable(false);
                title.setLineWrap(true);
                title.setWrapStyleWord(true);

                resultItem.add(title);
                resultItem.setBackground(Constants.getBackground());
                resultItem.setForeground(Constants.getFontColor());

                int castID = castResults[i].getID();
                title.addMouseListener(new MouseInputAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                CastMemberScreen.createPanel(masterPanel, castID, stores);
                            }
                        });
                    }
                });
                resultItem.addMouseListener(new MouseInputAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                CastMemberScreen.createPanel(masterPanel, castID, stores);
                            }
                        });
                    }
                });

                resultsPane.add(resultItem);

                if (i < castResults.length - 1) {
                    JSeparator sep = new JSeparator();
                    sep.setBackground(Constants.getHighlight());
                    sep.setForeground(Constants.getFontColor());
                    resultsPane.add(sep);
                }
            }
            scrollPane.setVisible(true);
        } else {
            IPerson[] crewResults = credits.findCrew(searchTerm);

            if (crewResults == null || crewResults.length == 0) {
                loadingText.setText("No Crew Members found for \"" + searchTerm + "\"!");
                System.out.println("\t0 Crew Members found");
                return;
            } else {
                loadingText.setText("Processing " + crewResults.length + " Crew Members...");
                System.out.println("\t" + crewResults.length + " Crew Members found");
            }

            scrollPane.setVisible(false);

            final int itemHeight = 50;

            resultsPane.removeAll();
            resultsPane.setLayout(new BoxLayout(resultsPane, BoxLayout.Y_AXIS));
            resultsPane.setSize(scrollPane.getWidth()-40, crewResults.length * (itemHeight + ((new JSeparator()).getHeight())));

            for (int i = 0; i < crewResults.length; i++) {
                JPanel resultItem = new JPanel();
                resultItem.setBounds(0, (itemHeight * i), resultsPane.getWidth(), itemHeight);

                JTextArea title = new JTextArea(crewResults[i].getName());
                title.setBounds(0, (itemHeight * i), resultsPane.getWidth(), itemHeight);
                title.setForeground(Constants.getFontColor());
                title.setBackground(Constants.getHighlight());
                title.setEditable(false);
                title.setLineWrap(true);
                title.setWrapStyleWord(true);

                resultItem.add(title);
                resultItem.setBackground(Constants.getHighlight());
                resultItem.setForeground(Constants.getFontColor());

                resultsPane.add(resultItem);

                if (i < crewResults.length - 1) {
                    JSeparator sep = new JSeparator();
                    sep.setBackground(Constants.getHighlight());
                    sep.setForeground(Constants.getFontColor());
                    resultsPane.add(sep);
                }
            }
            scrollPane.setVisible(true);
        }
    }
}

class KeywordSearchRunnable implements Runnable {
    private JScrollPane scrollPane;
    private JPanel resultsPane;
    private IKeywords keywords;
    private JLabel loadingText;
    private String searchTerm;

    public KeywordSearchRunnable(JScrollPane scrollPane, JPanel resultsPane, IKeywords keywords, String searchTerm) {
        this.scrollPane = scrollPane;
        this.resultsPane = resultsPane;
        this.keywords = keywords;
        this.searchTerm = searchTerm;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching Keywords...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPane.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        IKeyword[] keywordResults = keywords.findKeywords(searchTerm);

        if (keywordResults == null || keywordResults.length == 0) {
            loadingText.setText("No keywords found for \"" + searchTerm + "\"!");
            System.out.println("\t0 Keywords found");
            return;
        } else {
            loadingText.setText("Processing " + keywordResults.length + " keywords...");
            System.out.println("\t" + keywordResults.length + " Keywords found");
        }

        scrollPane.setVisible(false);

        final int itemHeight = 50;

        resultsPane.removeAll();
        resultsPane.setLayout(new BoxLayout(resultsPane, BoxLayout.Y_AXIS));
        resultsPane.setSize(scrollPane.getWidth() - 40,
                keywordResults.length * (itemHeight + ((new JSeparator()).getHeight())));

        for (int i = 0; i < keywordResults.length; i++) {
            JPanel resultItem = new JPanel();

            JTextArea title = new JTextArea(keywordResults[i].getName());
            title.setBounds(0, (itemHeight * i), resultsPane.getWidth(), itemHeight);
            title.setForeground(Constants.getFontColor());
            title.setBackground(Constants.getHighlight());
            title.setEditable(false);
            title.setLineWrap(true);
            title.setWrapStyleWord(true);

            resultItem.add(title);
            resultItem.setBackground(Constants.getHighlight());
            resultItem.setForeground(Constants.getFontColor());

            resultsPane.add(resultItem);

            if (i < keywordResults.length - 1) {
                JSeparator sep = new JSeparator();
                sep.setBackground(Constants.getHighlight());
                sep.setForeground(Constants.getFontColor());
                resultsPane.add(sep);
            }
        }
        scrollPane.setVisible(true);
    }
}