package cn.fudges.server.common.result;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * @author 王平远
 * @since 2024/8/20
 */
public class ResultResponse<T> implements Serializable {

    private String code = "00000";

    private String message = "success";

    private T data;

    private Long total;

    public ResultResponse() {
    }

    public ResultResponse(T data) {
        this.data = data;
    }

    public ResultResponse(T data, Long total) {
        this.data = data;
        this.total = total;
    }

    public ResultResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean whetherSuccess() {
        return "00000".equals(this.code);
    }

    public static <T> ResultResponse<T> success(T data) {
        return new ResultResponse<>(data);
    }

    public static <T,V> ResultResponse<List<V>> success(IPage<T> data, Class<V> tClass) {
        return new ResultResponse<>(BeanUtil.copyToList(data.getRecords(), tClass), data.getTotal());
    }

    public static <T,V> ResultResponse<V> success(T data, Class<V> tClass) {
        return new ResultResponse<>(BeanUtil.copyProperties(data, tClass));
    }

    public static <T> ResultResponse<T> fail(String code, String message) {
        return new ResultResponse<>(code, message, null);
    }

    public static <T> ResultResponse<T> fail(String code, String message, T data) {
        return new ResultResponse<>(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
