package com.ml.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ml.beans.Node;
import com.ml.id3.Misclassification;
import com.ml.id3.Train;
import com.ml.utils.Utils;

public class PrintRules {
	static Map<Integer, Map<Character, String>> ref = new HashMap<>();
	static Map<Integer, String> node_ref = new HashMap<>();
	static {

		node_ref.put(5, "odor");node_ref.put(22, "habitat:");node_ref.put(8, "gill-size");
		node_ref.put(20, "spore-print-color");node_ref.put(3, "cap-color");node_ref.put(2, "cap-surface");node_ref.put(13, "stalk-surface-below-ring");

		Map<Character, String> t = new HashMap<Character, String>();
		t.put('a',"almond");t.put('l',"anise");t.put('c',"creosote");t.put('y',"fishy");t.put('f',"foul");
		t.put('m',"musty");t.put('n',"none");t.put('p',"pungent");t.put('s',"spicy");
		ref.put(5, t);
		Map<Character, String> t1 = new HashMap<Character, String>();
		t1.put('k',"black");t1.put('n',"brown");t1.put('b',"buff");t1.put('h',"chocolate");t1.put('r',"green");
		t1.put('o',"orange");t1.put('u',"purple");t1.put('w',"white");t1.put('y',"yellow");
		ref.put(20, t1);
		Map<Character, String> t2 = new HashMap<Character, String>();
		t2.put('g',"grasses");t2.put('l',"leaves");t2.put('m',"meadows");t2.put('p',"paths");
		t2.put('u',"urban");t2.put('w',"waste");t2.put('d',"woods");
		ref.put(22, t2);
		Map<Character, String> t3 = new HashMap<Character, String>();
		t3.put('b',"broad");t3.put('n',"narrow");
		ref.put(8, t3);
		Map<Character, String> t4 = new HashMap<Character, String>();
		t4.put('c',"cinnamon");t4.put('n',"brown");t4.put('b',"buff");t4.put('g',"gray");t4.put('r',"green");
		t4.put('p',"pink");t4.put('u',"purple");t4.put('w',"white");t4.put('e',"red");t4.put('y',"yellow");
		ref.put(3, t4);
		Map<Character, String> t5 = new HashMap<Character, String>();
		t5.put('f',"fibrous");t5.put('g',"grooves");t5.put('y',"scaly");t5.put('s',"smooth");
		ref.put(2, t5);
		Map<Character, String> t6 = new HashMap<Character, String>();
		t6.put('f',"fibrous");t6.put('k',"silky");t6.put('y',"scaly");t6.put('s',"smooth");
		ref.put(13, t6);


	}

	public static void main(String[] args) {

		List<char []> input = Utils.loadData("C:\\Users\\Varun\\Documents\\data\\training.txt");
		Train trainer = new Misclassification();
		Node tree = trainer.initTree(input);
		print(tree, new StringBuilder());
	}

	public static void print(Node t, StringBuilder sb){
		if(t.getChildren().isEmpty()) {
			sb.append("Then ");
			if(t.getData().equalsIgnoreCase("e"))
				sb.append("edible;");
			else
				sb.append("poisonous;");
			System.out.println(sb.toString()); 
		} else {
			int i = Integer.parseInt(t.getData());
			for(Node n : t.getChildren()) {
				StringBuilder new_sb = new StringBuilder(sb);
				new_sb.append("If(").append(node_ref.get(i)).append(" = ").append(ref.get(i).get(n.getData().charAt(0))).append(") and ");
				if(!n.getChildren().isEmpty())
					print(n.getChildren().get(0), new_sb);
			}

		}
	}
}
