package io.github.imsejin.file.model;

import io.github.imsejin.constant.interfaces.KeyValue;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 웹툰 플랫폼<br>
 * Platform
 * 
 * <p>
 * 웹툰을 제공하는 웹사이트<br>
 * Website that serves webtoon for subscribers
 * </p>
 * 
 * @author SEJIN
 */
@ToString
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

    /**
     * 축약하지 않은 플랫폼의 이름<br>
     * Non-reduced name of platform
     */
    private final String fullText;

    /**
     * 웹툰을 제공하는 웹사이트의 URL<br>
     * the website URL
     */
    private final String url;

    @Override
    public String key() {
        return name();
    }

    @Override
    public String value() {
        return this.fullText;
    }

    public String url() {
        return this.url;
    }

}
