package com.dalcho.adme.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingResult<T> {
    private T data; // data
    private int count; // 총 페이지 수

    private int startPage; // 시작 페이지 번호
    private int endPage; // 끝 페이지 번호
    private boolean prev; // 이 전 버튼
    private boolean next; // 다음 버튼

}