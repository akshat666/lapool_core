/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao;

import com.lapool.exception.BaseException;
import com.lapool.model.SystemUser;
import java.util.List;

/**
 *
 * @author akshat666
 */
public interface SystemDAO{
    
    /**
     *  Fetches the accounts by matching the query against the given fields
     * @param field
     * @param query
     * @return
     * @throws BaseException 
     */
    public List fetchAccountsByQuery(String field, String query) throws BaseException;

    /**
     * Save new System User
     * @param newSystemUser
     * @throws BaseException 
     */
    public void saveSystemUser(SystemUser newSystemUser) throws BaseException;
    
}
