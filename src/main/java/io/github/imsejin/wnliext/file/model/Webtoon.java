package io.github.imsejin.wnliext.file.model;

import lombok.*;

/**
 * 웹툰<br>
 * Webtoon
 *
 * @author SEJIN
 */
@Getter
@Setter
@Builder
@ToString(of = {"title", "authors", "platform"})
@EqualsAndHashCode(of = {"title", "authors", "platform", "completed"})
public class Webtoon {

    /**
     * 웹툰 제목<br>
     * Title of webtoon
     */
    @NonNull
    private String title;

    /**
     * 웹툰 작가<br>
     * Authors of webtoon
     */
    @NonNull
    private String authors;

    /**
     * 웹툰을 제공하는 웹사이트<br>
     * Website that serves webtoon for subscribers
     */
    @NonNull
    private String platform;

    /**
     * 웹툰을 제공하는 웹사이트의 URL<br>
     * the website URL
     */
    private String platformUrl;

    /**
     * 웹툰의 완결 여부<br>
     * Whether webtoon is completed or not
     */
    private boolean completed;

    /**
     * 웹툰 파일의 생성시간<br>
     * Creation time of webtoon file
     */
    @NonNull
    private String creationTime;

    /**
     * 웹툰 파일의 압축 포맷<br>
     * Compression format of webtoon file
     */
    private String fileExtension;

    /**
     * 웹툰 파일의 크기<br>
     * File size of webtoon file
     */
    private long size;

}
