CREATE TABLE `registrations`
(
    `id`               BIGINT(20) NOT NULL AUTO_INCREMENT,
    `trainingclass_id` BIGINT(20) NOT NULL,
    `student_id`       BIGINT(20) NOT NULL,
    `status`           ENUM ('ACTIVE','EXIT_IN_PROGRESS','EXITED') COLLATE 'utf8mb4_hungarian_ci',
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_trainingclass_id`
        FOREIGN KEY (`trainingclass_id`) REFERENCES `training_classes` (`id`)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT,
    CONSTRAINT `fk_student_id`
        FOREIGN KEY (`student_id`) REFERENCES `students` (`id`)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT
) COLLATE = 'utf8mb4_hungarian_ci'
  ENGINE = InnoDB;