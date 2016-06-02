package com.example.aparna.buddy.model;

import java.util.List;

/**
 * Created by Aparna on 18/4/16.
 */
public class VerificationInfo {

    String  _id, userid, verifiedThrough, studentRepFound, studentRepReaction,
            initalVerificationNotes, referenceYear, referenceDepartment,
            punctualityInClass, sincerityInStudies, coCurricularParticipation,
            financiallyResponsible, repayCapacity, friendVerificationNotes,
            familyVerificationNotes, status,  finalVerificationNotes;

    Boolean collegeIdVerified, gpaVerified, borrowerIsLying = null, finacaillySound,
            canRepay, docsSigned, referenceIsGoodFriend, collegeVerified,
            academicYearVerified, dobVerified, permanentAddressVerified,
            annualFeeVerified, techNegative, qualitativeChecksDone,
            bankStatementVerified, socialProfileVerifed, haveYearBack = null, giveHimLoan = null;

    public VerificationInfo() { }

    public String get_id() {
        return _id;
    }

    public String getUserid() {
        return userid;
    }

    public String getVerifiedThrough() {
        return verifiedThrough;
    }

    public String getStudentRepFound() {
        return studentRepFound;
    }

    public String getStudentRepReaction() {
        return studentRepReaction;
    }

    public String getInitalVerificationNotes() {
        return initalVerificationNotes;
    }

    public String getReferenceYear() {
        return referenceYear;
    }

    public String getReferenceDepartment() {
        return referenceDepartment;
    }

    public String getPunctualityInClass() {
        return punctualityInClass;
    }

    public String getSincerityInStudies() {
        return sincerityInStudies;
    }

    public String getCoCurricularParticipation() {
        return coCurricularParticipation;
    }

    public String getRepayCapacity() {return repayCapacity; }

    public String getFinanciallyResponsible() {
        return financiallyResponsible;
    }

    public String getFriendVerificationNotes() {
        return friendVerificationNotes;
    }

    public String getFamilyVerificationNotes() {
        return familyVerificationNotes;
    }

    public String getStatus() {
        return status;
    }

    public String getFinalVerificationNotes() {
        return finalVerificationNotes;
    }

    public Boolean getCollegeIdVerified() {
        return collegeIdVerified;
    }

    public Boolean getGpaVerified() {
        return gpaVerified;
    }

    public Boolean getBorrowerIsLying() {
        return borrowerIsLying;
    }

    public Boolean getFinacaillySound() {
        return finacaillySound;
    }

    public Boolean getCanRepay() {
        return canRepay;
    }

    public Boolean getDocsSigned() {
        return docsSigned;
    }

    public Boolean isReferenceIsGoodFriend() {
        return referenceIsGoodFriend;
    }

    public Boolean isHaveYearBack(){ return haveYearBack; }

    public Boolean isGiveHimLoan() { return giveHimLoan; }

    public Boolean isBorrowerIsLying() {return borrowerIsLying; }

    public Boolean getCollegeVerified() {
        return collegeVerified;
    }

    public Boolean getAcademicYearVerified() {
        return academicYearVerified;
    }

    public Boolean getDobVerified() {
        return dobVerified;
    }

    public Boolean getPermanentAddressVerified() {
        return permanentAddressVerified;
    }

    public Boolean getAnnualFeeVerified() {
        return annualFeeVerified;
    }

    public Boolean getTechNegative() {
        return techNegative;
    }

    public Boolean getQualitativeChecksDone() {
        return qualitativeChecksDone;
    }

    public Boolean getBankStatementVerified() {
        return bankStatementVerified;
    }

    public Boolean getSocialProfileVerifed() {
        return socialProfileVerifed;
    }
}
