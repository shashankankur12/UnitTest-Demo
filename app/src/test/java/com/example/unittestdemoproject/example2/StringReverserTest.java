package com.example.unittestdemoproject.example2;

import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class StringReverserTest {

    StringReverser stringReverser;

    @Before
    public void setup() throws Exception {
        stringReverser = new StringReverser();
    }

    @Test
    public void reverse_emptyString_emptyStringReturned() throws Exception {
        String result = stringReverser.reverse("");
        Truth.assertThat(result).isEmpty();
    }

    @Test
    public void reverse_singleCharacter_sameStringReturned() throws Exception {
        String result = stringReverser.reverse("a");
        Truth.assertThat(result).isEqualTo("a");
    }

    @Test
    public void reverse_longString_reversedStringReturned() throws Exception {
        String result = stringReverser.reverse("Shashank");
        Truth.assertThat(result).isEqualTo("knahsahS");
    }
}