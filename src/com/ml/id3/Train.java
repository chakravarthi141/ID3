package com.ml.id3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ml.beans.Node;
import com.ml.utils.Constants;

/**
 * This is a common abstract class for Training input data
 * This class implements the basic functionalities required for training
 */
public abstract class Train {

	//set stored the remaining attributes to be processed
	Set<Integer> attrSet = new HashSet<>();

	//checks if the data set has only one outcome
	private char checkClass(List<char []> input) {
		char type = '0';

		for(char [] c : input) {
			if(type == '0' ) 
				type = c[Constants.type];
			else if(type != c[Constants.type]) {
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
		attrSet.remove(0);
		//checking if the input training data set is empty.
		if(input.isEmpty()) 
			System.err.println("Invalid input training data");
		else if(type != '0') {	//checks if input data type has only one outcome.
			head = new Node(String.valueOf(type)); }
		else {
			maxGainAttr = getMaxGainAttr(input);
			head = new Node(String.valueOf(maxGainAttr));
			attrSet.remove(maxGainAttr);
			//creating root node with maxGain Value and iterating through all its possible values
			for(char c : Constants.attrMap.get(maxGainAttr)) {
				List<char []> split = split(input, maxGainAttr, c);
				Node new_parent = new Node(String.valueOf(c));
				head.addChild(new_parent);
				Node child = buildTree(split, new_parent, c);
				if(child != null) 
					new_parent.addChild(child);
			}
		}
		return head;
	}

	/**
	* This method constructs the tree from 2nd level recursively.
	* Input: data set, parent node
	* output: tree
	*/
	private Node buildTree(List<char []> input, Node parent, char attr) {
		Node curr = null;
		//checking for any dead end
		if(input == null || input.isEmpty()) {
			//System.err.println("Instance with empty data detected!!"); 
			return null;			
		}
		int maxGainAttr = getMaxGainAttr(input);
		if(maxGainAttr == -1) {		//if entropy is 0 for any attribute then no more expansion
			char c = getLeaf(input);
			curr = new Node(String.valueOf(c));
		} else {	
			//adding current property type as parent and its properties as child 
			//and proceeding to construct the tree recursively.
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
		int c1 = 0,c2 = 0;
		char leaf;
		for(char [] c : input) {
			if(c[0] == 'e')
				c1++;
			else
				c2++;
		}
		if(c1 > c2)
			leaf = 'e';
		else leaf = 'p';
		return leaf;
	}
	
	/**
	* This method is abstract and to be implemented by the classes extending this class.
	* Input: data set
	* output: max gain property type
	*/
	protected abstract int getMaxGainAttr(List<char []> input); 

	/**
	* This method counts number of edibles and poisonous in the passed data set.
	* Input: data set
	* output: counts of e & p
	*/
	protected static int [] getEPCount(List<char []> input) {
		int [] EPCount = new int[2];
		for(char [] c: input) {
			if(c[0] == 'e')
				EPCount[0]++;
			else
				EPCount[1]++;
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
				EPCountMap.put(c[attr], new int[2]);
			}
			if(c[0] == 'e')
				EPCountMap.get(c[attr])[0]++; 
			else
				EPCountMap.get(c[attr])[1]++;
		}
		return EPCountMap;
	}
}
