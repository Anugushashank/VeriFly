package com.interns.verifly.buddy.model;

/**
 * Created by Shashank on 03-06-2016.
 */
public class ApiResponse {

    String status, msg;
    Response data;

    public String getStatus(){ return status; }

    public Response getData() { return data; }

    public String getMsg() { return msg; }

    public class Response{

        String userid;
        Boolean isActive;
        int earning;

        public String getUserid(){ return userid; }

        public Boolean getIsActive() { return isActive; }

        public int getEarning() { return earning; }

    }
}
