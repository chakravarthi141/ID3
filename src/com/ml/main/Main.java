package com.ml.main;

import java.util.List;

import com.ml.beans.Node;
import com.ml.id3.ChiSquareTest;
import com.ml.id3.Entropy;
import com.ml.id3.Misclassification;
import com.ml.id3.TestAccuracy;
import com.ml.id3.TestValidation;
import com.ml.id3.Train;
import com.ml.utils.Utils;

/**
 * @author Varun
 *
 */
public class Main {

	/**
	 * This main is the entry point for the program
	 * It takes following argument 
	 * 		-e: for selecting Entropy classifier
	 *		-m: for selecting MisClassificationError classifier
	 *		-c: For choosing ChiSquareTest, -c should be followed by the confidence value.
	 *			Valid confidence values are 0, 50, 95, 99
	 */
	public static void main(String[] args) {

		boolean classifier_type = false;
		int confidence = -1;
		Train trainer = null;
		//Reading command line arguments
		for(int i=0;i<args.length;i++) {
			if(args[i].equalsIgnoreCase("-E")) {
				classifier_type = false;
			} if(args[i].equalsIgnoreCase("-M")) {
				classifier_type = true;
			} if(args[i].equalsIgnoreCase("-C")) {
				if(i+1 < args.length && args[i+1] != null ) {
					confidence = Integer.parseInt(args[i+1]);
					if(confidence < 0)
						System.err.println("Invalid confidence value!!"); 
				} else {
					System.err.println("Invalid option -C!!");
					System.err.println("-C should be followed by valid confidence level!!");
				}
			}
		}
		List<char []> input = Utils.loadData("training.txt");
		//uncomment these lines and comment above line for handling missing attributes.
		//		PreProcessor p = new PreProcessor();		
		//		List<char []> input = p.process("C:\\Users\\Varun\\Documents\\data\\training.txt");
		if(classifier_type) 
			trainer = new Misclassification();
		else 
			trainer = new Entropy();
		Node tree = trainer.initTree(input);
		if(confidence > 0) {
			ChiSquareTest ctest = new ChiSquareTest(confidence);
			tree = ctest.process(tree, input);
		}
		TestAccuracy test = new TestAccuracy("testing.txt", tree);
		test.acuracy();
		TestValidation validate = new TestValidation("validation.txt", tree);
		validate.validate();
		//		Utils.printTree(new_tree, 0);  //uncomment this line to print the tree.
	}

}
