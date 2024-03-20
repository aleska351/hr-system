CREATE TABLE `addresses` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `country` VARCHAR(255),
    `city` VARCHAR(255),
    `address` VARCHAR(255),
    `zip` VARCHAR(20),
    PRIMARY KEY (`id`)
);


CREATE TABLE `companies`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(255) NOT NULL,
    `companyCode`  VARCHAR(255) NOT NULL UNIQUE,
    `title`        VARCHAR(255) NOT NULL,
    `companyType`  VARCHAR(255),
    `website`      VARCHAR(255) NOT NULL,
    `phoneNumber`  VARCHAR(32)  NOT NULL,
    `email`        VARCHAR(255) NOT NULL,
    `address_id`   BIGINT,
    `companyStatus` VARCHAR(255),
    `created`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_address_id`
        FOREIGN KEY (`address_id`)
        REFERENCES `addresses` (`id`)
        ON DELETE SET NULL
);


CREATE TABLE `departments`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `name`         VARCHAR(255) NOT NULL,
    `email`        VARCHAR(255) NOT NULL,
    `title`        VARCHAR(255) NOT NULL,
    `description`  VARCHAR(255),
    `company_id`   BIGINT       NOT NULL,
    `created`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_department_company_id`
        FOREIGN KEY (`company_id`)
        REFERENCES `companies` (`id`)
        ON DELETE CASCADE
);



CREATE TABLE `users`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT,
    `first_name`          VARCHAR(255) NOT NULL,
    `middle_name`         VARCHAR(255),
    `last_name`           VARCHAR(255) NOT NULL,
    `email`               VARCHAR(255) NOT NULL UNIQUE,
    `phoneNumber`         VARCHAR(32)  NOT NULL UNIQUE,
    `skype`               VARCHAR(255) NOT NULL UNIQUE,
    `linkedin`            VARCHAR(255) NOT NULL UNIQUE,
    `telegram`            VARCHAR(255) NOT NULL UNIQUE,
    `address_id`          BIGINT,
    `password`            VARCHAR(255),
    `passwordExpiredDate` TIMESTAMP,
    `emailVerified`       BOOLEAN,
    `status`              VARCHAR(255),
    `department_id`       BIGINT       NOT NULL,
    `company_id`          BIGINT       NOT NULL,
    `created`             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated`             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_user_address_id`
        FOREIGN KEY (`address_id`)
        REFERENCES `addresses` (`id`)
        ON DELETE SET NULL,
    CONSTRAINT `fk_user_department_id`
        FOREIGN KEY (`department_id`)
        REFERENCES `departments` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_user_company_id`
        FOREIGN KEY (`company_id`)
        REFERENCES `companies` (`id`)
        ON DELETE CASCADE
);

CREATE TABLE `salaries` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `baseSalary` DECIMAL(10, 2) NOT NULL,
    `bonuses` DECIMAL(10, 2),
    `allowances` DECIMAL(10, 2),
    `deductions` DECIMAL(10, 2),
    `overtimePay` DECIMAL(10, 2),
    `commission` DECIMAL(10, 2),
    `benefits` VARCHAR(255),
    `currency` VARCHAR(10) NOT NULL,
    `frequency` VARCHAR(20) NOT NULL,
    `salaryDay` DATETIME,
    `user_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_salary_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
);



CREATE TABLE `roles`
(
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `name`    VARCHAR(100) NOT NULL,
    `created` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC)
);


CREATE TABLE `technologies`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `name`              VARCHAR(255) NOT NULL,
    `description`       VARCHAR(255),
    `category`          VARCHAR(255) NOT NULL,
    `defaultTechnology` BOOLEAN,
    `company_id`        BIGINT,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_technology_company_id`
        FOREIGN KEY (`company_id`)
        REFERENCES `companies` (`id`)
        ON DELETE SET NULL
);


CREATE TABLE `user_technologies`
(
    `user_id`       BIGINT NOT NULL,
    `technology_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `technology_id`),
    CONSTRAINT `fk_user_technology_user_id`
        FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_user_technology_technology_id`
        FOREIGN KEY (`technology_id`)
        REFERENCES `technologies` (`id`)
        ON DELETE CASCADE
);


CREATE TABLE `clients`
(
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `name`                 VARCHAR(255) NOT NULL,
    `contactPersonName`    VARCHAR(255) NOT NULL,
    `contactPersonEmail`   VARCHAR(255) NOT NULL,
    `contactPersonPhone`   VARCHAR(32)  NOT NULL,
    `description`          VARCHAR(255),
    PRIMARY KEY (`id`)
);


CREATE TABLE `projects`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(255) NOT NULL,
    `description`    VARCHAR(255) NOT NULL,
    `projectStatus`  VARCHAR(255),
    `company_id`     BIGINT       NOT NULL,
    `client_id`      BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_project_company_id`
        FOREIGN KEY (`company_id`)
        REFERENCES `companies` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_project_client_id`
        FOREIGN KEY (`client_id`)
        REFERENCES `clients` (`id`)
        ON DELETE CASCADE
);


CREATE TABLE `project_technologies`
(
    `project_id`    BIGINT NOT NULL,
    `technology_id` BIGINT NOT NULL,
    PRIMARY KEY (`project_id`, `technology_id`),
    CONSTRAINT `fk_project_technology_project_id`
        FOREIGN KEY (`project_id`)
        REFERENCES `projects` (`id`)
        ON DELETE CASCADE,
    CONSTRAINT `fk_project_technology_technology_id`
        FOREIGN KEY (`technology_id`)
        REFERENCES `technologies` (`id`)
        ON DELETE CASCADE
);




CREATE TABLE `user_roles`
(
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`,`role_id`),
    INDEX     `fk_user_roles_roles_idx` (`role_id` ASC),
    INDEX     `fk_user_roles_users_idx` (`user_id` ASC),
    CONSTRAINT `fk_user_roles_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `users` (`id`)
            ON DELETE CASCADE
            ON UPDATE RESTRICT,
    CONSTRAINT `fk_user_roles_roles`
        FOREIGN KEY (`role_id`)
            REFERENCES `roles` (`id`)
            ON DELETE CASCADE
            ON UPDATE RESTRICT
);

INSERT INTO `roles` (`id`, `name`, `created`, `updated`)
VALUES ('1', 'ROLE_EMPLOYEE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('2', 'ROLE_HR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('3', 'ROLE_ACCOUNTANT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('4', 'ROLE_COMPANY_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('5', 'ROLE_DEPARTMENT_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('6', 'ROLE_ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

CREATE TABLE `permissions`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45)  NOT NULL,
    `description` VARCHAR(255) NULL,
    `created`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

CREATE TABLE `role_permissions`
(
    `permission_id` BIGINT NOT NULL,
    `role_id`       BIGINT NOT NULL,
    PRIMARY KEY (`permission_id`,`role_id`),
    INDEX `fk_role_permissions_permissions_idx` (`permission_id` ASC),
    INDEX `fk_role_permissions_roles_idx` (`role_id` ASC),
    CONSTRAINT `fk_role_permissions_permissions`
        FOREIGN KEY (`permission_id`)
            REFERENCES `permissions` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_role_permissions_roles`
        FOREIGN KEY (`role_id`)
            REFERENCES `roles` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
