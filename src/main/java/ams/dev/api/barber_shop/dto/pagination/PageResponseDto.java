package ams.dev.api.barber_shop.dto.pagination;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResponseDto <T>{

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long  totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;


    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.last = page.isLast();
        this.first = page.isFirst();
    }


}
