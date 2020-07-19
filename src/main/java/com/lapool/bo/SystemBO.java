/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo;

import com.lapool.exception.BaseException;
import com.lapool.model.Account;
import com.lapool.model.SystemUser;
import java.util.List;

/**
 *
 * @author akshat666
 */
public interface SystemBO {
    
    /**
     *  Fetches all the accounts in the system matching the query
     * @param query 
     * @return
     * @throws BaseException 
     */
    public List fetchAccountByDescription(String query) throws BaseException;

    /**
     * Saves a new SytemUser object into the DB
     * @param newSystemUser 
     * @throws com.lapool.exception.BaseException 
     */
    public void saveSystemUser(SystemUser newSystemUser) throws BaseException;
    

}
