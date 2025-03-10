
package com.example.sofra.data.model.foodlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Foodlist {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private FoodListPagination data;

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

    public FoodListPagination getData() {
        return data;
    }

    public void setFoodListPagination(FoodListPagination data) {
        this.data = data;
    }

}
