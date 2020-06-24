package io.github.imsejin.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 객체 유틸리티<br>
 * Object utilities
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
public final class ObjectUtils {

    private ObjectUtils() {}

    public static Object cloneDeep(Object obj) throws IOException, ClassNotFoundException {
        Object clone = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        clone = ois.readObject();

        return clone;
    }

}
