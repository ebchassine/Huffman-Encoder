import java.io.*;
import java.util.*;
//Worked with Sam Starrs
public class HuffmanEncode implements Huffman {

    //Adds each character to the dictionary
    public Map<Character, Long> countFrequencies(String pathName) throws IOException{
        Map<Character, Long> freqTable = new HashMap<>(); //Initializing new HashMap with characters and their frequencies
        BufferedReader input = new BufferedReader(new FileReader(pathName));
        File inputFile = new File(pathName);

        if (inputFile.length()<2) throw new IOException("File is too short or empty.");
        if (!inputFile.exists()) throw new IOException("File can't be found.");

        int letter = input.read();
        while (letter != -1) { //
            char val = (char) letter;
            if (freqTable.containsKey(val)) {
                freqTable.put(val, freqTable.get(val) + 1);
            } else {
                freqTable.put(val, 1L);
            }
            letter = input.read();
        }
        return freqTable;
    }
    public BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies){
        PriorityQueue<BinaryTree<CodeTreeElement>> initialTrees = new PriorityQueue<>((e1, e2) -> (int) (e1.getData().getFrequency() - e2.getData().getFrequency()));
        Set<Character> keys = frequencies.keySet();

        //Adding the single BT to the priority queue
        for (Character key : keys) {
            initialTrees.add(new BinaryTree<>(new CodeTreeElement((long) frequencies.get(key), key)));
        }
        // Merging the BT into one large BT
        while (initialTrees.size() > 1) {
            BinaryTree<CodeTreeElement> T1 = initialTrees.remove();
            BinaryTree<CodeTreeElement> T2 = initialTrees.remove();
            CodeTreeElement root = new CodeTreeElement(T1.getData().getFrequency() + T2.getData().getFrequency(), null);
            BinaryTree<CodeTreeElement> newTree = new BinaryTree<>(root, T1, T2);
            initialTrees.add(newTree);
        }
        BinaryTree<CodeTreeElement> last = initialTrees.remove();
        return last; //Since there is only one value in the priority queue, popping returns a BT<CTE>
    }
    public void buildHuffMap(BinaryTree<CodeTreeElement> node, Map<Character, String> HuffMap, String currentPath){
        if (!node.hasRight() && !node.hasLeft()){
            HuffMap.put(node.getData().getChar(), currentPath);
        } if (node.hasRight()){
            buildHuffMap(node.getRight(), HuffMap, currentPath + "1");
        } if (node.hasLeft()){
            buildHuffMap(node.getLeft(), HuffMap, currentPath + "0");
        }
    }
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree) {
        Map<Character, String> codeMap = new HashMap<>();
        String codeString = new String();
        buildHuffMap(codeTree, codeMap, codeString); //Parameters BT, Empty map, emptyString, fills the empty Map
//        System.out.println(codeMap);
        return codeMap; //Returns the map of letter:codes
    }

    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException{
        BufferedReader input = new BufferedReader(new FileReader(pathName));
        BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);
        int letter;

        while ((letter = input.read()) != -1) {
            char val = (char) letter;
//            System.out.println(codeMap.get(val));
            String encodedVal = codeMap.get(val); //assigns string var to codeMap # value for the
            char [] charrArray = encodedVal.toCharArray();
            for (Character i: charrArray){
                if (i == '0') {
                    bitOutput.writeBit(false);
//                    System.out.println("wrote false");
                } if (i == '1') {
                    bitOutput.writeBit(true);
//                    System.out.println("wrote true");
                }
            }
        }
        bitOutput.close();
        input.close();
    }

    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException{

        BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);
        BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));

        BinaryTree<CodeTreeElement> tree = codeTree;

        while (bitInput.hasNext()) {
            boolean bit = bitInput.readBit();
//            System.out.println(bit);
//            System.out.println("Inputted tree:" + codeTree);
//            System.out.println("Sub tree:" + tree);
            if(bit == false){
                if(tree.getLeft().getData().getChar() != null){
                    output.write(tree.getLeft().getData().getChar());
                    tree = codeTree;
                }else{
                    tree = tree.getLeft();
                }
            }else if(bit == true){
                if(tree.getRight().getData().getChar() != null){
                    output.write(tree.getRight().getData().getChar());
                    tree = codeTree;
                }else{
                    tree = tree.getRight();
                }
            }
        }
        bitInput.close();
        output.close();
    }
}




