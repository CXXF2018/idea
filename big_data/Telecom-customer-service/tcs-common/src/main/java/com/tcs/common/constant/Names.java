package com.tcs.common.constant;

import com.tcs.common.bean.Val;
/**
 * 名称常量枚举类
 */

public enum Names implements Val{
    NAMESPACE("tcs"),
    TOPIC("tcs"),
    TABLENAME("tcs:calllog"),
    CF_CALLER("caller"),
    CF_CALLEE("callee"),
    CF_INFO("info");

    private String name;

    private Names(String name){
        this.name=name;
    }

    public void setvalue(Object val) {
        this.name=(String)val;
    }

    public String getvalue() {
        return name;
    }


}
