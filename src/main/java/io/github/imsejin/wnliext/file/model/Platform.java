package io.github.imsejin.wnliext.file.model;

import io.github.imsejin.common.constant.interfaces.KeyValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

/**
 * Platform for webtoon.
 *
 * <p> Website that serves webtoon for subscribers
 */
@RequiredArgsConstructor
public enum Platform implements KeyValue {

    CA("Comica", "http://www.comica.com"),
    CO("Comico", "http://comico.kr/webtoon/week"),
    D("Daum", "http://webtoon.daum.net"),
    H("Hankyoreh", "http://nuri.hani.co.kr/hanisite/dev/board/list.html?h_gcode=board&h_code=100"),
    I("IlyoMedia", "http://ilyo.co.kr/?ac=list&cate_id=118"),
    K("Kakao", "https://page.kakao.com/main"),
    L("Lezhin", "https://www.lezhin.com/ko"),
    N("Naver", "https://comic.naver.com/index.nhn"),
    NT("Nate", "http://comics.nate.com/main"),
    O("Ktoon", "https://www.myktoon.com/web/homescreen/main.kt"),
    OS("OneStore", "https://m.onestore.co.kr/mobilepoc/books/main.omp"),
    P("Paran", "http://www.paran.com"),
    SD("SportsDonga", "http://sports.donga.com/Cartoon?cid=0100000201"),
    ST("SportsToday", "http://stoo.asiae.co.kr/cartoon"),
    T("Toptoon", "https://toptoon.com"),
    TM("Toomics", "http://www.toomics.com"),
    TS("Tstore", "https://www.tstore.co.kr");

    private static final Map<String, Platform> keyMap = Arrays.stream(values())
            .collect(collectingAndThen(toMap(Platform::key, it -> it), Collections::unmodifiableMap));

    private static final Map<String, Platform> valueMap = Arrays.stream(values())
            .collect(collectingAndThen(toMap(Platform::value, it -> it), Collections::unmodifiableMap));

    /**
     * Non-reduced name of platform.
     */
    private final String fullText;

    /**
     * Website URL.
     */
    @Getter
    private final String url;

    public static Platform ofKey(String key) {
        return keyMap.get(key);
    }

    public static Platform ofValue(String value) {
        return valueMap.get(value);
    }

    @Override
    public String toString() {
        return this.fullText;
    }

    @Override
    public String key() {
        return name();
    }

    @Override
    public String value() {
        return this.fullText;
    }

}
