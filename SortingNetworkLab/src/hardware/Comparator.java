package hardware;

/**
 * A comparator that can the swap values on a pair of wires. Following the standard visual 
 * representation of comparison networks using Knuth diagrams, the wires are imagined to extend 
 * horizontally and the goal is to sort the values in ascending order from top to bottom. 
 * 
 * @author Drue Coles
 */
public class Comparator {
    
    // indices of the wires to which this comparator is attached
    private int top; 
    private int bottom;
    
    /**
     * Creates a new comparator on a specified pair of wires.
     * 
     * @param top
     * @param bottom 
     */
    public Comparator(int top, int bottom) {
        this.top = Math.min(bottom, top);
        this.bottom = Math.max(bottom, top);
    }

    /**
     * @return index of the top wire to which this comparator is attached
     */
    public int getTop() {
        return top;
    }

    /**
     * @return index of the bottom wire to which this comparator is attached
     */    
    public int getBottom() {
        return bottom;
    }    
    
    /**
     * Sets the top wire index.
     * @param top new index for the top wire
     */
    public void setTop(int top) {
        this.top = top;
    }
    
    /**
     * Sets the bottom wire index
     * @param bottom new index for the bottom wire
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }
    
    /**
     * Sorts the bits on the pair of wires to which this comparator is attached.
     * 
     * @param wires the values carried by the wires of the network
     */
    public void operateOn(BinarySequence wires) {
        wires.sort(top, bottom);
    }
    
    /**
     * @return the wire indices (two digits) of this comparator
     */
    @Override
    public String toString() {
        if (bottom < 10) {
            return "0" + top + "-0" + bottom;
        }
        if (top < 10) {
            return "0" + top + "-" + bottom;
        }
        return top + "-" + bottom;
    }
}
