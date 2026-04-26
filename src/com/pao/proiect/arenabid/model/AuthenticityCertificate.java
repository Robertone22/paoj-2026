package com.pao.proiect.arenabid.model;

import java.time.LocalDate;

public final class AuthenticityCertificate {
    private final String certificateId;
    private final String issuedBy;
    private final LocalDate issueDate;
    private final boolean verified;

    public AuthenticityCertificate(String certificateId, String issuedBy, LocalDate issueDate, boolean verified) {
        this.certificateId = certificateId;
        this.issuedBy = issuedBy;
        this.issueDate = issueDate;
        this.verified = verified;
    }

    public String getCertificateId() {
        return certificateId;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public boolean isVerified() {
        return verified;
    }

    @Override
    public String toString() {
        return "AuthenticityCertificate{" +
                "certificateId='" + certificateId + '\'' +
                ", issuedBy='" + issuedBy + '\'' +
                ", issueDate=" + issueDate +
                ", verified=" + verified +
                '}';
    }
}