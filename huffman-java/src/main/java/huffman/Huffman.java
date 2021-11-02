package huffman;

import huffman.tree.Branch;
import huffman.tree.Leaf;
import huffman.tree.Node;
import java.util.*;
/**
 * The class implementing the Huffman coding algorithm.
 */
public class Huffman {

	/**
	 * Build the frequency table containing the unique characters from the String `input' and the number of times
	 * that character occurs.
	 *
	 * @param   input   The string.
	 * @return          The frequency table.
	 */
	public static Map<Character, Integer> freqTable(String input) {

		// If the input is null, return null
		if (input == null || input.equals("")) return null;

		// Create new HashMap mapping chars to their frequency
		Map<Character, Integer> ft = new HashMap<>();

		char c;
		for (int i = 0; i < input.length(); i++) {

			// Store current iteration of character in c variable
			c = input.charAt(i);

			// If the character is in the map, update value
			if (ft.containsKey(c)) ft.replace(c, ft.get(c)+1);

			// If not then add a new entry for the character
			else ft.put(c, 1);
		}

		// Return the frequency table
		return ft;
	}

	/**
	 * Given a frequency table, construct a Huffman tree.
	 *
	 * First, create an empty priority queue.
	 *
	 * Then make every entry in the frequency table into a leaf node and add it to the queue.
	 *
	 * Then, take the first two nodes from the queue and combine them in a branch node that is
	 * labelled by the combined frequency of the nodes and put it back in the queue. The right hand
	 * child of the new branch node should be the node with the larger frequency of the two.
	 *
	 * Do this repeatedly until there is a single node in the queue, which is the Huffman tree.
	 *
	 * @param freqTable The frequency table.
	 * @return          A Huffman tree.
	 */
	public static Node treeFromFreqTable(Map<Character, Integer> freqTable) {
		
		// If we get a table that is null, return null
		if (freqTable == null) return null;

		// Create new priority queue for frequency table values
		PQueue q = new PQueue();
		
		// Create new leaf node for each mapped value in the frequency table
		for (char c : freqTable.keySet()) q.enqueue(new Leaf(c, freqTable.get(c)));
		
		// Temp variables used in while loop
		Node left, right;

		// Loop over queue until only 1 element is left merging leafs and branches together
		while (q.size() > 1) {

			// Right will always be bigger since the priority queue places smallest to largest
			// allowing us to assume left and right without a check
			left = q.dequeue();
			right = q.dequeue();

			// Enqueue the new branch with the combined frequency values with the left and right nodes
			q.enqueue(new Branch(left.getFreq() + right.getFreq(), left, right));
		}
		
		// Return the last element in the queue which contains the full tree
		return q.dequeue();
	}

	/**
	 * Construct the map of characters and codes from a tree. Just call the traverse
	 * method of the tree passing in an empty list, then return the populated code map.
	 *
	 * @param tree  The Huffman tree.
	 * @return      The populated map, where each key is a character, c, that maps to a list of booleans
	 *              representing the path through the tree from the root to the leaf node labelled c.
	 */
	public static Map<Character, List<Boolean>> buildCode(Node tree) {
		return tree.traverse(new ArrayList<Boolean>());
	}

	/**
	 * Create the huffman coding for an input string by calling the various methods written above. I.e.
	 *
	 * + create the frequency table,
	 * + use that to create the Huffman tree,
	 * + extract the code map of characters and their codes from the tree.
	 *
	 * Then to encode the input data, loop through the input looking each character in the map and add
	 * the code for that character to a list representing the data.
	 *
	 * @param input The data to encode.
	 * @return      The Huffman coding.
	 */
	public static HuffmanCoding encode(String input) {
		
		// Create a frequency table using the input string
		Map<Character, Integer> ft = freqTable(input);
		
		// Create a tree from the frequency table
		Node ht = treeFromFreqTable(ft);
		
		// Build optimal codes for each character used
		Map<Character, List<Boolean>> codes = buildCode(ht);
		
		// Prepare store for encoded data
		List<Boolean> data = new ArrayList<>();
		
		// Character variable used each loop to hold the current char value
		char c;
		
		// For each character in the input add the corresponding code to the array list
		for(int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			data.addAll(codes.get(c));
		}
		
		// Return the codes and data as a new HuffmanCoding object
		return new HuffmanCoding(codes, data);
		
	}

	/**
	 * Reconstruct a Huffman tree from the map of characters and their codes. Only the structure of this tree
	 * is required and frequency labels of all nodes can be set to zero.
	 *
	 * Your tree will start as a single Branch node with null children.
	 *
	 * Then for each character key in the code, c, take the list of booleans, bs, corresponding to c. Make
	 * a local variable referring to the root of the tree. For every boolean, b, in bs, if b is false you want to "go
	 * left" in the tree, otherwise "go right".
	 *
	 * Presume b is false, so you want to go left. So long as you are not at the end of the code so you should set the
	 * current node to be the left-hand child of the node you are currently on. If that child does not
	 * yet exist (i.e. it is null) you need to add a new branch node there first. Then carry on with the next entry in
	 * bs. Reverse the logic of this if b is true.
	 *
	 * When you have reached the end of this code (i.e. b is the final element in bs), add a leaf node
	 * labelled by c as the left-hand child of the current node (right-hand if b is true). Then take the next char from
	 * the code and repeat the process, starting again at the root of the tree.
	 *
	 * @param code  The code.
	 * @return      The reconstructed tree.
	 */
	public static Node treeFromCode(Map<Character, List<Boolean>> code) {
		
		// Create a new blank tree containing a single branch node with null values left and right
		Node tree = new Branch(0, null, null);
		
		// Set the current node to the root of the tree
		Node currentNode = tree;
		
		// Variable to hold our current code as a list of booleans
		List<Boolean> currentCode;
		
		// For each character in the code list
		for (char c : code.keySet()) {
			
			// Set the current code
			currentCode = code.get(c);
			
			// For each boolean in the current code
			for (int i = 0; i < currentCode.size(); i++) {
				
				// If false (in the code a 0) we go left
				if (!currentCode.get(i)) {
					
					// Create missing branch nodes if missing only when not last in sequence
					if (((Branch)currentNode).getLeft() == null) {
						((Branch)currentNode).setLeft(new Branch(0, null, null));
					}
					
					// If last boolean in sequence then create leaf node and reset current node to root of tree
					if (i == currentCode.size()-1) {
						((Branch)currentNode).setLeft(new Leaf(c, 0));
						currentNode = tree;
					}
					
					// If not last then move to left node
					else currentNode = ((Branch)currentNode).getLeft();
					
				// If true (in the code a 1) we go right
				} else {
					
					// Create missing branch nodes if missing
					if (((Branch)currentNode).getRight() == null) ((Branch)currentNode).setRight(new Branch(0, null, null));
					
					// If last boolean in sequence then create leaf node and reset current node to root of tree
					if (i == currentCode.size()-1) {
						((Branch)currentNode).setRight(new Leaf(c, 0));
						currentNode = tree;
					}
					
					// If not last then move to right node
					else currentNode = ((Branch)currentNode).getRight();
					
				} // End current code check
				
			} // End code for loop
			
		} // End character for loop
		
		// Return final tree
		return tree;
	}


	/**
	 * Decode some data using a map of characters and their codes. To do this you need to reconstruct the tree from the
	 * code using the method you wrote to do this. Then take one boolean at a time from the data and use it to traverse
	 * the tree by going left for false, right for true. Every time you reach a leaf you have decoded a single
	 * character (the label of the leaf). Add it to the result and return to the root of the tree. Keep going in this
	 * way until you reach the end of the data.
	 *
	 * @param code  The code.
	 * @param data  The encoded data.
	 * @return      The decoded string.
	 */
	public static String decode(Map<Character, List<Boolean>> code, List<Boolean> data) {
		
		// A string to store our decoded data
		String decoded = "";
		
		// Create a new huffman tree using the character codes
		Node tree = treeFromCode(code);
		
		// Set the current active node to the root of our tree
		Branch currentNode = (Branch) tree;
		
		// The node we are checking against
		Node checkNode;
		
		// For each boolean (0 or 1) in the data
		for (boolean b : data) {
			
			// If false (0) then we go left
			if (!b) checkNode = currentNode.getLeft();
			
			// If true (1) we go right
			else checkNode = currentNode.getRight();
			
			// If we have found a leaf node then add the corresponding label to our string
			if (checkNode instanceof Leaf) {
				decoded+=((Leaf) checkNode).getLabel();
				currentNode = (Branch) tree;
			// If it's a branch node then update our current node to the checked node
			} else {
				currentNode = (Branch) checkNode;
			}
			
		} // End of data iteration loop

		// Return the decoded string from the encoded data
		return decoded;
	}
}
