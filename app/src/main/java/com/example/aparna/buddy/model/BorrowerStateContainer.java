package com.example.aparna.buddy.model;


import com.example.aparna.buddy.app.BorrowerDetailsActivity;

import java.util.ArrayList;

/**
 * Created by Shashank on 11/6/16.
 */
public class BorrowerStateContainer {
    private BorrowerDetailsActivity activity;

    public BorrowerStateContainer(BorrowerDetailsActivity activity) {
        this.activity = activity;
    }

    public Boolean isCompleted() {
        if(activity.getFrontImageCollegeId() != null && !activity.getFrontImageCollegeId().isEmpty()
                && activity.getCollegeIDs().get(0).getIsVerified()
                && activity.getBackImageCollegeId() != null && !activity.getBackImageCollegeId().isEmpty()
                && activity.getCollegeIDs().get(1).getIsVerified()
                && checkBankProofs() &&  checkGradeSheets()
                && activity.getGoodFriendsRadio() != null && !activity.getGoodFriendsRadio().isEmpty()
                && activity.getYearBackRadio() != null && !activity.getYearBackRadio().isEmpty()
                && activity.getRefDept() != null  && !activity.getRefDept().isEmpty()
                && activity.getRefYear() != null && !activity.getRefYear().isEmpty()
                && activity.getRepayBackLoan() != null && !activity.getRepayBackLoan().isEmpty()
                && activity.getTransparentRadio() != null && !activity.getTransparentRadio().isEmpty()
                && activity.getGiveLoanRadio() != null && !activity.getGiveLoanRadio().isEmpty()
                && activity.getSpinnerCocurricular() != null && !activity.getSpinnerCocurricular().isEmpty()
                && activity.getSpinnerFinRes() != null  && !activity.getSpinnerFinRes().isEmpty()
                && activity.getSpinnerPunc() != null && !activity.getSpinnerPunc().isEmpty()
                && activity.getSpinnerSincere() != null && !activity.getSpinnerSincere().isEmpty()
                && activity.getSpinnerLoanRepay() != null  && !activity.getSpinnerLoanRepay().isEmpty()
                && activity.getNumBankProofs() > 0 && activity.getNumGradeSheets() > 0 ){

            return true;
        }
        else{
            return false;
        }
    }

    public Boolean isOngoing() {
        if( (activity.getGoodFriendsRadio() != null && !activity.getGoodFriendsRadio().isEmpty())
                || (activity.getYearBackRadio() != null && !activity.getYearBackRadio().isEmpty())
                || (activity.getRefDept() != null && !activity.getRefDept().isEmpty())
                || (activity.getRefYear() != null && !activity.getRefYear().isEmpty())
                || (activity.getRepayBackLoan() != null && !activity.getRepayBackLoan().isEmpty())
                || (activity.getTransparentRadio() != null && !activity.getTransparentRadio().isEmpty())
                || (activity.getGiveLoanRadio() != null && !activity.getGiveLoanRadio().isEmpty())
                || (activity.getSpinnerCocurricular() != null && !activity.getSpinnerCocurricular().isEmpty())
                || (activity.getSpinnerFinRes() != null && !activity.getSpinnerFinRes().isEmpty())
                || (activity.getSpinnerPunc() != null && !activity.getSpinnerPunc().isEmpty())
                || (activity.getSpinnerSincere() != null && !activity.getSpinnerSincere().isEmpty())
                || (activity.getSpinnerLoanRepay() != null && !activity.getSpinnerLoanRepay().isEmpty())
                || (activity.getOtherNotes() != null && !activity.getOtherNotes().isEmpty())
                || activity.getNumBankProofs() > 0 || activity.getNumGradeSheets() > 0
                || checkIfUploaded(activity.getCollegeIDs()) || checkIfUploaded(activity.getAddressProofs())
                || checkIfUploaded(activity.getBankProofs()) || checkIfUploaded(activity.getGradeSheets())){

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

    public Boolean checkIfUploaded(ArrayList<ImageBox> arrayList) {
        for(int i = 0 ; i < arrayList.size() ; i++){
            if(arrayList.get(i).getPath().equals("local")){
                return true;
            }
        }
        return false;
    }
}
