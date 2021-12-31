package huffman;

import huffman.tree.Branch;
import huffman.tree.Leaf;
import huffman.tree.Node;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
/**
 * The class implementing the Huffman coding algorithm.
 */
public class Huffman {

  /**
   * Build the frequency table containing the unique characters from the String `input' and the number of times
   * that character occurs.
   *
   * O(n) where n is the number of characters in the string
   *
   * @param   input   The string.
   * @return          The frequency table.
   */
  public static Map<Character, Integer> freqTable(String input) {

    // If the input is null, return null immediately to save time
    if (input == null || input.equals("")) {
      return null;
    }

    // Create new HashMap mapping characters to their frequency
    Map<Character, Integer> frequencyTable = new HashMap<>();

    // For each character in the string
    for (char character : input.toCharArray()) {
      if (frequencyTable.containsKey(character)) {
        // If the character is in the map, increment its frequency value by 1
        frequencyTable.replace(character, frequencyTable.get(character)+1);
      } else {
        // If not then add a new entry for the character with a count of 1
        frequencyTable.put(character, 1);
      }
    }

    // Return the frequency table
    return frequencyTable;
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
   * O(n log n) where n is the number of characters in the frequency table
   * This is reduced from O(n^2) caused by the old implementation of the priority queue
   * 
   * @param freqTable The frequency table.
   * @return          A Huffman tree.
   */
  public static Node treeFromFreqTable(Map<Character, Integer> freqTable) {

    // If we get a table that is null, return null immediately
    if (freqTable == null) {
      return null;
    }

    // Create new priority queue for frequency table values
    PQueue queue = new MinHeap();

    // Create new leaf node for each mapped value in the frequency table and add it to the queue
    for (char character : freqTable.keySet()) {
      queue.enqueue(new Leaf(character, freqTable.get(character)));
    }

    Node leftNode, rightNode;

    // Loop over queue until only 1 element is left
    while (queue.size() > 1) {
      // Left will always be smaller since the priority queue orders elements smallest to largest
      // This allows us to assume left and right without checking which is larger
      leftNode = queue.dequeue();
      rightNode = queue.dequeue();

      // Enqueue the new branch with the combined frequency values with the left and right nodes
      queue.enqueue(new Branch(leftNode.getFreq() + rightNode.getFreq(), leftNode, rightNode));
    }

    // Return the last element in the queue which contains the full tree
    return queue.dequeue();
  }

  /**
   * Construct the map of characters and codes from a tree. Just call the traverse
   * method of the tree passing in an empty list, then return the populated code map.
   *
   * O(n) where n is the number of leaf nodes in the tree
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
   * <br><br>
   * Utilises frequency table O(n), tree from table O(n log n), build code O(n)
   * Converting from character to booleans in for loop is O(n) where n is the number of characters
   * <br><br>
   * Overall this method is worst-case O(n log n) due to the use of tree from table method
   *
   * @param input The data to encode.
   * @return      The Huffman coding.
   */
  public static HuffmanCoding encode(String input) {

    // Create a frequency table using the input string
    Map<Character, Integer> frequencyTable = freqTable(input);

    // Create a huffman tree using the generated frequency table
    Node huffmanTree = treeFromFreqTable(frequencyTable);

    // Build and store codes for each character
    Map<Character, List<Boolean>> characterCodes = buildCode(huffmanTree);

    // Prepare store for encoded data
    List<Boolean> encodedData = new ArrayList<>();

    // For each character in the input add the corresponding code to the array list
    for(int i = 0; i < input.length(); i++) {
      encodedData.addAll(characterCodes.get(input.charAt(i)));
    }

    // Return the codes and encoded data as a new HuffmanCoding object
    return new HuffmanCoding(characterCodes, encodedData);

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
   * 
   *
   * @param code  The code.
   * @return      The reconstructed tree.
   */
  public static Node treeFromCode(Map<Character, List<Boolean>> code) {

    // Create a new blank tree containing a single branch node with null values left and right
    Node huffmanTree = new Branch(0, null, null);

    // Set the current node to the root of the tree
    Node currentNode = huffmanTree;

    // Variable to hold our current code as a list of booleans
    List<Boolean> currentCode;

    // For each character in the code list
    for (char character : code.keySet()) {

      // Set the current code
      currentCode = code.get(character);

      // For each boolean in the current code
      for (int i = 0; i < currentCode.size(); i++) {

        // If false (code '0') we go left. If not false (true / code '1') we go right
        if (!currentCode.get(i)) {
          // If last boolean in sequence then create new left leaf node and reset the current node to the root of the tree
          if (i == currentCode.size()-1) {
            ((Branch)currentNode).setLeft(new Leaf(character, 0));
            currentNode = huffmanTree;
          } else {
            // If not last then create missing branch nodes if missing
            if (((Branch)currentNode).getLeft() == null) {
              ((Branch)currentNode).setLeft(new Branch(0, null, null));
            }
            // Then move to left node
            currentNode = ((Branch)currentNode).getLeft();
          }
        } else {
          // If last boolean in sequence then create new right leaf node and reset the current node to the root of the tree
          if (i == currentCode.size()-1) {
            ((Branch)currentNode).setRight(new Leaf(character, 0));
            currentNode = huffmanTree;
          } else {
            // If not last then create missing branch nodes if missing
            if (((Branch)currentNode).getRight() == null) {
              ((Branch)currentNode).setRight(new Branch(0, null, null));
            }
            // Then move to right node
            currentNode = ((Branch)currentNode).getRight();
          }

        } // End current code check

      } // End code for loop

    } // End character for loop

    // Return constructed tree
    return huffmanTree;
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
    String decodedString = "";

    // Create a new huffman tree using the character codes
    Node huffmanTree = treeFromCode(code);

    // Set the current active node to the root of our tree
    Branch currentNode = (Branch) huffmanTree;

    // The node we will checking against
    Node checkNode;

    // For each boolean in the data
    for (boolean bit : data) {

      // If false (0) then we go left. If true (1) we go right
      if (!bit) {
        checkNode = currentNode.getLeft();
      } else {
        checkNode = currentNode.getRight();
      }

      // If a leaf node is found, add the label to our decoded string and reset the current node to the tree's root node
      if (checkNode instanceof Leaf) {
        decodedString+=((Leaf) checkNode).getLabel();
        currentNode = (Branch) huffmanTree;
        // If the leaf node is a pseudo end-of-file character we return return the decoded string 
        // Note that this is used in functional implementation of 'compress' and 'decompress'
        if (((Leaf) checkNode).getLabel() == (char)0x1F) {
          return decodedString;
        }
      } else {
        // If it's a branch node then update our current node to the checked node
        currentNode = (Branch) checkNode;
      }

    } // End of data iteration loop

    // Return the decoded string from the encoded data
    return decodedString;
  }


  /**
   * Compresses the huffman tree and character codes into a compact header
   * comprised of a magic number, number of characters, chars paired with
   * their code lengths followed by each char bit code and data stream.
   * 
   * A pseudo EOF character is appended to the input to determine the end 
   * before the file EOF. This is a functional example of the huffman 
   * coding compressing and saving a binary file.
   * 
   * @param input the text being compressed
   * @param path the location the binary file will be stored
   * @author Jazer Barclay
   * @throws IOException 
   */
  public static void compress(String input, String path) throws IOException {

    // The bit data we will store in our file which includes the compressed
    // header describing the tree and the compressed input as a bit stream
    List<Boolean> bitData = new ArrayList<>();

    // Magic number (hex for characters 'h' and 'f') is used to check if the file is a huffman encoded file
    byte[] magicNumber = new byte[] {0x68, 0x66};

    // A character used to delimit the end of the bit stream as the file EOF
    // may not be at the end of the bit stream (between 0-7 out potentially)
    // Here I am using the "Unit Separator" byte as it is not a used character
    byte pseudoEOF = 0x1F;

    // Add pseudo end-of-file to end of input as we need it encoded in our huffman tree
    // and store it into a HuffmanCoding object as that is what is parsed
    HuffmanCoding encodedHuffman = encode(input + ((char)pseudoEOF));

    // Map to store characters and their corresponding codes
    Map<Character, List<Boolean>> characterCodes = encodedHuffman.getCode();

    // Add Magic Number to the data
    bitData = addByte(magicNumber[0], bitData);
    bitData = addByte(magicNumber[1], bitData);

    // Add Number of Codes as byte
    bitData = addByte((byte)characterCodes.size(), bitData);

    // For each character add the char byte code followed by its code size
    for (char c : characterCodes.keySet()) {
      for (byte b : (""+c).getBytes("ISO-8859-1")) {
        bitData = addByte(b , bitData);
        bitData = addByte((byte)characterCodes.get(c).size() , bitData);
      }
    }
    
    // Add all encoded character binary values
    for (char c : characterCodes.keySet()) {
      bitData.addAll(characterCodes.get(c));
    }

    // Add all encoded data to the data array
    bitData.addAll(encodedHuffman.getData());

    // We can only write bytes in chunks of 8. We need padding at the end. Take the data and mod 8 for a remainder
    int padding = 8 - (bitData.size() % 8);


    // Add the padding as 0s or in this case 'false' boolean values
    for (int i = 0; i < padding; i++) {
      bitData.add(false);
    }

    // Print compression statistics
    System.out.println("Compressing file: " + path);
    System.out.println("Input Size: " + input.toCharArray().length);
    System.out.println("Compressed: " + bitData.size()/8);
    float comp = 100 - (((float) (bitData.size()/8) / input.length()) * 100);
    System.out.printf("Reduction: %4.2f%%", comp);
    System.out.println("");

    byte[] b = new byte[bitData.size()/8];

    byte activeByte;

    // For each byte we will be storing where i is the first bit and we add 8 to each iteration
    for (int i = 0; i < bitData.size(); i+=8) {

      // Add each bit from 0 to 7 to the active byte right to left
      activeByte = (byte)(
          (bitData.get(i)  ?1<<7:0)
          + (bitData.get(i+1)?1<<6:0)
          + (bitData.get(i+2)?1<<5:0)
          + (bitData.get(i+3)?1<<4:0)
          + (bitData.get(i+4)?1<<3:0)
          + (bitData.get(i+5)?1<<2:0)
          + (bitData.get(i+6)?1<<1:0)
          + (bitData.get(i+7)?1   :0));

      // Store this byte in the byte array 'b'
      b[i/8] = activeByte;
    }

    // Write bytes to a file
    Files.write(Paths.get(path), b);
    System.out.println("Successfully written data to the file");
  }

  /**
   * Opens a file at the given path and converts the huffman encoded data
   * into its decompressed string.
   * 
   * @param path the location of the huffman file
   * @return the plain string of the huffman compressed file
   * @throws IOException
   * @throws Exception
   */
  public static String decompress(String path) throws IOException, Exception {

    // Buffer stores the magic number
    byte[] buffer = new byte[2];

    // Read in the first 2 bytes of the file
    InputStream is = new FileInputStream(path);
    if (is.read(buffer) != buffer.length);;
    is.close();

    // If the file doesn't start with the magic number 'hf' then this is not a huffman file
    if (buffer[0] == (byte)0x68 && buffer[1] == (byte)0x66){
    } else {
      throw new Exception("Failed Magic Number Check");
    }

    // Read all bytes found in the file
    byte[] fileContent = Files.readAllBytes(Paths.get(path));

    // Store the number of encoded characters found at the 3rd byte
    int codeCount = fileContent[2];

    // Create a new map of characters and their code lengths
    Map<Character, Integer> codeLengths = new HashMap<>();

    // From after the magic number and length bytes (3 bytes), read all codes and their code lengths
    for (int i = 3; i < 3+(codeCount*2); i+=2) {
      codeLengths.put( (new String(new byte[] {fileContent[i]}, StandardCharsets.ISO_8859_1)).toCharArray()[0] , (int)fileContent[i+1]);
    }

    // Store all bits in bytes as booleans
    List<Boolean> encodedData = new ArrayList<Boolean>();
    for (int i = 3+(codeCount*2); i < fileContent.length; i++) {
      encodedData.addAll(booleanFromByte(fileContent[i]));
    }

    // Track our position in the encoded data
    int encodedPos = 0;

    // Create a new map of characters and their codes as a list of booleans
    Map<Character, List<Boolean>> codes = new HashMap<Character, List<Boolean>>();
    List<Boolean> currentCode;

    // For each char we have and its lengths, read that number of booleans from the list and store them in the map
    for (char c : codeLengths.keySet()) {
      currentCode = new ArrayList<Boolean>();
      for (int i = 0; i < codeLengths.get(c); i++) {
        currentCode.add(encodedData.get(encodedPos));
        encodedPos++;
      }
      codes.put(c, currentCode);
    }

    // Store only the encoded contents without the char codes
    List<Boolean> encoded = encodedData.subList(encodedPos, encodedData.size());

    // Use the decode method with the codes map and encoded contents
    String decoded = decode(codes, encoded);

    // Strip the pseudo end of file char from the end of the string
    decoded = decoded.substring(0, decoded.length()-1);

    // Return the decoded string
    return decoded;
  }

  /**
   * Returns true if the specified bit in a byte is set to 1
   * 
   * @param b the byte being checked
   * @param bit the bit in the byte to check
   * @return true if bit is set
   */
  private static Boolean isBitSet(byte b, int bit) {
    return (b & (1 << bit)) != 0;
  }

  /**
   * Prints an integer value as a byte
   * @param val
   */
  public static void printByte(int val) {
    printByte((byte)val);
  }

  /**
   * Prints a byte bit by bit
   * @param b the byte to be printed
   */
  private static void printByte(byte b) {
    for (int i = 7; i >= 0; i--) {
      System.out.print( "" + ((isBitSet(b, i)) ? "1" : "0") );
    }
  }

  /**
   * Adds a byte to a boolean list
   * @param b byte to be added
   * @param data the list the byte is to be added to
   * @return the new list of booleans with the byte added
   */
  private static List<Boolean> addByte(byte b, List<Boolean> data) {
    for (int i = 7; i >= 0; i--) {
      data.add(isBitSet(b, i));
    }
    return data;
  }

  /**
   * Returns a list of booleans representing the given byte
   * @param b the byte to be converted to a boolean list
   * @return the list of booleans representing the byte
   */
  private static List<Boolean> booleanFromByte(byte b) {
    List<Boolean> list = new ArrayList<Boolean>();
    list.add(((b & 0x80) != 0));
    list.add(((b & 0x40) != 0));
    list.add(((b & 0x20) != 0));
    list.add(((b & 0x10) != 0));
    list.add(((b & 0x08) != 0));
    list.add(((b & 0x04) != 0));
    list.add(((b & 0x02) != 0));
    list.add(((b & 0x01) != 0));
    return list;
  }

}
