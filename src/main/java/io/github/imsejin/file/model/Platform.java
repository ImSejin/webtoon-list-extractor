package io.github.imsejin.file.model;

/**
 * Website that serves webtoon to subscribers.
 * 
 * @author SEJIN
 */
public enum Platform {

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

    private String fullText;

    private String url;

    Platform(String fullText, String url) {
        this.fullText = fullText;
        this.url = url;
    }

    public String getFullText() {
        return fullText;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "[fullText=" + this.fullText + ", url=" + this.url + "]";
    }

}
