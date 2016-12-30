package com.springroll.orm;

import com.springroll.core.exceptions.ExceptionStore;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Created by anishjoseph on 29/12/16.
 */
public class SpringrollJtaTransactionManager extends JtaTransactionManager {

    @Override
    protected void doCommit(DefaultTransactionStatus status){
        try {
            super.doCommit(status);
        }catch (Exception e){
            ExceptionStore.setExceptionCauses(e, null);
            throw e;
        }
    }
}
