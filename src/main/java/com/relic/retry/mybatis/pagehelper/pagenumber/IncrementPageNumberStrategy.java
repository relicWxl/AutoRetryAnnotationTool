package com.relic.retry.mybatis.pagehelper.pagenumber;

public class IncrementPageNumberStrategy implements IPageNumberStrategy {
    public IncrementPageNumberStrategy() {
    }

    public int getPageNumber(int pageNumber) {
        ++pageNumber;
        return pageNumber;
    }
}