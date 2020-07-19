/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.exception;

/**
 *
 * @author akshat
 */
public class BaseException extends Exception{
    
    private int status;
    private int code;
    private String devMessage;
    private String link;

    
   public BaseException(){
       super();
   }
    
   public BaseException (String message){
       super(message);
   }
   
   public BaseException(String message, Throwable th){
       super(message, th);
   }
   
   public BaseException(int status, int code, String message, String devMessage, String link){
       super(message);
       this.status=status;
       this.code=code;
       this.devMessage = devMessage;
       this.link=link;
   }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDevMessage() {
        return devMessage;
    }

    public void setDevMessage(String devMessage) {
        this.devMessage = devMessage;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
   
   

}
