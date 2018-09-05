package jmidi;

/*
	节奏型
 */
public class Rhythm {

	private static final byte[][] data = new byte[][] {
		{ 1, 0, 1, 0, 1, 0, 1, 0 },
		{ 1, 0, 1, 1, 0, 1, 0, 1 },
		{ 1, 1, 1, 1, 0, 0, 1, 0 },
		{ 1, 0, 1, 1, 0, 1, 1, 1 },
		{ 1, 1, 1, 1, 0, 1, 1, 0 },
		{ 1, 0, 1, 1, 1, 1 },
	};

	public static byte[] get(int type) {
		if(type < data.length)
			return data[type];
		
		return new byte[0];
	}
	
	public static int size(){
		return data.length;
	}
	
	public static byte[] rand(){
		return get(Note.rand(data.length));
	}
}