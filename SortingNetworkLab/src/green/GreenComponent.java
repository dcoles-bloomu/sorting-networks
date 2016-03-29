package green;

import hardware.Network;
import hardware.Utilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.border.EtchedBorder;

/**
 * Presents a 16-wire comparison network with the first 32-comparisons of Green's network fixed in
 * place. The user can append new comparators and observe their effect on the set of unsorted
 * outputs.
 *
 * @author Drue Coles
 */
public class GreenComponent extends JComponent {

    private static final Color BG_COLOR = Color.BLACK;

    // wire colors
    private static final Color GRAD_START = Color.RED; // for gradient paint of wires 
    private static final Color GRAD_END = Color.ORANGE; // for gradient paint of wires 
    private static final Color WIRE_HIGHLIGHT_COLOR = Color.WHITE;

    // comparator colors
    private static final Color FIXED_COMP_COLOR = Color.GREEN; // comparator in Green's network
    private static final Color NEW_COMP_COLOR = Color.CYAN; // comparator added by user
    private static final Color ENDPOINT_COLOR = Color.CYAN; // circular endpoint of comparator    

    // integer constants
    private static final int NUM_WIRES = 16;
    private static final int WIRE_THICKNESS = 5;
    private static final int PADDING = 20; // space between wires and vertical borders of container
    private static final int ENDPOINT_DIAMETER = 10; // circular endpoint of comparator
    private static final int H_SPACE = 30; // horizontal space between sets

    // subnetworks composed into a single network
    private Network afterGreenNetwork = new Network(16);

    // drawing values that depend on size of container
    private int vSpaceBetweenWires;

    // True if the first 32 comparators of Green's network are visible.
    private boolean green32Visible = true;

    // updated via mouse clicks
    private int xCoordOfComparatorToAdd;
    private int wireIndexOfComparatorToAdd;
    private int indexOfWireToHighlight = -1; // when moving mouse over wires

    private final ArrayList<DrawingInfo> addedComparators = new ArrayList<>();
    private int clickCount = 0;

    private DrawingInfo draggingComparator = null;
    private int yAbove = -1; // distance from click point to top of dragged comparator
    private int yBelow = -1; // distance from click point to bot of dragged comparator
    private int dragY = -1;  // current y-coordinate of cursor during drag

    private final DataCenter dataCenter;

    /**
     * Creates a new Green Component and stores a reference to a Data Center where statistics about
     * the subnetwork added by the user are displayed.
     */
    public GreenComponent(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
        setBorder(new EtchedBorder());

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int pixelX = e.getX();
                int pixelY = e.getY();

                // If not on wire, reset.
                if (!onWire(pixelX, pixelY)) {
                    clickCount = 0;
                    repaint();
                    return;
                }

                clickCount++;

                if (clickCount == 1) {
                    xCoordOfComparatorToAdd = pixelX;
                    wireIndexOfComparatorToAdd = toWireIndex(pixelY);

                    // One wire has already been selected, but this click was not on a wire; so we
                    // deselect the first wire.
                    if (wireIndexOfComparatorToAdd == -1) {
                        clickCount = 0;
                    }
                    repaint();
                    return;
                }

                if (clickCount == 2) {
                    int top = wireIndexOfComparatorToAdd;
                    int bot = toWireIndex(pixelY);

                    if (isAfterGreen(pixelX) && top != bot) {
                        addedComparators.add(new DrawingInfo(pixelX, top, bot));
                        afterGreenNetwork = toNetwork(addedComparators);
                        dataCenter.setComparisonsTextArea(afterGreenNetwork);
                        dataCenter.setUnsortedOutputsTextArea(afterGreenNetwork);
                    }
                    clickCount = 0;
                    indexOfWireToHighlight = -1;
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {                
                if (clickCount != 0) {
                    return;
                }
                int x = e.getX();
                int y = e.getY();
                // If the click was on a comparator, remove it from list of added comparators.
                // It will be drawn separately in paintComponent and then added to the list if
                // it is released on a wire.
                draggingComparator = getDrawingInfo(x, y);
                if (draggingComparator != null) {
                    addedComparators.remove(draggingComparator);
                    // calculate distance of y-coordinate of click from top and bottom endpoints.
                    yAbove = y - toPixelPositionY(draggingComparator.top);
                    yBelow = toPixelPositionY(draggingComparator.bot) - y;
                }
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int pixelX = e.getX();
                int pixelY = e.getY();
                if (draggingComparator != null) {
                    draggingComparator = null;
                    clickCount = 0;
                    // If the comparator was dropped with endpoints on wires, add it in its new
                    // position to the list.                    
                    if (onWire(pixelX, pixelY + yBelow)) {
                        int a = toWireIndex(pixelY - yAbove);
                        int b = toWireIndex(pixelY + yBelow);
                        addedComparators.add(new DrawingInfo(pixelX, a, b));
                        Collections.sort(addedComparators);
                    }
                    recalculate();
                }
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (clickCount == 1 && draggingComparator == null) {
                    indexOfWireToHighlight = toWireIndex(e.getY());
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingComparator != null) {
                    draggingComparator.x = e.getX();
                    dragY = e.getY();
                    repaint();
                }
            }

            // Gets a network corresponding to the added comparators
            private Network toNetwork(ArrayList<DrawingInfo> list) {
                DrawingInfo[] t = new DrawingInfo[list.size()];
                for (int i = 0; i < t.length; i++) {
                    t[i] = list.get(i);
                }
                Arrays.sort(t);
                Network n = new Network(16);
                for (DrawingInfo triple : t) {
                    n.addComparator(triple.top, triple.bot);
                }
                return n;
            }

        };
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    /**
     * Draws the subnetwork of comparators added by the user to follow the first 32 comparators of
     * Green's sorting network. By default, Green's subnetwork is shown before the comparators added
     * by the user, but the user can select an option to show only the added comparators.
     *
     * @param g the graphics context
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.addRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        int w = getWidth();
        int h = getHeight();

        // paint the background       
        g2.setColor(BG_COLOR);
        g2.fill(new Rectangle(w, h));

        drawWires(g2, w, h);
        drawComparators(g2);

        // The mouse was clicked on a wire and the cursor is now moving, so highlight the wire as
        // the cursor moves across it. 
        if (clickCount == 1 && draggingComparator == null) {
            drawPoint(g2, xCoordOfComparatorToAdd, wireIndexOfComparatorToAdd, vSpaceBetweenWires,
                    ENDPOINT_COLOR);
        }

        if (draggingComparator != null) {
            drawDraggedComparator(g2, draggingComparator.x, dragY - yAbove, dragY + yBelow);
        }
    }

    /**
     * Remove all added comparators.
     */
    public void reset() {
        addedComparators.clear();
        afterGreenNetwork = new Network(16);
        recalculate();
    }

    /**
     * Modifies data center after a comparator has been added or removed.
     */
    public void recalculate() {
        afterGreenNetwork = new Network(16);
        addedComparators.stream().forEach((t) -> {
            afterGreenNetwork.addComparator(t.top, t.bot);
        });
        dataCenter.setComparisonsTextArea(afterGreenNetwork);
        dataCenter.setUnsortedOutputsTextArea(afterGreenNetwork);
        dataCenter.repaint();
        repaint();
    }

    /**
     * Used to activate or deactivate display of the first 32 comparators of Green's network.
     */
    public void setGreen32Visible(boolean isVisible) {
        green32Visible = isVisible;

        // It now becomes visible, so we have to shift x-coordinates of added comparators to right.
        if (green32Visible) {
            addedComparators.stream().forEach((dInfo) -> {
                dInfo.x += getLeftmostX();
            });
        } // It now becomes invisible, so we shift x-coordinates of added comparators to left.
        else if (addedComparators.size() > 0) {
            int shiftLeft = addedComparators.get(0).x - PADDING - H_SPACE;
            addedComparators.stream().forEach((dInfo) -> {
                dInfo.x -= shiftLeft;
            });
        }
        repaint();
    }

    /**
     * Adjusts x-coordinates of added comparators so that they are uniformly spaced from left to
     * right.
     */
    public void horizontalAutoSpacing() {
        String msg = "Horizontal auto-spacing of comparators is not yet implemented.";
        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Writes added comparators to disk.
     */
    public void save() {
        String msg = "Save added comparators feature not yet implemented.";
        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Returns the smallest x-coordinate at which a comparator can be added, which depends on
     * whether Green's subnetwork is visible.
     */
    private int getLeftmostX() {
        if (green32Visible) {
            return 15 * H_SPACE + 2 * PADDING;
        } else {
            return PADDING;
        }
    }

    private void drawWires(Graphics2D g2, int w, int h) {
        // draw the wires (thin horizontal rectangles with gradient paint)       
        vSpaceBetweenWires = (h - 2 * PADDING) / NUM_WIRES + 2;
        for (int i = 0; i < NUM_WIRES; i++) {
            Rectangle wire = new Rectangle(PADDING, PADDING + i * vSpaceBetweenWires,
                    w - 2 * PADDING, WIRE_THICKNESS);

            if (indexOfWireToHighlight == i) {
                g2.setColor(WIRE_HIGHLIGHT_COLOR);
            } else {
                GradientPaint paint = new GradientPaint(PADDING, PADDING + i * vSpaceBetweenWires,
                        GRAD_START, w - PADDING, PADDING + i * vSpaceBetweenWires, GRAD_END);
                g2.setPaint(paint);
            }
            g2.fill(wire);
        }
    }

    /**
     * Returns drawing information for the comparator at a given pixel position, or null if no
     * comparator has been added at that position.
     */
    private DrawingInfo getDrawingInfo(int x, int y) {
        for (DrawingInfo t : addedComparators) {
            int i1 = toPixelPositionY(t.top);
            int i2 = toPixelPositionY(t.bot);
            if (Math.abs(t.x - x) < 3 && i1 < y && y < i2) {
                return t;
            }
        }
        return null;
    }

    /**
     * Returns true if a given pixel position lies on a wire and past the displayed fixed subnetwork
     * (first 32 comparisons of Green's network) . In other words, indicates whether or not the user
     * has clicked on a wire at a position where a new comparator can be added.
     */
    private boolean onWire(int x, int y) {
        return (isAfterGreen(x)) && toWireIndex(y) >= 0;
    }

    /**
     * Returns true if the given x-coordinate lies strictly between the displayed fixed subnetwork
     * (first 32 comparisons of Green's network) and the right end of the wires. In other words,
     * this method tells if a new comparator can be added at the given x-coordinate.
     */
    private boolean isAfterGreen(int x) {
        return (getLeftmostX() + 6) <= x && x < getWidth() - PADDING;
    }

    /**
     * Draws all comparators.
     */
    private void drawComparators(Graphics2D g2) {
        g2.setStroke(new BasicStroke(2));
        if (green32Visible) {
            for (int i = 0; i < Utilities.numberOfGreenLayers(); i++) {
                drawLayer(g2, 2 * PADDING + H_SPACE * i, Utilities.getGreenLayer(i),
                        vSpaceBetweenWires);
            }
        }
        addedComparators.stream().forEach((t) -> {
            drawComparator(g2, t.x, t.top, t.bot, vSpaceBetweenWires, NEW_COMP_COLOR);
        });
    }

    /**
     * Draws a comparator.
     *
     * @param g2 the graphics context
     * @param x the x-coordinate of the comparator
     * @param top index of the top wire
     * @param bottom index of the bottom wire
     * @param space the horizontal space between wires
     * @param c the color to be used in drawing the comparator
     */
    private void drawComparator(Graphics2D g2, int x, int top, int bottom, int space, Color c) {
        drawPoint(g2, x, top, space, c);
        drawPoint(g2, x, bottom, space, c);
        int y1 = PADDING + top * space; // y-coordinate of top
        int y2 = PADDING + bottom * space; // y-coordinate of bottom
        g2.drawLine(x, y1, x, y2);
    }

    /**
     * Draws a comparator that is being dragged by the mouse.
     *
     * @param g2 the graphics context
     * @param x the x-coordinate of the comparator
     * @param yTop the y-coordinate of the top endpoint
     * @param yBot the y-coordinate of the bottom endpoint
     */
    private void drawDraggedComparator(Graphics2D g2, int x, int yTop, int yBot) {
        g2.setColor(NEW_COMP_COLOR);
        g2.fill(new Ellipse2D.Double(x - 4, yTop - 2, ENDPOINT_DIAMETER, ENDPOINT_DIAMETER));
        g2.fill(new Ellipse2D.Double(x - 4, yBot - 2, ENDPOINT_DIAMETER, ENDPOINT_DIAMETER));
        g2.drawLine(x, yTop, x, yBot);
    }

    /**
     * Draws the endpoint of a comparator.
     *
     * @param g2 the graphics context
     * @param x the x-coordinate of the comparator
     * @param i the index of the wire to which the endpoint is attached
     * @param space the horizontal space between wires
     * @param color the color to be used in drawing the endpoint
     */
    private void drawPoint(Graphics2D g2, int x, int i, int space, Color color) {
        g2.setColor(color);
        int y = PADDING + i * space;
        g2.fill(new Ellipse2D.Double(x - 4, y - 2, ENDPOINT_DIAMETER, ENDPOINT_DIAMETER));
    }

    /**
     * Draws a set of comparators within a single layer. This means that they all having the same
     * x-coordinate and no two of them overlap visually, so that they can be rendered in a single
     * vertical column.
     *
     * @param g2 the graphics context
     * @param x the x-coordinate of this set of comparators
     * @param comp the wire indices of the comparators
     * @param space the horizontal space between wires
     */
    private void drawLayer(Graphics2D g2, int x, int[][] comp, int space) {
        // Wire indices of the i-th comparator are comp[i][0] and comp[i][1].
        for (int[] wireIndex : comp) {
            drawComparator(g2, x, wireIndex[0], wireIndex[1], space, FIXED_COMP_COLOR);
        }
    }

    /**
     * Returns the index of the wire with points having a given y-coordinate, or -1 if there is no
     * wire at the specified vertical position.
     */
    private int toWireIndex(int y) {
        for (int index = 0; index < NUM_WIRES; index++) {
            int a = PADDING + index * vSpaceBetweenWires;
            if (a - 2 <= y && y <= a + WIRE_THICKNESS + 2) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Returns the y-coordinate of a (horizontal) wire of the network.
     */
    private int toPixelPositionY(int indexOfWire) {
        return PADDING + indexOfWire * vSpaceBetweenWires;

    }
}

/**
 * Encapsulates information needed to draw a comparator.
 */
class DrawingInfo implements Comparable {

    int x; // x-coordinate of comparator
    int top; // index of top wire
    int bot; // index of bottom wire

    public DrawingInfo(int x, int top, int bot) {
        this.x = x;
        this.top = Math.min(top, bot);
        this.bot = Math.max(top, bot);
    }

    /**
     * Used to sort comparators by x-coordinate. Since comparators are drawn vertically, the top and
     * bottom wires have the same x-coordinate.
     */
    @Override
    public int compareTo(Object o) {
        DrawingInfo t = (DrawingInfo) o;
        if (x < t.x) {
            return -1;
        }
        if (x > t.x) {
            return 1;
        }
        return 0;
    }
}
