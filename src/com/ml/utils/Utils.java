package com.ml.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ml.beans.Mushroom;
import com.ml.beans.Node;

/**
* This class contains all the methods which are commonly used through out the program.
*/
public class Utils {

	/**
	* This method computes the entropy for the given pos and neg values
	* Input: +, - values
	* Output: entropy value
	*/
	public static float entropy(int pos, int neg) {
		float entropy = 0f;
		int total = pos+neg;
		if(pos > 0) {
			float p_pos = probability(pos, total);
			entropy -= (p_pos*Math.log(p_pos))/Math.log(2);
		} if(neg > 0) {
			float p_neg = probability(neg, total);
			entropy -= (p_neg*Math.log(p_neg))/Math.log(2);
		}
		return entropy;
	}

	/**
	* This method computes the information gain using entropy for the given values
	* Input: +, - counts of all sub nodes of a property-type
	* Output: information gain of a property-type
	*/	
	public static float gain_entropy(float s_entropy, Map<Character, int[]> elmMap, int total) {
		float gain = s_entropy;
		for(char elm : elmMap.keySet()) {
			int pos = elmMap.get(elm)[0];
			int neg = elmMap.get(elm)[1];
			gain -= (pos+neg)*entropy(pos, neg)/total;
		}
		return gain;
	}

	/**
	* This method computed the misclassification error for the given pos and neg values
	* Input: +, - values
	* Output: entropy value
	*/
	public static float misclassification(int [] attr_count, int total) {
		float msc = 0f;
		for(int i : attr_count) {
			float p_attr = probability(i, total);
			if(p_attr > msc)
				msc = p_attr;
		}
		return (1-msc);
	}
	
	/**
	* This method computes the information gain using misclassification for the given values
	* Input: +, - counts of all sub nodes of a property-type
	* Output: information gain of a property-type
	*/	
	public static float gain_misclassification(float s_mce, Map<Character, int[]> elmMap, int total ) {
		float gain = s_mce;
		for(char elm : elmMap.keySet()) {
			int pos = elmMap.get(elm)[0];
			int neg = elmMap.get(elm)[1];
			gain -= (pos+neg)*misclassification(elmMap.get(elm), pos+neg)/total;
		}
		return gain;
	}
	
	/**
	* This method computes the probability for the given values
	* Input: attribute count and total count
	* Output: probability of an attribute
	*/		
	public static float probability(int a, int t) {
		return a*1f/t;
	}

	/**
	* This method loads the data set from file and initialize necessary constants
	* Input: file path
	* Output: List of mushroom records
	*/	
	public static List<char []> loadData(String path) {
		List<char []> input = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(Paths.get(path))) {
				if(line != null) {
					char [] cArr = new char[23];
					for(int i=0, j=0;i<line.length();i+=2, j++) {
						cArr[j] = line.charAt(i);
						if(Constants.attrMap.containsKey(j)) {
							Set<Character> attrVal = Constants.attrMap.get(j);
							attrVal.add(cArr[j]);
							Constants.attrMap.put(j, attrVal);
						} else {
							Set<Character> attrVal = new HashSet<>();
							attrVal.add(cArr[j]);
							Constants.attrMap.put(j, attrVal);
						}
					}
					input.add(cArr);
				}
			}
//			System.out.println("Processed lines : "+input.size()); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}

	/**
	* This method prints the tree
	* Input: Tree Root
	*/	
	public static void printTree(Node t, int depth) {
		for(int i=0;i<depth;i++)
			System.out.print("---");
		System.out.println(">"+t.getData()); 
		for(Node n : t.getChildren()) 
			printTree(n, depth+1); 
	}
	
	/**
	* This method reads the input data file into a list of mushroom objects
	*/	
	public static List<Mushroom> readData(String path) {
		List<Mushroom> input = new ArrayList<>();
		Mushroom mushroom = null;
		try {
			for (String line : Files.readAllLines(Paths.get(path))) {
				if(line != null) {
					mushroom = new Mushroom(); 
					mushroom.setType(line.charAt(0));
					mushroom.setCap_shape(line.charAt(2));
					mushroom.setCap_surface(line.charAt(4));
					mushroom.setCap_color(line.charAt(6));
					mushroom.setBruises(line.charAt(8));
					mushroom.setOdor(line.charAt(10));
					mushroom.setGill_attachment(line.charAt(12));
					mushroom.setGill_spacing(line.charAt(14));
					mushroom.setGill_size(line.charAt(16));
					mushroom.setGill_color(line.charAt(18));
					mushroom.setStalk_shape(line.charAt(20));
					mushroom.setStalk_root(line.charAt(22));
					mushroom.setStalk_surface_above_ring(line.charAt(24));
					mushroom.setStalk_color_below_ring(line.charAt(26));
					mushroom.setStalk_color_above_ring(line.charAt(28));
					mushroom.setStalk_color_below_ring(line.charAt(30));
					mushroom.setVeil_type(line.charAt(32));
					mushroom.setVeil_color(line.charAt(34));
					mushroom.setRing_number(line.charAt(36));
					mushroom.setRing_type(line.charAt(38));
					mushroom.setSpore_print_color(line.charAt(40));
					mushroom.setPopulation(line.charAt(42));
					mushroom.setHabitat(line.charAt(44));
					input.add(mushroom);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
}
