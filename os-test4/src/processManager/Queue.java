package processManager;
import java.util.LinkedList;

public class Queue<TYPE> {
	
	private LinkedList<TYPE> queue;

	public Queue() {
		this.queue = new LinkedList<TYPE>();
	}
	
	public void enqueue(TYPE object) {
		
		this.queue.offer(object);
	}

	public TYPE dequeue() {
		
		return this.queue.poll();
		
	}

	public int size() {
		return this.queue.size();
	}

}
