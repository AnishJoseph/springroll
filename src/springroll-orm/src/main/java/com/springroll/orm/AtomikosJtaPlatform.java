package com.springroll.orm;

import com.springroll.orm.AtomikosFactory;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
import org.springframework.beans.factory.annotation.Configurable;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * Created by anishjoseph on 08/09/16.
 */

@Configurable
public class AtomikosJtaPlatform extends AbstractJtaPlatform {

    private static final long serialVersionUID = -2829251509057881074L;

    @Override
    protected TransactionManager locateTransactionManager() {
        return AtomikosFactory.createUserTransactionManager();
    }

    @Override
    protected UserTransaction locateUserTransaction() {
        return AtomikosFactory.createUserTransactionImp();
    }
}