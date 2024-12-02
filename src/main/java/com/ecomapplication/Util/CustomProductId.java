package com.ecomapplication.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class CustomProductId implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        String prifix = "prod" ;

        long ranNum = ThreadLocalRandom.current().nextInt(9999, 999999);
        return prifix + ranNum;
    }
}
