package com.ljm.chat.utils;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @Description 继承自己的MyMapper
 * @Author Liujinmai
 * @Date 2019/7/13-17:02
 * @Version V1.0
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //TODO
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
