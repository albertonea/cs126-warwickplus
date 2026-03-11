package screen;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.NumberFormatter;

import stores.*;
import ui.MovieReel;
import utils.*;
import interfaces.AbstractStores;

public class KeywordScreen {
    public static void createPanel(JPanel panel, AbstractStores stores){
        System.out.println("Keyword screen");
        panel.setVisible(false);
        panel.setLayout(null);
        panel.removeAll();

        TitledBorder uniqueKeywordSelectorBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Keyword Selector");
        uniqueKeywordSelectorBorder.setTitleColor(Constants.getFontColor());
        JPanel uniqueKeywordSelector = new JPanel();
        uniqueKeywordSelector.setBounds(5, 5, 430, 490);
        uniqueKeywordSelector.setBorder(uniqueKeywordSelectorBorder);
        uniqueKeywordSelector.setBackground(Constants.getHighlight());
        uniqueKeywordSelector.setForeground(Constants.getFontColor());

        JPanel uniqueKeywordInner = new JPanel();
        uniqueKeywordInner.setForeground(Constants.getFontColor());
        uniqueKeywordInner.setBackground(Constants.getHighlight());
        JScrollPane uniqueKeywordScroll = new JScrollPane(uniqueKeywordInner);
        uniqueKeywordScroll.setBounds(7, 10, 420, 420);
        uniqueKeywordScroll.setMinimumSize(new Dimension(420, 420));
        uniqueKeywordScroll.setPreferredSize(new Dimension(420, 420));
        uniqueKeywordScroll.getViewport().setMinimumSize(new Dimension(420, 420));
        uniqueKeywordScroll.getViewport().setPreferredSize(new Dimension(420, 420));
        uniqueKeywordSelector.add(uniqueKeywordScroll);

        JPanel uniqueKeywordSelected = new JPanel();
        uniqueKeywordSelected.setLayout(new BoxLayout(uniqueKeywordSelected, BoxLayout.Y_AXIS));
        uniqueKeywordSelected.setBounds(0, 525, 135, 35);
        uniqueKeywordSelected.setMinimumSize(new Dimension(135, 35));
        uniqueKeywordSelected.setPreferredSize(new Dimension(135, 35));
        uniqueKeywordSelected.setForeground(Constants.getFontColor());
        uniqueKeywordSelected.setBackground(Constants.getHighlight());
        JLabel uniqueKeywordSelectedText = new JLabel("No Keyword");
        uniqueKeywordSelectedText.setForeground(Constants.getFontColor());
        uniqueKeywordSelectedText.setBackground(Constants.getHighlight());
        JLabel uniqueKeywordSelectedID = new JLabel("No ID");
        uniqueKeywordSelectedID.setForeground(Constants.getFontColor());
        uniqueKeywordSelectedID.setBackground(Constants.getHighlight());
        uniqueKeywordSelected.add(uniqueKeywordSelectedText);
        uniqueKeywordSelected.add(uniqueKeywordSelectedID);

        UniqueKeywordsRunnable uniqueKeywordsRunnable = new UniqueKeywordsRunnable(panel, uniqueKeywordScroll, uniqueKeywordInner, stores, uniqueKeywordSelectedText, uniqueKeywordSelectedID);

        JButton uniqueKeywordSelectedButton = new JButton("Search");
        uniqueKeywordSelectedButton.setForeground(Constants.getFontColor());
        uniqueKeywordSelectedButton.setBackground(Constants.getBackground());
        uniqueKeywordSelectedButton.setBounds(185, 525, 50, 45);
        
        uniqueKeywordSelector.add(uniqueKeywordSelected);
        uniqueKeywordSelector.add(uniqueKeywordSelectedButton);

        JPanel selectedKeywordOuter = new JPanel();
        selectedKeywordOuter.setBounds(400, 0, 725, 500);
        selectedKeywordOuter.setBackground(Constants.getHighlight());

        SelectedKeywordMovieReel selectedKeywordMovieReel = new SelectedKeywordMovieReel(panel, stores);
        selectedKeywordMovieReel.setBounds(400, 5, 720, 490);
        selectedKeywordMovieReel.setMinimumSize(new Dimension(625, 490));
        selectedKeywordMovieReel.setPreferredSize(new Dimension(625, 490));
        selectedKeywordMovieReel.getViewport().setMinimumSize(new Dimension(625, 490));
        selectedKeywordMovieReel.getViewport().setPreferredSize(new Dimension(625, 490));
        selectedKeywordOuter.add(selectedKeywordMovieReel);

        uniqueKeywordSelectedButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (uniqueKeywordSelectedID.getText() == "No ID") {
                    System.out.println("No ID provided, not searching");
                    return;
                }
                selectedKeywordMovieReel.setKeyword(uniqueKeywordSelectedText.getText(), Integer.parseInt(uniqueKeywordSelectedID.getText()));
                SwingUtilities.invokeLater(selectedKeywordMovieReel);
            }
        });

        JPanel separator = new JPanel();
        separator.setBackground(Constants.getBackground());
        separator.setBounds(0, 500, 1090, 10);

        TitledBorder keywordsForSelectedFilmBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Keywords for Selected Film");
        keywordsForSelectedFilmBorder.setTitleColor(Constants.getFontColor());
        JPanel keywordsForSelectedFilm = new JPanel();
        keywordsForSelectedFilm.setBounds(220, 515, 855, 115);
        keywordsForSelectedFilm.setBorder(keywordsForSelectedFilmBorder);
        keywordsForSelectedFilm.setBackground(Constants.getHighlight());
        JPanel keywordsForSelectedFilmInner = new JPanel();
        keywordsForSelectedFilmInner.setBackground(Constants.getHighlight());
        JScrollPane keywordsForSelectedFilmScroll = new JScrollPane(keywordsForSelectedFilmInner);
        keywordsForSelectedFilmScroll.setBounds(225, 520, 845, 90);
        keywordsForSelectedFilmScroll.setMinimumSize(new Dimension(845, 90));
        keywordsForSelectedFilmScroll.setPreferredSize(new Dimension(845, 90));
        keywordsForSelectedFilmScroll.getViewport().setMinimumSize(new Dimension(845, 90));
        keywordsForSelectedFilmScroll.getViewport().setPreferredSize(new Dimension(845, 90));
        keywordsForSelectedFilm.add(keywordsForSelectedFilmScroll);

        KeywordsFromFilmRunnable keywordsFromFilmRunnable = new KeywordsFromFilmRunnable(panel, keywordsForSelectedFilmScroll, keywordsForSelectedFilmInner, stores, uniqueKeywordSelectedText, uniqueKeywordSelectedID, keywordsForSelectedFilmBorder);

        TitledBorder filmSelectorBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), "Film Selector");
        filmSelectorBorder.setTitleColor(Constants.getFontColor());
        JPanel filmSelector = new JPanel();
        filmSelector.setBounds(5, 515, 200, 115);
        filmSelector.setBorder(filmSelectorBorder);
        filmSelector.setBackground(Constants.getHighlight());

        JLabel filmIDSelectorText = new JLabel("Film ID:");
        filmIDSelectorText.setBackground(Constants.getHighlight());
        filmIDSelectorText.setForeground(Constants.getFontColor());

        NumberFormat filmIDFormat = NumberFormat.getIntegerInstance();
        filmIDFormat.setGroupingUsed(false);
        NumberFormatter filmIDFormatter = new NumberFormatter(filmIDFormat);
        filmIDFormatter.setValueClass(Integer.class);
        filmIDFormatter.setMinimum(0);
        filmIDFormatter.setMaximum(Integer.MAX_VALUE);
        filmIDFormatter.setAllowsInvalid(false);
        JFormattedTextField filmTextField = new JFormattedTextField(filmIDFormatter);
        filmTextField.setBounds(55, 525, 100, 25);
        filmTextField.setMinimumSize(new Dimension(100, 25));
        filmTextField.setPreferredSize(new Dimension(100, 25));
        filmTextField.setText("0");
        filmTextField.setHorizontalAlignment(JTextField.CENTER);
        filmTextField.setForeground(Constants.getFontColor());
        filmTextField.setBackground(Constants.getBackground());

        JButton filmSelectorButton = new JButton("Search");
        filmSelectorButton.setForeground(Constants.getFontColor());
        filmSelectorButton.setBackground(Constants.getBackground());
        filmSelectorButton.setBounds(5, 570, 50, 45);
        filmSelectorButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                keywordsFromFilmRunnable.setFilmID(Integer.parseInt(filmTextField.getText()));
                SwingUtilities.invokeLater(keywordsFromFilmRunnable);
            }
        });
        
        filmSelector.add(filmIDSelectorText);
        filmSelector.add(filmTextField);
        filmSelector.add(filmSelectorButton);

        panel.add(uniqueKeywordSelector);
        panel.add(selectedKeywordOuter);
        panel.add(separator);
        panel.add(filmSelector);
        panel.add(keywordsForSelectedFilm);

        SwingUtilities.invokeLater(uniqueKeywordsRunnable);
        SwingUtilities.invokeLater(selectedKeywordMovieReel);

        panel.setVisible(true);
    }
}

class KeywordsFromFilmRunnable implements Runnable {
    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private int filmID;
    private JLabel loadingText;
    private JLabel keywordID;
    private JLabel keywordText;
    private TitledBorder border;

    public KeywordsFromFilmRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores, JLabel keywordText, JLabel keywordID, TitledBorder border) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;
        this.filmID = -1;
        this.keywordText = keywordText;
        this.keywordID = keywordID;
        this.border = border;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Please enter a valid film ID");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);
    }

    public void setFilmID(int filmID) {
        System.out.println("\tKeywords from Film ID set: " + filmID);
        this.filmID = filmID;
    }

    @Override
    public void run() {
        System.out.println("\tID provided: " + filmID);

        if (filmID < 0) {
            System.out.println("\tInvalid ID provided");
            loadingText.setText("Please enter a valid film ID");
            return;
        }

        String searchText = "Searching for film ";
        String borderText = "Keywords for ";
        String filmName = stores.getMovies().getTitle(filmID);
        if (filmName == null) {
            searchText += "<UNKNOWN FILM NAME>";
            borderText += "<UNKNOWN FILM NAME>";
        } else if (filmName == "") {
            searchText += "<EMPTY FILM NAME>";
            borderText += "<EMPTY FILM NAME>";
        } else {
            searchText += filmName;
            borderText += filmName;
        }
        searchText += " (id: " + filmID + ")...";
        borderText += " (id: " + filmID + ")";

        System.out.println("\t"+searchText);
        loadingText.setText(searchText);
        border.setTitle(borderText);

        scrollPane.setVisible(false);

        Keyword[] filmKeywords = stores.getKeywords().getKeywordsForFilm(filmID);

        if (filmKeywords == null || filmKeywords.length == 0) {
            System.out.println("\tNo keywords found for film with ID: " + filmID);
            loadingText.setText("No keywords found for film with ID: " + filmID);
            scrollPane.setVisible(true);
            return;
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        final int gapSize = 5;
        final int castPanelWidth = (int) (resultsPanel.getWidth());
        int currentWidth = 0;
        int currentHeight = gapSize;
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setBackground(Constants.getHighlight());

        for (int i = 0; i < filmKeywords.length; i++) {
                Keyword keyword = filmKeywords[i];
                JPanel tmpPanel = new JPanel();
                tmpPanel.setBackground(Constants.getBackground());
                JLabel tmpLabel = new JLabel(filmKeywords[i].getName());
                tmpLabel.setForeground(Constants.getFontColor());
                tmpPanel.add(tmpLabel);
                tmpPanel.addMouseListener(new MouseInputAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                keywordText.setText(keyword.getName());
                                keywordID.setText(Integer.toString(keyword.getID()));
                            }
                        });
                    }
                });

                int stringWidth = masterPanel.getGraphics().getFontMetrics().stringWidth(keyword.getName()) + 40;
                if (currentWidth + (4 * gapSize) + stringWidth > castPanelWidth) {
                    currentHeight += 30 + gapSize;
                    tmpPanel.setBounds(gapSize, currentHeight, stringWidth, 30);
                    currentWidth = gapSize + stringWidth;
                    resultsPanel.setSize(castPanelWidth, currentHeight + gapSize + 30);
                    resultsPanel.add(horizontalPanel);
                    horizontalPanel = new JPanel();
                    horizontalPanel.setBackground(Constants.getHighlight());
                    horizontalPanel.add(tmpPanel);
                } else {
                    tmpPanel.setBounds(currentWidth + gapSize, currentHeight, stringWidth, 30);
                    currentWidth += gapSize + stringWidth;
                    horizontalPanel.add(tmpPanel);
                }
                
                resultsPanel.add(horizontalPanel);
            }

        scrollPane.setVisible(true);
    }
}

class UniqueKeywordsRunnable implements Runnable {
    private JPanel masterPanel;
    private JScrollPane scrollPane;
    private JPanel resultsPanel;
    private AbstractStores stores;
    private JLabel keywordText;
    private JLabel keywordID;
    private JLabel loadingText;

    public UniqueKeywordsRunnable(JPanel masterPanel, JScrollPane scrollPane, JPanel resultsPanel, AbstractStores stores, JLabel keywordText, JLabel keywordID) {
        this.masterPanel = masterPanel;
        this.scrollPane = scrollPane;
        this.resultsPanel = resultsPanel;
        this.stores = stores;

        this.keywordText = keywordText;
        this.keywordID = keywordID;

        scrollPane.setVisible(false);

        loadingText = new JLabel("Searching for unique keywords...");
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        resultsPanel.add(loadingText);

        scrollPane.setVisible(true);
    }

    @Override
    public void run() {
        Keyword[] uniqueKeywords = stores.getKeywords().getUnique();

        scrollPane.setVisible(false);

        if (uniqueKeywords == null || uniqueKeywords.length == 0) {
            loadingText.setText("No unique keywords were found!");
            System.out.println("\tNo unique keywords were found");
            scrollPane.setVisible(true);
            return;
        } else {
            loadingText.setText("Processing " + uniqueKeywords.length + " unique keywords...");
            System.out.println("\t" + uniqueKeywords.length + " unique keywords found");
        }

        final int itemHeight = 50;

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        final int gapSize = 5;
        final int castPanelWidth = (int) (resultsPanel.getWidth());
        int currentWidth = 0;
        int currentHeight = gapSize;
        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setBackground(Constants.getHighlight());

        for (int i = 0; i < uniqueKeywords.length; i++) {
                Keyword keyword = uniqueKeywords[i];
                JPanel tmpPanel = new JPanel();
                tmpPanel.setBackground(Constants.getBackground());
                JLabel tmpLabel = new JLabel(uniqueKeywords[i].getName());
                tmpLabel.setForeground(Constants.getFontColor());
                tmpPanel.add(tmpLabel);
                tmpPanel.addMouseListener(new MouseInputAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                keywordText.setText(keyword.getName());
                                keywordID.setText(Integer.toString(keyword.getID()));
                            }
                        });
                    }
                });

                int stringWidth = masterPanel.getGraphics().getFontMetrics().stringWidth(keyword.getName()) + 40;
                if (currentWidth + (4 * gapSize) + stringWidth > castPanelWidth) {
                    currentHeight += 30 + gapSize;
                    tmpPanel.setBounds(gapSize, currentHeight, stringWidth, 30);
                    currentWidth = gapSize + stringWidth;
                    resultsPanel.setSize(castPanelWidth, currentHeight + gapSize + 30);
                    resultsPanel.add(horizontalPanel);
                    horizontalPanel = new JPanel();
                    horizontalPanel.setBackground(Constants.getHighlight());
                    horizontalPanel.add(tmpPanel);
                } else {
                    tmpPanel.setBounds(currentWidth + gapSize, currentHeight, stringWidth, 30);
                    currentWidth += gapSize + stringWidth;
                    horizontalPanel.add(tmpPanel);
                }
                
                resultsPanel.add(horizontalPanel);
            }

        scrollPane.setVisible(true);
    }
}

class SelectedKeywordMovieReel extends MovieReel {
    private Keyword keyword = null;
    public SelectedKeywordMovieReel(JPanel screenPanel, AbstractStores stores) {
        super(screenPanel, stores, "Films that contain the selected keyword", "No keyword selected");
    }

    public SelectedKeywordMovieReel(JPanel screenPanel, AbstractStores stores, Keyword keyword) {
        super(screenPanel, stores, "Films that contain the keyword \"" + keyword.getName()+"\"", "Searching for films that have the keyword \"" + keyword.getName() + "\" (" + keyword.getID() + ")...");
        this.keyword = keyword;
    }

    public void setKeyword(String keywordText, int keywordID) {
        if (keywordText == null || keywordText == "No Keyword") {return;}
        Keyword keyword = new Keyword(keywordID, keywordText);
        setLoadingText("Searching for films that have the keyword \"" + keyword.getName() + "\" (" + keyword.getID() + ")...");
        this.keyword = keyword;
    }

    public void run() {
        if (keyword == null) {
            System.out.println("Running Keyword to Film --> null");
            System.out.println("Finished running Keyword to Film");
            return;
        }

        System.out.println("Running Keyword to Film --> " + keyword.getName() + " (" + keyword.getID()+")");
        int[] movieResults = stores.getKeywords().getFilmsWithKeyword(keyword.getID());

        if (movieResults == null || movieResults.length == 0){
            System.out.println("No movies with this particular keyword ID found");
        } else {
            String[] labels = new String[movieResults.length];
            for (int i = 0; i < movieResults.length; i++) {
                labels[i] = String.format("%s", stores.getMovies().getTitle(movieResults[i]));
            }
            displayItems(movieResults, labels);
            System.out.println("Finished running Keyword to Film");
        }
    }
}
