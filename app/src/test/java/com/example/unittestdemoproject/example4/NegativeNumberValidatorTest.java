package com.example.unittestdemoproject.example4;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class NegativeNumberValidatorTest {
    NegativeNumberValidator negativeNumberValidator;

    @Before
    public void setup() {
        negativeNumberValidator = new NegativeNumberValidator();
    }

    @Test
    public void test1() {
        boolean result = negativeNumberValidator.isNegative(-1);
        Truth.assertThat(result).isTrue();
    }

    @Test
    public void test2() {
        boolean result = negativeNumberValidator.isNegative(0);
        Truth.assertThat(result).isFalse();
    }

    @Test
    public void test3() {
        boolean result = negativeNumberValidator.isNegative(1);
        Truth.assertThat(result).isFalse();
    }
}

