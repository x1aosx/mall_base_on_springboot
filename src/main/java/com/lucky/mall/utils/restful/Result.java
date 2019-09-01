package com.lucky.mall.utils.restful;

import lombok.Data;

import java.util.Objects;

/**
 * @Description 统一返回结果
 * @Author shuxian.xiao
 * @Date 2019/8/2 18:58
 * @param <T> tag sfsadfsad
 */
@Data
public class Result<T> {
    /**
     * 生成一个空对象
     */
    private final static Result<?> EMPTY = new Result<>();
    /**
     * 数据
     */
    private T data;


    /**
     * 错误信息
     */
    private String msg;
    /**
     * 状态 1成功 其它失败，失败需要返回msg
     */
    private int state;
    /**
     * 是否跳转
     */
    private boolean isRedirect = false;

    /**
     * 跳转地址
     */
    private String redirectUrl;
    /**
     * token
     */
    private String token;

    /**
     * 无参构造
     */
    private Result() {
        this.data = null;
    }

    /**
     * 有参构造
     * @param message 信息
     * @param state 状态
     */
    private Result(String message, int state) {
        this.msg = message;
        this.state = state;
    }

    /**
     * 有参构造
     * @param data 数据
     * @param state 状态
     */
    private Result(T data, int state) {
        this.data = data;
        this.state = state;
    }



    /**
     * 创建一个空result类
     * @param <T> 范型
     * @return 空对象
     */
    public static <T> Result<T> empty() {
        @SuppressWarnings("unchecked")
        Result<T> t = (Result<T>) EMPTY;
        return t;
    }

    /**
     * 生成一个成功状态Result类
     * @param data 数据
     * @param <T> 对象类型
     * @return Result<T>
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data,1);
    }

    /**
     * 生成一个失败状态Result类
     * @param message 错误信息
     * @param <T> 类型
     * @return Result<T>
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(message, 0);
    }

    /**
     * 设置跳转地址
     * @param url 地址
     * @param <T> 类型
     * @return Result<T>
     */
    public <T> Result<T> redirect(String url) {
        this.isRedirect = true;
        this.redirectUrl = url;
        return (Result<T>) this;
    }

    /**
     * 设置失败状态跳转地址
     * @param url 地址
     * @param <T> 类型
     * @return Result<T>
     */
    public <T> Result<T> orFailRedirect(String url) {
        if (state == 1) {
            return (Result<T>) this;
        }
        this.isRedirect = true;
        this.redirectUrl = url;
        return (Result<T>) this;
    }

    /**
     * 判断是否传入值是否为空,非空则返回值，为空则返回失败信息
     * @param message 信息
     * @param <T> 类型
     * @return Result<T>
     */

    public <T> Result<T> orFail(String message) {
        if (null != data) {
            return (Result<T>) this;
        } else {
            this.msg = message;
            this.state = 0;
        }
        return (Result<T>) this;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }


    @Override
    public String toString() {
        return data != null
                ? String.format("result[%s]", data)
                : "result.empty";
    }




}