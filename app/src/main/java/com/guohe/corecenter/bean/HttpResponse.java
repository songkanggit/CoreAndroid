package com.guohe.corecenter.bean;


import android.text.TextUtils;

import com.guohe.corecenter.constant.CodeConst;

public class HttpResponse {
    /**
     * 状态吗
     */
    private String code;
    /**
     * 数据总数
     */
    private Long count;
    /**
     * 当前页码
     */
    private Integer currentPage;
    /**
     * 当前页码
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Long totalPage;
    /**
     * 数据域
     */
    private Object data;
    /**
     * 附加消息
     */
    private String msg;

    /**
     * Http状态
     */
    private String status;

    /**
     * TOKEN
     */
    private String accessToken;

    public HttpResponse() {
        code = CodeConst.CODE_OK;
        msg = "SUCCESS";
    }

    public HttpResponse(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isSuccess(){
        if(!TextUtils.isEmpty(status)) {
            return status.equals("200");
        }
        return CodeConst.CODE_OK.equals(this.code);
    }
}
