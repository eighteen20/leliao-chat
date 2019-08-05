package com.ljm.chat.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 自定义响应数据结构
 *  			这个类是提供给门户，ios，安卓，微信商城用的
 *  			门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 *  			其他自行处理
 *  			200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 * @Author Liujinmai
 * @Date 2019/7/15-15:59
 * @Version V1.0
 */
@Data
@NoArgsConstructor
public class JsonResult {
    /**
     * 响应业务状态
     */
    private Integer status;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应中的数据
     */
    private Object data;

    /**
     * 不使用
     */
    private String ok;

    public static JsonResult build(Integer status, String msg, Object data) {
        return new JsonResult(status, msg, data);
    }

    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }

    public static JsonResult errorMsg(String msg) {
        return new JsonResult(500, msg, null);
    }

    public static JsonResult errorMap(Object data) {
        return new JsonResult(501, "error", data);
    }

    public static JsonResult errorTokenMsg(String msg) {
        return new JsonResult(502, msg, null);
    }

    public static JsonResult errorException(String msg) {
        return new JsonResult(555, msg, null);
    }

    private JsonResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    private JsonResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }
}
