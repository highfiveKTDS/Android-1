package com.ktds.myprogressbar2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

interface DialogWindow {
    int DIALOG_PROGRESS = 0;
    String IN_PROGRESS  = "IN_PROGRESS";
    String DOWNLOADING  = "DOWNLOADING";
    String FINALIZE     = "FINALIZE";
    String COMPLETED    = "COMPLETED";
}
public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private Button btnShowProgressDialog;
    private Button btnOption;

    public ProgressDialog getDialog() {
        return dialog;
    }

    public class MyTask extends Handler {
        private TimerScheduler timerScheduler;
        private Handler handler;

        public MyTask(Context context) {
            this.handler = this;

            timerScheduler
                    = new TimerScheduler(5000, 0, ScheduleOption.ALLWAYS_EXECUTION);
            timerScheduler.setHandler(handler);

        }

        public void execution() {
            Log.i("timerScheduler.execute", "execute()");
            timerScheduler.execute();
        }

        @Override
        public void handleMessage(Message msg) {
            String message = msg.obj.toString();
            Log.i("Handler executed:" + message, "handleMessage()");

            if(message.equals(DialogWindow.IN_PROGRESS)) {
                btnOption.setText(message);
                timerScheduler.handlingTask(TaskControllerOption.EXECUTE);
            }

            if(message.equals(DialogWindow.DOWNLOADING)) {
                btnOption.setText(message);
                timerScheduler.handlingTask(TaskControllerOption.EXECUTE);
            }

            if(message.equals(DialogWindow.FINALIZE)) {
                btnOption.setText(message);
                timerScheduler.handlingTask(TaskControllerOption.EXECUTE);
            }

            if(message.equals(DialogWindow.COMPLETED)) {
                btnOption.setText(message);
                timerScheduler.handlingTask(TaskControllerOption.EXECUTE);
                timerScheduler.handlingTask(TaskControllerOption.STOP);
            }

            if(message.equals(DialogWindow.IN_PROGRESS)) {
                dialog.setMessage("다운로드를 준비중 입니다.");
            }

            if(message.equals(DialogWindow.DOWNLOADING)) {
                dialog.setMessage("다운로드중..");
            }

            if(message.equals(DialogWindow.FINALIZE)) {
                dialog.setMessage("클라이언트 업데이트중...");
            }

            if(message.equals(DialogWindow.COMPLETED)) {
                dialog.dismiss();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowProgressDialog =
                (Button) findViewById(R.id.btnShowProgressDialog);

        btnOption =
                (Button) findViewById(R.id.btnOption);

        btnShowProgressDialog.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask(MainActivity.this);
                task.execution();
                showDialog(DialogWindow.DIALOG_PROGRESS);
            }
        });

        btnOption.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask(MainActivity.this);
                task.execution();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DialogWindow.DIALOG_PROGRESS)  {
            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("서버에서 데이터를 확인하는 중입니다.");

            return dialog;
        }
        return null;
    }
}