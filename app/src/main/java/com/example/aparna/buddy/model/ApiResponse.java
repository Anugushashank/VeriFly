package com.example.aparna.buddy.model;

/**
 * Created by Shashank on 03-06-2016.
 */
public class ApiResponse {

    String status, msg;
    Response data;

    public String getStatus(){ return status; }

    public String getMsg() { return msg; }

    public Response getData() { return data; }

    public class Response{

        String userid;
        Boolean isActive, forcePasswordChange;

        public String getUserid(){ return userid; }

        public Boolean getIsActive() { return isActive; }

        public Boolean getForcePasswordChange() { return forcePasswordChange; }
    }
}
