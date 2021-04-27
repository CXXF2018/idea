package com.tcs.common.bean;

/**
 * 数据对象
 */

public abstract class Data implements Val{

    public String content;

    public void setvalue(Object val) {
        content=(String)val;
    }

    public String getvalue() {
        return content;
    }
}
