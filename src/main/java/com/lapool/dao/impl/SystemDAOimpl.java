/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lapool.dao.impl;

import com.lapool.dao.SystemDAO;
import com.lapool.exception.BaseException;
import com.lapool.model.Account;
import com.lapool.model.SystemUser;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author akshat666
 */
public class SystemDAOimpl implements SystemDAO {

    final static Logger log = LoggerFactory.getLogger(SystemDAOimpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List fetchAccountsByQuery(String field, String query) throws BaseException {
        final String methodName = "fetchAccountsByQuery()";
        log.info("Entering :" + methodName);

        List<Account> results = null;
        Criteria criteria = null;

        try {
            criteria = this.sessionFactory.getCurrentSession().createCriteria(Account.class);

            //Add filters
            Criterion likeCricCriterion = Restrictions.like(field, query, MatchMode.ANYWHERE);
            criteria.add(likeCricCriterion);
            criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            results = criteria.list();

        } catch (HibernateException he) {
            log.error(methodName + ": Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error fetching accounts");
        } catch (Exception ex) {
            log.error(methodName + " :Exception : " + ex.getMessage());
            throw new BaseException("Error fetching accounts");
        }

        return results;

    }

    @Override
    public void saveSystemUser(SystemUser newSystemUser) throws BaseException {
        final String methodName = "saveSystemUser()";
        log.info("Entering :" + methodName);

        try {
            this.sessionFactory.getCurrentSession().save(newSystemUser);
        } catch (HibernateException he) {
            log.error("Hibernate Exception : " + he.getMessage());
            throw new BaseException("Error saving System user -" + he.getMessage());
        } catch (Exception ex) {
            log.error("Exception : " + ex.getMessage());
            throw new BaseException("Error saving system user -" + ex.getMessage());
        }

        log.info("Exiting :" + methodName);
    }

}
