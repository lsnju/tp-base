package com.lsnju.base.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lisong
 * @since 2020/1/23 9:32
 * @version V1.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class PageList<T> extends BaseMo {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final PageList EMPTY_PAGE_LIST = new PageList() {

        {
            super.setResultList(Collections.EMPTY_LIST);
        }

        @Override
        public void setResultList(List resultList) {
        }

        @Override
        public void setTotalCount(long totalCount) {
        }

        @Override
        public void setMaxPageNum(long maxPageNum) {
        }

        @Override
        public void setPageNum(int pageNum) {
        }

        @Override
        public void setPageSize(int pageSize) {
        }
    };
    /** 分页大小 */
    private int pageSize;
    /** 分页页数 */
    private int pageNum;
    /** 最大页数 */
    private long maxPageNum;
    /** 总数量 */
    private long totalCount;
    /** 结果列表 */
    private List<T> resultList;

    public PageList() {
        super();
    }

    public PageList(PageList<?> pageList) {
        super();
        this.maxPageNum = pageList.maxPageNum;
        this.totalCount = pageList.totalCount;
        this.pageNum = pageList.pageNum;
        this.pageSize = pageList.pageSize;
    }

    public <R> PageList(PageList<R> pageList, Function<R, T> func) {
        super();
        Objects.requireNonNull(pageList);
        this.maxPageNum = pageList.maxPageNum;
        this.totalCount = pageList.totalCount;
        this.pageNum = pageList.pageNum;
        this.pageSize = pageList.pageSize;
        if (pageList.resultList != null && !pageList.resultList.isEmpty()) {
            this.resultList = pageList.resultList.stream().map(func).collect(Collectors.toList());
        } else {
            this.resultList = Collections.emptyList();
        }
    }

    public PageList(List<T> list, int pageSize, int pageNum) {
        Objects.requireNonNull(list);
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.totalCount = list.size();
        this.maxPageNum = calcMaxPageNum();
        int offset = pageSize * (pageNum - 1);
        this.resultList = list.stream().skip(offset).limit(pageSize).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static <T> PageList<T> emptyPageList() {
        return (PageList<T>) EMPTY_PAGE_LIST;
    }

    public long calcMaxPageNum() {
        return Math.round(Math.ceil(this.totalCount * 1.0 / this.pageSize));
    }
}
