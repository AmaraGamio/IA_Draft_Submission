package com.example.myia;

import java.util.Arrays;

public class Letter {
        public String[] words;

        @Override
        public String toString() {
                return "Words: " + Arrays.toString(words);
        }
}
