package ru.job4j.test;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestingTest {
    @Test
    public void name() {
        assertEquals(Testing.test(), "hello");

    }
}