
package common;

public interface MidiMessage {

	byte[] getData();

	byte[] toBytes();

	int getLength();
}