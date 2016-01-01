# Java API for Sorting Networks

A collection of Java classes for experimenting with small sorting networks. 

### What is a Sorting Network?

A sorting network is a mathematical model of a particular kind of sorting algorithm --- one that works by making comparisons at predetermined positions in a list and swapping them if they are out of order. The original interest in sorting networks was due to their potential for simple and efficient realization in hardware. 

A comparison network is a more general kind of construction, typically illustrated a Knuth diagram:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![sorting network](images/ex1.png "Sorting Network")

The horizontal lines represent wires that carry data from left to right. Vertical lines denote comparators, abstract gadgets that compare two values and swap them if they are out of order. A sorting network is simply a comparison network that sorts all possible input sequences. It turns out that this is equivalent to sorting all possible *binary* sequences. It is not hard to see that the comparison network in the picture is in fact a sorting network, but you could verify it by inspecting the output for all 32 possible input sequences of 5 bits. 

The shaded regions in the picture indicate groups of comparators that, having disjoint inputs, can operate in parallel. 
The size of a comparison network is defined to be the number of comparators, and its depth (or delay) is a measure of its comparison-level parallelism: the maximum number of comparators incident to a common wire. The network shown above has size 5 and depth 3. 

### Small Sorting Networks

### References and Further Reading

<ul>
<li>[Chapter on sorting networks](https://mitpress.mit.edu/sites/default/files/Chapter%2027.pdf) from the classic [Introduction to Algorithms.](https://mitpress.mit.edu/books/introduction-algorithms)</li>

<li>[An 11-Step Sorting Network for 18 Elements](https://www.kent.edu/sites/default/files/TR-KSU-CS-2007-06.pdf)
</ul>

