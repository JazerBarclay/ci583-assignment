package huffman;

import huffman.tree.Node;

import java.util.ArrayList;
import java.util.List;
/**
 * A priority queue of @Node@ objects. Each node has an int as its label representing its frequency.
 * The queue should order objects in ascending order of frequency, i.e. lowest first.
 */
public class PQueue {

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

    // If the queue is empty just add the node
    if (queue.size() == 0) {
      queue.add(n);
      return;
    }

    // Loop through to compare each value until a 
    int i = 0;
    for (Node node : queue) {
      if (node.getFreq() >= n.getFreq()) {
        queue.add(i, n);
        return;
      }
      i++;
    }

    // If no existing element is larger then add the node to the end
    queue.add(n);
  }

  /**
   * Remove a node from the queue.
   * @return  The first node in the queue.
   */
  public Node dequeue() {
    Node n = (size() > 0) ? queue.remove(0) : null;
    return n;
  }

  /**
   * Return the size of the queue.
   * @return  Size of the queue.
   */
  public int size() {
    return queue.size();
  }
  
}