package com.ef.dao;

import com.ef.model.ServerIllegalAccess;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by yaoquanyu on 11/15/17.
 */
public class ServerIllegalAccessDaoImpl implements ServerIllegalAccessDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(ServerIllegalAccess serverIllegalAccess) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(serverIllegalAccess);
        transaction.commit();
        session.close();
    }

    public void saveExceedLimitAccesses(List<ServerIllegalAccess> exceedLimitAccesses) {
        for (ServerIllegalAccess access: exceedLimitAccesses) {
            save(access);
        }
    }
}
