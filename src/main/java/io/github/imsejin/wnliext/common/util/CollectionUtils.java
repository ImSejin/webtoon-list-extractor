package io.github.imsejin.wnliext.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 컬렉션 유틸리티<br>
 * Collection utilities
 * 
 * <p>
 * 
 * </p>
 * 
 * @author SEJIN
 */
public final class CollectionUtils {

    private CollectionUtils() {}

    /**
     * Javascript의 splice와 동일하다.<br>
     * 불변리스트는 가변 리스트로 변경해야 한다.
     * 
     * <pre>
     * List list = new ArrayList<>(Arrays.asList("A", "B", "C"));
     * 
     * CollectionUtils.splice(list, 0): ["A", "B", "C"] / list: []
     * CollectionUtils.splice(list, 1): ["B", "C"] / list: ["A"]
     * CollectionUtils.splice(list, 2): ["C"] / list: ["A", "B"]
     * CollectionUtils.splice(list, 3): [] / list: ["A", "B", "C"]
     * </pre>
     */
    public static <T> List<T> splice(List<T> arr, int start) {
        return splice(arr, start, arr.size());
    }

    /**
     * Javascript의 splice와 동일하다.<br>
     * 불변리스트는 가변 리스트로 변경해야 한다.
     * 
     * <pre>
     * List list = new ArrayList<>(Arrays.asList("A", "B", "C"));
     * 
     * CollectionUtils.splice(list, 0, 0): [] / list: ["A", "B", "C"]
     * CollectionUtils.splice(list, 0, 2): ["A", "B"] / list: ["C"]
     * CollectionUtils.splice(list, 1, 1): ["B"] / list: ["A", "C"]
     * CollectionUtils.splice(list, 3, 1): [] / list: ["A", "B", "C"]
     * </pre>
     */
    public static <T> List<T> splice(List<T> arr, int start, int deleteCount) {
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

}
