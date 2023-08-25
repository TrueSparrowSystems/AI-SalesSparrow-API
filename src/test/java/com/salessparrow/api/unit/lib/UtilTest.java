package com.salessparrow.api.unit.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.salessparrow.api.lib.Util;

public class UtilTest {
    
    @Test
    public void testEscapeSpecialChars() {
        String[] inputStrings = {"\\a","%a","_a","'a","\"a"};
        String[] expectedOutputs = {"\\\\a","\\%a","\\_a","\\'a","\\\"a"};

        for(int i = 0; i < inputStrings.length; i++) {
            String result = Util.escapeSpecialChars(inputStrings[i]);
            assertEquals(expectedOutputs[i], result);
        }
    }

    @Test
    public void testUrlEncoder() {
        String[] inputStrings = {"\\\\a","\\%a","\\_a","\\'a","\\\"a","a+b","a b"};
        String[] expectedOutputs = {"%5C%5Ca","%5C%25a","%5C_a","%5C%27a","%5C%22a","a%2Bb","a+b"};

        for(int i = 0; i < inputStrings.length; i++) {
            String result = Util.urlEncoder(inputStrings[i]);
            assertEquals(expectedOutputs[i], result);
        }
    }
}
