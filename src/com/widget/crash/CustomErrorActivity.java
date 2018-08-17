package com.widget.crash;

import com.app.xstore.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * 发生异常时统一跳转的界面
 * @author Ni Guijun
 *
 */
public class CustomErrorActivity extends Activity {

	private Button btn_restart;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_error);

        //**IMPORTANT**
        //The custom error activity in this sample is uglier than the default one and just
        //for demonstration purposes, please don't copy it to your project!
        //We recommend taking the original library's DefaultErrorActivity as a basis.
        //Of course, you are free to implement it as you wish in your application.

        //These three methods are available for you to use:
        //CustomActivityOnCrash.getStackTraceFromIntent(getIntent()): gets the stack trace as a string
        //CustomActivityOnCrash.getAllErrorDetailsFromIntent(context, getIntent()): returns all error details including stacktrace as a string
        //CustomActivityOnCrash.getRestartActivityClassFromIntent(getIntent()): returns the class of the restart activity to launch, or null if none

        //Now, treat here the error as you wish. If you allow the user to restart the app,
        //don't forget to finish the activity, otherwise you will get the CustomErrorActivity
        //on the activity stack and it will be visible again under some circumstances.

        TextView errorDetailsText = (TextView) findViewById(R.id.error_details);
        errorDetailsText.setText(CrashHelper.getStackTraceFromIntent(getIntent()));

        btn_restart = (Button) findViewById(R.id.btn_restart);

        final Class<? extends Activity> restartActivityClass = CrashHelper.getRestartActivityClassFromIntent(getIntent());

        if (restartActivityClass != null) {
        	btn_restart.setText("重启App");
        	btn_restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(CustomErrorActivity.this, restartActivityClass);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    finish();
                }
            });
        } else {
        	btn_restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

}
