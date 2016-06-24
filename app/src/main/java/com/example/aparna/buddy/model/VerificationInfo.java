package com.example.aparna.buddy.model;


/**
 * Created by Aparna on 18/4/16.
 */
public class VerificationInfo {

    String  _id, userid, verifiedThrough, studentRepFound, studentRepReaction,
            initalVerificationNotes, referenceYear, referenceDepartment,
            punctualityInClass, sincerityInStudies, coCurricularParticipation,
            financiallyResponsible, repayCapacity, friendVerificationNotes,
            familyVerificationNotes, status, finalVerificationNotes, taskStatus, completionDate;

    Boolean collegeIdVerified, gpaVerified, borrowerIsLying, finacaillySound,
            canRepay, docsSigned, referenceIsGoodFriend, collegeVerified,
            academicYearVerified, dobVerified, permanentAddressVerified,
            annualFeeVerified, techNegative, qualitativeChecksDone,
            bankStatementVerified, socialProfileVerifed, haveYearBack, giveHimLoan ;

    public VerificationInfo() { }

    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }

    public String getReferenceYear() {
        return referenceYear;
    }

    public void setReferenceYear(String referenceYear) { this.referenceYear = referenceYear; }

    public String getReferenceDepartment() {
        return referenceDepartment;
    }

    public void setReferenceDepartment(String referenceDepartment) { this.referenceDepartment = referenceDepartment; }

    public String getPunctualityInClass() {
        return punctualityInClass;
    }

    public void setPunctualityInClass(String punctualityInClass) { this.punctualityInClass = punctualityInClass; }

    public String getSincerityInStudies() {
        return sincerityInStudies;
    }

    public void setSincerityInStudies(String sincerityInStudies) { this.sincerityInStudies = sincerityInStudies; }

    public String getCoCurricularParticipation() {
        return coCurricularParticipation;
    }

    public void setCoCurricularParticipation(String coCurricularParticipation) { this.coCurricularParticipation = coCurricularParticipation; }

    public String getRepayCapacity() {return repayCapacity; }

    public void setRepayCapacity(String repayCapacity) { this.repayCapacity = repayCapacity; }

    public String getFinanciallyResponsible() {
        return financiallyResponsible;
    }

    public void setFinanciallyResponsible(String financiallyResponsible) { this.financiallyResponsible = financiallyResponsible; }

    public String getFinalVerificationNotes() {
        return finalVerificationNotes;
    }

    public void setFinalVerificationNotes(String finalVerificationNotes) { this.finalVerificationNotes = finalVerificationNotes; }

    public Boolean getBorrowerIsLying() {
        return borrowerIsLying;
    }

    public void setBorrowerIsLying(Boolean borrowerIsLying) { this.borrowerIsLying = borrowerIsLying; }

    public Boolean getReferenceIsGoodFriend() {
        return referenceIsGoodFriend;
    }

    public void setReferenceIsGoodFriend(Boolean referenceIsGoodFriend) { this.referenceIsGoodFriend = referenceIsGoodFriend; }

    public Boolean getHaveYearBack(){ return haveYearBack; }

    public void setHaveYearBack(Boolean haveYearBack) { this.haveYearBack = haveYearBack; }

    public Boolean getGiveHimLoan() { return giveHimLoan; }

    public void setGiveHimLoan(Boolean giveHimLoan) { this.giveHimLoan = giveHimLoan; }

}
