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
        if(activity.getFrontImageCollegeId() !=null && activity.getBackImageCollegeId() != null && activity.getFrontImageAddressProof() != null
                && activity.getBackImageAddressProof() != null && activity.getGoodFriendsRadio() != null && activity.getYearBackRadio() != null
                && activity.getRefDept() != null && activity.getRefYear() != null && activity.getRepayBackLoan() != null &&
                activity.getTransparentRadio() != null && activity.getGiveLoanRadio() != null && activity.getSpinnerCocurricular() != null
                && activity.getSpinnerFinRes() != null && activity.getSpinnerPunc() != null && activity.getSpinnerSincere() != null &&
                activity.getNumBankProofs() > 0 && activity.getNumGradeSheets() > 0 ){

            return true;
        }
        else{
            return false;
        }
    }

    public Boolean isOngoing() {
        if(     activity.getGoodFriendsRadio() != null || activity.getYearBackRadio() != null || activity.getRefDept() != null ||
                activity.getRefYear() != null || activity.getRepayBackLoan() != null ||
                activity.getTransparentRadio() != null || activity.getGiveLoanRadio() != null || activity.getSpinnerCocurricular() != null
                && activity.getSpinnerFinRes() != null || activity.getSpinnerPunc() != null || activity.getSpinnerSincere() != null ||
                activity.getNumBankProofs() > 0 || activity.getNumGradeSheets() > 0 ){

            return true;
        }
        else{
            return false;
        }
    }
}
