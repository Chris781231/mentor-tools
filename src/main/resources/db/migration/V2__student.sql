CREATE TABLE `students`
(
    `id`             BIGINT(20)   NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(255) NOT NULL COLLATE 'utf8mb4_hungarian_ci',
    `email`          VARCHAR(255) NOT NULL COLLATE 'utf8mb4_hungarian_ci',
    `github_account` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_hungarian_ci',
    `description`    TEXT         NULL DEFAULT NULL COLLATE 'utf8mb4_hungarian_ci',
    PRIMARY KEY (`id`)
) COLLATE = 'utf8mb4_hungarian_ci'
  ENGINE = InnoDB;
