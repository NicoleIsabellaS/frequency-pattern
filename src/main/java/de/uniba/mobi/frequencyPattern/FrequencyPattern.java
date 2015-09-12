package de.uniba.mobi.frequencyPattern;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FrequencyPattern {

	private LocalTime begin;
	private LocalTime end;

	private int numberOfSegments;
	private int timeslotsPerSegment = 6;
	private int timelineSize;
	private DateTimeFormatter dateTimeFormat = DateTimeFormatter
			.ofPattern("yyyy-MM-dd HH:mm:ssX");

	public FrequencyPattern(LocalTime begin, LocalTime end) {
		this.begin = begin;
		this.end = end;
		this.numberOfSegments = (int) begin.until(end, ChronoUnit.HOURS);
		timelineSize = timeslotsPerSegment * numberOfSegments;
	}

	public float frequencyGenerator(Node a, Node b) {
		int[] segmentSizes = getSegmentSizes();

		float[] alphaValues = getAlphaValues();

		boolean[] commonOccurrences = calculateCommonOccurrences(a, b);
		float[] averageOfSegments = calculateAverageOfSegments(
				commonOccurrences, segmentSizes);

		float relativeFrequency = calculateRelativeFrequency(averageOfSegments,
				alphaValues);

		return relativeFrequency;
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

	private boolean[] calculateCommonOccurrences(Node a, Node b) {
		boolean[] output = new boolean[timelineSize];

		for (int i = 0; i < output.length; i++) {
			output[i] = false;
		}

		List<TimeAreaPair> timelineA = a.getTimeline();
		List<TimeAreaPair> timelineB = b.getTimeline();

		int counter = 0;
		LocalTime moment = LocalTime.from(begin);
		LocalTime lastMoment = LocalTime.from(end);
		while (moment.isBefore(lastMoment)) {
			List<TimeAreaPair> timelineAForCurrentTimeslot = getTimeAreaPairListFromTimeLine(
					timelineA, moment);
			List<TimeAreaPair> timelineBForCurrentTimeslot = getTimeAreaPairListFromTimeLine(
					timelineB, moment);
			output[counter++] = shareBeamzone(timelineAForCurrentTimeslot,
					timelineBForCurrentTimeslot);

			moment = moment.plusMinutes(60 / timeslotsPerSegment);
		}

		return output;
	}

	private boolean shareBeamzone(List<TimeAreaPair> timelineA,
			List<TimeAreaPair> timelineB) {
		for (TimeAreaPair entryA : timelineA) {
			for (TimeAreaPair entryB : timelineB) {
				if (entryA.getArea().getBeamzone()
						.equals(entryB.getArea().getBeamzone())) {
					return true;
				}
			}
		}
		return false;
	}

	private List<TimeAreaPair> getTimeAreaPairListFromTimeLine(
			List<TimeAreaPair> timeline, LocalTime moment) {
		List<TimeAreaPair> result = new ArrayList<TimeAreaPair>();
		LocalTime momentPlusTenMinutes = LocalTime.from(moment).plusMinutes(10);
		for (TimeAreaPair entry : timeline) {
			LocalTime entryTime = LocalTime.from(LocalDateTime.parse(
					entry.getTimestamp(), dateTimeFormat));
			if (entryTime.isAfter(moment)
					&& entryTime.isBefore(momentPlusTenMinutes)) {
				result.add(entry);
			}
		}
		return result;
	}

	private float[] calculateAverageOfSegments(boolean[] commonOccurrences,
			int[] segmentSizes) {
		float[] output = new float[numberOfSegments];

		int timelineIndex = 0;
		for (int i = 0; i < segmentSizes.length; i++) {
			int ammountOfCommonOccurrences = 0;
			for (int j = timelineIndex; j < timelineIndex + segmentSizes[i]; j++) {
				if (commonOccurrences[j]) {
					ammountOfCommonOccurrences++;
				}
			}
			output[i] = (float) ammountOfCommonOccurrences
					/ (float) segmentSizes[i];
			timelineIndex += segmentSizes[i];
		}

		return output;
	}

	private float calculateRelativeFrequency(float[] averageOfSegments,
			float[] alphaValues) {
		float output = 0.0f;

		for (int i = 0; i < numberOfSegments; i++) {
			output += averageOfSegments[i] * alphaValues[i];
		}

		return output;
	}

}