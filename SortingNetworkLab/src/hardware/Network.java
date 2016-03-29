package hardware;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A sequence of comparators on a set of wires carrying binary values.
 *
 * @author Drue Coles
 */
public class Network {
    
    private final ArrayList<Comparator> list;
    private final int NUM_WIRES;   
       
    /**
     * Creates an empty comparison network.
     *
     * @param n the number of wires
     */
    public Network(int n) {
        list = new ArrayList<>();
        NUM_WIRES = n;
    }
    
    /**
     * 
     * @param i a wire index
     * @param topOrBottom indicates top (0) or bottom (1)
     * @return the index of the top or bottom wire of this comparator
     */
    public int getComparison(int i, int topOrBottom) {
        return (topOrBottom == 0 ? list.get(i).getTop() : list.get(i).getBottom());
    }
   
    /**
     * Appends a comparator to this network.
     *
     * @param comp a comparator
     */
    public void addComparator(Comparator comp) {
        list.add(comp);
    }

    /**
     * Appends a comparator to this network.
     *
     * @param top index of the top wire
     * @param bottom index of the bottom wire
     */
    public void addComparator(int top, int bottom) {
        list.add(new Comparator(top, bottom));
    }

    /**
     * Inserts a comparator to this network.
     *
     * @param i the index at which to perform the insertion
     * @param top index of the top wire
     * @param bottom index of the bottom wire
     */
    public void addComparator(int i, int top, int bottom) {
        list.add(i, new Comparator(top, bottom));
    }

    /**
     * Inserts a comparator into this network.
     *
     * @param i the index at which to perform the insertion
     * @param comp the comparator to insert
     */
    public void addComparator(int i, Comparator comp) {
        list.add(i, comp);
    }

    /**
     * @return the number of comparators in this network
     */
    public int size() {
        return list.size();
    }

    /**
     * Operates on a binary input sequence.
     *
     * @param wires the binary input sequence
     */
    public void operateOn(BinarySequence wires) {
        list.stream().forEach((comp) -> {
            comp.operateOn(wires);
        });
    }
    
    /**
     * Evaluates this network on all binary sequences. 
     * 
     * @return the set of all unsorted outputs produced by this network
     */
    public Set<BinarySequence> operateOnAll() {
        HashSet<BinarySequence> unsortedOutputs = new HashSet<>();
        BinarySequence seq = new BinarySequence(NUM_WIRES);
        seq.increment();
        while (!seq.allZeros()) {

            BinarySequence testSequence = seq.clone();
            operateOn(testSequence);
            if (!testSequence.isSorted()) {                
                unsortedOutputs.add(testSequence.clone());
            }
            seq.increment();
        }
        return unsortedOutputs;
    }
    
    /**
     * @param inputs a set of input sequences
     * @return the set of all unsorted outputs produced by this network on a given set of inputs
     */
    public Set<BinarySequence> getUnsortedOutputs(Set<BinarySequence> inputs) {

        if (inputs == null) {
            return operateOnAll();
        }
        
        HashSet<BinarySequence> unsortedOutputs = new HashSet<>();

        // Iterate over all sequences in the input set. For each, clone it and operate on it.
        // If the result is not sorted, add it to the set of unsorted outputs.
        inputs.stream().map((seq) -> seq.clone()).map((testSequence) -> {
            operateOn(testSequence);
            return testSequence;
        }).filter((testSequence) -> (!testSequence.isSorted())).forEach((testSequence) -> {
            unsortedOutputs.add(testSequence.clone());
        });
      
        return unsortedOutputs;
    }

    /**
     * @return the comparisons in this network
     */
    @Override
    public String toString() {
        String str = "";
        return list.stream().map((comp) -> (comp + " ")).reduce(str, String::concat).trim();        
    }
}
