/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data.fb;

/**
 *
 * @author akshat
 */
public class FacebookJSONResult {

    private FBData[] data;
    private FBPaging paging;
    private FBSummary summary;

    public FBSummary getSummary() {
        return summary;
    }

    public void setSummary(FBSummary summary) {
        this.summary = summary;
    }

    public FBData[] getData() {
        return data;
    }

    public void setData(FBData[] data) {
        this.data = data;
    }

    public FBPaging getPaging() {
        return paging;
    }

    public void setPaging(FBPaging paging) {
        this.paging = paging;
    }

}
