package eu.steffo.cleaver.logic.utils;

/**
 * An utility class to serialize and deserialize byte arrays.
 */
public final class SaltSerializer {
    /**
     * Disallow instantiations of this class.
     */
    private SaltSerializer() {}

    /**
     * Serialize a byte array (converting it into a {@link String}).
     * @param array The input array.
     * @return The resulting string.
     */
    public static String serialize(byte[] array) {
        StringBuilder serialized = new StringBuilder();
        for(byte b : array) {
            serialized.append(Byte.toString(b));
            serialized.append(",");
        }
        return serialized.toString();
    }

    /**
     * Deserialize a {@link String} containing a byte array.
     * @param string The input string.
     * @return The resulting byte array.
     */
    public static byte[] deserialize(String string) {
        String[] stringArray = string.split(",");
        byte[] byteArray = new byte[stringArray.length];
        for(int i = 0; i < stringArray.length; i++) {
            byteArray[i] = Byte.parseByte(stringArray[i]);
        }
        return byteArray;
    }
}
