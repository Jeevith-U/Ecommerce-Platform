package com.ecomapplication.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class CustomAddressId implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        String prifix = "add" ;

        long ranNum = ThreadLocalRandom.current().nextInt(9, 999);
        return prifix + ranNum;
    }
}
