package tech.xfs.xfsgolibj.common;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class Coin {
    private static final BigDecimal atto = BigDecimal.valueOf(10).pow(18);
    private static final BigDecimal nano2atto = BigDecimal.valueOf(10).pow(9);
    private static final BigDecimal atto2base = BigDecimal.valueOf(10).pow(9);

    public static BigInteger baseCoinToAtto(BigDecimal base){
        return base.multiply(atto).toBigInteger();
    }
    public static BigInteger baseCoinToNano(BigDecimal base){
        return base.multiply(atto).toBigInteger();
    }
    public static BigInteger nanoToAtto(BigDecimal value){
        return value.multiply(nano2atto).toBigInteger();
    }
}
