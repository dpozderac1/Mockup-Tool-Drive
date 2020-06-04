CREATE DATABASE IF NOT EXISTS `system_events`;
CREATE DATABASE IF NOT EXISTS `nwtproba`;
CREATE DATABASE IF NOT EXISTS `projectdb`;
CREATE DATABASE IF NOT EXISTS `online_testing`;

CREATE USER 'us'@'localhost' IDENTIFIED BY 'password';
GRANT ALL ON system_events.* TO 'us'@'%';
GRANT ALL ON nwtproba.* TO 'us'@'%';
GRANT ALL ON projectdb.* TO 'us'@'%';
GRANT ALL ON online_testing.* TO 'us'@'%';