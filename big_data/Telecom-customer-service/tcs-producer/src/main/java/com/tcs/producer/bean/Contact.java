package com.tcs.producer.bean;

import com.tcs.common.bean.Data;

//联系人
public class Contact extends Data{
    private String tel;
    private String name;

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setvalue(Object val) {

        content=(String)val;
        String[] values = content.split("\t");
        setName(values[1]);
        setTel(values[0]);
    }

}
