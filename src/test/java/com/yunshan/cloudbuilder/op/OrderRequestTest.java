package com.yunshan.cloudbuilder.op;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yunshan.cloudbuilder.ResultSet;

public class OrderRequestTest {
    
    private OrderRequest rc;

    @Before
    public void setUp() throws Exception {
        rc = new OrderRequest("10.33.37.28", "KVMPool", "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOrderVM() {
        ResultSet rs = rc.orderVM("dd32", "2c02e128-1881-4fd8-90e5-e106c81c25b1", "centos6.5").execute();
        assertEquals(200, rs.status());
    }

    @Test
    public void testOrderLB() {
        fail("Not yet implemented");
    }

    @Test
    public void testOrderVGW() {
        fail("Not yet implemented");
    }

    @Test
    public void testOrderValve() {
        fail("Not yet implemented");
    }

    @Test
    public void testOrderIP() {
        fail("Not yet implemented");
    }

    @Test
    public void testOrderBW() {
        fail("Not yet implemented");
    }

}
