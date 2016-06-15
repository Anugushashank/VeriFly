package com.example.aparna.buddy.model;

import android.util.Log;
import android.view.View;

import com.example.aparna.buddy.app.BorrowerDetailsActivity;

/**
 * Created by Aparna on 1/5/16.
 */
public class BorrowerStateContainer {
    private BorrowerDetailsActivity activity;

    public BorrowerStateContainer(BorrowerDetailsActivity activity) {
        this.activity = activity;
    }

    public Boolean isCompleted() {
        if(!activity.getFrontImageCollegeId().equals("")  && !activity.getBackImageCollegeId().equals("") && checkBankProofs() &&  checkGradeSheets()&&
                !activity.getGoodFriendsRadio().equals("")
                && !activity.getYearBackRadio().equals("") && !activity.getRefDept().equals("")  && !activity.getRefYear().equals("") &&
                !activity.getRepayBackLoan().equals("") && !activity.getTransparentRadio().equals("") && !activity.getGiveLoanRadio().equals("") &&
                !activity.getSpinnerCocurricular().equals("") && !activity.getSpinnerFinRes().equals("")  && !activity.getSpinnerPunc().equals("")
                && !activity.getSpinnerSincere().equals("") && !activity.getSpinnerLoanRepay().equals("")  && activity.getNumBankProofs() > 0
                && activity.getNumGradeSheets() > 0 ){
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean isOngoing() {
        if( !activity.getGoodFriendsRadio().equals("") || !activity.getYearBackRadio().equals("") || !activity.getRefDept().equals("")
                || !activity.getRefYear().equals("") || !activity.getRepayBackLoan().equals("") || !activity.getTransparentRadio().equals("")
                || !activity.getGiveLoanRadio().equals("") || !activity.getSpinnerCocurricular().equals("") || !activity.getSpinnerFinRes().equals("")
                || !activity.getSpinnerPunc().equals("") || !activity.getSpinnerSincere().equals("") || !activity.getSpinnerLoanRepay().equals("")
                || activity.getNumBankProofs() > 0 || activity.getNumGradeSheets() > 0     ){

            return true;
        }
        else{
            return false;
        }
    }

    public Boolean checkBankProofs() {
        for (int i = 0; i < activity.getBankProofs().size()-1; i++) {
            if (!activity.getBankProofs().get(i).getMatch().equals("valid") && !activity.getBankProofs().get(i).getMatch().equals("invalid")){
                return false;
            }
        }
        return  true;
    }

    public Boolean checkGradeSheets() {
        for(int i = 0 ; i < activity.getGradeSheets().size()-1; i++){
            if(!activity.getGradeSheets().get(i).getMatch().equals("valid") && !activity.getGradeSheets().get(i).getMatch().equals("invalid")){
                return false;
            }
        }
        return true;
    }
}
