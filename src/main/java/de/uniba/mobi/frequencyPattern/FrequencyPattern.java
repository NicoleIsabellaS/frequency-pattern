package de.uniba.mobi.frequencyPattern;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class FrequencyPattern {

	private LocalTime begin;
	private LocalTime end;

	private int numberOfSegments;
	private int timeslotsPerSegment = 6;
	int[] segmentSizes;
	float[] alphaValues;
	private DateTimeFormatter dateTimeFormat = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ssX");

	public FrequencyPattern(LocalTime begin, LocalTime end) {
		this.begin = begin;
		this.end = end;
		this.numberOfSegments = (int) begin.until(end, ChronoUnit.HOURS);

		segmentSizes = getSegmentSizes();
		alphaValues = getAlphaValues();
	}

	private float[] getAlphaValues() {
		float[] alphaValues = new float[numberOfSegments];

		for (int i = 0; i < numberOfSegments; i++) {
			alphaValues[i] = 1.0f / numberOfSegments;
		}
		return alphaValues;
	}

	private int[] getSegmentSizes() {
		int[] segmentSizes = new int[numberOfSegments];
		for (int i = 0; i < numberOfSegments; i++) {
			segmentSizes[i] = timeslotsPerSegment;
		}
		return segmentSizes;
	}

	public float frequencyGenerator(Node a, Node b) {
		int commonOccurrences = calculateNumberOfCommonOccurrences(a, b);
		return calculateRelativeFrequency(commonOccurrences);
	}

	private int calculateNumberOfCommonOccurrences(Node a, Node b) {
		int numberOfCommonOccurrences = 0;

		Map<String, String> timelineA = a.getTimeline();
		Map<String, String> timelineB = b.getTimeline();

		LocalTime moment = LocalTime.from(begin);
		LocalTime lastMoment = LocalTime.from(end);
		while (moment.isBefore(lastMoment)) {
			Map<String, String> timelineAForMoment = getTimeAreaPairListFromTimeLine(
					timelineA, moment);
			Map<String, String> timelineBForMoment = getTimeAreaPairListFromTimeLine(
					timelineB, moment);
			if (shareBeamzone(timelineAForMoment, timelineBForMoment)) {
				numberOfCommonOccurrences++;
			}
			moment = moment.plusMinutes(60 / timeslotsPerSegment);
		}

		return numberOfCommonOccurrences;
	}

	private boolean shareBeamzone(Map<String, String> timelineA,
			Map<String, String> timelineB) {
		for (String key : timelineA.keySet()) {
			if (timelineB.containsKey(key)) {
				return timelineA.get(key).equals(timelineB.get(key));
			}
		}
		return false;
	}

	private Map<String, String> getTimeAreaPairListFromTimeLine(
			Map<String, String> timeline, LocalTime moment) {
		Map<String, String> result = new HashMap<>();
		LocalTime momentPlusTenMinutes = LocalTime.from(moment).plusMinutes(10);
		for (String timestamp : timeline.keySet()) {
			LocalTime entryTime = LocalTime.from(LocalDateTime.parse(timestamp,
					dateTimeFormat));
			if (entryTime.isAfter(moment)
					&& entryTime.isBefore(momentPlusTenMinutes)) {
				result.put(timestamp, timeline.get(timestamp));
			}
		}
		return result;
	}

	private float calculateRelativeFrequency(int commonOccurrences) {
		float output = 0.0f;

		for (int i = 0; i < segmentSizes.length; i++) {
			output += (float) commonOccurrences / (float) segmentSizes[i]
					* alphaValues[i];
		}

		return output;
	}

}