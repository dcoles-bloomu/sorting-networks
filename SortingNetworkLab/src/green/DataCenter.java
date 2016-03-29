package green;

import hardware.BinarySequence;
import hardware.Network;
import hardware.Utilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Displays a list of comparators appended to the Green filter and a list of unsorted outputs 
 * produced by the resulting comparison network.
 * 
 * @author Drue Coles
 */
public class DataCenter extends JPanel {

    private final JTextArea addedComparisonsTextArea = new JTextArea(8, 30);
    private final JTextArea unsortedOutputsTextArea = new JTextArea(8, 100);
    private final Font font = new Font(Font.MONOSPACED, Font.BOLD, 14);
    private final  TitledBorder tBorder;
    private final TitledBorder tBorder2;
    private final EtchedBorder eBorder;
    private final JScrollPane scrollPane1;
    private final JScrollPane scrollPane2;
            
    /**
     * Creates scroll panes and borders for displaying added comparisons and unsorted outputs.
     * 
     * @param set a set of binary sequences representing unsorted outputs
     */
    public DataCenter(Set<BinarySequence> set) {
        setLayout(new BorderLayout());
        initTextArea(addedComparisonsTextArea);
        initTextArea(unsortedOutputsTextArea);
        
        scrollPane1 = new JScrollPane(addedComparisonsTextArea);
        scrollPane2 = new JScrollPane(unsortedOutputsTextArea);
        scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                
        eBorder = new EtchedBorder();
        tBorder =  new TitledBorder(eBorder, "Added Comparisons: 0");
        tBorder.setTitleColor(Color.CYAN);
        scrollPane1.setBackground(Color.DARK_GRAY);
        scrollPane1.setBorder(tBorder);        
        
        tBorder2 = new TitledBorder(eBorder, "Unsorted Outputs: 151");
        tBorder2.setTitleColor(Color.CYAN);        
        scrollPane2.setBackground(Color.DARK_GRAY);
        scrollPane2.setBorder(tBorder2);
        
        
        add(scrollPane1, BorderLayout.WEST);
        add(scrollPane2, BorderLayout.CENTER);
        setBorder(new EtchedBorder());
        
        set.stream().forEach((seq) -> {
            unsortedOutputsTextArea.append(seq.toString() + " ");
        });
    }
    
    public void setComparisonsTextArea(Network n) {
        addedComparisonsTextArea.setText(n.toString());
        tBorder.setTitle("Added Comparisons: " + n.size());
        scrollPane1.repaint();  
    }
    
    public void setUnsortedOutputsTextArea(Network n) {
        Set<BinarySequence> set = n.getUnsortedOutputs(Utilities.getGreenFilter().operateOnAll());
        unsortedOutputsTextArea.setText("");
        set.stream().forEach((seq) -> {
            unsortedOutputsTextArea.append(seq.toString() + " ");
        });
        tBorder2.setTitle("Unsorted Outputs: " + set.size());
        scrollPane2.repaint();  
    }
    
    private void initTextArea(JTextArea t) {
            t.setEditable(false);
            t.setBackground(Color.BLACK);
            t.setForeground(Color.YELLOW);
            t.setFont(font);
            t.setLineWrap(true);
            t.setWrapStyleWord(true);            
    }  
}
