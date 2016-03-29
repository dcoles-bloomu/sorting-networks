## NetBeans Project for Experimenting with Small Sorting Networks

Current functionality includes an application to manually extend the first 32 comparators of Green's sorting network on 16 wires and monitor the effect on the number of unsorted binary outputs. Here is the view from NetBeans when the project is opened:

![Project view](/netbeans.png "Project view from NetBeans")

The user can append new comparisons using the mouse to select a pair of wires.  When the application runs, a frame appears depicting the first stage of Green's sorting network with room for appending new comparators. The data panel at the bottom displays a list of added comapators on the left and a list of all unsorted binary outputs on the right. The 151 binary unsorted strings that appear initially are those that are produced by the first stage of Green's network:

![Extending Green's first stage](/gui1.png "Extending Green's first stage")

The user can add a comparator by clicking the mouse on a pair of wires. A comparator can also be picked up with the mouse and dragged to a new location, or discared. For every change, the data panel displays a current view of the unsorted binary outputs produced by the composite network (Green's 32 with the user-selected comparators added):

![Extending Greens first stage](/gui2.png "Extending Green's first stage")

Menu options include the ability to hide/show the 32 fixed comparators, auto-space the added comparators, and reset by deleting all comparators added by the user. 

![Menu options](/gui3.png "Menu options")

### Suggestions for Additional Features
<ul>
<li>Drag the mouse to highlight a rectangular region in order to move or delete a group of comparators.</li>
<li>Support the inclusion of small sub-sorting networks on specified wires.</li>
</ul>

### Suggestions for Code to Support Other Experiments
<ul>
<li>Manual creation of filters on a given number of wires</li>
</ul>

