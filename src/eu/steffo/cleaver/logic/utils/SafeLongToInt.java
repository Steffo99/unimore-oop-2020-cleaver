package eu.steffo.cleaver.logic.utils;

/**
 * An utility class to prevent overflows while casting {@link Long Longs} to {@link Integer Integers}.
 */
public final class SafeLongToInt {
    /**
     * Disallow instantiations of this class.
     */
    private SafeLongToInt() {}

    /**
     * Cast a {@literal long} to an {@literal int}, returning {@link Integer#MAX_VALUE} if the value of the {@literal long} exceeds the value of
     * {@link Integer#MAX_VALUE}, {@link Integer#MIN_VALUE} if the value of the {@literal long} is lower than {@link Integer#MIN_VALUE} and the result of the
     * {@literal int} cast otherwise.
     * @param l The long to cast.
     * @return The casted int.
     */
    public static int longToInt(long l) {
        if(l > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        else if(l < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)l;
    }
}
