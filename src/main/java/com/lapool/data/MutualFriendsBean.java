/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author akshat666
 */
public class MutualFriendsBean {
    
    private long totalMutualFriends;
    private List<User> user = new ArrayList<User>();

    public long getTotalMutualFriends() {
        return totalMutualFriends;
    }

    public void setTotalMutualFriends(long totalMutualFriends) {
        this.totalMutualFriends = totalMutualFriends;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
    
    

    public static class User {
        public User(){}
        public String name;
        public String picUrl;
        
    }
    
}
