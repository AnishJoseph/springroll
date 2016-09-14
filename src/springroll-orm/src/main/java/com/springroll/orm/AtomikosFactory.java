package com.springroll.orm;

/**
 * Created by anishjoseph on 08/09/16.
 */
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

import javax.transaction.SystemException;

/**
 * Created with IntelliJ IDEA.
 * User: shylu
 * Date: 20/12/13, 6:38 PM
 */
public class AtomikosFactory {

    private static final UserTransactionManager userTransactionManager;

    private static final UserTransactionImp userTransactionImp;

    static {
        userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        userTransactionManager.setStartupTransactionService(false);
        try {
            userTransactionManager.init();
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }

        userTransactionImp = new UserTransactionImp();
        try {
            userTransactionImp.setTransactionTimeout(240000);
        } catch (SystemException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserTransactionManager createUserTransactionManager() {
        return userTransactionManager;
    }

    public static UserTransactionImp createUserTransactionImp() {
        return userTransactionImp;
    }
}
