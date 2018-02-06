//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.util.Random;
//
//
//public class test_conversion {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
///*		int k = 4;
////		long ggga = 0b10101000;
//		long aagt = 0b00001011;
//		long ggga = 168;
//		SubStringConverter convert = new SubStringConverter(31);
//		System.out.println("TEST converting ggga should equal 10101000 " + convert.longToString(ggga));
//
//
//		System.out.println("TEST converting aagt should equal 00001011 " + convert.longToString(aagt));
//		SubStringConverter conv = new SubStringConverter(31);
//		String longone = "ttttttttttttttttttttttttttttttt";
//		long lg = conv.stringToLong(longone);
//
//		System.out.println(convert.longToString(lg));
//
//
//		final String FILEPATH = "thisFile";
//		int iarray[] =  {6,7,8,9,10,11,12,13,14,15,16};
//		try{
//	    RandomAccessFile file = new RandomAccessFile(FILEPATH, "rw");
//	    for(int i = iarray.length - 1; i >= 0; i--){
//	    	System.out.println("writing " + iarray[i] + " to file");
//	    	file.writeInt(iarray[i]);
//	    }
//	    long fp = file.getFilePointer();
//	    System.out.println("file pointer is at: " + fp);
//	    System.out.println("file size is " + file.length());
//	    int revarray[] = new int[iarray.length];
//	    file.seek(0);
//	    for(int i = 0; i < revarray.length; i++){
//	    	revarray[i] = file.readInt();
//	    	System.out.println("placing " + revarray[i] + " in reverse array");
//	    }
//	    System.out.print("test to get # 10 from file: ");
//	    file.seek(24);
//	    int temp = file.readInt();
//	    System.out.println(temp);
//	    System.out.println(10 == temp);
//	    for(int i = 0; i< iarray.length; i++){
//	    	System.out.print(iarray[i] + " ");
//	    }
//	    System.out.println("");
//	    for(int i = 0; i < revarray.length; i++){
//	    	System.out.print(revarray[i] + " ");
//	    }
//		}
//		catch(IOException e){
//			System.err.printf("EXCEPTION\n%S\n",e);
//		}
//
//		System.out.println("TEST");
//		int firstArg = 1;
//		int secArg = 2;
//		String test = String.format("Thefilewas.%d.%d", firstArg,secArg);
//		System.out.println(test);
//
//
//		System.out.println("TEST floorMod");
//		int result = Math.floorDiv(7, 2);
//		System.out.println(result);
//*/
//
//
////		long a = 0b11;
////		long b = 0b01;
////		long c = 0b10;
////		TreeObject t3 = new TreeObject(a, 3);
////		TreeObject t1 = new TreeObject(b, 1);
////		TreeObject t2 = new TreeObject(c, 2);
////
////		BTree tree = new BTree(4, 3);
////		System.out.println("TEST next index " + tree.nextWriteIndex);
////		System.out.println("TEST index before insert 3: " + tree.nextWriteIndex);
////		tree.insert(t3);
////		System.out.println("TEST index after insert 3 " + tree.nextWriteIndex);
////		tree.insert(t2);
////		System.out.println("TEST index after insert 2 " + tree.nextWriteIndex);
////		tree.insert(t1);
////		System.out.println("TEST index after insert 1 " + tree.nextWriteIndex);
//
//
//
//
//
//
//
//
//
//		int dt = 2;  //degree of tree
//		BTree tree = new BTree(dt, 3);
////		long a = 0b00;
//		Random rand = new Random(2);
//		int l;
//		int num = 20; //num of keys to insert//10
//		for(l = 0; l < num; l++){
//			long a = (long)rand.nextInt(30);//20
//			TreeObject t1 = new TreeObject(a, 1);
//			System.out.println("INSERTING KEY " + a);
////			a++;
////			System.out.println("TEST next node index before insert " + l + ":" + tree.nextWriteIndex);
//			tree.insert(t1);
////			System.out.println("TEST next node index after insert " + l + ":" + tree.nextWriteIndex);
//		}
//		long k = 8;
//		long g = 10;
//		TreeObject dup = new TreeObject(k, 1);
//		TreeObject dup2 = new TreeObject(g, 1);
//		System.out.println("INSERTING KEY " + k);
//		tree.insert(dup);
//		System.out.println("INSERTING KEY " + g);
//		tree.insert(dup2);
//
//
////		for(l = 0; l < 2; l++){
////			TreeObject t1 = new TreeObject(a, 0);
////			a++;
////			tree.insert(t1);
////		}
//		System.out.println("TEST next index " + tree.nextWriteIndex);
//
//	//**************************************************************************************
//		int m = 0
//				;
//		int n = 15; //#of nodes to read
//
//		final String FILE = "xyz.gbk.btree.data.3." + dt + "";
//
//		RandomAccessFile file;
//		try {
//			file = new RandomAccessFile(FILE, "rw");
//	    System.out.println(FILE);
//	    file.seek(0);
//	    int t = file.readInt();
//	    System.out.println("degree T: " + t);
//	    System.out.println("sub sz K: "+ file.readInt());
//	    System.out.println("node siz: "+ file.readInt());
//	    System.out.println("rootIndx: "+ file.readInt() + "\n");
//	    while(n > 0){
//
//	    System.out.println("*********NODE "+ (m) + " ***********");
//	    System.out.println("Offset  : " + file.getFilePointer());
//	    System.out.println("Node   n: " + file.readInt());
//	    System.out.println("boolLeaf: " + file.readBoolean());
//	    System.out.println("empty 00: " + file.readBoolean());
//	    System.out.println("empty 00: " + file.readBoolean());
//	    System.out.println("empty 00: " + file.readBoolean());
//	    System.out.println("prnt Idx: " + file.readInt());
//	    for(int i = 0; i < 2*t; i++){
//	    	System.out.println("chldIdx" + i + ": " + file.readInt());
//	    }
//	    for(int i = 0; i < (2*t) -1; i++){
//	    	System.out.println("treeObj " + i + ": " + file.readLong());
//	    	System.out.println("FreqCnt " + i + ": " + file.readInt());
//	    }
//	    n--;
//	    m++;
//	    }
////	    System.out.println("");
//
//
//		} catch (FileNotFoundException e) {
//			System.err.println("FILENOTFOUND " + e);
//			e.printStackTrace();
//		} catch (IOException e) {
//			System.err.println("NO MORE NODES TO PRINT OUT\n" + e);
//
//			e.printStackTrace();
//		}
//
//
//
//
//
////
////
////
////
////
////
////
////
////		try {
////			    RandomAccessFile file = new RandomAccessFile(FILEPATH, "r");
////
////
////
////
////
////
////
////
////
////			    file.seek(position);
////			    byte[] bytes = new byte[size];
////			    file.read(bytes);
////			    file.close();
////			    return bytes;
////
////			 writeToFile(FILEPATH, "JavaCodeGeeks Rocks!", 22);
////
////
////
////
////			System.out.println(new String(readFromFile(FILEPATH, 0, 23)));
////			         } catch (IOException e) {
////			             e.printStackTrace();
////			         }
////
////
////
////
//////		long hing =
//////		unsigned long th;
//	}
////
////
////
////private static byte[] readFromFile(String filePath, int position, int size)
////        throws IOException {
////
////}
////private static void writeToFile(String filePath, String data, int position)
////        throws IOException {
////    RandomAccessFile file = new RandomAccessFile(filePath, "rw");
////    file.seek(position);
////    file.write(data.getBytes());
////    file.close();
////}
//}
