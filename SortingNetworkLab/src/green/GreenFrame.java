package green;

import hardware.BinarySequence;
import hardware.Utilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 * A frame that contains a Green Component and some menu options.
 * 
 * @author Drue Coles
 */
public class GreenFrame extends JFrame {

    private GreenComponent greenComponent;
    private boolean showGreen = true;
    private JMenuItem viewFilterMenuItem;
    
    public GreenFrame(String title) {
        super(title);        
        Set<BinarySequence> set = Utilities.getGreenFilter().operateOnAll();
        DataCenter dataCenter = new DataCenter(set);
        greenComponent = new GreenComponent(dataCenter);
        add(greenComponent, BorderLayout.CENTER);        
        add(dataCenter, BorderLayout.SOUTH);
        setJMenuBar(getMyMenuBar());
    }
    
    public static void main(String[] args) {
        GreenFrame frame = new GreenFrame("Green Lab");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700); 
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }

    /**
     * Utility method called by constructor to build the menu bar to be attached to this frame.
     */
    private JMenuBar getMyMenuBar() {
        JMenuBar mBar = new JMenuBar();

        JMenu viewMenu = new JMenu("View");
        viewFilterMenuItem = new JMenuItem("Hide Green subnetwork");
        mBar.add(viewMenu);
        viewMenu.add(viewFilterMenuItem);
        
        JMenuItem spacingItem = new JMenuItem("Horizontal auto-space");
        viewMenu.add(spacingItem);

        viewFilterMenuItem.addActionListener((ActionEvent e) -> {
            showGreen = !showGreen;
            if (showGreen) {
                viewFilterMenuItem.setText("Hide Green subnetwork");
            } else {
                viewFilterMenuItem.setText("Show Green subnetwork");
            }
            greenComponent.setGreen32Visible(showGreen);
            greenComponent.repaint();            
        });
        
        spacingItem.addActionListener((ActionEvent e) -> {
            greenComponent.horizontalAutoSpacing();
        });
        
        viewMenu.add(new JSeparator());
        
        JMenuItem resetItem = new JMenuItem("Remove all added comparators");
        viewMenu.add(resetItem);
        
        resetItem.addActionListener((ActionEvent e) -> {
            greenComponent.reset();
        });
        
        JMenu saveMenu = new JMenu("Save");
        JMenuItem comparisonsMenuItem = new JMenuItem("Added comparators");
        saveMenu.add(comparisonsMenuItem);
        mBar.add(saveMenu);
        
        comparisonsMenuItem.addActionListener((ActionEvent e) -> {
            greenComponent.save();            
        });

        return mBar;
    }
}
