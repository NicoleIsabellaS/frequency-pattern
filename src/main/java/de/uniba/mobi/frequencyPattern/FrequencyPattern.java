package de.uniba.mobi.frequencyPattern;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniba.mobi.jdbc.DBConnection;

public class FrequencyPattern {

	private final int numberOfSegments = 5;
	private final int timeslotsPerSegment = 6;
	private final int timelineSize = timeslotsPerSegment * numberOfSegments;


	public float frequencyGenerator(Node a, Node b) {
		int[] segmentSizes = { timeslotsPerSegment, timeslotsPerSegment,
				timeslotsPerSegment, timeslotsPerSegment, timeslotsPerSegment };

		float[] alphaValues = { 0.2f, 0.2f, 0.2f, 0.2f, 0.2f };

		boolean[] commonOccurrences = calculateCommonOccurrences(a, b);
		float[] averageOfSegments = calculateAverageOfSegments(
				commonOccurrences, segmentSizes);

		float relativeFrequency = calculateRelativeFrequency(averageOfSegments,
				alphaValues);

		return relativeFrequency;
	}

	private boolean[] calculateCommonOccurrences(Node a, Node b) {
		boolean[] output = new boolean[timelineSize];

		for (int i = 0; i < output.length; i++) {
			output[i] = false;
		}

		List<TimeAreaPair> timelineA = a.getTimeline();
		List<TimeAreaPair> timelineB = b.getTimeline();

		/*
		 * Area[] aggregTimelineA = new Area[timelineSize]; Area[]
		 * aggregTimelineB = new Area[timelineSize];
		 */

		Map<Integer, List<Area>> timeslotAreaMappingA = new HashMap<>();
		Map<Integer, List<Area>> timeslotAreaMappingB = new HashMap<>();

		// Take only the last area of a timeslot
		for (TimeAreaPair each : timelineA) {
			int hour = Integer.parseInt(each.getTimestamp().split(" ")[1]
					.split(":")[0]);
			int minute = Integer.parseInt(each.getTimestamp().split(" ")[1]
					.split(":")[1]);

			int index = getIndexByHourAndMinute(hour, minute);

			if (timeslotAreaMappingA.get(index) == null) {
				List<Area> tmpList = new ArrayList<>();
				tmpList.add(each.getArea());
				timeslotAreaMappingA.put(index, tmpList);
			} else {
				List<Area> tmpList = (List<Area>) timeslotAreaMappingA
						.get(index);
				tmpList.add(each.getArea());
				timeslotAreaMappingA.put(index, tmpList);
			}
			// aggregTimelineA[getIndexByHourAndMinute(hour, minute)] =
			// each.getArea();
		}

		for (TimeAreaPair each : timelineB) {
			int hour = Integer.parseInt(each.getTimestamp().split(" ")[1]
					.split(":")[0]);
			int minute = Integer.parseInt(each.getTimestamp().split(" ")[1]
					.split(":")[1]);

			int index = getIndexByHourAndMinute(hour, minute);

			if (timeslotAreaMappingB.get(index) == null) {
				List<Area> tmpList = new ArrayList<>();
				tmpList.add(each.getArea());
				timeslotAreaMappingB.put(index, tmpList);
			} else {
				List<Area> tmpList = (List<Area>) timeslotAreaMappingB
						.get(index);
				tmpList.add(each.getArea());
				timeslotAreaMappingB.put(index, tmpList);
			}
		}

		for (int i = 0; i < timelineSize; i++) {
			if (timeslotAreaMappingA.get(i) != null) {
				for (Area eachA : timeslotAreaMappingA.get(i)) {
					if (timeslotAreaMappingB.get(i) != null) {
						for (Area eachB : timeslotAreaMappingB.get(i)) {
							if (eachA != null & eachB != null) {
								if (eachA.getBeamzone().equals(
										eachB.getBeamzone())) {
									output[i] = true;
									break;
								}
							}
						}
					}
				}
			}
		}

		/*
		 * for (int i = 0; i < timelineSize; i++) { if (aggregTimelineA[i] !=
		 * null && aggregTimelineB[i] != null) { if
		 * (aggregTimelineA[i].getBeamzone().equals(
		 * aggregTimelineB[i].getBeamzone())) { output[i] = true; } else {
		 * output[i] = false; } } }
		 */

		return output;
	}

	private int getIndexByHourAndMinute(int hour, int minute) {
		int output = 0;

		output = ((hour - 19) * timeslotsPerSegment)
				+ (minute / (60 / timeslotsPerSegment));

		return output;
	}

	private float[] calculateAverageOfSegments(
			boolean[] commonOccurrences, int[] segmentSizes) {
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