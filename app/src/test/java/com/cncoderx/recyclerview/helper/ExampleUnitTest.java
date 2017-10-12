package com.cncoderx.recyclerview.helper;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        int[] a = {5, 8, 9, 8, 1, 7, 6};
        Arrays.sort(a);
        System.out.println(Arrays.toString(a));
    }
}