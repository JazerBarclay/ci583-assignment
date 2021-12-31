package huffman;

import huffman.tree.Node;
import java.util.ArrayList;
import java.util.List;

/**
 * A priority queue of @Node@ objects. Each node has an int as its label representing its frequency.
 * This priority queue implements the MinHeap data structure to ensure O(log n) time complexity when inserting and removing.
 */
public class MinHeap extends PQueue {

  /** A list of all nodes in the queue */
  private List<Node> heap;

  /**
   * Creates a PriorityQueue that orders huffman tree nodes from smallest to largest
   */
  public MinHeap() {
    heap = new ArrayList<>();
  }


  @Override
  public void enqueue(Node n) {
    // Add the new node to the end of the array list
    heap.add(n);

    // Set the active pointer to the new node position
    int current = heap.size()-1;

    // While the active node is smaller than its parent, swap and update the pointer
    while (heap.get(current).getFreq() < heap.get(getParent(current)).getFreq()) {
      swap(current, getParent(current));
      current = getParent(current);
    }
  }


  @Override
  public Node dequeue() {
    // If the queue is empty return null immediately
    if (heap.size() == 0) {
      return null;
    }

    // Store the top most node to be returned
    Node popped = heap.get(0);

    // Set the last element in the queue to replace the first
    heap.set(0, heap.get(heap.size()-1));

    // Remove the last element in the array as it has now be moved to the top
    heap.remove(heap.size()-1);

    // If the queue has more than 1 element then sort from the top down
    if (heap.size() > 1) {
      sortHeap(0);
    }

    // Return the stored top most value
    return popped;
  }

  /**
   * Return the size of the queue.
   * @return  Size of the queue.
   */
  @Override
  public int size() {
    return heap.size();
  }

  /**
   * Prints the frequencies of the nodes stored in the heap array.
   * Note that this order is not smallest to largest. This is the order the
   * tree maintains for the binary tree structure.
   */
  public void print() {
    System.out.print("| ");
    for (Node n : heap) {
      System.out.print(n.getFreq() + " | ");
    }
    System.out.println();
  }

  /**
   * Get the smallest value from the top of the heap without removing it.
   * @return the smallest value in the heap.
   */
  public Node peek() {
    if (heap.size() == 0) return null;
    return heap.get(0);
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
    return (pos > (heap.size() / 2) && pos <= heap.size()) ? true : false;
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
    tmp = heap.get(pos1);
    heap.set(pos1, heap.get(pos2));
    heap.set(pos2, tmp);
  }

  /**
   * Compare the node at a given position with its children and swap smaller child nodes with current.
   * Repeat this function at the new position if swapped until no smaller value found or a leaf node is reached.
   * 
   * @param pos the position we are checking a node against its children.
   */
  private void sortHeap(int pos) {
    // If we reach a leaf then return as we can no longer trickle down
    if (isLeaf(pos)) {
      return;
    }

    // Reused references to current, left and right node frequencies
    int currentFreq = heap.get(pos).getFreq();

    // Left frequency is the left child's frequency if it exits. If not then it defaults to the max integer value
    int leftFreq = (leftChild(pos) < heap.size())
        ? heap.get(leftChild(pos)).getFreq()
        : Integer.MAX_VALUE;

    // Right frequency is the right child's frequency if it exits. If not then it defaults to the max integer value
    int rightFreq = (rightChild(pos) < heap.size())
        ? heap.get(rightChild(pos)).getFreq()
        : Integer.MAX_VALUE;

    // If the current frequency is larger than either left or right then we will need to trickle the value down
    if (currentFreq > leftFreq || currentFreq > rightFreq) {
      // If the left is smaller then swap with it or swap with the right. Repeat the sort at the new position
      if (leftFreq < rightFreq) {
        swap(pos, leftChild(pos));
        sortHeap(leftChild(pos));
      } else {
        swap(pos, rightChild(pos));
        sortHeap(rightChild(pos));
      }
    }
  }
}
