# Java API for Sorting Networks

A collection of Java classes for exploring conjectures and open questions about small sorting networks. 

### What is a Sorting Network?

Two preliminary definitions are needed. A *comparator* is an abstract device that compares the values encoded on two wires and swaps them if they are out of order.  A *comparison network* consists of wires that carry data, with certain pairs of wires connected by comparators. A *sorting network* is simply a comparison network that sorts every possible input sequence. Here 
is a simple example:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![sorting network](images/ex1.png "Sorting Network of size 5 and Depth 3")

The horizontal lines represent wires that carry data from left to right. Vertical lines denote comparators. The shaded regions indicate groups of comparators that, having disjoint inputs, can operate in parallel. The size of a comparison network is defined to be the number of comparators, and its depth (or delay) is a measure of its comparison-level parallelism: the maximum number of comparators incident to a common wire. The network shown above has size 5 and depth 3. 

A sorting network is a mathematical model of an oblivious sorting algorithm -- one for which all comparisons take place in a fixed order at predetermined positions in the list.

### Small Sorting Networks

### Further Reading

<ul>
<li>[Chapter on sorting networks](https://mitpress.mit.edu/sites/default/files/Chapter%2027.pdf) from the classic [Introduction to Algorithms.](https://mitpress.mit.edu/books/introduction-algorithms)</li>

</ul>

