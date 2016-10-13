package com.ml.id3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.ml.beans.Node;

/**
* This class contains the basic methods required for prediction.
*/
public abstract class Test {

	List<char []> test_input = null;
	Node head = null;
	
	/**
	* Constructor leading all test/validation data and initializing necessary variables
	*/
	Test(String path, Node tree) {
		this.head = tree;
		this.test_input = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(Paths.get(path))) {
				if(line != null) {
					char [] cArr = new char[24];
					for(int i=0, j=1;i<line.length();i+=2, j++) {
						cArr[j] = line.charAt(i);
					}
					test_input.add(cArr);
				}
			}
		} catch (IOException e) {
			System.err.println("invalid ");
			e.printStackTrace();
		}
	}
	
	/**
	* This method predicts if the mushroom is of type 'e' or 'p' based on its properties using tree
	*/
	protected char predict(char [] input, Node parent) {
		char predicted_val = ' ';
		if(parent.getChildren().isEmpty() || parent.getChildren().size() == 0) 
			predicted_val = parent.getData().charAt(0); 
		else {
			char sub_node = input[Integer.parseInt(parent.getData())+1];
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
		for(char [] cArr : test_input) {
			cArr[0] = predict(cArr, head);
		}
		return test_input;
	}
}
