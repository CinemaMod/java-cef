package org.cef.misc;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
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
		try {
			dataBuffer = (ByteBuffer) memByteBuffer.invoke(address, capacity);
			initialized = true;
			return this;
		} catch (Throwable err) {
			throw new RuntimeException("Failed to invoke memByteBuffer?", err);
		}
	}
	
	public DataPointer withAlignment(int alignment) {
		this.alignment = alignment;
		return this;
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
	
	// TODO: ideally we'd just directly depend on lwjgl, since we require it for GLFW anyway
	private static final MethodHandle memByteBuffer;
	
	static {
		MethodHandles.Lookup lookup = MethodHandles.lookup();
		try {
			Class<?> clz = lookup.findClass("org.lwjgl.system.MemoryUtil");
			memByteBuffer = lookup.findStatic(clz, "memByteBuffer", MethodType.methodType(ByteBuffer.class, new Class[]{Long.TYPE, Integer.TYPE}));
		} catch (Throwable err) {
			System.err.println("Could not find LWJGL MemoryUtil's memByteBuffer method.\nAre you using LWJGL 3.x?");
			throw new RuntimeException(err);
		}
	}
}
