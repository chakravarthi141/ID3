package com.ml.id3;

import java.util.List;

import com.ml.beans.Node;

/**
* This class extends the Test class and implements methods required for calculation accuracy
*/
public class TestAccuracy extends Test{

	public TestAccuracy(String path, Node tree) {
		super(path, tree);
	}

	/**
	* This method calculate accuracy of predictions using test data set
	*/
	public void acuracy() {
		List<char []> test_result = getPredictions();
		int total = test_result.size();
		int correct_count = 0;
		for(char [] c : test_result)
			if(c[0] == c[1])
				correct_count++;
		System.out.println("Acuracy for given test set: "+(correct_count*100.0f/total)); 
	}
}
