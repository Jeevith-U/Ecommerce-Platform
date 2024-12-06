package com.ecomapplication.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class CustomPaymentId implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        String prifix = "Payment" ;

        long ranNum = ThreadLocalRandom.current().nextInt(111, 99999);

        StringBuilder randomString = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            char randomChar = (char) ('A' + ThreadLocalRandom.current().nextInt(26));
            randomString.append(randomChar);
        }
        return prifix + ranNum+ randomString.toString();
    }
}
