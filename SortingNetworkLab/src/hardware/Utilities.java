package hardware;

import java.util.ArrayList;
import java.util.Set;

/**
 * A collection of static methods dealing with comparison networks.
 * 
 * @author Drue Coles
 */
public class Utilities {

    // The first 32 comparators of Green's 16-wire network, partitioned into sets of comparators
    // (here called layers) that can be drawn at the same horizontal depth.
    private static final ArrayList<int[][]> layers = new ArrayList<>();
    static {
        layers.add(new int[][] {{0, 1}, {2, 3}, {4, 5}, {6, 7}, {8, 9}, {10, 11}, {12, 13}, 
            {14, 15}});
        layers.add(new int[][] {{0, 2}, {4, 6}, {8, 10}, {12, 14}});
        layers.add(new int[][] {{1, 3}, {5, 7}, {9, 11}, {13, 15}});
        layers.add(new int[][] {{0, 4}, {8, 12}});
        layers.add(new int[][] {{1, 5}, {9, 13}});
        layers.add(new int[][] {{2, 6}, {10, 14}});
        layers.add(new int[][] {{3, 7}, {11, 15}});
        layers.add(new int[][] {{0, 8}});
        layers.add(new int[][] {{1, 9}});
        layers.add(new int[][] {{2, 10}});
        layers.add(new int[][] {{3, 11}});
        layers.add(new int[][] {{4, 12}});
        layers.add(new int[][] {{5, 13}});
        layers.add(new int[][] {{6, 14}});
        layers.add(new int[][] {{7, 15}});
    }
        
    /**
     * Returns the comparators at a given depth in the Green filter (i.e., the first 32 comparisons
     * of Green's sorting network).
     * @param i the depth
     */
    public static int[][] getGreenLayer(int i) {
        return layers.get(i);
    }
    
    /**
     * Returns the depth of the Green filter (i.e., the first 32 comparisons of Green's sorting 
     * network).
     */
    public static int numberOfGreenLayers() {
        return layers.size();
    }
    
    /**
     * @param set a set of binary sequences
     * @return the diameter (maximum pairwise distance of two sequences) of the set
     */
    public static int diameter(Set<BinarySequence> set) {
        int maxDistance = 0;
        for (BinarySequence x : set) {
            for (BinarySequence y : set) {
                int dist = x.distance(y);
                if (dist > maxDistance) {
                    maxDistance = dist;
                }
            }
        }
        return maxDistance; 
    }
    
    /**
     * @return a comparison network obtained by restricting Green's sorting network on 16 wires to
     * its first 32 comparisons
     */
    public static Network getGreenFilter() {
        Network network = new Network(16);
        for (int i = 0; i < 16; i +=2 ) {
            network.addComparator(i, i + 1);
        }
        for (int i = 0; i < 16; i += 4) {
            network.addComparator(i, i + 2);
            network.addComparator(i + 1, i + 3);
        }
        for (int i = 0; i < 4; i++) {
            network.addComparator(i, i + 4);
            network.addComparator(i + 8, i + 12);
        }
        for (int i = 0; i < 8; i++) {
            network.addComparator(i, i + 8);
        }
        return network;
    }
}
