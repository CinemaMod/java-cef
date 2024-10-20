package org.cef.misc;

import java.nio.ByteBuffer;

public class DataPointer {
	private final long address;
	private ByteBuffer dataBuffer;
	boolean initialized = false;
	int alignment = 3;
	
	public DataPointer(long address) {
		this.address = address;
	}
	
	public DataPointer forCapacity(int capacity) {
		dataBuffer = bufferForMemory(address, capacity);
		initialized = true;
		return this;
	}
	
	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}
	
	public long getAddress() {
		return address;
	}
	
	public DataPointer getData(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return new DataPointer(dataBuffer.getLong(offset << alignment));
	}
	
	public long getLong(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return dataBuffer.getLong(offset << alignment);
	}
	
	public int getInt(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return dataBuffer.getInt(offset << alignment);
	}
	
	public short getShort(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return dataBuffer.getShort(offset << alignment);
	}
	
	public byte getByte(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return dataBuffer.get(offset << alignment);
	}
	
	public double getDouble(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return dataBuffer.getDouble(offset << alignment);
	}
	
	public float getFloat(int offset) {
		if (!initialized) throw new RuntimeException("DataPoint#forCapacity must be called before the data can be accessed.");
		return dataBuffer.getFloat(offset << alignment);
	}
	
	private static native ByteBuffer bufferForMemory(long addr, int capacity);
}
