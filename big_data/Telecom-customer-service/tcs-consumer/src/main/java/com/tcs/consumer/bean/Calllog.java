package com.tcs.consumer.bean;

import com.tcs.common.api.Column;
import com.tcs.common.api.RowKey;
import com.tcs.common.api.TableRef;

//通话日志
@TableRef("tcs:calllog")
public class Calllog {
    @RowKey
    private String rowkey;
    @Column(family="caller")
    private String call1;
    @Column(family="caller")
    private String call2;
    @Column(family="caller")
    private String calltime;
    @Column(family="caller")
    private String duration;
    @Column(family="caller")
    private String flag="1";

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calllog(){

    }

    public Calllog(String date){
        String[] values=date.split("\t");
        call1=values[0];
        call2=values[1];
        calltime=values[2];
        duration=values[3];
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getCall1() {
        return call1;
    }

    public void setCall1(String call1) {
        this.call1 = call1;
    }

    public String getCall2() {
        return call2;
    }

    public void setCall2(String call2) {
        this.call2 = call2;
    }

    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
