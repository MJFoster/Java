       /*
        * In-order traversal, left-child, root, then right-child,
        * order.  At each leaf, output the DNA subsequence found.
        * Output goes to standard a text file named 'dump'.
        * 
        * @param node - Current root of subtree being traversed.
        */
       public void inOrderTraversal(BTreeNode node) {
    	   
    	   int freq = 0;
    	   String subSequence = null;
    	   TreeObject currObj = null;
    	   
    	   SubStringConverter converter = new SubStringConverter(seqLengthK);
    	   
    	   // TODO:  Add try-catch for IOException thrown by FileWriter
    	   FileWriter outputFile = new FileWriter("dump");

    	   if (node.leaf) {  //TODO:  RE-write BTreeNode.toString() to output String of ALL <frequenc> <subsequence> entries of node.
    		   node.toString();  
    	   } else {  // for each key in node, recursively traverse until leaf reached.
    		   for (int i = 0; i < node.n; i++) {
    			   if (i == 0) {  // only 1 left child to traverse in node.
    				   inOrderTraversal(getNodeFromDisk(node.childPtrs[i]));
    			   } else { 
    				   // Write current key's subSequence 
    				   currObj = node.getObject(i);  // TODO:  Write in BTreeNode class -> public TreeObject node.getObject(i)
    				   freq = currObj.getFreqCount();
    				   subSequence = converter.longToString(currObj.getKey());
    				   outputFile.write(freq + " " + subSequence + "\n");
    				   
    				   // Traverse right child next.
    				   inOrderTraversal(getNodeFromDisk(node.childPtrs[i+1]));
    			   }
    		   }
    	   }
       }