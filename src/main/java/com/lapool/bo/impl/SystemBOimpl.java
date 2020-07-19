/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.bo.impl;

import com.lapool.bo.SystemBO;
import com.lapool.dao.SystemDAO;
import com.lapool.exception.BaseException;
import com.lapool.model.SystemUser;
import com.lapool.util.ApplicationConstants;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author akshat666
 */
public class SystemBOimpl implements SystemBO {

    final static Logger log = LoggerFactory.getLogger(SystemBOimpl.class);

    private SystemDAO systemDAO;

    public void setSystemDAO(SystemDAO systemDAO) {
        this.systemDAO = systemDAO;
    }

    @Override
    @Transactional
    public List fetchAccountByDescription(String query) throws BaseException {
        final String methodName = "fetchAccountByDescription()";
        log.info("Entering and exiting:" + methodName);

        return systemDAO.fetchAccountsByQuery(ApplicationConstants.STR_DESCRIPTION, query);
    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void saveSystemUser(SystemUser newSystemUser) throws BaseException {
        final String methodName = "saveSystemUser()";
        log.info("Entering and exiting:" + methodName);

        systemDAO.saveSystemUser(newSystemUser);
    }

}
