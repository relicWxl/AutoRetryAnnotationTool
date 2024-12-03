package com.relic.retry.mybatis.pagehelper;


import java.util.List;

public interface IService<T> {
    void execute(List<T> var1);
}
