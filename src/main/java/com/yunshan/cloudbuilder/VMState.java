package com.yunshan.cloudbuilder;

public enum VMState {
    VM0(0, "Temporary"), VM1(1, "Creating"), VM2(2, "Created"), VM3(3, "Starting"), VM4(4, "Running"), 
    VM5(5, "Pausing"), VM6(6,"Suspend"), VM7(7, "Resume"), VM8(8, "Stopping"), VM9(9, "Halt"), 
    VM10(10, "Modify"), VM11(11, "Exception"), VM12(12, "Destroy"), VM13(13, "Destroyed"), 
    VM14(14, "Migrate");
    
    private int index;
    private String displayName;

    private VMState(int index, String displayName) {
        this.index = index;
        this.displayName = displayName;
    }
    
    public static VMState getVMStateByIndex(int index) {
        for (VMState sp : VMState.values()) {
            if (index == sp.index) {
                return sp;
            }
        }
        return null;
    }
    
    public static VMState getVMStateByDisplayName(String displayName) {
        for (VMState sp : VMState.values()) {
            if (sp.displayName.equalsIgnoreCase(displayName)) {
                return sp;
            }
        }
        return null;
    }

    public static String getDisplayName(int index) {
        for (VMState sp : VMState.values()) {
            if (index == sp.index) {
                return sp.displayName;
            }
        }
        return null;
    }

    public static int getIndex(String displayName) {
        for (VMState it : VMState.values()) {
            if (it.displayName.equalsIgnoreCase(displayName)) {
                return it.index;
            }
        }
        return -1;
    }
    
    public static void main(String args[])   
    {   
        VMState v = VM2;
        System.out.println(v.equals(VMState.getVMStateByDisplayName("Creating")));  // 调用了toString()方法   
    }    
}
