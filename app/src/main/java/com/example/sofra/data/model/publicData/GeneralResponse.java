
package com.example.sofra.data.model.publicData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private RegionPagination data;
    private RegionData city;

    public RegionData getCity() {
        return city;
    }

    public void setCity(RegionData city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RegionPagination getData() {
        return data;
    }

    public void setData(RegionPagination data) {
        this.data = data;
    }

}
