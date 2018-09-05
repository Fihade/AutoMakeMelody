
package common;

import java.io.*;

public class MidiFileWriter {

    public static final byte M = 0x4D;
    public static final byte T = 0x54;
    public static final byte h = 0x68;
    public static final byte d = 0x64;
    public static final byte r = 0x72;
    public static final byte k = 0x6B;

    private static final byte HEADER_LENGTH = 6;

    public static final void write(Sequence sequence, OutputStream out) throws IOException {
        writeHeader(sequence, out);

        int numTracks = sequence.getNumTracks();
        for (int i = 0; i < numTracks; i++) {
            writeTrack(sequence.getTrackAt(i), out);
        }
    }

    private static void writeHeader(Sequence sequence, OutputStream out) throws IOException {
        writeHeaderId(out);
        writeHeaderLength(out);
        writeMidiFormatOne(out);
        out.write(intToBytes(sequence.getNumTracks(), 2));
        out.write(intToBytes(sequence.getResolution(), 2));
    }

    private static void writeHeaderId(OutputStream out) throws IOException {
        out.write(new byte[]{M, T, h, d});
    }

    private static void writeHeaderLength(OutputStream out) throws IOException {
        out.write(new byte[]{0, 0, 0, HEADER_LENGTH});
    }

    private static void writeMidiFormatOne(OutputStream out) throws IOException {
        out.write(new byte[]{0, 1});
    }

    private static void writeTrack(Track track, OutputStream out) throws IOException {
        writeTrackId(out);
        writeTrackLength(track, out);

        MidiEvent firstEvent = track.get(0);
        writeMidiEvent(firstEvent, out, firstEvent.getTimeIndex());

        MidiEvent previousEvent = firstEvent;

        for (int i = 1; i < track.size(); i++) {
            MidiEvent event = track.get(i);
            writeMidiEvent(event, out, getDeltaTime(previousEvent, event));
            previousEvent = event;
        }
    }

    private static void writeTrackLength(Track track, OutputStream out) throws IOException {
		out.write(intToBytes(track.getLength(), 4));
    }

    private static void writeMidiEvent(MidiEvent event, OutputStream out, long actualTimeIndex) throws IOException {
        out.write(MidiFileUtils.getVariableLengthFieldBytes(actualTimeIndex));
        out.write(event.getMessage().toBytes());
    }

    private static long getDeltaTime(MidiEvent previousEvent, MidiEvent event) {
        return event.getTimeIndex() - previousEvent.getTimeIndex();
    }

    private static void writeTrackId(OutputStream out) throws IOException {
        out.write(new byte[]{M, T, r, k});
    }

    public static byte[] intToBytes(int value, int size) {
        switch (size) {
            case 1:
                return new byte[]{(byte) value};
            case 2:
                return new byte[]{(byte) (value >>> 8), (byte) value};
            case 3:
                return new byte[]{(byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
            case 4:
            default:
                return new byte[]{(byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value};
        }
    }
}