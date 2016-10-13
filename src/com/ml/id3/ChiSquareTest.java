package com.ml.id3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ml.beans.Node;

/**
* This class contains the methods to prune the tree using chi-square test.
*/
public class ChiSquareTest {

	float threshold;	//Alpha value
	int chi_index;		
	
	//Pre loading all required chi-square values 
	static Map<Integer, float []> ChiSquareTable = new HashMap<Integer, float[]>();

	static {
		ChiSquareTable.put(1, new float[]{0.4549f,3.841f,6.635f});
		ChiSquareTable.put(2, new float[]{1.3863f,5.991f,9.21f});
		ChiSquareTable.put(3, new float[]{2.366f,7.815f,11.345f});
		ChiSquareTable.put(4, new float[]{3.3567f,9.488f,13.277f});
		ChiSquareTable.put(5, new float[]{4.3515f,11.07f,15.086f});
		ChiSquareTable.put(6, new float[]{5.3481f,12.592f,16.812f});
		ChiSquareTable.put(7, new float[]{6.3458f,14.067f,18.475f});
		ChiSquareTable.put(8, new float[]{7.3441f,15.507f,20.09f});
		ChiSquareTable.put(9, new float[]{8.3428f,16.919f,21.666f});
		ChiSquareTable.put(10, new float[]{9.3418f,18.307f,23.209f});
		ChiSquareTable.put(11, new float[]{10.341f,19.675f,24.725f});
		ChiSquareTable.put(12, new float[]{11.3403f,21.026f,26.217f});
	}

	/**
	* Constructor to calculate alpha value from given confidence value
	*/
	public ChiSquareTest(int confidence) {
		threshold = 1- confidence*1f/100;
		if(confidence == 50) {
			chi_index = 0;
		} else if(confidence == 95) {
			chi_index = 1;
		} else if(confidence == 99) {
			chi_index = 2;
		} else
			chi_index = -1;
	}

	/**
	* This method validates the alpha value
	*/
	public Node process(Node tree, List<char []> input) {
		if(chi_index == -1) {
			System.err.println("Invalid confidence value!!");
			return tree;
		} else {
			return prune(tree, input);
		}
	}

	/**
	* This method prunes the tree using chi-squate test
	*/
	public Node prune(Node parent, List<char []> input) {
		Node curr = null;
		if(input == null || input.isEmpty()) {
			//System.err.println("Instance with empty data detected!!"); 
			return null;			
		}else if(parent.getChildren().isEmpty()) {
			return new Node(parent.getData());
		}
		//checking if the split happened by chance or relevance
		boolean isbyChance = testNode(parent, input);
		if(isbyChance) {	//omit the remaining nodes if the split happens by chance
			char c = Train.getLeaf(input);
			curr = new Node(String.valueOf(c));
		} else {		//continue to explore if the split is relevant
			curr = new Node(parent.getData());
			for(Node n : parent.getChildren()) {
				List<char []> split = Train.split(input, Integer.parseInt(parent.getData()), n.getData().charAt(0));
				Node new_parent = new Node(n.getData());
				curr.addChild(new_parent);
				for(Node c : n.getChildren()) {
					Node child = prune(c, split);
					if(child != null) 
						new_parent.addChild(child);
				}
			}
		}
		return curr;
	}
	
	/**
	* This method checks if the split is by chance or relevant.
	* Input: parent node, data set
	* Output: true if the split is by chance
	* 		  false if the split is relavant
	*/
	private boolean testNode(Node parent, List<char []> input) {
		int [] s_counts = Train.getEPCount(input);
		Map<Character, int []> attr_counts = Train.getEPCount(input, Integer.parseInt(parent.getData()));
		float chiSquareValue = calcChiSquare(attr_counts, s_counts, input.size());
		int dof = attr_counts.keySet().size()-1;
		if(chiSquareValue > ChiSquareTable.get(dof)[chi_index]) 
			return false;
		else
			return true;
	}

	/**
	* This method calculated the expected probability of given sub node
	*/
	private float getExpectedProbability(int attr_count, int type_count, int total) {
		return ((attr_count*type_count*1f)/total);
	}

	/**
	* This method calculates the chi-square value for the node
	* Input: all sub nodes of a parent node, p and e counts of all sub nodes and total count in S
	* Output: chi-square value of the node
	*/
	private float calcChiSquare(Map<Character, int []> attr_counts, int [] s_counts, int total) {
		float chiSquare = 0f;
		for(char c : attr_counts.keySet()) {
			int pos = attr_counts.get(c)[0];
			int neg = attr_counts.get(c)[1];
			float ep_pos = getExpectedProbability(pos+neg, s_counts[0], total);
			float ep_neg = getExpectedProbability(pos+neg, s_counts[1], total);
			chiSquare += Math.pow((pos - ep_pos), 2)/ep_pos;
			chiSquare += Math.pow((neg - ep_neg), 2)/ep_neg;
		}
		return chiSquare;
	}
}
