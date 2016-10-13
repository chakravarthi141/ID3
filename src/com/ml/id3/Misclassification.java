package com.ml.id3;

import java.util.List;
import java.util.Map;

import com.ml.utils.Utils;

/**
* This class extends the Train class and implements info gain using Misclassification Error.
*/
public class Misclassification extends Train{

	/**
	* This method computed the info gain using misclassification error.
	* Input: data set
	* output: max gain attribute 
	*/
	@Override
	protected int getMaxGainAttr(List<char[]> input) {
		int maxGainAttr = -1;
		float maxGainVal = -1;
		int [] EPCount = Train.getEPCount(input); 
		float s_mce = Utils.misclassification(EPCount, EPCount[0]+EPCount[1]);
		if(s_mce == 0)
			return maxGainAttr;
		for(int i : attrSet) {
			Map<Character, int[]> valueMap = getEPCount(input, i);
			float gain = Utils.gain_misclassification(s_mce, valueMap, input.size());
			if(gain > maxGainVal) {
				maxGainAttr = i;
				maxGainVal = gain;
			}
		}
		return maxGainAttr;
	}

}
