
package common;

import java.util.*;

public class Sequence {

	private static final int DEFAULT_RESOLUTION = 192;

	private List<Track> tracks;
	private int resolution;

	public Sequence() {
		this(DEFAULT_RESOLUTION);
	}

	public Sequence(int resolution) {
		tracks = Collections.synchronizedList(new ArrayList<Track>());
		this.resolution = resolution;
	}

	public Track createTrack() {
		Track track = new Track();
		tracks.add(track);
		return track;
	}

	public Track getTrackAt(int index) {
		if (index < 0 || index > tracks.size() - 1) {
			throw new IllegalArgumentException();
		}
		return tracks.get(index);
	}

	public int getNumTracks() {
		return tracks.size();
	}

	public long getTimeIndexLength() {

		long largestTotalTime = 0L;

		for (int i = 0; i < tracks.size(); i++) {
			long trackTotalTime = tracks.get(i).getTotalTime();
			if (trackTotalTime > largestTotalTime) {
				largestTotalTime = trackTotalTime;
			}
		}
		return largestTotalTime;
	}

	public int getResolution() {
		return this.resolution;
	}
}