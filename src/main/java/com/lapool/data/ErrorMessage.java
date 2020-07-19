/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

/**
 *
 * @author akshat
 */
public class ErrorMessage {

    private String message;
    private int status;
    private int code;
    private String link;
    private String developerMessage;
    
    public ErrorMessage(){
        
    }

    public ErrorMessage(String message, int status, int code, String link, String devMessage){
        this.message = message;
        this.code = code;
        this.status = status;
        this.link = link;
        this.developerMessage = devMessage;        
    }
    
    /**
     *  This method returns the internal error codes of the application
     * @return business error code
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

}
