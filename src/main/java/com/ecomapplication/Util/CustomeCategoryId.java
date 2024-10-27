package com.ecomapplication.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class CustomeCategoryId implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        String prifix = "cat" ;

        int ranNum = ThreadLocalRandom.current().nextInt(99, 9999);
        return prifix + ranNum;
    }
}
