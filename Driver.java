import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Driver {
    public static void main(String[] args) throws IOException {

        String inputFile = "inputs/USConstitution.txt";
        String compressedFile = "inputs/compressedTest.txt";
        String outputFile = "inputs/output.txt";

        HuffmanEncode Test = new HuffmanEncode();

        try {
            Map<Character, Long> testMap = Test.countFrequencies(inputFile);
            BinaryTree<CodeTreeElement> BT = Test.makeCodeTree(testMap);
            Map<Character, String> emptyMap = Test.computeCodes(BT);
            try {
                Test.compressFile(emptyMap, inputFile, compressedFile);
                Test.decompressFile(compressedFile, outputFile, BT);
            }catch(IOException e){
                System.out.println(e);
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
