package com.codingdrama.hrsystem.model;

import lombok.Getter;

@Getter
public enum Position {

    BACKEND_DEVELOPER,
    FRONTEND_DEVELOPER,
    DEVOPS_ENGINEER,
    PROJECT_MANAGER,
    BUSINESS_ANALYST,
    QA_ENGINEER,
    UI_UX_DESIGNER,
    SALES_MANAGER,
    MARKETING_MANAGER,
    HR,
    RECRUITER,
    ACCOUNTANT,
    ADMIN;

    public int getId() {
        return ordinal();
    }
}
