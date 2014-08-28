package de.leanovate.dose.billing.logging;

import java.util.Random;

public class UnsignedRandom extends Random{
    @Override
    public long nextLong() {

        return super.nextLong() & Long.MAX_VALUE;
    }
}
