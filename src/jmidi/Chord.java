package jmidi;

/**
 * 和弦
 */
public class Chord {

	public static byte[][] get(int key) {
		if (key == 6)
			return new byte[][] { { -2, 6 }, { -1, 3 }, { -1, 6 }, { -1, 7 }, { 0, 1 } };
		if (key == 5)
			return new byte[][] { { -2, 5 }, { -1, 2 }, { -1, 5 }, { -1, 6 }, { -1, 7 } };
		if (key == 4)
			return new byte[][] { { -2, 4 }, { -1, 1 }, { -1, 4 }, { -1, 5 }, { -1, 6 } };
		if (key == 3)
			return new byte[][] { { -2, 3 }, { -2, 7 }, { -1, 3 }, { -1, 5 }, { -1, 7 } };
		if (key == 2)
			return new byte[][] { { -2, 2 }, { -2, 6 }, { -1, 2 }, { -1, 4 }, { -1, 6 } };
		if (key == 1)
			return new byte[][] { { -1, 1 }, { -1, 5 }, { 0, 1 }, { 0, 2 }, { 0, 3 } };

		return new byte[0][];
	}
}