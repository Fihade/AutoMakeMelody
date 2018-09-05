package jmidi;

/**
 * 和弦走向
 */
public class Path {
	
	private static final byte[][] data = new byte[][]{
		{ 4, 5, 3, 6 },
		{ 6, 4, 1, 5 },
		{ 6, 5, 4, 3, 2, 3, 4, 5 },
		{ 1, 5, 6, 3, 4, 1, 4, 5 },
		{ 1, 6, 4, 5, 3, 6, 4, 5 },
		{ 6, 4, 5, 3, 4, 5, 3, 6 },
	};
	
	public static byte[] get(int seed) {
		if(seed < data.length)
			return data[seed];
		
		return new byte[0];
	}
	
	public static int size(){
		return data.length;
	}
	
	public static byte[] rand(){
		return get(Note.rand(data.length));
	}
}