package os;

import processManager.Process;

public class MemoryManager {

	//allocate memory and set PCB info
	public void allocate(Process process) {
		process.association(this);
	}

	public HeapSegment makeHeap(int size) {
		HeapSegment heap = new HeapSegment(size);
		return heap;
	}
	
	public class HeapSegment {
		private int[] memory;
		public HeapSegment(int size) {
			this.memory = new int[size];
		}
		public void store(int address, int data) {
			this.memory[address] = data;
		}
		public int fetch(int address) {
			return this.memory[address];
		}
		public int getSize() {
			return this.memory.length;
		}
	}
	
}
