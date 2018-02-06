
public class MyMaxHeapTest {

	public static void main(String[] args) {

		int[] priorities = {1,2,3,10,4,5,6,0,7,8,20,54,99,2,25,30,35,40,45,50};
//							91,82,73,10,64,55,46,30,27,18,20,54,99,32,25,30,35,40,45,50};
		MaxHeap heap = new MaxHeap();
		Process p;
		for(int i = 0; i < priorities.length; i++) {
			p = new Process(i, 1, 100);
			p.setPriority(priorities[i]);
			// System.out.printf("Priority set is: %d\n", p.getPriority());
			heap.insertNode(p);
			//System.out.printf("Current heap: %s\tHeapSize: %d\n", heap.toString(), heap.getHeapSize());
		}
		
		System.out.printf("Final Heap: %s\n", heap.toString());
		int elements = heap.getHeapSize();
		for (int i = 1; i <= elements; i++) {
			heap.extractMax();
			System.out.printf("Final Heap: %s\n", heap.toString());
		}
		
	}

	
	
	
	
	
}
