package com.github.JavacLMD.projectZero.controller;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

public class MySQL_DOATest extends DOATest {


    @Before
    @Override
    public void testAccessor() {
        dataAccessor = new MySQL_DOA();
    }

    @After
    @Override
    public void testClose() {
        dataAccessor.close();
    }
}