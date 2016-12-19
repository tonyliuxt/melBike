package com.homepass.melbike;

import com.homepass.melbike.utility.Functions;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tony Liu
 */
public class MelBikeUnitTest {
    @Test
    public void retrieveData_isCorrect() throws Exception {
        // retrieve data without exception
        assertTrue(Functions.retrieveBikeSites());
    }
}