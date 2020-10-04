use bbs_0;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `content` text COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

use bbs_1;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `content` text COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

use bbs_2;
CREATE TABLE `article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `content` text COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

INSERT INTO `bbs_0`.`article` (`title`, `content`) VALUES ( 'shard-db0', 'shard-db2-data0');
INSERT INTO `bbs_0`.`article` (`title`, `content`) VALUES ( 'shard-db0', 'shard-db2-data1');
INSERT INTO `bbs_0`.`article` (`title`, `content`) VALUES ( 'shard-db0', 'shard-db2-data2');
INSERT INTO `bbs_1`.`article` (`title`, `content`) VALUES ( 'shard-db1', 'shard-db2-data0');
INSERT INTO `bbs_1`.`article` (`title`, `content`) VALUES ( 'shard-db1', 'shard-db2-data1');
INSERT INTO `bbs_1`.`article` (`title`, `content`) VALUES ( 'shard-db1', 'shard-db2-data2');
INSERT INTO `bbs_2`.`article` (`title`, `content`) VALUES ( 'shard-db2', 'shard-db2-data0');
INSERT INTO `bbs_2`.`article` (`title`, `content`) VALUES ( 'shard-db2', 'shard-db2-data1');
INSERT INTO `bbs_2`.`article` (`title`, `content`) VALUES ( 'shard-db2', 'shard-db2-data2');
