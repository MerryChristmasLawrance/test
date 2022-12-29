package com.czy.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终会封装成此对象
 * @param <T>
 */
@Data
public class R<T> {
    private Integer code;//编码
    private String msg;//错误信息
    private T data;//数据
    private Map map=new HashMap();//动态数据

    //请求响应成功，返回r对象
    public static <T> R<T> success(T object){
        R<T> r = new R<>();
        r.data=object;
        r.code=1;
        return r;
    }

    //请求响应失败，返回r对象
    public static <T> R<T> error(String msg){
        R r = new R();
        r.msg=msg;
        r.code= 0;
        return r;
    }

    //这个方法是操作上面动态数据的，用到再说
    public R<T> add(String key, Object value){
        this.map.put(key,value);
        return this;
    }
}
