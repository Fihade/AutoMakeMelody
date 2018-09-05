package jmidi;

import java.io.*;

import javax.sound.midi.*;


public class Play {
	
	private static Sequence sequence;
	
	Receiver receiver;
	ShortMessage msg = new ShortMessage();

	public Play() {
		try {
			receiver = MidiSystem.getReceiver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Play(int timbre) {
		try {
			receiver = MidiSystem.getReceiver();
			
			msg.setMessage(ShortMessage.PROGRAM_CHANGE, timbre, 0);
	        receiver.send(msg, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Play(File file){
		try {
			// 从本地文件加载midi
			sequence = MidiSystem.getSequence(file);
			// 从url加载midi
			// sequence = MidiSystem.getSequence(new URL("http://hostname/midifile"));
			// Create a sequencer for the sequence
			final Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequencer.setSequence(sequence);
			
			Thread.sleep(3000);
			// Start playing
			sequencer.addMetaEventListener(new MetaEventListener() {
				public void meta(MetaMessage event) {
					// 播放完毕
					if (event.getType() == 47) {
						System.exit(0);
					}
				}
			});
			sequencer.start();
		} catch (Exception e) {}
	}

	public void melody(int key) {
		base((byte) 127, key);
	}

	public void chord(int key) {
		base((byte) 90, key);
	}
	
	private void base(byte volume, int key) {
		try {
			msg.setMessage(ShortMessage.NOTE_ON, key, volume);
			receiver.send(msg, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}