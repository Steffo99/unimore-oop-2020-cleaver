package eu.steffo.cleaver.logic.utils;

public class SaltSerializer {
    public static String serialize(byte[] array) {
        StringBuilder serialized = new StringBuilder();
        for(byte b : array) {
            serialized.append(Byte.toString(b));
            serialized.append(",");
        }
        return serialized.toString();
    }

    public static byte[] deserialize(String string) {
        String[] stringArray = string.split(",");
        byte[] byteArray = new byte[stringArray.length];
        for(int i = 0; i < stringArray.length; i++) {
            byteArray[i] = Byte.parseByte(stringArray[i]);
        }
        return byteArray;
    }
}
