package com.relic.retry.mybatis.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.relic.retry.mybatis.pagehelper.pagenumber.IPageNumberStrategy;
import com.relic.retry.mybatis.pagehelper.pagenumber.IncrementPageNumberStrategy;
import com.relic.retry.util.IntegerUtil;

import java.util.ArrayList;
import java.util.List;

public final class PageHelperUtil {
    private static final int defaultPageNum = 1;
    private static final int defaultPageSize = 100;

    public static <T> List<T> executeQuery(IMybatisQuery<T> queryWrapper) {
        return executeQuery(1, 100, queryWrapper);
    }

    public static <T> List<T> executeQuery(int pageNum, int pageSize, IMybatisQuery<T> query) {
        pageNum = IntegerUtil.defaultIfNonPositive(pageNum, 1);
        pageSize = IntegerUtil.defaultIfNonPositive(pageSize, 100);
        List<T> resultList = new ArrayList();

        PageInfo pageInfo;
        do {
            PageHelper.startPage(pageNum, pageSize);
            pageInfo = new PageInfo(query.executeQuery());
            resultList.addAll(pageInfo.getList());
            ++pageNum;
        } while(pageInfo.isHasNextPage());

        return resultList;
    }

    public static <T> void executeService(IMybatisQuery<T> query, IService<T> service) {
        executeService(1, 100, query, service);
    }

    public static <T> void executeService(int pageNum, int pageSize, IMybatisQuery<T> queryWrapper, IService<T> service) {
        executeService(pageNum, pageSize, new IncrementPageNumberStrategy(), queryWrapper, service);
    }

    public static <T> void executeService(int pageNum, int pageSize, IPageNumberStrategy pageNumberStrategy, IMybatisQuery<T> queryWrapper, IService<T> service) {
        pageNum = IntegerUtil.defaultIfNonPositive(pageNum, 1);
        pageSize = IntegerUtil.defaultIfNonPositive(pageSize, 100);

        PageInfo pageInfo;
        do {
            PageHelper.startPage(pageNum, pageSize);
            pageInfo = new PageInfo(queryWrapper.executeQuery());
            service.execute(pageInfo.getList());
            pageNum = pageNumberStrategy.getPageNumber(pageNum);
        } while(pageInfo.isHasNextPage());

    }

    private PageHelperUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}