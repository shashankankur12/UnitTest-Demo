package com.example.unittestdemoproject.example3;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;

public class IntervalsOverlapDetectorTest {

    IntervalsOverlapDetector intervalsOverlapDetector;

    @Before
    public void setup() throws Exception {
        intervalsOverlapDetector = new IntervalsOverlapDetector();
    }

    // interval1 is before interval2

    @Test
    public void isOverlap_interval1BeforeInterval2_falseReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(8, 12);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isFalse();
    }

    // interval1 overlaps interval2 on start

    @Test
    public void isOverlap_interval1OverlapsInterval2OnStart_trueReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(3, 12);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isTrue();
    }
    // interval1 is contained within interval2

    @Test
    public void isOverlap_interval1ContainedWithinInterval2_trueReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-4, 12);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isTrue();
    }
    // interval1 contains interval2

    @Test
    public void isOverlap_interval1ContainsInterval2_trueReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(0, 3);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isTrue();
    }
    // interval1 overlaps interval2 on end

    @Test
    public void isOverlap_interval1OverlapsInterval2OnEnd_trueReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-4, 4);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isTrue();
    }
    // interval1 is after interval2

    @Test
    public void isOverlap_interval1AfterInterval2_falseReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-10, -3);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void isOverlap_interval1BeforeAdjacentInterval2_falseReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(5, 8);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void isOverlap_interval1AfterAdjacentInterval2_falseReturned() throws Exception {
        Interval interval1 = new Interval(-1, 5);
        Interval interval2 = new Interval(-3, -1);
        boolean result = intervalsOverlapDetector.isOverlap(interval1, interval2);
        Truth.assertThat(result).isFalse();
    }
}