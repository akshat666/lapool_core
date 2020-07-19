/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.data.EmailVerificationBean;
import com.lapool.exception.BaseException;
import com.lapool.model.Account;
import com.lapool.model.Channel;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author akshat
 */
public interface NetworkBO {

    public void saveChannelEmail(EmailVerificationBean emailVerificationBean) throws BaseException;

    public List fetchUserChannelDetails(long authID) throws BaseException;

    /**
     * Fetch all the Channels using pagination
     *
     * @param first
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param filters
     * @return List
     * @throws BaseException
     */
    public List fetchChannels(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) throws BaseException;

    /**
     * Save a new channel
     *
     * @param channel
     * @throws BaseException
     */
    public void saveChannel(Channel channel) throws BaseException;

    /**
     * Update the channel
     *
     * @param channel
     * @throws BaseException
     */
    public void updateChannel(Channel channel) throws BaseException;

    /**
     * Return the no of Channels applying the given filters
     *
     * @param filters
     * @return
     * @throws com.lapool.exception.BaseException
     */
    public long fetchChannelCount(Map<String, Object> filters) throws BaseException;

    /**
     * Fetches all the channels in the system matching the query
     *
     * @param query
     * @return
     * @throws BaseException
     */
    public List fetchChannelsByName(String query) throws BaseException;

}
