package hardware;

import java.util.Arrays;

/**
 * Represents binary strings of fixed length.
 *
 * @author Drue Coles
 */
public class BinarySequence implements Comparable {

    private final byte[] sequence;

    /**
     * Constructs a binary sequence consisting of all zeros.
     *
     * @param n the length of the sequence
     */
    public BinarySequence(int n) {
        sequence = new byte[n];
        for (int i = 0; i < n; i++) {
            sequence[i] = 0;
        }
    }

    /**
     * Constructs a binary sequence as a copy of an existing sequence
     */
    public BinarySequence(byte[] sequence) {
        this.sequence = sequence.clone();
    }

    /**
     * Operates on two bits of the sequence by swapping them if necessary so that the first bit is
     * smaller or equal to the second.
     *
     * @param i index of first bit
     * @param j index of second bit
     */
    public void sort(int i, int j) {
        if (sequence[i] == 1 && sequence[j] == 0) {
            sequence[i] = 0;
            sequence[j] = 1;
        }
    }

    /**
     * Returns true if the bits in this sequence are monotonically increasing.
     */
    public boolean isSorted() {
        for (int i = 0; i < sequence.length - 1; i++) {
            if (sequence[i] > sequence[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Mutates this sequence by interpreting it as a binary integer and adding 1.
     */
    public void increment() {
        byte carry = 1;
        for (int i = sequence.length - 1; i >= 0; i--) {
            byte nextCarry = (byte) (carry * sequence[i]);
            sequence[i] = (byte) ((sequence[i] + carry) % 2);
            carry = nextCarry;
        }
    }

    /**
     * Returns true if all bits in this sequence are zero.
     */
    public boolean allZeros() {
        for (byte b : this.sequence) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the Hamming distance between this binary sequence and another.
     */
    public int distance(BinarySequence other) {
        int dist = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] != other.sequence[i]) {
                dist++;
            }
        }
        return dist;
    }

    /**
     * Returns a clone of this binary sequence.
     */
    public BinarySequence clone() {
        return new BinarySequence(this.sequence);
    }

    /**
     * Returns true if this binary sequence is the same as another given sequence.
     */
    @Override
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }

    /**
     * Returns 0 if this binary sequence is the same as another given sequence, or -1/+1 if the 
     * first 1 of this sequence occurs before/after the first one of the other. 
     */
    @Override
    public int compareTo(Object o) {
        BinarySequence other = (BinarySequence) o;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] < other.sequence[i]) {
                return -1;
            }
            if (sequence[i] > other.sequence[i]) {
                return 1;
            }
        }
        return 0;
    }

    /**
     * Returns a string a bits denoting this binary sequence.
     */
    @Override
    public String toString() {
        String str = "";
        for (byte b : sequence) {
            str += b;
        }
        return str;
    }

    /**
     * Returns a hash code for this binary sequence.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Arrays.hashCode(this.sequence);
        return hash;
    }
}
