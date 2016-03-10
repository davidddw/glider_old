package com.yunshan.common;

import com.yunshan.cloudbuilder.op.EPCRequest;

public class Client {
    public static void main(String[] args) {
        EPCRequest rc = new EPCRequest("10.33.37.28", "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
        System.out.println(rc.getEPCs());
    }
}
