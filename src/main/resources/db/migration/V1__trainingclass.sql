CREATE TABLE `internal_dates_for_training_classes` (
`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
`finish_date` DATE NULL DEFAULT NULL,
`start_date` DATE NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) COLLATE='utf8mb4_hungarian_ci' ENGINE=InnoDB;

CREATE TABLE `training_classes` (
                                    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                                    `name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_hungarian_ci',
                                    `internal_dates_id` BIGINT(20) NOT NULL ,
                                    PRIMARY KEY (`id`),
                                    CONSTRAINT `fk_internal_dates_id`
                                        FOREIGN KEY (`internal_dates_id`) REFERENCES `internal_dates_for_training_classes` (`id`)
                                            ON UPDATE RESTRICT
                                            ON DELETE RESTRICT
) COLLATE='utf8mb4_hungarian_ci' ENGINE=InnoDB;
