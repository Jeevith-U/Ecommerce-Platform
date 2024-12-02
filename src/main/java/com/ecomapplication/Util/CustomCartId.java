package com.ecomapplication.Util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.concurrent.ThreadLocalRandom;

public class CustomCartId implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

        String prifix = "cart-" ;

        long ranNum = ThreadLocalRandom.current().nextInt(999, 99999);
        return prifix + ranNum;
    }
}
