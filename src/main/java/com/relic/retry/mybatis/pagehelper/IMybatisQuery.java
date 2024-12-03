package com.relic.retry.mybatis.pagehelper;


import java.util.List;

public interface IMybatisQuery<T> {
    List<T> executeQuery();
}
