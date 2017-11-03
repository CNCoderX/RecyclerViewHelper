package com.cncoderx.recyclerview.helper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            array.add(i);
        }

        System.out.println(Arrays.toString(array.toArray()));

        array.add(array.size(), 10);

        System.out.println(Arrays.toString(array.toArray()));
    }
}