
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `domain` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_imux4m190glnqxeh8gapkk2t` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `question` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `score` int(11) NOT NULL,
  `text` varchar(255) NOT NULL,
  `domain_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_d0yhf4xgi1a0y6m5v1vqexwq4` (`domain_id`),
  CONSTRAINT `FK_d0yhf4xgi1a0y6m5v1vqexwq4` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `option` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `correct` bit(1) NOT NULL,
  `text` varchar(255) NOT NULL,
  `question_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eix9du6u2r4wxwu415wq8yb99` (`question_id`),
  CONSTRAINT `FK_eix9du6u2r4wxwu415wq8yb99` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `quiz_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime NOT NULL,
  `score` int(11) NOT NULL,
  `domain_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_b040f9cqq1oxnjahhi59jyy8v` (`domain_id`),
  KEY `FK_59ramm9rk8gpqv1oim47sl0v8` (`user_id`),
  CONSTRAINT `FK_59ramm9rk8gpqv1oim47sl0v8` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_b040f9cqq1oxnjahhi59jyy8v` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
