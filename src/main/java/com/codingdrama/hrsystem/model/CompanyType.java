package com.codingdrama.hrsystem.model;

public enum CompanyType {
    IT("Information Technology"),
    FINANCE("Finance"),
    HEALTHCARE("Healthcare"),
    MANUFACTURING("Manufacturing"),
    RETAIL("Retail"),
    CONSULTING("Consulting"),
    EDUCATION("Education"),
    HOSPITALITY("Hospitality"),
    TRANSPORTATION("Transportation"),
    OTHER("Other");
    
    private final String description;

    CompanyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
