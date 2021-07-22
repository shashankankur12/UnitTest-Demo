package com.example.unittestdemoproject.example6;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class FitnessTrackerTest {
    FitnessTracker fitnessTracker;

    @Before
    public void setup() throws Exception {
        fitnessTracker = new FitnessTracker();
    }

    @Test
    public void step_totalIncremented() throws Exception {
        fitnessTracker.step();
        assertThat(fitnessTracker.getTotalSteps()).isEqualTo(1);
    }

    @Test
    public void runStep_totalIncrementedByCorrectRatio() throws Exception {
        fitnessTracker.runStep();
        assertThat(fitnessTracker.getTotalSteps()).isEqualTo(3);
    }
}