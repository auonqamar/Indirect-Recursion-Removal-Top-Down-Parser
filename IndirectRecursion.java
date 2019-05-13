/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indirect.recursion;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;
import java.util.Set;
import java.util.Scanner;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Auon Naqvi
 */

public class IndirectRecursion {
    static String[] productions;
    static String tempNT = "";
    static LinkedHashMap<String, String[]> hmap = new LinkedHashMap<>();
    static void printProductions() {
        for (String production : productions) {
            System.out.println(production);
        }
    }
    static void printHashMap() {
	
		Set set = hmap.entrySet();
		Iterator i = set.iterator();
		String keyName;
		String keyValues[];		
		while(i.hasNext()) {		
			Map.Entry me = (Map.Entry)i.next();
			keyName = me.getKey().toString();
			keyValues = hmap.get(keyName);
			
			System.out.print("\n" + keyName + " -> ");
                    for (String keyValue : keyValues) {
                        System.out.print(keyValue + " ");
                    }
		}
	}
    static Stack<String>[] sort(Stack<String> s[]) 
    { 
        int n = s.length;
        // One by one move boundary of unsorted subarray 
        for (int i = 0; i < n-1; i++) 
        { 
            // Find the minimum element in unsorted array 
            int min_idx = i; 
            for (int j = i+1; j < n; j++){ 
                if (s[j].size()<s[min_idx].size()){ 
                    min_idx = j;}
            }
            // Swap the found minimum element with the first 
            // element
            if(i!=min_idx)
            tempNT=swap(tempNT,i,min_idx);
            Stack<String> temp = s[min_idx]; 
            s[min_idx] = s[i]; 
            s[i] = temp;
        }
        return s;
    } 
    static String swap(String str, int i, int j) 
    { 
        if (j == str.length() - 1) 
            return str.substring(0, i) +str.charAt(j)+ str.substring(i + 1, j)+ str.charAt(i); 
  
        return str.substring(0, i)+str.charAt(j)+str.substring(i + 1, j)+str.charAt(i)+str.substring(j + 1, str.length());  
    } 
    static void initHash() {
	
		Stack<String> stack[] = new Stack[tempNT.length()];
		String[] hmapString;
		
		for(int i = 0; i < stack.length; i++)
			stack[i] = new Stack<>();
		
        for (String production : productions) {
        stack[tempNT.indexOf(production.charAt(0))].push(production.substring(3));
        }
        stack=sort(stack);
		for(int i = 0; i < stack.length; i++) {
			
			hmapString = new String[stack[i].size()];

			if(!stack[i].empty()) {
			
				for(int j = 0; !stack[i].empty(); j++){
					hmapString[j] = stack[i].pop();}
                                hmap.put((tempNT.charAt(i) + ""), hmapString);
         
			}
		}
	}
 
    static void findNonTerminals() {
	for (String production : productions) {
            for (int j = 0; j < production.length(); j++) {
                if ((production.charAt(j) >= 'A' && production.charAt(j) <= 'Z') && (tempNT.indexOf(production.charAt(j)) == -1)) {
                    tempNT += production.charAt(j);                    
                }
            }
        }

		System.out.println("\nNon Terminals : " + tempNT);
	}
    static boolean removeIndirect() {
	
		Set set = hmap.entrySet();
         	Iterator i = set.iterator();
		String keyName;
		String keyValues[];
		Stack<String> tempp = new Stack<>();
		
		while(i.hasNext()) {		
			Map.Entry me = (Map.Entry)i.next();
			keyName = me.getKey().toString();
			keyValues = hmap.get(keyName);
			for(int j = 0; j < keyValues.length; j++)
				if(keyValues[j].charAt(0) >= 'A' && keyValues[j].charAt(0) <= 'Z' && hmap.containsKey(keyValues[j].charAt(0) + ""))
					if(tempNT.indexOf(keyName.charAt(0)) > tempNT.indexOf(keyValues[j].charAt(0))) {
						String[] sub = hmap.get(keyValues[j].charAt(0) + "");
						for(int z = 0; z < keyValues.length; z++)
							if(z != j)
								tempp.push(keyValues[z]);
						
                            for (String sub1 : sub) {
                                tempp.push(sub1 + keyValues[j].substring(1));
                            }				
                            
						String[] hmapString = new String[tempp.size()];
						for(int z = 0; !tempp.empty(); z++)
							hmapString[z] = tempp.pop();
						
						hmap.put(keyName, hmapString);
                                                printHashMap();
                                                ArrayList<String> arr = new ArrayList<>();
                                                arr.add(keyValues[0]);
                                                for(int n=0; n<hmap.size();n++){
                                                    arr.add(n, keyValues[0]);
                                                }
                                                char s = arr.get(0).charAt(0);
                                                for(int n=1; n<arr.size();n++){
                                                    if(arr.get(n).charAt(0)==s){
                                                    if(n==arr.size()-1){
                                                        return false;
                                                    }
                                                    }
                                                }
                                                printHashMap();
						return true;
                    }
		}
		return false;
	}
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
                Scanner terminal = new Scanner(System.in);
		System.out.print("Enter the number of productions : ");
		int noProductions = terminal.nextInt();
		terminal = new Scanner(System.in);
		productions = new String[noProductions];
		
		System.out.println("Enter the productions");
		for(int i = 0; i < noProductions; i++)
			productions[i] = terminal.nextLine();
		
		System.out.println("\nGiven Productions");
		printProductions();
		
                findNonTerminals();
		initHash();
		while(removeIndirect()){}
                System.out.println("\nRemoving Indirect Recursion...");
                printHashMap();
    }    
}