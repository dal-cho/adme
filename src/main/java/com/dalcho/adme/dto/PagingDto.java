package com.dalcho.adme.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	public PagingDto(Pageable page, List<T> content, long total){
		this.content = content;
		this.curPage = page.getPageNumber()+1;
		this.totalPage = total;
		this.startPage = (int) ((curPage-1) / (double) DISPLAY_PAGE_NUM) * DISPLAY_PAGE_NUM + 1;
		this.endPage = (int) Math.min(startPage + DISPLAY_PAGE_NUM - 1, totalPage);
		this.prev = startPage != 1;
		this.next = endPage < totalPage;
}

	@Builder
	public PagingDto(int curPage, boolean prev, boolean next, int startPage, int endPage, long totalPage, List<T> content) {
		this.curPage = curPage;
		this.prev = prev;
		this.next = next;
		this.startPage = startPage;
		this.endPage = endPage;
		this.totalPage = totalPage;
		this.content = content;
	}

	public static <T> PagingDto<T> of(Page<T> page) {
		return new PagingDto<>(page.getPageable(), page.getContent(), page.getTotalElements());
	}

	public static <T> PagingDto<T> of(Pageable page, List<T> content, long total) {
		return new PagingDto<>(page, content, total);
	}
}
