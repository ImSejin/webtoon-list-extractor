package io.github.imsejin.file.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Webtoon
 * 
 * @author SEJIN
 */
@Getter
@Setter
@ToString(of = { "title", "authors", "platform" })
@Builder
public class Webtoon {

    /** Title in webtoon */
    @NonNull
    private String title;

    /** Authors of webtoon */
    @NonNull
    private String authors;

    /** Website that serves webtoon to subscribers */
    @NonNull
    private String platform;

    /** URL of the website */
    private String platformUrl;

    /** Completed or uncompleted */
    private boolean isCompleted;

    /** Creation time */
    @NonNull
    private String creationTime;

    /** Compression format */
    private String fileExtension;

    /** Compressed file size */
    private long size;

}
