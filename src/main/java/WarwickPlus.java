import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.InsetsUIResource;

import org.apache.commons.cli.*;

import interfaces.*;
import screen.*;
import stores.Stores;
import utils.Constants;
import utils.DisplayImage;
import utils.LoadData;
import utils.LoadData.DataLoadException;

public class WarwickPlus {
    // private static AbstractStores stores = new Stores();

    public static int getHSize() {
        return Constants.hSize;
    }

    public static int getVSize(){
        return Constants.vSize;
    }
    public static void main(String[] args) {

        // Parse command line arguments
        Options options = new Options();
        
        options.addOption(Option.builder().longOpt("credits").argName("credits csv file")
                                .hasArg().desc("The credits csv file to be used")
                                .build());
        options.addOption(Option.builder().longOpt("keywords").argName("keywords csv file")
                                .hasArg().desc("The keywords csv file to be used")
                                .build());
        options.addOption(Option.builder().longOpt("movies").argName("movies csv file")
                                .hasArg().desc("The movies csv file to be used")
                                .build());
        options.addOption(Option.builder().longOpt("ratings").argName("ratings csv file")
                                .hasArg().desc("The ratings csv file to be used")
                                .build());
        options.addOption(Option.builder("n").argName("number of movies").hasArg()
                                .desc("The number of movies to load in (and thereby loading in less of the other files too)")
                                .build());
        options.addOption("h","help", false, "Show this help message");

        // Handle input data files.

        String creditsPath       = Constants.defaultCreditsPath;
        String keywordsPath      = Constants.defaultKeywordsPath;
        String movieMetadataPath = Constants.defaultMovieMetadataPath;
        String ratingsPath       = Constants.defaultRatingsPath;
        Integer numMovies        = null;

        CommandLineParser cliParser = new DefaultParser();
        try{
            CommandLine line = cliParser.parse(options, args);
            if (line.hasOption("help")){
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("WarwickPlus", options);
                return;
            }
            
            if (line.hasOption("credits"))  { creditsPath  = line.getOptionValue("credits"); }
            if (line.hasOption("keywords")) { keywordsPath = line.getOptionValue("keywords"); }
            if (line.hasOption("movies"))   { movieMetadataPath = line.getOptionValue("movies"); }
            if (line.hasOption("ratings"))  { ratingsPath  = line.getOptionValue("ratings"); }
            if (line.hasOption("n")) 
            { 
                String optionString = line.getOptionValue("n");
                try{
                    numMovies    = Integer.parseInt(optionString); 
                }
                catch (NumberFormatException e){
                    System.out.println("Unable to parse \"" + optionString + "\" as an integer number of strings");
                    throw e;
                }
                if (numMovies <= 0){
                    System.out.println("n parameter must be a positive integer");
                    return;
                }

            }
        }
        catch (ParseException e){
            System.err.println("Exception in parsing command line arguments: \n" + e.getMessage());
            return;
        }

        AbstractStores stores = new Stores();
        
        start(stores, creditsPath, keywordsPath, movieMetadataPath, ratingsPath, numMovies);
    }

    static public void start(AbstractStores stores){
        start(stores, Constants.defaultCreditsPath, Constants.defaultKeywordsPath, Constants.defaultMovieMetadataPath, Constants.defaultRatingsPath, null);
    }

    static public void start(AbstractStores stores, Integer numMovies){
        start(stores, Constants.defaultCreditsPath, Constants.defaultKeywordsPath, Constants.defaultMovieMetadataPath, Constants.defaultRatingsPath, numMovies);
    }

    static public void start(AbstractStores stores, String creditsPath, String keywordsPath, String movieMetadataPath, String ratingsPath, Integer numMovies) {
        //Create the UI
        JFrame frame = new JFrame("Warwick+");
        frame.setVisible(false);

        frame.setBackground(Constants.getBackground());
        frame.getContentPane().setBackground(Constants.getBackground());
        ImageIcon icon = new ImageIcon(Constants.getIcon());
        frame.setIconImage(icon.getImage());

        DisplayImage logo;
        try {
            logo = new DisplayImage("src/main/resources/img/WarwickPlusLogo.png");
            logo.setBounds(0, (int) (Constants.vSize * 0.125), Constants.hSize, (int) (Constants.vSize * 0.25));
            frame.getContentPane().add(logo);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
       

        JProgressBar loadingBar = new JProgressBar(0, 100000);
        loadingBar.setBounds(40, (int) (Constants.vSize*0.5), (Constants.hSize-(40*2)), 40);
        loadingBar.setValue(JProgressBar.CENTER);
        loadingBar.setStringPainted(true);

        JLabel loadingText = new JLabel("Loading...");
        loadingText.setBounds(0, (int) (Constants.vSize*0.75), Constants.hSize, 40);
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());

        frame.getContentPane().add(loadingBar);
        frame.getContentPane().add(loadingText);

        frame.setSize(Constants.hSize, Constants.vSize);

        //frame.add(content);

        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try{
            LoadData loading = new LoadData(loadingBar, loadingText, creditsPath, keywordsPath, movieMetadataPath, ratingsPath);
            //Populate the student stores
            if (numMovies == null){
                loading.populate(stores);
            }
            else {
                loading.populate(stores, 0, numMovies);
            }

            System.out.println("\nFinished Loading...");
            setHomescreen(frame.getContentPane(), stores);
        }
        catch (DataLoadException e){
            // Something went wrong in loading
            // Note: Assumes that errors caused in the middle of a file will contain a record count.
            loadingText.setVisible(false); 
            loadingBar.setValue(JProgressBar.CENTER); // Put loading bar back to 0% as it failed.
            JLabel errorText = new JLabel("<html><div style='text-align: center'>" + e + "<br> See Terminal for details.</div></html>");
            System.err.println(e.getMessage());
            errorText.setBounds(0, (int) (Constants.vSize*0.75), Constants.hSize, 40);
            errorText.setHorizontalAlignment(JLabel.CENTER);
            errorText.setForeground(Constants.getFontColor());
            frame.getContentPane().add(errorText);
        }
        
    }

    private static void setHomescreen(Container frame, AbstractStores stores) {
        System.out.println("Home screen");
        frame.setVisible(false);
        frame.removeAll();

        JPanel content = new JPanel();
        JTextField searchBox = new JTextField("Search...");

        content.setBounds((int) (Constants.hSize * 0.15), (int) (Constants.vSize * 0.07), (int) (Constants.hSize * 0.85), (int) (Constants.vSize * 0.88));
        content.setBackground(Constants.getHighlight());
        content.setForeground(Constants.getFontColor());
        content.setLayout(null);

        //Build logo with required click listener
        DisplayImage logo;
        try {
            logo = new DisplayImage("src/main/resources/img/WarwickPlusLogo.png");
            logo.setBounds(0, (int) (Constants.vSize * 0.01), (int) (Constants.hSize * 0.15), (int) (Constants.vSize * 0.05));
            logo.addMouseListener(new MouseInputAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    content.removeAll();
                    searchBox.setText("Search...");
                    setHomescreen(frame, stores);
                }
            });

            frame.add(logo);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        //Build search box with required listeners
        searchBox.setBounds((int) (Constants.hSize * 0.15), (int) (Constants.vSize * 0.01), (int) (Constants.hSize*0.85), (int) (Constants.vSize * 0.05));
        searchBox.setBackground(Constants.getHighlight());
        searchBox.setForeground(Constants.getFontColor());
        searchBox.setMargin(new InsetsUIResource(3, 3, 3, 3));
        searchBox.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {searchBox.setText("");}
        });
        searchBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        SearchScreen.createPanel(content, e.getActionCommand().toString(), stores);
                    }
                });
            }
        });

        String[] menuString = {"Cast", "Crew", "Ratings", "Keywords"};
        int menuVStart = (int) (Constants.vSize*0.07);
        int menuVEnd = (int) (Constants.vSize*0.7);
        int menuVSize = (menuVEnd - menuVStart)/menuString.length;

        for (int i = 0; i < menuString.length; i++) {
            JPanel menuItemPanel = new JPanel();
            menuItemPanel.setBounds(0, menuVStart + (menuVSize * i), (int) (Constants.hSize * 0.15), menuVSize);
            menuItemPanel.setLayout(new GridBagLayout());
            menuItemPanel.setBackground(Constants.getBackground());
            menuItemPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            switch(menuString[i]) {
                case "Cast": 
                    menuItemPanel.addMouseListener(new MouseInputAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    CastScreen.createPanel(content, stores);
                                }
                            });
                        }
                    });
                    break;
                case "Crew": 
                    menuItemPanel.addMouseListener(new MouseInputAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    CrewScreen.createPanel(content, stores);
                                }
                            });
                        }
                    });
                    break;
                case "Ratings": 
                    menuItemPanel.addMouseListener(new MouseInputAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    RatingsScreen.createPanel(content, stores);
                                }
                            });
                        }
                    });
                    break;

                    case "Keywords": 
                    menuItemPanel.addMouseListener(new MouseInputAdapter() {

                        @Override
                        public void mouseClicked(MouseEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    KeywordScreen.createPanel(content, stores);
                                }
                            });
                        }
                    });
                    break;
                default:
                    System.err.println("\tNot sure where to go with \"" + menuString[i] + "\"...");
            }

            JLabel menuItemLabel = new JLabel(menuString[i]);
            menuItemLabel.setHorizontalAlignment(JLabel.CENTER);
            menuItemLabel.setVerticalAlignment(JLabel.CENTER);
            menuItemLabel.setForeground(Constants.getFontColor());
            
            menuItemPanel.add(menuItemLabel);

            frame.add(menuItemPanel);
        }

        //Build stat block
        String statString = " Films: " + stores.getMovies().size() + " movies\n Film Credits: " + stores.getCredits().size() + " movies\n     Unique Cast: ";
        if (stores.getCredits().getUniqueCast() == null ) {
            statString += "???";
        } else {
            statString += stores.getCredits().getUniqueCast().length;
        }
        statString += "\n     Unique Crew: ";
        if (stores.getCredits().getUniqueCrew() == null) {
            statString += "???";
        } else {
            statString += stores.getCredits().getUniqueCrew().length;
        }
        statString += "\n Ratings: " + stores.getRatings().size() + " ratings \n Keywords: " + stores.getKeywords().size() + " movies\n     (";
        if (stores.getKeywords().getUnique() == null) {
            statString += "???";
        } else {
            statString += stores.getKeywords().getUnique().length;
        }
        statString += " unique keywords)";

        JTextArea stats = new JTextArea(statString);
        stats.setForeground(Constants.getFontColor());
        stats.setBackground(Constants.getBackground());
        stats.setEditable(false);
        stats.setMargin(new InsetsUIResource(3, 3, 3, 3));
        stats.setBounds(0, (int) (Constants.vSize * 0.75), (int) (Constants.hSize * 0.15), (int) (Constants.vSize * 0.25));

        //Build frame
        frame.add(searchBox);
        frame.add(stats);
        frame.add(content);

        frame.setVisible(true);

        //Go to the ratings screen by default:
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HomeScreen.createPanel(content, stores);                                    
            }
        });
    }
}