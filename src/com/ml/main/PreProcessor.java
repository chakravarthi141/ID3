package com.ml.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ml.beans.Node;
import com.ml.utils.Constants;
import com.ml.utils.Utils;

/**
 * This class provides the functionality to clean-up the missing attributes
 *
 */
public class PreProcessor {

	static List<Character> leafSet = new ArrayList<>();
	
	Set<Integer> attrSet = new HashSet<>();
	Map<Integer, Set<Character>> attrMap = new HashMap<>();
	List<char []> train_set = new ArrayList<>();
	List<char []> test_set = new ArrayList<>();
	Node head = null;

	static {
		leafSet.add('b');leafSet.add('c');leafSet.add('u');
		leafSet.add('z');leafSet.add('r');leafSet.add('e');
	}
	
	public List<char []> process(String path) {
		loadData(path);
		head = initTree(train_set);
		test_set = getPredictions();
		train_set.addAll(test_set);
		return train_set;
	}
	
	/**
	* This method predicts if the mushroom is of type 'e' or 'p' based on its properties using tree
	*/
	protected char predict(char [] input, Node parent) {
		char predicted_val = ' ';
		if(parent.getChildren().isEmpty() || parent.getChildren().size() == 0) 
			predicted_val = parent.getData().charAt(0); 
		else {
			char sub_node = input[Integer.parseInt(parent.getData())];
			for(Node n : parent.getChildren()) {
				if(sub_node == n.getData().charAt(0)) {
					return predict(input, n.getChildren().get(0)); 
				}
			}
		}
		return predicted_val;
	}
	
	/**
	* This method stores all the values.
	*/
	protected List<char []> getPredictions() {
		for(char [] cArr : test_set) {
			System.out.print(cArr[11]); 
			cArr[11] = predict(cArr, head);
			System.out.print(cArr[11]+"\n");
		}
		return test_set;
	}
	
	//checks if the data set has only one outcome
	private char checkClass(List<char []> input) {
		char type = '0';

		for(char [] c : input) {
			if(type == '0' ) 
				type = c[11];
			else if(type != c[11]) {
				type = '0';
				break;
			}
		}
		return type;
	}

	/**
	* This method initiates tree construction.
	* Input: data set
	* output: tree
	*/
	public Node initTree(List<char []> input) {
		Node head = null;
		int maxGainAttr = -1;
		char type = checkClass(input);
		attrSet.addAll(Constants.attrMap.keySet());
		attrSet.remove(11);
		if(input.isEmpty()) 
			System.err.println("Invalid input training data");
		else if(type != '0') {
			head = new Node(String.valueOf(type)); }
		else {
			maxGainAttr = getMaxGainAttr(input);
			head = new Node(String.valueOf(maxGainAttr));
			attrSet.remove(maxGainAttr);
			for(char c : Constants.attrMap.get(maxGainAttr)) {
				List<char []> split = split(input, maxGainAttr, c);
				Node new_parent = new Node(String.valueOf(c));
				head.addChild(new_parent);
				Node child = buildTree(split, new_parent, c);
				if(child != null) 
					new_parent.addChild(child);
			}
		}
		Utils.printTree(head, 0);
		return head;
	}
	
	/**
	* This method constructs the tree from 2nd level recursively.
	* Input: data set, parent node
	* output: tree
	*/
	private Node buildTree(List<char []> input, Node parent, char attr) {
		Node curr = null;

		if(input == null || input.isEmpty()) {
//			System.err.println("Instance with empty data detected!!"); 
			return null;			
		}
		int maxGainAttr = getMaxGainAttr(input);
		if(maxGainAttr == -1) {
			char c = getLeaf(input);
			curr = new Node(String.valueOf(c));
		} else {
			curr = new Node(String.valueOf(maxGainAttr));
			attrSet.remove(maxGainAttr);
			for(char c : Constants.attrMap.get(maxGainAttr)) {
				List<char []> split = split(input, maxGainAttr, c);
				Node new_parent = new Node(String.valueOf(c));
				curr.addChild(new_parent); 
				Node child = buildTree(split, new_parent, c);
				if(child != null) 
					new_parent.addChild(child);
			}
		}
		return curr;
	}
	
	/**
	* This method filters the input data set with passed attributes.
	* Input: data set, property-type and sub category
	* output: list of mushroom records with properties passed in arguments
	*/
	protected static List<char []> split(List<char []> input, int attr, char val) {
		List<char []> split_data = new ArrayList<>();
		for(char [] row: input) {
			if(row[attr] == val) 
				split_data.add(row);
		}
		return split_data;
	}

	/**
	* This method finds the leaf node when the result is obvious.
	* Input: data set
	* output: most common property value in data set
	*/
	protected static char getLeaf(List<char []> input) {
		int [] count = new int[leafSet.size()];
		char leaf = ' ';
		int max_count = 0;
		for(char [] c : input) {
			count[leafSet.indexOf(c[11])]++;
		}
		for(int i=0;i<count.length;i++) {
			if(count[i] > max_count) {
				max_count = count[i];
				leaf = leafSet.get(i);
			}
		}
		return leaf;
	}
	
	/**
	* This method computes the info gain using entropy.
	* Input: data set
	* output: max gain attribute 
	*/
	protected int getMaxGainAttr(List<char []> input) {
		int maxGainAttr = -1;
		float maxGainVal = -1;
		int [] EPCount = getEPCount(input); 
		float s_entropy = entropy(EPCount);
		if(s_entropy == 0)
			return maxGainAttr;
		for(int i : attrSet) {
			Map<Character, int[]> valueMap = getEPCount(input, i);
			float gain = gain_entropy(s_entropy, valueMap, input.size());
			if(gain > maxGainVal) {
				maxGainAttr = i;
				maxGainVal = gain;
			}
		}
		return maxGainAttr;
	}

	/**
	* This method computes the entropy for the given pos and neg values
	* Input: +, - values
	* Output: entropy value
	*/
	private static float entropy(int [] vals) {
		float entropy = 0f;
		int total = 0;
		for(int i : vals)
			total += i;
		for(int i : vals) {
			if(i > 0) {
				float p = Utils.probability(i, total);
				entropy -= (p*Math.log(p))/Math.log(2);
			}
		}
		return entropy;
	}
	

	/**
	* This method computes the information gain using entropy for the given values
	* Input: +, - counts of all sub nodes of a property-type
	* Output: information gain of a property-type
	*/	
	private static float gain_entropy(float s_entropy, Map<Character, int[]> elmMap, int total) {
		float gain = s_entropy;
		for(char elm : elmMap.keySet()) {
			int sub_total = 0;
			for(int i : elmMap.get(elm)) {
				sub_total += i; 
			}
			gain -= (sub_total)*entropy(elmMap.get(elm))/total;
		}
		return gain;
	}
	
	/**
	* This method counts number of edibles and poisonous in the passed data set with respect to the passed attributes.
	* Input: data set, property-type
	* output: map with counts of e & p for all sub categories of passed property type.
	*/
	protected static int [] getEPCount(List<char []> input) {
		int [] EPCount = new int[leafSet.size()];
		for(char [] c: input) {
			EPCount[leafSet.indexOf(c[11])]++;
		}
		return EPCount;
	}

	/**
	* This method counts number of edibles and poisonous in the passed data set with respect to the passed attributes.
	* Input: data set, property-type
	* output: map with counts of e & p for all sub categories of passed property type.
	*/
	protected static Map<Character, int[]> getEPCount(List<char []> input, int attr) {
		Map<Character, int[]> EPCountMap = new HashMap<>();
		for(char [] c: input) {
			if(!EPCountMap.containsKey(c[attr])) {
				EPCountMap.put(c[attr], new int[leafSet.size()]);
			}
			EPCountMap.get(c[attr])[leafSet.indexOf(c[11])]++; 
		}
		return EPCountMap;
	}

	/**
	* This method loads the data set from file and initialize necessary constants
	* Input: file path
	* Output: List of mushroom records
	*/	
	private void loadData(String path) {
		try {
			for (String line : Files.readAllLines(Paths.get(path))) {
				if(line != null) {
					char [] cArr = new char[23];
					for(int i=0, j=0;i<line.length();i+=2, j++) {
						cArr[j] = line.charAt(i);
						if(attrMap.containsKey(j)) {
							Set<Character> attrVal = Constants.attrMap.get(j);
							attrVal.add(cArr[j]);
							Constants.attrMap.put(j, attrVal);
						} else {
							Set<Character> attrVal = new HashSet<>();
							attrVal.add(cArr[j]);
							Constants.attrMap.put(j, attrVal);
						}
					}
					if(cArr[11] != '?')
						train_set.add(cArr);
					else
						test_set.add(cArr);
				}
			}
			System.out.println("Processed lines : "+train_set.size()); 
			System.out.println("Filtered lines : "+test_set.size()); 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
