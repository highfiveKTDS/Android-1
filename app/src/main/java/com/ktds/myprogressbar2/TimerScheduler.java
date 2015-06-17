package com.ktds.myprogressbar2;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

enum TaskControllerOption {
    EXECUTE,
    PAUSE,
    STOP
}

interface JobAction {
    /**
     * Job들을 실행할 기능들을 구현한다.
     * @return
     */
    public boolean executionJob();

    /**
     * 쓰레드를 제어한다. Message에 따라 스레드를 제어한다.
     */
    public void handlingTask(TaskControllerOption option);
}

enum ScheduleOption {
    EXACTLY_ONE_EXECUTION
    , AT_LEAST_ONE_EXECUTION
    , ALLWAYS_EXECUTION
    , USER_EXECUTION
}

class TimerScheduler extends TimerTask implements JobAction {

    private ScheduleOption option;
    private Handler handler;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private Timer jobScheduler;

    private long delayTimePerSecond;
    private int countOfCycle;
    private int cycle;

    public void setScheduleOption(ScheduleOption option) {
        this.option = option;
    }

    public TimerScheduler(long delayTimePerSecond
            , int cycle
            , ScheduleOption option) {

        this.delayTimePerSecond = delayTimePerSecond;
        this.cycle	= cycle;
        this.option	= option;

        jobScheduler = new Timer();
    }

    public void execute() {
        switch(option) {
            case EXACTLY_ONE_EXECUTION:
                jobScheduler.schedule(this, delayTimePerSecond);
                break;

            case AT_LEAST_ONE_EXECUTION:
            case ALLWAYS_EXECUTION:
                jobScheduler.schedule(this, 0, delayTimePerSecond);
                break;

            case USER_EXECUTION:
                System.out.println("Undefined option");
                break;
        }
    }

    @Override
    public void run() {

        switch(option) {

            case EXACTLY_ONE_EXECUTION:
                executionJob();
                jobScheduler.cancel();
                break;

            case AT_LEAST_ONE_EXECUTION:
                executionJob();
                countOfCycle++;
                if(countOfCycle>=cycle) {
                    jobScheduler.cancel();
                }
                break;

            case ALLWAYS_EXECUTION:
                executionJob();
                break;

            case USER_EXECUTION:
                jobScheduler.cancel();
                break;
        }
    }

    @Override
    public boolean executionJob() {
        //Android handler에서 메세지를 받아와서 해당 메세지일 경우 처리한다.
        switch(countOfCycle) {
            case 0:
                handler.sendMessage(handler.obtainMessage(0, "IN_PROGRESS"));
                break;
            case 1:
                handler.sendMessage(handler.obtainMessage(0, "DOWNLOADING"));
                break;
            case 2:
                handler.sendMessage(handler.obtainMessage(0, "FINALIZE"));
                break;
            case 3:
                handler.sendMessage(handler.obtainMessage(0, "COMPLETED"));
                break;
        }
        return true;
    }

    @Override
    public void handlingTask(TaskControllerOption option) {
        switch(option) {
            case EXECUTE:
                countOfCycle++;
                break;
            case STOP:
                jobScheduler.cancel();
                countOfCycle = 0;
                break;
        }

    }
}
