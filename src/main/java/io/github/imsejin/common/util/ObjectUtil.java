package io.github.imsejin.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * 객체의 로딩을 지원하는 유틸 클래스
 */
@UtilityClass
public class ObjectUtil {

    /**
     * 클래스명으로 객체를 로딩한다.
     *
     * @param className the class name
     * @return the class
     * @throws ClassNotFoundException the class not found exception
     */
    @SneakyThrows(ClassNotFoundException.class)
    public Class<?> loadClass(String className) {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);

        if (clazz == null) clazz = Class.forName(className);

        return clazz;
    }

    /**
     * 클래스명으로 객체를 로드한 후 인스턴스화한다.
     *
     * @param className the class name
     * @return the object
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    @SneakyThrows({ InstantiationException.class, IllegalAccessException.class })
    public Object instantiate(String className) {
        return loadClass(className).newInstance();
    }

    /**
     * 클래스명으로 파라매터가 있는 클래스의 생성자를 인스턴스화한다.
     * 
     * <pre>
     * Class <?> clazz = EgovObjectUtil.loadClass(this.mapClass);
     * Constructor <?> constructor = clazz.getConstructor(new Class[] { DataSource.class, String.class });
     * Object[] params = new Object[] { getDataSource(), getUsersByUsernameQuery() };
     * this.usersByUsernameMapping = (EgovUsersByUsernameMapping) constructor.newInstance(params);
     * </pre>
     * 
     * @param className the class name
     * @param types the types
     * @param values the values
     * @return the object
     * @throws InstantiationException the class not found exception
     * @throws IllegalAccessException the instantiation exception
     * @throws InvocationTargetException the illegal access exception
     * @throws NoSuchMethodException the no such method exception
     */
    @SneakyThrows({ InstantiationException.class, IllegalAccessException.class, InvocationTargetException.class, NoSuchMethodException.class })
    public Object instantiate(String className, String[] types, Object[] values) {
        Class<?> clazz = loadClass(className);
        Class<?>[] classParams = new Class[values.length];
        Object[] objectParams = new Object[values.length];

        for (int i = 0; i < values.length; i++) {
            classParams[i] = loadClass(types[i]);
            objectParams[i] = values[i];
        }

        Constructor<?> constructor = clazz.getConstructor(classParams);
        return constructor.newInstance(values);
    }
    
    /**
     * 컬렉션이 null인지 또는 비어있는지 확인한다.
     */
    public boolean isNullOrEmpty(Collection<?> cl) {
        return cl == null || cl.isEmpty();
    }

    /**
     * 컬렉션이 null인지 또는 비어있는지 확인한다.
     */
    public boolean isNullOrEmpty(Collection<?>... cls) {
        // `빈 컬렉션 배열`이 파라미터로 넘어 왔을 때
        if (cls == null || cls.length == 0) return true;

        for (Collection<?> cl : cls) {
            if (isNullOrEmpty(cl)) return true;
        }
        return false;
    }

    /**
     * 요소가 있는 컬렉션인지 확인한다.
     */
    public boolean isNotEmpty(Collection<?> cl) {
        return !isNullOrEmpty(cl);
    }

    /**
     * 요소가 있는 컬렉션인지 확인한다.
     */
    public boolean isNotEmpty(Collection<?>... cls) {
        return !isNullOrEmpty(cls);
    }

    /**
     * Javascript의 splice와 동일하다.
     * 불변리스트는 가변 리스트로 변경해야 한다.
     * 
     * <pre>
     * List list = new ArrayList<>(Arrays.asList("A", "B", "C"));
     * 
     * ObjectUtil.splice(list, 0): ["A", "B", "C"] / list: []
     * ObjectUtil.splice(list, 1): ["B", "C"] / list: ["A"]
     * ObjectUtil.splice(list, 2): ["C"] / list: ["A", "B"]
     * ObjectUtil.splice(list, 3): [] / list: ["A", "B", "C"]
     * </pre>
     */
    public <T> List<T> splice(List<T> arr, int start) {
        return splice(arr, start, arr.size());
    }

    /**
     * Javascript의 splice와 동일하다.
     * 불변리스트는 가변 리스트로 변경해야 한다.
     * 
     * <pre>
     * List list = new ArrayList<>(Arrays.asList("A", "B", "C"));
     * 
     * ObjectUtil.splice(list, 0, 0): [] / list: ["A", "B", "C"]
     * ObjectUtil.splice(list, 0, 2): ["A", "B"] / list: ["C"]
     * ObjectUtil.splice(list, 1, 1): ["B"] / list: ["A", "C"]
     * ObjectUtil.splice(list, 3, 1): [] / list: ["A", "B", "C"]
     * </pre>
     */
    public <T> List<T> splice(List<T> arr, int start, int deleteCount) {
        List<T> remained = arr; // new ArrayList<>(arr); 새로운 리스트로 래핑하면 파라미터로 받은 리스트에 영향이 가지 않는다
        List<T> deleted = new ArrayList<>();
        if (deleteCount <= 0) return deleted;

        if (start > remained.size()) {
            start = remained.size();
        } else if (-remained.size() <= start && start < 0) {
            start += remained.size();
        } else if (-remained.size() > start) {
            start = 0;
        }

        int size = remained.size();
        for (int j = 1; start < size; j++) {
            deleted.add(remained.get(start));
            remained.remove(start);

            // 인덱스와 엘리먼트의 불일치 문제를 방지한다
            size = remained.size();

            if (deleteCount == j) break;
        }

        return deleted;
    }

    @SneakyThrows({ IOException.class, ClassNotFoundException.class })
    public Object deepCopy(Object o) {
        Object clone = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);

        byte[] buff = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(buff);
        ObjectInputStream ois = new ObjectInputStream(bais);
        clone = ois.readObject();

        return clone;
    }

}
