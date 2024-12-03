package com.relic.retry.mybatis.pagehelper.pagenumber;

public class AlwaysPageNumberStrategy implements IPageNumberStrategy {
    public AlwaysPageNumberStrategy() {
    }

    public int getPageNumber(int pageNumber) {
        return pageNumber;
    }
}
