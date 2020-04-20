package io.github.imsejin.common.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 객체의 로딩을 지원하는 유틸 클래스
 */
public class ObjectUtil {

    private ObjectUtil() {}

    /**
     * 클래스명으로 객체를 로딩한다.
     *
     * @param className the class name
     * @return the class
     * @throws ClassNotFoundException the class not found exception
     * @throws Exception the exception
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException, Exception {

        Class<?> clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException();
        } catch (Exception e) {
            throw new Exception(e);
        }

        if (clazz == null) {
            clazz = Class.forName(className);
        }

        return clazz;

    }

    /**
     * 클래스명으로 객체를 로드한 후 인스턴스화한다.
     *
     * @param className the class name
     * @return the object
     * @throws ClassNotFoundException the class not found exception
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws Exception the exception
     */
    public static Object instantiate(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        Class<?> clazz;

        try {
            clazz = loadClass(className);
            return clazz.newInstance();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new ClassNotFoundException(className + ": Class is cannot instantialized.");
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            throw new InstantiationException(className + ": Class is cannot instantialized.");
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new IllegalAccessException(className + ": Class is not accessed.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(className + ": Class is not accessed.");
        }
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
     * @throws ClassNotFoundException the class not found exception
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     * @throws Exception the exception
     */
    public static Object instantiate(String className, String[] types, Object[] values) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception {
        Class<?> clazz;
        Class<?>[] classParams = new Class[values.length];
        Object[] objectParams = new Object[values.length];

        try {
            clazz = loadClass(className);

            for (int i = 0; i < values.length; i++) {
                classParams[i] = loadClass(types[i]);
                objectParams[i] = values[i];
            }

            Constructor<?> constructor = clazz.getConstructor(classParams);
            return constructor.newInstance(values);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new ClassNotFoundException(className + ": Class is cannot instantialized.");
        } catch (InstantiationException ex) {
            ex.printStackTrace();
            throw new InstantiationException(className + ": Class is cannot instantialized.");
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            throw new IllegalAccessException(className + ": Class is not accessed.");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(className + ": Class is not accessed.");
        }
    }

    /**
     * 컬렉션이 null인지 또는 비어있는지 확인한다.
     */
    public static boolean isNullOrEmpty(Collection<?> cl) {
        return cl == null || cl.isEmpty();
    }

    /**
     * 컬렉션이 null인지 또는 비어있는지 확인한다.
     */
    public static boolean isNullOrEmpty(Collection<?>... cls) {
        // `빈 컬렉션 배열`이 파라미터로 넘어 왔을 때
        if (cls == null || cls.length == 0) {
            return true;
        }

        for (Collection<?> cl : cls) {
            if (cl == null || cl.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 요소가 있는 컬렉션인지 확인한다.
     */
    public static boolean isNotEmpty(Collection<?> cl) {
        return !isNullOrEmpty(cl);
    }

    /**
     * 요소가 있는 컬렉션인지 확인한다.
     */
    public static boolean isNotEmpty(Collection<?>... cls) {
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
    public static <T> List<T> splice(List<T> arr, int start) {
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
    public static <T> List<T> splice(List<T> arr, int start, int deleteCount) {
        List<T> remained = arr; // new ArrayList<>(arr); 새로운 리스트로 래핑하면 파라미터로 받은 리스트에 영향이 가지 않는다
        List<T> deleted = new ArrayList<>();

        if (start > remained.size()) {
            start = remained.size();
        } else if (-remained.size() <= start && start < 0) {
            start += remained.size();
        } else if (-remained.size() > start) {
            start = 0;
        }

        if (deleteCount > 0) {
            int size = remained.size();
            for (int j = 1; start < size; j++) {
                deleted.add(remained.get(start));
                remained.remove(start);

                // 인덱스와 엘리먼트의 불일치 문제를 방지한다
                size = remained.size();

                if (deleteCount == j) {
                    break;
                }
            }
        }

        return deleted;
    }

}
