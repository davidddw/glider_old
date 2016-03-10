package com.yunshan.cloudbuilder.op;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yunshan.cloudbuilder.ResultSet;

public class EPCRequestTest {
    
    private EPCRequest rc;
    
    @Before
    public void setUp() throws Exception {
        rc = new EPCRequest("10.33.37.28", 
                "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetEPCById() {
        ResultSet rs = rc.getEPCById(5);
        assertEquals(200, rs.status());
    }

}
