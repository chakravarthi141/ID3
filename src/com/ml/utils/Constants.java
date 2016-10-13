package com.ml.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public interface Constants {

	Map<Integer, Set<Character>> attrMap = new HashMap<>();
	
	int REMAINING_ATTR = 23;
	
	int type = 0;
	int cap_shape = 1;
	int  cap_surface = 2;
	int cap_color = 3;
	int bruises = 4;
	int odor = 5;
	int gill_attachment = 6;
	int gill_spacing = 7;
	int gill_size = 8;
	int gill_color = 9;
	int stalk_shape = 10;
	int stalk_root = 11;
	int stalk_surface_above_ring = 12;
	int stalk_surface_below_ring = 13;
	int stalk_color_above_ring = 14;
	int stalk_color_below_ring = 15;
	int veil_type = 16;
	int veil_color = 17;
	int ring_number = 18;
	int ring_type = 19;
	int spore_print_color = 20;
	int population = 21;
	int habitat = 22;
	int predicted_type = 23;
}
