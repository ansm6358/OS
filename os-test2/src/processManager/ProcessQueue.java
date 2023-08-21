package processManager;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessQueue {
	Queue<Integer> queue;
	public ProcessQueue() {
		this.queue = new LinkedList<Integer>();
	}
	
	
	public void enqueue(Integer process) {
		this.queue.offer(process);
	}

	public Integer dequeue() {
		
		return this.queue.poll();
	}

}
