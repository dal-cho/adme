package com.dalcho.adme.dto.registry;

import com.dalcho.adme.domain.Registry;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagingDto {
	private List<RegistryResponseDto> boardList;
	private int curPage;
	private boolean prev;
	private boolean next;
	private int startPage;
	private int endPage;
	private int totalPage;


	public static PagingDto of(List<RegistryResponseDto> registry, int totalPage, int curPage, int startPage, int endPage, boolean prev, boolean next){
		return PagingDto.builder()
				.boardList(registry)
				.totalPage(totalPage)
				.curPage(curPage)
				.startPage(startPage)
				.endPage(endPage)
				.prev(prev)
				.next(next)
				.build();
	}
}
