package com.yunshan.cloudbuilder.op;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yunshan.cloudbuilder.ResultSet;
import com.yunshan.cloudbuilder.VMState;

public class VMRequestTest {
    
    private  VMRequest rc; 

    @Before
    public void setUp() throws Exception {
        rc = new VMRequest("10.33.37.28", "KVMPool", 
                "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetVmByName() {
        ResultSet rs = rc.getVmByName("dd32");
        System.out.println(rs);
        assertEquals(200, rs.status());
    }

    @Test
    public void testGetVmIdByName() {
        int id = rc.getVmIdByName("dd32");
        assertEquals(17, id);
    }

    @Test
    public void testGetVmUuidByName() {
        String name = rc.getVmUuidByName("dd32");
        assertEquals("28343301-6d1c-4e78-99ee-2dd90be4999f", name);
    }

    @Test
    public void testGetVMById() {
        ResultSet rs = rc.getVMById(17);
        System.out.println(rs);
        assertEquals(200, rs.status());
    }

    @Test
    public void testSetVMToEPC() {
        String name = rc.getVmUuidByName("dd32");
        assertEquals("28343301-6d1c-4e78-99ee-2dd90be4999f", name);
    }

    @Test
    public void testGetVMStatusById() {
        VMState state = rc.getVMStatusById(17);
        assertTrue(state.equals(VMState.getVMStateByDisplayName("Running")));
    }

    @Test
    public void testGetVMLaunchServerById() {
        String str = rc.getVMLaunchServerById(17);
        assertEquals("172.16.1.106", str);
    }

    @Test
    public void testGetVMCtrlIPById() {
        String str = rc.getVMCtrlIPById(17);
        System.out.println(str);
        assertEquals("172.16.87.84", str);
    }

    @Test
    public void testGetVMSnapByName() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetVMSnapUuidByName() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testStartVmIfHalt() {
        ResultSet rs = rc.startVmIfHalt("vm-13-1");
        assertEquals(200, rs.status());
    }
    
    @Test
    public void testDeleteVMIfExist() {
        ResultSet rs = rc.deleteVMIfExist("vm-13-1");
        assertEquals(200, rs.status());
    }
}
