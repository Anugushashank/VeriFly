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

    public String get_id() { return _id; }

    public void set_id(String _id) { this._id = _id; }

    public String getTaskStatus() { return taskStatus; }

    public String getCompletionDate() { return completionDate; }

    public void setCompletionDate(String completionDate){ this.completionDate = completionDate; }

    public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) { this.userid = userid; }

    public String getVerifiedThrough() {
        return verifiedThrough;
    }

    public void setVerifiedThrough(String verifiedThrough) { this.verifiedThrough = verifiedThrough; }

    public String getStudentRepFound() {
        return studentRepFound;
    }

    public void setStudentRepFound(String studentRepFound) { this.studentRepFound = studentRepFound; }

    public String getStudentRepReaction() {
        return studentRepReaction;
    }

    public void setStudentRepReaction(String studentRepReaction) { this.studentRepReaction = studentRepReaction; }

    public String getInitalVerificationNotes() {
        return initalVerificationNotes;
    }

    public void setInitalVerificationNotes(String initalVerificationNotes) { this.initalVerificationNotes = initalVerificationNotes; }

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

    public String getFriendVerificationNotes() {
        return friendVerificationNotes;
    }

    public void setFriendVerificationNotes(String friendVerificationNotes) { this.friendVerificationNotes = friendVerificationNotes; }

    public String getFamilyVerificationNotes() {
        return familyVerificationNotes;
    }

    public void setFamilyVerificationNotes(String friendVerificationNotes) { this.familyVerificationNotes = familyVerificationNotes; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) { this.status = status; }

    public String getFinalVerificationNotes() {
        return finalVerificationNotes;
    }

    public void setFinalVerificationNotes(String finalVerificationNotes) { this.finalVerificationNotes = finalVerificationNotes; }

    public Boolean getCollegeIdVerified() {
        return collegeIdVerified;
    }

    public void setCollegeIdVerified(Boolean collegeIdVerified) { this.collegeIdVerified = collegeIdVerified; }

    public Boolean getGpaVerified() {
        return gpaVerified;
    }

    public void setGpaVerified(Boolean gpaVerified) { this.gpaVerified = gpaVerified; }

    public Boolean getBorrowerIsLying() {
        return borrowerIsLying;
    }

    public void setBorrowerIsLying(Boolean borrowerIsLying) { this.borrowerIsLying = borrowerIsLying; }

    public Boolean getFinacaillySound() {
        return finacaillySound;
    }

    public void setFinacaillySound(Boolean finacaillySound) { this.finacaillySound = finacaillySound; }

    public Boolean getCanRepay() {
        return canRepay;
    }

    public void setCanRepay(Boolean canRepay) { this.canRepay = canRepay; }

    public Boolean getDocsSigned() {
        return docsSigned;
    }

    public void setDocsSigned(Boolean docsSigned) { this.docsSigned = docsSigned; }

    public Boolean getReferenceIsGoodFriend() {
        return referenceIsGoodFriend;
    }

    public void setReferenceIsGoodFriend(Boolean referenceIsGoodFriend) { this.referenceIsGoodFriend = referenceIsGoodFriend; }

    public Boolean getHaveYearBack(){ return haveYearBack; }

    public void setHaveYearBack(Boolean haveYearBack) { this.haveYearBack = haveYearBack; }

    public Boolean getGiveHimLoan() { return giveHimLoan; }

    public void setGiveHimLoan(Boolean giveHimLoan) { this.giveHimLoan = giveHimLoan; }

    public Boolean getCollegeVerified() {
        return collegeVerified;
    }

    public void setCollegeVerified(Boolean collegeIdVerified) { this.collegeIdVerified = collegeIdVerified; }

    public Boolean getAcademicYearVerified() {
        return academicYearVerified;
    }

    public void setAcademicYearVerified(Boolean academicYearVerified) { this.academicYearVerified = academicYearVerified; }

    public Boolean getDobVerified() {
        return dobVerified;
    }

    public void setDobVerified(Boolean dobVerified) { this.dobVerified = dobVerified; }

    public Boolean getPermanentAddressVerified() {
        return permanentAddressVerified;
    }

    public void setPermanentAddressVerified(Boolean permanentAddressVerified) { this.permanentAddressVerified = permanentAddressVerified; }

    public Boolean getAnnualFeeVerified() {
        return annualFeeVerified;
    }

    public void setAnnualFeeVerified(Boolean annualFeeVerified) { this.annualFeeVerified = annualFeeVerified; }

    public Boolean getTechNegative() {
        return techNegative;
    }

    public void setTechNegative(Boolean techNegative) { this.techNegative = techNegative; }

    public Boolean getQualitativeChecksDone() {
        return qualitativeChecksDone;
    }

    public void setQualitativeChecksDone(Boolean qualitativeChecksDone) {this.qualitativeChecksDone = qualitativeChecksDone; }

    public Boolean getBankStatementVerified() {
        return bankStatementVerified;
    }

    public void setBankStatementVerified(Boolean bankStatementVerified) { this.bankStatementVerified = bankStatementVerified; }

    public Boolean getSocialProfileVerifed() {
        return socialProfileVerifed;
    }

    public void setSocialProfileVerifed(Boolean socialProfileVerifed) { this.socialProfileVerifed = socialProfileVerifed; }
}
