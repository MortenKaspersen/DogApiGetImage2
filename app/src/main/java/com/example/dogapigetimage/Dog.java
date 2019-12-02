package com.example.dogapigetimage;

public class Dog {
    private String Message;
    private String Status;

    public Dog(String message, String status){
        Message = message;
        Status = status;
    };

    public String getURL() {
        return Message;
    }

    public void setURL(String URL) {
        this.Message = URL;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
