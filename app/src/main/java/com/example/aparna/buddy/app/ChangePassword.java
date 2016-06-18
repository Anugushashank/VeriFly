package com.example.aparna.buddy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class ChangePassword extends AppCompatActivity {
    EditText newPassword, confirmPassword;
    String stringPassword, stringConfirmPassword;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);
        textView = (TextView)findViewById(R.id.doNotMatch);

    }

    public void onClick(View view){
        stringConfirmPassword = confirmPassword.getText().toString();
        stringPassword = newPassword.getText().toString();
        if(stringPassword.isEmpty() ){
            newPassword.setText("");
            newPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.enter_password));
            textView.setVisibility(View.VISIBLE);
        }
        else if(stringPassword.length()<4){
            newPassword.setText("");
            newPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.password_length));
            textView.setVisibility(View.VISIBLE);
        }
        else if(stringConfirmPassword.isEmpty()){
            confirmPassword.setText("");
            confirmPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.please_confirm_password));
            textView.setVisibility(View.VISIBLE);
        }
        else if(!stringPassword.equals(stringConfirmPassword)){
            confirmPassword.setText("");
            confirmPassword.setHintTextColor(Color.RED);
            textView.setText(getResources().getString(R.string.do_not_match));
            textView.setVisibility(View.VISIBLE);
        }
        else{
            textView.setVisibility(View.INVISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You cannot change your password later.Are you sure you want to keep this password?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Yes button clicked, do something


                        }
                    })
                    .setNegativeButton("No", null)						//Do nothing on no
                    .show();
        }
    }
}
