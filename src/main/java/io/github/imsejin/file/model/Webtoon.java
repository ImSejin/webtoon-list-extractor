package io.github.imsejin.file.model;

import java.util.List;

import lombok.Data;

/**
 * Webtoon
 * 
 * @author SEJIN
 */
@Data
public class Webtoon {

	/** Title in webtoon */
	private String title;

	/** Author of webtoon */
	private List<String> author;

	/** Website that serves webtoon to subscribers */
	private String platform;
	
	/** URL of the website */
	private String platformUrl;

	/** Completed or uncompleted */
	private boolean isCompleted;

	/** Creation time */
	private String creationTime;

	/** Compression format */
	private String fileExtension;

	/** Compressed file size */
	private long size;

}
