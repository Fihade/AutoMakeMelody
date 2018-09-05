package demo;

import javax.sound.midi.*;

//前奏版
public class Elise {
	
	public static void main(String[] args) throws Exception {
		int delay = 270;

		do {
			for (int i = 0; i < MELODY.length; i++) {
				play(MELODY[i], 1);
				play(CHORD[i], 2);

				Thread.sleep(delay);
			}
		}while(true);


	}

	static Receiver receiver;
	static ShortMessage msg = new ShortMessage();
	static {try {receiver = MidiSystem.getReceiver(); msg.setMessage(ShortMessage.PROGRAM_CHANGE, 0, 0); receiver.send(msg, -1);} catch (Exception e) {}}

	static final byte CENTER = 60;
	

	private static final byte[][] MELODY = {
		{1,3},{1,2,1},{1,3},{1,2,1},{1,3},{0,7},{1,2},{1,1},
		{0,6},{},{},{0,1},{0,3},{0,6},{0,7},{},{},{0,3},{0,5,1},{0,7},
		{1,1},{},{},{0,3},{1,3},{1,2,1},{1,3},{1,2,1},{1,3},{0,7},{1,2},{1,1},
		{0,6},{},{},{0,1},{0,3},{0,6},{0,7},{},{},{0,3},{1,1},{0,7},
		{0,6},{},{},{0,7},{1,1},{1,2},{1,3},{},{},{0,5},{1,4},{1,3},
		{1,2},{},{},{0,4},{1,3},{1,2},{1,1},{},{},{0,3},{1,2},{1,1},
		{0,7},{},{},{0,3},{1,3},{},{},{1,3},{2,3},{},{},{1,2,1},{1,3},{},{},
		{1,2,1},
	};

	/**
	 * 和弦
	 */
	private static final byte[][] CHORD = {
		{},{},{},{},{},{},{},{},
		{-2,6},{-1,3},{-1,6},{},{},{},{-2,3},{-1,3},{-1,5,1},{},{},{},
		{-2,6},{-1,3},{-1,6},{},{},{},{},{},{},{},{},{},
		{-2,6},{-1,3},{-1,6},{},{},{},{-2,3},{-1,3},{-1,5,1},{},{},{},
		{-2,6},{-1,3},{-1,6},{},{},{},{-1,1},{-1,5},{0,1},{},{},{},
		{-2,5},{-1,5},{-1,7},{},{},{},{-2,6},{-1,3},{-1,6},{},{},{},
		{-2,3},{-1,3},{0,3},{},{},{0,3},{1,3},{},{},{1,2,1},{1,3},{},{},{1,2,1},{1,3},
		{},
	};

	static void play(byte[] key, int type) throws Exception {
		if(key.length == 0) return;
		msg.setMessage(ShortMessage.NOTE_ON, key(key[0], key[1]) + (key.length > 2 ? key[2] : 0), type == 1 ? 127 : 100);
		receiver.send(msg, -1);
	}

	/**
	 * area第几组，note是音符：1 2 3 4 5 6 7 / do re mi fa so la si，过滤掉了半音
	 */
	static byte key(byte area, byte note) {
		byte result = CENTER - 1;
		result += 12 * area;
		for (byte i = 0; i < note; i++) {
			switch (i % 7 + 1) {
			case 1:
			case 4:
				result++;
				break;
			default:
				result += 2;
			}
		}
		return result;
	}
}