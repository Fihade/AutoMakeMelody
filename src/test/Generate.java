package test;

import java.io.*;

import common.*;
import jmidi.*;

/*
	生成曲谱
 */
public class Generate {

	private static final byte range = 15; // 随机生成的音符范围
	private static final byte[] rhythm = Rhythm.rand(); // 随机选择节奏型
	private static final byte[] path = Path.rand(); // 随机选择走向

	private static final int bpm = 118; // 速度
	private static final int velocity = 96; // 96代表半拍，192是一拍
	private static final int max = 64; // 生成多少小节，最好是8的倍数

	private static int section = 1; // 当前第几小节，用于选择走向
	private static int prev = 9; // 上一个音符，初始化设置为9

	public static void main(String... args) throws Exception {
		File file = new File("C:\\Users\\swaggymiller\\Desktop\\test.mid");

		Sequence seq = new Sequence();
		Track main = seq.createTrack();
		main.add(new MidiEvent(MetaMessage.tempoMessage(bpm), 0));

		Track trackMelody = seq.createTrack();
		Track trackChord = seq.createTrack();

		prev = must(trackMelody, 0);
		prev = must(trackMelody, velocity);
		
		int cur = 0;
		do {
			for (int i = 0, chd = 0; i < rhythm.length; i++) {
				int pos = velocity * ((cur * rhythm.length + i) + 2); // 计算音符时间
				
				// 旋律区
				{
					if (section == path.length) { // 每次走向的最后一小节
						if (i > 0 && Note.melody(prev) != path[section - 1])
							prev = must(trackMelody, pos); // 直到生成和弦根音为止
					} else {
						prev = must(trackMelody, pos, 3);
					}
				}
				// 和弦区
				{
					if (rhythm[i] == 1) {
						byte[][] chords = Chord.get(path[section - 1]);
						byte[] chord = chords[chd++ % chords.length];

						trackChord.add(Note.key(chord[0], chord[1]), pos, 95);
					}
				}
			}
			section = (section == path.length) ? 1 : section + 1;
		} while (++cur < max);

		FileOutputStream out = new FileOutputStream(file);
		MidiFileWriter.write(seq, out);

		new Play(file); // 播放
	}
	
	private static boolean chk(int key, int root) {
		if (key - prev > 3)
			return false;
		if (prev - key > 3)
			return false;
		if (key == prev)
			return false;

		return Melody.get(root, Note.melody(key));
	}
	
	/**
	 * 生成音符
	 * 
	 * @param track 音轨
	 * @param pos 时间轴上的位置
	 * @param count 重新生成次数，如果不传，一直生成，直到合法为止
	 * @return
	 * @throws Exception
	 */
	private static int must(Track track, int pos, int... count) throws Exception {
		int i = 0;
		int max = (count.length > 0) ? count[0] : 0;
		
		do {
			int key = Note.rand(range);
			if (chk(key, path[section - 1])) {
				int area = key / 6; // 转换成区域
				int melody = Note.melody(key); // 转换成音符

				track.add(Note.key(area, melody), pos, 127);
				return key;
			}
		} while (++i < max || count.length == 0);
		
		return prev;
	}
}