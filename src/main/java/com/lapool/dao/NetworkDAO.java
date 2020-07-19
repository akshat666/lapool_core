/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao;

import com.lapool.exception.BaseException;
import com.lapool.model.Channel;
import com.lapool.model.UserChannel;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author akshat
 */
public interface NetworkDAO {
    
    /**
     * Returns all the active user IDs subscribed to the passed list of channel IDs
     * 
     * @param channelIDs
     * @return List of AuthIDs
     * @throws BaseException 
     */
    public List<Long> getActiveUsersSubscribed(Collection channelIDs) throws BaseException;
    
    public List getUserChannelsFromAuthID(long authID) throws BaseException;

    public Channel getChannel(long channelID) throws BaseException;

    //public boolean isEmailPresent(String email) throws BaseException;
    
    public void saveChannelDetails(UserChannel workDetails) throws BaseException;
    
    public List fetchUserChannels(long authID) throws BaseException;
    
    /**
     * Fetch all the Channels
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return
     * @throws BaseException 
     */
    public List fetchChannels(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException;
    
    /**
     * Save a new channel
     * @param channel
     * @throws BaseException 
     */
    public void saveChannel(Channel channel) throws BaseException;

    /**
     *  Update the channel
     * @param channel 
     * @throws com.lapool.exception.BaseException 
     */
    public void updateChannel(Channel channel) throws BaseException;

    /**
     *  Fetch the count of Channels (For pagination)
     * @param filters
     * @return
     * @throws BaseException 
     */
    public long fetchChannelCount(Map<String, Object> filters) throws BaseException;

    /**
     * Fetch channels based on input query string
     * @param field
     * @param query
     * @return List
     * @throws com.lapool.exception.BaseException
     */
    public List fetchChannelsByQuery(String field, String query) throws BaseException;
}
