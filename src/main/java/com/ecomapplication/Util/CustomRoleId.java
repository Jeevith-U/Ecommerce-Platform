package com.ecomapplication.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class CustomRoleId implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        String prifix = "rol" ;

        long ranNum = ThreadLocalRandom.current().nextInt(999, 9999);

        StringBuilder randomString = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            char randomChar = (char) ('A' + ThreadLocalRandom.current().nextInt(26)); // 'A' to 'Z'
            randomString.append(randomChar);
        }
        return prifix + ranNum+ randomString.toString();
    }
}
