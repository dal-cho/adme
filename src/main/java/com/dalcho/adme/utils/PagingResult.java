package com.dalcho.adme.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingResult<T> {
    private T data;
    private int count;
}