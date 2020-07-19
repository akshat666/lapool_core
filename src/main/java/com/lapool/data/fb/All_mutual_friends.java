/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data.fb;

/**
 *
 * @author akshat666
 */
public class All_mutual_friends {
    
    private FBSummary summary;

    private FriendsData[] data;

    private Paging paging;
    
    public FBSummary getSummary ()
    {
        return summary;
    }

    public void setSummary (FBSummary summary)
    {
        this.summary = summary;
    }

    public FriendsData[] getData ()
    {
        return data;
    }

    public void setData (FriendsData[] data)
    {
        this.data = data;
    }

    public Paging getPaging ()
    {
        return paging;
    }

    public void setPaging (Paging paging)
    {
        this.paging = paging;
    }
    
    
}
