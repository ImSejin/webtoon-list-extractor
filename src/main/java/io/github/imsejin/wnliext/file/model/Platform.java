package io.github.imsejin.wnliext.file.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

/**
 * Platform for webtoon.
 *
 * <p> Website that serves webtoon for subscribers
 */
@Getter
@RequiredArgsConstructor
public enum Platform {

    COMICA("CA", "Comica", "http://www.comica.com"),
    COMICO("CO", "Comico", "http://comico.kr/webtoon/week"),
    DAUM("D", "Daum", "http://webtoon.daum.net"),
    HANKYOREH("H", "Hankyoreh", "http://nuri.hani.co.kr/hanisite/dev/board/list.html?h_gcode=board&h_code=100"),
    ILYO_MEDIA("I", "IlyoMedia", "http://ilyo.co.kr/?ac=list&cate_id=118"),
    KAKAO("K", "Kakao", "https://page.kakao.com/main"),
    LEZHIN("L", "Lezhin", "https://www.lezhin.com/ko"),
    NAVER("N", "Naver", "https://comic.naver.com/index.nhn"),
    NATE("NT", "Nate", "http://comics.nate.com/main"),
    OLLEH("O", "Ktoon", "https://www.myktoon.com/web/homescreen/main.kt"),
    ONE_STORE("OS", "OneStore", "https://m.onestore.co.kr/mobilepoc/books/main.omp"),
    PARAN("P", "Paran", "http://www.paran.com"),
    SPORTS_DONGA("SD", "SportsDonga", "http://sports.donga.com/Cartoon?cid=0100000201"),
    SPORTS_TODAY("ST", "SportsToday", "http://stoo.asiae.co.kr/cartoon"),
    TOPTOON("T", "Toptoon", "https://toptoon.com"),
    TOOMICS("TM", "Toomics", "http://www.toomics.com"),
    T_STORE("TS", "Tstore", "https://www.tstore.co.kr");

    private static final Map<String, Platform> $CODE_LOOKUP = EnumSet.allOf(Platform.class).stream()
            .collect(collectingAndThen(toMap(it -> it.code, it -> it), Collections::unmodifiableMap));

    private static final Map<String, Platform> $CODE_NAME_LOOKUP = EnumSet.allOf(Platform.class).stream()
            .collect(collectingAndThen(toMap(it -> it.codeName, it -> it), Collections::unmodifiableMap));

    private final String code;

    /**
     * Non-reduced name of platform.
     */
    private final String codeName;

    /**
     * Website URL.
     */
    private final URL url;

    @SneakyThrows
    Platform(String code, String codeName, String url) {
        this.code = code;
        this.codeName = codeName;
        this.url = new URL(url);
    }

    public static Platform fromCode(String code) {
        if ($CODE_LOOKUP.containsKey(code)) return $CODE_LOOKUP.get(code);
        throw new IllegalArgumentException("Enumeration 'Platform' has no code: " + code);
    }

    /**
     * @see Webtoon#getPlatform()
     */
    public static Platform fromCodeName(String codeName) {
        if ($CODE_NAME_LOOKUP.containsKey(codeName)) return $CODE_NAME_LOOKUP.get(codeName);
        throw new IllegalArgumentException("Enumeration 'Platform' has no code name: " + codeName);
    }

}
