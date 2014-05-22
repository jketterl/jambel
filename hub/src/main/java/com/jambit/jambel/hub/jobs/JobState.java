package com.jambit.jambel.hub.jobs;

import com.google.common.base.Function;
import com.google.common.base.Objects;

public final class JobState {

    // These are ordered from worst to best.

	public static enum Phase {
		STARTED, COMPLETED, FINISHED
	}

	public static enum Result {
		FAILURE, UNSTABLE, ABORTED, NOT_BUILT, SUCCESS
	}

	private final Phase phase;
	private final Result lastResult;

	public JobState(Phase phase, Result lastResult) {
		this.phase = phase;
		this.lastResult = lastResult;
	}

	public Phase getPhase() {
		return phase;
	}

	public Result getLastResult() {
		return lastResult;
	}

    // For convenience. (As always, this would be entirely unnecessary with proper closures.)
    static public Function<JobState, Phase> phaseFunction = new Function<JobState, Phase>() {
        @Override
        public Phase apply(JobState input) {
            return input.getPhase();
        }
    };

    static public Function<JobState, Result> lasResultFunction = new Function<JobState, Result>() {
        @Override
        public Result apply(JobState input) {
            return input.getLastResult();
        }
    };

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("phase", phase).add("last result", lastResult).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lastResult == null) ? 0 : lastResult.hashCode());
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobState other = (JobState) obj;

        return lastResult == other.lastResult && phase == other.phase;
    }

}
