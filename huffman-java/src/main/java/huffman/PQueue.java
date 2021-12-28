package huffman;

import huffman.tree.Node;

import java.util.ArrayList;
import java.util.List;
/**
 * A priority queue of @Node@ objects. Each node has an int as its label representing its frequency.
 * This priority queue implements the MinHeap data structure to ensure O(n log n) time complexity when inserting or removing.
 */
public class PQueue {

	/** A list of all nodes in the queue */
	private List<Node> queue;

	public PQueue() {
		queue = new ArrayList<>();
	}

	/**
	 * Add a node to the queue. The new node should be inserted at the point where the frequency of next node is
	 * greater than or equal to that of the next one.
	 * @param n The node to enqueue.
	 */
	public void enqueue(Node n) {
		// Add the new node to the end of the array list
		queue.add(n);
		
		// Set the active pointer to the new node position
		int current = queue.size()-1;

		// While the active node is smaller than its parent, swap and update the pointer
		while (queue.get(current).getFreq() < queue.get(getParent(current)).getFreq()) {
			swap(current, getParent(current));
			current = getParent(current);
		}
	}

	/**
	 * Remove a node from the queue.
	 * @return  The first node in the queue.
	 */
	public Node dequeue() {
		// If the queue is empty return null immediately
		if (queue.size() == 0) return null;
		
		// Store the top most node to be returned
		Node popped = queue.get(0);
		
		// Set the last element in the queue to replace the first
		queue.set(0, queue.get(queue.size()-1));
		
		// Remove the last element in the array as it has now be moved to the top
		queue.remove(queue.size()-1);
		
		// If the queue has more than 1 element then sort from the top down
		if (queue.size() > 1) sortHeap(0);
		
		// Return the stored top most value
		return popped;
	}

	/**
	 * Return the size of the queue.
	 * @return  Size of the queue.
	 */
	public int size() {
		return queue.size();
	}


	/**
	 * Get the position of a node's parent given its position.
	 * 
	 * @param pos the position of element in array.
	 * @return the parent of the node at position.
	 */
	private int getParent(int pos) {
		return pos / 2;
	}

	/**
	 * Get the left child of an element at a given position.
	 * 
	 * @param pos the position of the element in array.
	 * @return the left child of the node at position.
	 */
	private int leftChild(int pos) {
		return (pos * 2);
	}

	/**
	 * Get the right child of an element at a given position.
	 * 
	 * @param pos the position of the element in array.
	 * @return the right child of the node at position.
	 */
	private int rightChild(int pos) {
		return (pos * 2) + 1;
	}

	/**
	 * Check if an element at a given position is a leaf node.
	 * 
	 * @param pos the position of the element in the array.
	 * @return true if element is a leaf node.
	 */
	private boolean isLeaf(int pos) {
		return (pos > (queue.size() / 2) && pos <= queue.size()) ? true : false;
	}

	/**
	 * Swap two nodes around given their positions within the heap array.
	 * 
	 * @param pos1 the first nodes position.
	 * @param pos2 the second nodes position.
	 * 
	 */
	private void swap(int pos1, int pos2) {
		Node tmp;
		tmp = queue.get(pos1);
		queue.set(pos1, queue.get(pos2));
		queue.set(pos2, tmp);
	}

	/**
	 * Trickle the node at a given position down the tree swapping while a smaller value is found or until we reach a leaf node.
	 * 
	 * @param pos the position we are checking a node against its children.
	 */
	private void sortHeap(int pos) {

		// If we reach a leaf then return as we can no longer trickle down
		if (isLeaf(pos)) return;

		// Reused references to current, left and right node frequencies
		int currentFreq = queue.get(pos).getFreq();
		int leftFreq = (leftChild(pos) < queue.size()) ? queue.get(leftChild(pos)).getFreq() : Integer.MAX_VALUE;
		int rightFreq = (rightChild(pos) < queue.size()) ? queue.get(rightChild(pos)).getFreq() : Integer.MAX_VALUE;

		// If the current frequency is larger than either left or right then we will need to trickle the value down
		if (currentFreq > leftFreq || currentFreq > rightFreq) {
			// If the left is smaller then swap with it or swap with the right
			if (leftFreq < rightFreq) {
				swap(pos, leftChild(pos));
				sortHeap(leftChild(pos));
			} else {
				swap(pos, rightChild(pos));
				sortHeap(rightChild(pos));
			}
		}

	}

	/**
	 * Prints the frequencies of the nodes stored in the heap array.
	 */
	public void print() {
		System.out.print("| ");
		for (Node n : queue) System.out.print(n.getFreq() + " | ");
		System.out.println();
	}

	/**
	 * Get the smallest value from the top of the heap without removing it.
	 * @return the smallest value in the heap.
	 */
	public Node peek() {
		if (queue.size() == 0) return null;
		return queue.get(0);
	}

}
