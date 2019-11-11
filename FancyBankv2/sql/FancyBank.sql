SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `customer_id` int(11) NOT NULL,
  `account_number` int(11) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `active` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `account`
-- ----------------------------
BEGIN;
INSERT INTO `account` VALUES ('0', '1', 'Checking', '20000', '1'), ('0', '2', 'Savings', '10000', '1'), ('1', '1', 'Savings', '20000', '1');
COMMIT;

-- ----------------------------
--  Table structure for `customer`
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `customer_username` varchar(255) DEFAULT NULL,
  `customer_pass` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `customer`
-- ----------------------------
BEGIN;
INSERT INTO `customer` VALUES ('0', 'Yanshan Song', 'kimi', '123'), ('1', 'cpk', 'cpk', '123');
COMMIT;

-- ----------------------------
--  Table structure for `customer_stock`
-- ----------------------------
DROP TABLE IF EXISTS `customer_stock`;
CREATE TABLE `customer_stock` (
  `customer_id` int(255) NOT NULL,
  `account_number` int(11) NOT NULL,
  `code` varchar(255) NOT NULL,
  `n_stocks` double DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`account_number`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `loan`
-- ----------------------------
DROP TABLE IF EXISTS `loan`;
CREATE TABLE `loan` (
  `customer_id` int(11) NOT NULL,
  `loan_number` int(11) NOT NULL,
  `initial_amount` double DEFAULT NULL,
  `balance` double DEFAULT NULL,
  `interest_rate` double DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `collateral_name` varchar(255) DEFAULT NULL,
  `collateral_amount` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`customer_id`,`loan_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `loan`
-- ----------------------------
BEGIN;
INSERT INTO `loan` VALUES ('0', '1', '1000000', '1000000', '8', '3', 'house', '10.000000', 'Active');
COMMIT;

-- ----------------------------
--  Table structure for `stock`
-- ----------------------------
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `code` varchar(255) NOT NULL,
  `value` double DEFAULT NULL,
  `n_stocks` double DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `stock`
-- ----------------------------
BEGIN;
INSERT INTO `stock` VALUES ('Google', '200', '1000');
COMMIT;

---- ----------------------------
----  Table structure for `account_types`
---- ----------------------------
DROP TABLE IF EXISTS `account_types`;
CREATE TABLE `account_types` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `account_types` VALUES ('Checking'),('Savings'),('Security');
COMMIT;

---- ----------------------------
----  Table structure for `currencies`
---- ----------------------------
DROP TABLE IF EXISTS `currencies`;
CREATE TABLE `currencies` (
  `code` varchar(255) NOT NULL,
  `conversion` double NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

BEGIN;
INSERT INTO `currencies` VALUES ('USD','1.0'),('GBP','1.30'),('INR','0.014');
COMMIT;

-- ----------------------------
--  Table structure for `transaction`
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction` (
  `transaction_id` int(11) NOT NULL,
  `transaction_type` varchar(255) DEFAULT NULL,
  `receiving_account_id` int(11) DEFAULT NULL,
  `receiving_user_id` int(11) DEFAULT NULL,
  `sending_account_id` int(11) DEFAULT NULL,
  `sending_user_id` int(11) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `n_stock` double(255,0) DEFAULT NULL,
  PRIMARY KEY (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `transaction`
-- ----------------------------
BEGIN;
INSERT INTO `transaction` VALUES ('0', 'general', '1', '0', '-1', '-1', '10000', null, null), ('1', 'general', '-2', '-2', '1', '-1', '15', null, null), ('2', 'general', '1', '0', '-1', '-1', '10000', null, null), ('3', 'general', '2', '0', '-1', '-1', '10000', null, null), ('4', 'general', '1', '1', '-1', '-1', '20000', null, null);
COMMIT;

DROP TABLE IF EXISTS `bank`;
CREATE TABLE `bank` (
    `bank_name` varchar(255) DEFAULT NULL,
    `manager_username` varchar(255) DEFAULT NULL,
    `manager_pass` varchar(255) DEFAULT NULL,
    `starting_bal` double(255,0) DEFAULT NULL,
    `min_bal` double(255,0) DEFAULT NULL,
    `service_charge` double(255,0) DEFAULT NULL,
    `interest` double(255,0) DEFAULT NULL,
    `loan_interest` double(255,0) DEFAULT NULL,
    `high_interest_bal` double(255,0) DEFAULT NULL,
    `trading_fees` double(255,0) DEFAULT NULL,
    `min_security_bal` double(255,0) DEFAULT NULL
--    PRIMARY KEY (`manager_username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--BEGIN;
--INSERT INTO `bank_settings` VALUES ('USD','1.0'),('GBP','1.30'),('INR','0.014');
--COMMIT;
BEGIN;
INSERT INTO `bank` VALUES('Bank','manager','123','10000000','100','15.0','8.0','8.0','10000','20','10000');
COMMIT;

-- ----------------------------
--  Table structure for `bank_settings`
-- ----------------------------
--DROP TABLE IF EXISTS `bank_settings`
--CREATE TABLE `bank_settings` (
--  `bank_name` varchar(255) DEFAULT NULL,
--  `manager_username` varchar(255) DEFAULT NULL,
--  `manager_pass` varchar(255) DEFAULT NULL,
--  `starting_bal` double(255,0) DEFAULT NULL,
--  `min_bal` double(255,0) DEFAULT NULL,
--  `service_charge` double(255,0) DEFAULT NULL,
--  `interest` double(255,0) DEFAULT NULL,
--  `loan_interest` double(255,0) DEFAULT NULL,
--  `high_interest_bal` double(255,0) DEFAULT NULL,
--  `trading_fees` double(255,0) DEFAULT NULL,
--  `min_security_bal` double(255,0) DEFAULT NULL,
--  PRIMARY KEY (`manager_username`)
--) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--BEGIN;
--INSERT INTO `bank_settings` VALUES('Bank','manager','123','10000000','100','15.0','8.0','8.0','10000','20','10000');
--COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
