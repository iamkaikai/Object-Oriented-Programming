

import java.io.*;
import java.util.*;

public class BuildHuffman implements Huffman{

    /**
     * Read file provided in pathName and count how many times each character appears
     * @param pathName - path to a file to read
     * @return - Map with a character as a key and the number of times the character appears in the file as value
     * @throws IOException
     */
    public Map<Character, Long> countFrequencies(String pathName) throws IOException {
        Map<Character, Long> charCountMap = new TreeMap<Character, Long>();
        BufferedReader file;

        //Open the file from the path. Report error if error.
        try{
            file = new BufferedReader(new FileReader(pathName));
        }catch (FileNotFoundException e) {
            System.err.println("Cannot find file or wrong path: "+e.getMessage());
            return null;
        }
        //iterate through each char
        try{
            String line;
            int c;
            while ((c = file.read()) != -1) {
                char character = (char) c;
                if (!charCountMap.containsKey(character)) {
                    charCountMap.put(character, (long) 1);
                } else {
                    long freq = charCountMap.get(character);
                    charCountMap.put(character, freq + 1);
                }
            }
        }catch(IOException e){
            System.out.println("IO error while reading: " + e.getMessage());
        }
        return charCountMap;
    }

    /**
     * Construct a code tree from a map of frequency counts. Note: this code should handle the special
     * cases of empty files or files with a single character.
     *
     * @param frequencies a map of Characters with their frequency counts from countFrequencies
     * @return the code tree.
     */
    public BinaryTree<CodeTreeElement> makeCodeTree(Map<Character, Long> frequencies){
        //create a priority queue and define the comparator of CodeTreeElement
        PriorityQueue<BinaryTree<CodeTreeElement>> priorityQueue = new PriorityQueue<>(
                (BinaryTree<CodeTreeElement> tree1, BinaryTree<CodeTreeElement> tree2) -> (int) (tree1.getData().getFrequency() - tree2.getData().getFrequency())
        );

        //loop through all the keys in the map, turn them into a pair
        //create a new tree for each pair, and put them in priorityQueue
        if(frequencies.isEmpty()){
            System.out.println("the input is empty!");
            return null;
        }
        for (Character key: frequencies.keySet()){
            Long value = frequencies.get(key);
            CodeTreeElement FreqCharPair = new CodeTreeElement(value,key);
            BinaryTree<CodeTreeElement> charTree = new BinaryTree<CodeTreeElement>(FreqCharPair);
            priorityQueue.add(charTree);
        }

        //create a merge tree from the least two priority tree from priorityQueue
        //get the merge freq from the two tree and merge then to a new tree.
        //smaller freq to the left. greater to the right.
        //keep merging until only one big tree left
        while(priorityQueue.size() > 1){
            BinaryTree<CodeTreeElement> tree1 = priorityQueue.remove();
            BinaryTree<CodeTreeElement> tree2 = priorityQueue.remove();
            long freq1 = tree1.getData().getFrequency();
            long freq2 = tree2.getData().getFrequency();
            long merge_freq = freq1 + freq2;
            CodeTreeElement root = new CodeTreeElement(merge_freq,null);    //root only keep track of merged freq, thus the char is set to null
            BinaryTree<CodeTreeElement> mergeTree = new BinaryTree<CodeTreeElement>(root, tree1, tree2);
            priorityQueue.add(mergeTree);
        }

        BinaryTree<CodeTreeElement> final_tree = priorityQueue.peek();
        return final_tree;
    }




    /**
     * Computes the code for all characters in the tree and enters them
     * into a map where the key is a character and the value is the code of 1's and 0's representing
     * that character.
     *
     * @param codeTree the tree for encoding characters produced by makeCodeTree
     * @return the map from characters to codes
     */

    //A helper function to record the code for each char in recursion
    public  Map<Character, String> traverse(BinaryTree<CodeTreeElement> codeTree, String code, Map<Character, String> charToCodeMap){
        if (codeTree == null) return null; //edge case: if the tree is empty

        //base case
        if(codeTree.isLeaf()) charToCodeMap.put(codeTree.getData().getChar(), code);

        //recursion
        if(codeTree.hasLeft()){
            traverse(codeTree.getLeft(), code + "0", charToCodeMap);
        }
        if(codeTree.hasRight()){
            traverse(codeTree.getRight(), code + "1", charToCodeMap);
        }
        return charToCodeMap;
    }

    //Call the helper function 'traverse' to compute the codeTree
    public Map<Character, String> computeCodes(BinaryTree<CodeTreeElement> codeTree){
        Map<Character, String> charToCodeMap = new TreeMap<Character, String>();
        traverse(codeTree,"",charToCodeMap);
        return charToCodeMap;
    }

    @Override
    public void compressFile(Map<Character, String> codeMap, String pathName, String compressedPathName) throws IOException {
        try{
            BufferedReader strInput = new BufferedReader(new FileReader(pathName));
            BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);
            int letter;
            while ((letter = strInput.read()) != -1){
                char xLetter = (char)(letter);
                String codeStr = codeMap.get(xLetter);
                if (codeStr != null) {
                    for (int i = 0; i < codeStr.length(); i++) {
                        if (codeStr.charAt(i) == '0') {
                            bitOutput.writeBit(false);
                        }
                        if (codeStr.charAt(i) == '1') {
                            bitOutput.writeBit(true);
                        }
                    }
                }

            }
            strInput.close();
            bitOutput.close();

        }catch (FileNotFoundException e) {
            System.err.println("Cannot find file or wrong path: "+e.getMessage());
        }

    }


    @Override
    public void decompressFile(String compressedPathName, String decompressedPathName, BinaryTree<CodeTreeElement> codeTree) throws IOException {

        try {
            BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);
            BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));
            BinaryTree<CodeTreeElement> current = codeTree;
            System.out.println("decode");

            while (bitInput.hasNext() || (!bitInput.hasNext() && current.isLeaf())) {
                if (current.isLeaf()){
                    output.write(current.getData().getChar());
                    current = codeTree;
                    if(current.size() == 1) break;
                }
                if (bitInput.hasNext()){
                    boolean bit = bitInput.readBit();
                    if (bit == false){
                        current = current.getLeft();
                    }
                    if (bit == true){
                        current = current.getRight();
                    }
                }
            }
            bitInput.close();
            output.close();
        }catch(FileNotFoundException e){
            System.err.println("Cannot find file or wrong path: " + e.getMessage());
        }

    }


    public static void main(String[] args) throws Exception{
        ArrayList<String> fileNames = new ArrayList<>(Arrays.asList("test", "WarAndPeace", "USConstitution"));
        for (String fileName :fileNames) {
            String path = "./inputs/"+fileName+".txt";
            String compressedPath = "./inputs/"+fileName+"_comp.txt";
            String decompressedPath = "./inputs/"+fileName+"_decomp.txt";
            BuildHuffman huffman = new BuildHuffman();
            Map<Character, Long> charCount = huffman.countFrequencies(path);
            BinaryTree<CodeTreeElement> makeCodeTree = huffman.makeCodeTree(charCount);
            Map<Character, String> computeCodes = huffman.computeCodes(makeCodeTree);
            huffman.compressFile(computeCodes, path, compressedPath);
            huffman.decompressFile(compressedPath, decompressedPath, makeCodeTree);
        }
//        String path = "./inputs/"+fileNmae+".txt";
//        String compressedPath = "./inputs/"+fileNmae+"comp.txt";
//        String decompressedPath = "./inputs/"+fileNmae+"decomp.txt";
//        BuildHuffman huffman = new BuildHuffman();
//        Map<Character, Long> charCount = huffman.countFrequencies(path);
//        System.out.println(charCount);
//        BinaryTree<CodeTreeElement> makeCodeTree = huffman.makeCodeTree(charCount);
//        System.out.println(makeCodeTree);
//        Map<Character, String> computeCodes = huffman.computeCodes(makeCodeTree);
//        System.out.println(computeCodes);
//        huffman.compressFile(computeCodes, path, compressedPath);
//        huffman.decompressFile(compressedPath, decompressedPath, makeCodeTree);
    }

}
