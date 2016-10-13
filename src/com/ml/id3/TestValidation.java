package com.ml.id3;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import com.ml.beans.Node;

/**
* This class extends the Test class and implements methods required for generating file with predicted values
*/
public class TestValidation extends Test{

	public TestValidation(String path, Node tree) {
		super(path, tree);
	}

	/**
	* This method generates a text file with the name validation_results.txt with all predicted values.
	*/
	public void validate() {
		List<char []> test_result = getPredictions();
		BufferedWriter writer = null;
		try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("validation_result.txt"), "utf-8"));
    		for(char [] c : test_result) {
    			writer.write(c[0]);
    			writer.write('\n');
    		}
    		System.out.println("A file 'validation_result.txt' is generated with predicted values for validate data set"); 
		} catch(Exception e) {
			System.err.println("Error occured while writing validation results to file!! ");
		}finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
		}
	}
}
