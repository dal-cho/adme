package com.dalcho.adme.dto.registry;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.VideoFile;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingDto<T> {
	private static final int DISPLAY_PAGE_NUM = 5;
	private int curPage;
	private boolean prev;
	private boolean next;
	private int startPage;
	private int endPage;
	private long totalPage;
	private List<T> content;

	private PagingDto(Pageable page, List<T> content, long total){
		this.content = content;
		this.curPage = page.getPageNumber()+1;
		this.totalPage = total;
		this.startPage = (int) ((curPage-1) / (double) DISPLAY_PAGE_NUM) * DISPLAY_PAGE_NUM + 1;
		this.endPage = (int) Math.min(startPage + DISPLAY_PAGE_NUM - 1, totalPage);
		this.prev = startPage != 1;
		this.next = endPage < totalPage;
}

	public static <T> PagingDto<Registry> of(Page<T> page, List<Registry> registryContent) {
		Pageable pageable = page.getPageable();
		long total = page.getTotalPages();
		return new PagingDto<>(pageable, registryContent, total);
	}

	public static <T> PagingDto<Object> of(Page<T> page, List<Registry> registryContent, List<VideoFile> videoContent, long total) {
		List<Object> content = new ArrayList<>();
		content.addAll(registryContent);
		content.addAll(videoContent);
		return new PagingDto<>(page.getPageable(), content, total);
	}
}
