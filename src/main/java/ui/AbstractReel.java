package ui;

import java.awt.Image;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputAdapter;

import utils.Constants;

/***
 * Generic Class for a Reel (horizontal sequential view) of images with captions
 * To be used for things like displaying a list of movies with their titles
 * and ratings.
 */
abstract class AbstractReel extends JScrollPane implements Runnable {
    
    protected JPanel screenPanel;
    private JLabel loadingText;

    // private JScrollPane scrollPane;
    private JPanel resultsPanel;

    public AbstractReel(JPanel screenPanel){
        this(screenPanel, "Unknown Title", "Loading Reel");
    }
    public AbstractReel(JPanel screenPanel, String reelTitle, String loadingMessage){
        this.screenPanel = screenPanel;

        //Create panels

        TitledBorder border;
        border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.getFontColor()), reelTitle);
        border.setTitleJustification(TitledBorder.LEFT);
        border.setTitleColor(Constants.getFontColor());

        setBorder(border);
        setBackground(Constants.getHighlight());
        setForeground(Constants.getFontColor());

        //JPanel with all the results in which is viewed by the scrollPane
        resultsPanel = new JPanel();
        resultsPanel.setBackground(Constants.getHighlight());
        resultsPanel.setForeground(Constants.getFontColor());
        setViewportView(resultsPanel); //Set what is being viewed

        //Restrict size of the scrollpane to the dimensions of the resultsPanel.
        setBackground(Constants.getHighlight());
        setForeground(Constants.getFontColor());
        getHorizontalScrollBar().setUnitIncrement(80);

        setVisible(false);
        loadingText = new JLabel(loadingMessage);
        loadingText.setHorizontalAlignment(JLabel.CENTER);
        loadingText.setVerticalAlignment(JLabel.CENTER);
        loadingText.setForeground(Constants.getFontColor());
        resultsPanel.add(loadingText);

        setVisible(true);
    }

    /***
     * Display the items in the reel: A series of pictures with captions.
     * @param items The items to display. Pictures retreived from getImage function
     * @param labels The labels for the items.
     */
    protected void displayItems(int[] items, String[] labels) {
        setVisible(false);

        if (items == null || items.length == 0) {
            loadingText.setText("No reel items found!");
            System.out.println("\tNo reel items found");
            setVisible(true);
            return;
        } else {
            loadingText.setText("Processing " + items.length + " reel items...");
            System.out.println("\t" + items.length + " reel items found (max: " + Constants.topMoviesCount + ")");
        }

        setVisible(false); 
        int itemHeight = getHeight() - 60;
        int itemWidth = (int) (itemHeight * 0.66);

        resultsPanel.removeAll();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.X_AXIS));
        resultsPanel.setSize(itemWidth*items.length, itemHeight);

        for (int i = 0; i < items.length; i++){
            JLabel textLabel = new JLabel(labels[i]);
            int itemId = items[i];

            Image poster = getImage(itemId);
            if (poster == null){
                System.err.println("poster is null");
            }
            textLabel.setIcon(new ImageIcon(poster.getScaledInstance(itemWidth, itemHeight, Image.SCALE_DEFAULT)));
            textLabel.setHorizontalTextPosition(JLabel.CENTER);
            textLabel.setVerticalTextPosition(JLabel.BOTTOM);
            textLabel.setSize(itemWidth, itemHeight);
            textLabel.setForeground(Constants.getFontColor());
            textLabel.addMouseListener(new MouseInputAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            itemClickAction(itemId);
                        }
                    });
                }
            });

            resultsPanel.add(textLabel);
            if (i < items.length-1) {
                resultsPanel.add(new JSeparator(JSeparator.VERTICAL));
            }
        }

        setVisible(true);
    }

    protected void itemClickAction(int itemId){ }

    protected Image getImage(int itemId){
        return null;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText.setText(loadingText);
    }

    public void run(){ }

}
