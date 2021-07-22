package com.example.unittestdemoproject.example1;

import com.google.common.truth.Truth;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class PositiveNumberValidatorTest {

    PositiveNumberValidator positiveNumberValidator;

    @Before
    public void setup() {
        positiveNumberValidator = new PositiveNumberValidator();
    }

    @Test
    public void test1() {
        boolean result = positiveNumberValidator.isPositive(-1);
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void test2() {
        boolean result = positiveNumberValidator.isPositive(0);
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void test3() {
        boolean result = positiveNumberValidator.isPositive(1);
        Truth.assertThat(result).isTrue();
    }
}