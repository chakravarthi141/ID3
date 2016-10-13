package com.ml.id3;

import java.util.List;
import java.util.Map;

import com.ml.utils.Utils;

/**
* This class extends the Train class and implements info gain using entropy.
*/
public class Entropy extends Train{

	/**
	* This method computes the info gain using entropy.
	* Input: data set
	* output: max gain attribute 
	*/
	@Override
	protected int getMaxGainAttr(List<char []> input) {
		int maxGainAttr = -1;
		float maxGainVal = -1;
		int [] EPCount = Train.getEPCount(input); 
		float s_entropy = Utils.entropy(EPCount[0], EPCount[1]);
		if(s_entropy == 0)
			return maxGainAttr;
		for(int i : attrSet) {
			Map<Character, int[]> valueMap = getEPCount(input, i);
			float gain = Utils.gain_entropy(s_entropy, valueMap, input.size());
			if(gain > maxGainVal) {
				maxGainAttr = i;
				maxGainVal = gain;
			}
		}
		return maxGainAttr;
	}

}
