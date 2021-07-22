package com.example.unittestdemoproject.example8;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
public class StringRetrieverTest {

    public static final int ID = 10;
    public static final String STRING = "string";
    @Mock Context mContextMock;
    StringRetriever SUT;

    @Before
    public void setup() throws Exception {
        SUT = new StringRetriever(mContextMock);
    }

    @Test
    public void getString_correctParameterPassedToContext() throws Exception {
        SUT.getString(ID);
        verify(mContextMock).getString(ID);
    }

    @Test
    public void getString_correctResultReturned() throws Exception {
        when(mContextMock.getString(ID)).thenReturn(STRING);
        String result = SUT.getString(ID);
        assertThat(result, is(STRING));
    }
}