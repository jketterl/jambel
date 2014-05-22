package com.jambit.jambel.hub.lights;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.jambit.jambel.hub.jobs.JobState;
import com.jambit.jambel.light.LightMode;
import com.jambit.jambel.light.SignalLightStatus;

public class LightStatusCalculator {
	public SignalLightStatus calc(Iterable<JobState> states) {
		if(Iterables.isEmpty(states)) {
			return SignalLightStatus.all(LightMode.OFF);
		}

		// PHASE
        FluentIterable<JobState.Phase> phases = FluentIterable.from(states).transform(JobState.phaseFunction);
		JobState.Phase worstPhase = Ordering.natural().min(phases);

		LightMode activeLightMode;
		switch (worstPhase) {
			case STARTED:
				activeLightMode = LightMode.ON;
				break;
			case COMPLETED:
				activeLightMode = LightMode.ON;
				break;
			case FINISHED:
				activeLightMode = LightMode.ON;
				break;
			default:
				throw new RuntimeException("phase " + worstPhase + " is unknown");
		}

		// RESULT
        FluentIterable<JobState.Result> results = FluentIterable.from(states).transform(JobState.lasResultFunction);
		JobState.Result aggregatedResult = Ordering.natural().min(results);

		switch (aggregatedResult) {
			case SUCCESS:
				return SignalLightStatus.onlyGreen(activeLightMode);
			case UNSTABLE:
				return SignalLightStatus.onlyYellow(activeLightMode);
			case FAILURE:
				return SignalLightStatus.onlyRed(activeLightMode);
			case ABORTED:
				return SignalLightStatus.onlyYellow(activeLightMode);
			case NOT_BUILT:
				return SignalLightStatus.all(activeLightMode).butYellow(LightMode.OFF);
			default:
				throw new RuntimeException("result " + aggregatedResult + " is unknown");
		}
	}
}
