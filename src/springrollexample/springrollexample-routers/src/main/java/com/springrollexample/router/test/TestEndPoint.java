package com.springrollexample.router.test;

import com.springroll.core.DTO;
import com.springroll.core.ServiceDTO;
import com.springroll.core.services.INotificationManager;
import com.springroll.notification.CoreNotificationChannels;
import com.springroll.notification.FyiNotificationMessage;
import com.springroll.router.ReceiveInNewTransaction;
import com.springroll.router.SpringrollEndPoint;
import com.springrollexample.orm.entities.TestTableWithLocking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by anishjoseph on 11/09/16.
 */
@Service
public class TestEndPoint extends SpringrollEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(TestEndPoint.class);

    @PersistenceContext
    EntityManager entityManager;

    private void lockTest(TestDTO testDTO) {
        String tableName = "TestTableWithLocking";

        TestTableWithLocking rowOne = (TestTableWithLocking) entityManager.createQuery("select o from " + tableName + " o where id = 1").getSingleResult();
        TestTableWithLocking rowTwo = (TestTableWithLocking) entityManager.createQuery("select o from " + tableName + " o where id = 2").getSingleResult();

        String thread = testDTO.getThread();

        if (testDTO.getTestType().equals(TestDTO.TestType.OPTIMISTIC_LOCKING_COMPETING_THREADS)) {
            waitAWhile(5);        //Wait so that the 2nd thread also has time to read in the record from the DB before we start changing it
            if (thread.equals("Thread1")) {
                System.out.println("THREAD 1 woke up!!!!!!!!!!!!!!!!!");
                rowOne.setField1(testDTO.getValueToWrite());
            }else {
                System.out.println("THREAD 2 woke up!!!!!!!!!!!!!!!!!");
                rowOne.setField2(testDTO.getValueToWrite());
            }
            return;
        }
        if (testDTO.getTestType().equals(TestDTO.TestType.OPTIMISTIC_LOCKING_DB_DEADLOCK)) {
            if (thread.equals("Thread1")) {
                rowOne.setField1(testDTO.getValueToWrite());
            } else {
                rowTwo.setField2(testDTO.getValueToWrite());
            }

            entityManager.flush(); //This will cause oracle to lock the row that is being updated

            waitAWhile(5);

            if (thread.equals("Thread1")) {
                rowTwo.setField1(testDTO.getValueToWrite());
            } else {
                rowOne.setField2(testDTO.getValueToWrite());
            }

            entityManager.flush(); //DB will try and lock the row and will run into a deadlock
        }
    }

    @Autowired INotificationManager notificationManager;

    public void on(TE1_1 event){

        FyiNotificationMessage payload = new FyiNotificationMessage("messageKEY", new String[]{"Arg1", "Arg2"}, "BOM");
        notificationManager.sendNotification(CoreNotificationChannels.FYI, payload);
        checkAndRunTest(event);
        TE1_2 te2 = new TE1_2();
        te2.setPayload(event.getPayload());
        route(te2);
        TE1_3 te3 = new TE1_3();
        te3.setPayload(event.getPayload());
        route(te3);
        TE1_4 te4 = new TE1_4();
        te4.setPayload(event.getPayload());
        route(te4);

    }
    public void on(TE1_2 event){
        checkAndRunTest(event);
        TE1_5 te = new TE1_5();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE1_3 event){
        checkAndRunTest(event);

        AbstractTestEvent te = new TE1_6();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE1_4 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE1_7();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE1_5 event){
        checkAndRunTest(event);
    }
    public void on(TE1_6 event){
        checkAndRunTest(event);
    }
    public void on(TE1_7 event){
        checkAndRunTest(event);
        SynchToAsynchDTO dto = new SynchToAsynchDTO();
        List<ServiceDTO> payloads = new ArrayList<>(1);
        payloads.add(dto);
        routeToSynchronousSideFromAsynchronousSide(payloads);
        System.out.println("Othrside odne");
    }
    public void on(TestRootEvent event) {
        int testCase = event.getPayload().getTestCase();
        int testCaseIndex = event.getPayload().getTestLocation();

        AbstractTestEvent te = null;
        if(testCase == 1 && (event.getPayload().getTestType().equals(TestDTO.TestType.OPTIMISTIC_LOCKING_COMPETING_THREADS) ||
                             event.getPayload().getTestType().equals(TestDTO.TestType.OPTIMISTIC_LOCKING_DB_DEADLOCK)))
        {
            TestDTO testDO = new TestDTO();
            testDO.setThread("Thread1");
            testDO.setValueToWrite(testDO.getThread() + "--" + testCase);
            testDO.setTestType(event.getPayload().getTestType());
            lockTest(testDO);
            return;

        }
        String thread = "Thread2";


        String testLocationEventName = "TE" + testCase + "_" + testCaseIndex;
        event.getPayload().setValueToWrite("123");

        TestDTO testDO = new TestDTO();
        testDO.setTestLocationEventName(testLocationEventName);
        testDO.setThread(thread);
        testDO.setValueToWrite(testDO.getThread() + "--" + testLocationEventName);
        testDO.setTestCase(testCase);
        testDO.setTestLocation(testCaseIndex);
        testDO.setTestType(event.getPayload().getTestType());

        switch(testCase){
            case 1:
                te = new TE1_1();
                te.setPayload(testDO);
                route(te);break;
            case 2:
                te = new TE2_1();
                te.setPayload(testDO);
                route(te);
                break;
            case 3:
                te = new TE3_1();
                te.setPayload(testDO);
                route(te);
                break;
            case 4:
                te = new TE4_1();
                te.setPayload(testDO);
                route(te);
                te = new TE4_2();
                testDO = new TestDTO();
                testDO.setTestLocationEventName(testLocationEventName);
                testDO.setThread(thread);
                testDO.setValueToWrite(testDO.getThread() + "--" + testLocationEventName);
                te.setPayload(testDO);
                route(te);
                te = new TE4_3();
                testDO = new TestDTO();
                testDO.setTestLocationEventName(testLocationEventName);
                testDO.setThread(thread);
                testDO.setValueToWrite(testDO.getThread() + "--" + testLocationEventName);
                te.setPayload(testDO);
                route(te);

                break;
            case 5:
                te = new TE5_1();
                te.setPayload(testDO);
                route(te);
                break;
            default:
                return;
        }
    }
    public void on(TE2_1 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE2_2();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE2_2 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE2_3();
        te.setPayload(event.getPayload());
        route(te);
//        SynchToAsynchDTO dto = new SynchToAsynchDTO();
//        List<DTO> payloads = new ArrayList<>(1);
//        payloads.add(dto);
//        routeToSynchronousSideFromAsynchronousSide(payloads);

    }
    @ReceiveInNewTransaction(value = true)
    public void on(TE2_3 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE2_4();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE2_4 event){
        checkAndRunTest(event);
    }
    public void on(TE_SynchFromAsynchSide event){
        System.out.println(event);
    }
    public void on(TE3_1 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE3_2();
        te.setPayload(event.getPayload());
        route(te);
        AbstractTestEvent te1 = new TE3_3();
        te1.setPayload(event.getPayload());
        route(te1);
        AbstractTestEvent te2 = new TE3_6();
        te2.setPayload(event.getPayload());
        route(te2);
    }
    public void on(TE3_2 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE3_4();
        te.setPayload(event.getPayload());
        route(te);
    }
    @ReceiveInNewTransaction(value = true)
    public void on(TE3_4 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE3_9();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE3_3 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE3_5();
        te.setPayload(event.getPayload());
        route(te);
    }
    @ReceiveInNewTransaction(value = true)
    public void on(TE3_5 event){
        checkAndRunTest(event);
    }
    public void on(TE3_6 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE3_7();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE3_7 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE3_8();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE3_8 event){
        checkAndRunTest(event);
    }
    public void on(TE3_9 event){
        checkAndRunTest(event);
    }
    public void on(TE4_1 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE4_4();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE4_4 event){
        checkAndRunTest(event);
    }
    public void on(TE4_2 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE4_5();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE4_5 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE4_8();
        te.setPayload(event.getPayload());
        route(te);
    }
    @ReceiveInNewTransaction(value = true)
    public void on(TE4_8 event){
        checkAndRunTest(event);
    }
    public void on(TE4_3 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE4_6();
        te.setPayload(event.getPayload());
        route(te);
    }
    @ReceiveInNewTransaction(value = true)
    public void on(TE4_6 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE4_7();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE4_7 event){
        checkAndRunTest(event);
    }


    public void on(TE5_1 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE5_2();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE5_2 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE5_3();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE5_3 event){
        checkAndRunTest(event);
        AbstractTestEvent te = new TE5_4();
        te.setPayload(event.getPayload());
        route(te);
    }
    public void on(TE5_4 event){
        checkAndRunTest(event);
    }


    private void checkAndRunTest(AbstractTestEvent event ){
        TestDTO testDO = (TestDTO) event.getPayload();
        if(testDO.getTestLocationEventName().equals(event.getClass().getSimpleName()))
            if(testDO.getTestType().equals(TestDTO.TestType.OPTIMISTIC_LOCKING_COMPETING_THREADS) ||
                    testDO.getTestType().equals(TestDTO.TestType.OPTIMISTIC_LOCKING_DB_DEADLOCK))
                lockTest(testDO);
            else if (testDO.getTestType().equals(TestDTO.TestType.EXCEPTION))
                blowup();
        logger.debug("-------------------Received Test Event {} - Job - {} XA - {} -------------------",event.getClass().getSimpleName(), event.getJobId(), event.getLegId());
    }
    private void waitAWhile(long seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void blowup(){
        throw new RuntimeException("Deliberately blowing up for Tests");
    }
}
