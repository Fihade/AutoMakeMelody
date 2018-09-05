
package common;

import java.util.*;

public class Track {

	private List<MidiEvent> events;
	private MidiEvent endOfTrackEvent;

	public Track() {
		events = new ArrayList<MidiEvent>();
		MidiMessage endOfTrackMessage = new EndOfTrackMessage();
		endOfTrackEvent = new MidiEvent(endOfTrackMessage, 0);
		events.add(endOfTrackEvent);
	}

	public int getNumEvents() {
		return events.size();
	}

	public boolean add(MidiEvent event) {
		if (event == null || event.getTimeIndex() < 0) {
			throw new IllegalArgumentException();
		}

		synchronized (events) {
			if (event.getMessage() instanceof EndOfTrackMessage) {
				updateEndOfTrackMessage(event);
				return true;
			}

			int numEvents = events.size();

			int i = numEvents;
			for (; i > 0; i--) {
				if (event.getTimeIndex() >= events.get(i - 1).getTimeIndex()) {
					break;
				}
			}

			if (i == numEvents) {
				swapNewEventWithEndOfTrackEvent(event, numEvents);
			} else {
				events.add(i, event);
			}

			return true;
		}
	}

	private int getDeltaTimeIndexLengthForEvent(int eventIndex) {
		long deltaTime = eventIndex > 0
				? events.get(eventIndex).getTimeIndex() - events.get(eventIndex - 1).getTimeIndex()
				: events.get(eventIndex).getTimeIndex();
		return MidiFileUtils.getVariableLengthFieldByteLength(deltaTime);
	}

	private int swapNewEventWithEndOfTrackEvent(MidiEvent event, int numEvents) {
		int lastEventIndex = numEvents - 1;
		events.set(lastEventIndex, event);

		if (endOfTrackEvent.getTimeIndex() < event.getTimeIndex()) {
			endOfTrackEvent.setTimeIndex(event.getTimeIndex());
		}
		events.add(endOfTrackEvent);
		return lastEventIndex;
	}

	private void updateEndOfTrackMessage(MidiEvent event) {
		if (event.getTimeIndex() > endOfTrackEvent.getTimeIndex()) {
			endOfTrackEvent.setTimeIndex(event.getTimeIndex());
		}
	}

	public MidiEvent get(int index) {
		return events.get(index);
	}

	public int size() {
		return events.size();
	}

	public long getTotalTime() {
		return events.get(events.size() - 1).getTimeIndex();
	}

	public int getLength() {
		int length = 0;
		for (int i = 0; i < events.size(); i++) {
			length += events.get(i).getMessage().getLength() + getDeltaTimeIndexLengthForEvent(i);
		}

		return length;
	}

	private static class EndOfTrackMessage implements MidiMessage {
		private byte[] data;

		private EndOfTrackMessage() {
			data = new byte[3];
			data[0] = MetaMessage.META_STATUS_BYTE;
			data[1] = MetaMessage.Type.END_OF_TRACK.value;
			data[2] = 0;
		}

		public byte[] getData() {
			return data;
		}

		public byte[] toBytes() {
			return data;
		}

		public int getLength() {
			return 3;
		}

		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(this.data);
			return result;
		}

		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EndOfTrackMessage other = (EndOfTrackMessage) obj;
			if (!Arrays.equals(this.data, other.data))
				return false;
			return true;
		}
	}

	public void add(int key, int time, int volume) throws Exception {
		ChannelMessage on = ChannelMessage.noteOn(0, key, volume);
		ChannelMessage off = ChannelMessage.noteOff(0, key, volume);
		this.add(new MidiEvent(off, time));
		this.add(new MidiEvent(on, time));
	}
}