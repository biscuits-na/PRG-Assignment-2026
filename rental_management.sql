-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 22, 2026 at 04:41 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rental_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `leases`
--

CREATE TABLE `leases` (
  `lease_id` int(11) NOT NULL,
  `tenant_id` int(11) NOT NULL,
  `property_id` int(11) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `monthly_rent_amount` double NOT NULL,
  `security_deposit` double NOT NULL,
  `payment_due_day` int(11) NOT NULL,
  `grace_period` int(11) NOT NULL,
  `late_penalty_rate` double NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `leases`
--

INSERT INTO `leases` (`lease_id`, `tenant_id`, `property_id`, `start_date`, `end_date`, `monthly_rent_amount`, `security_deposit`, `payment_due_day`, `grace_period`, `late_penalty_rate`, `status`) VALUES
(2, 1, 1, '2026-01-01', '2026-12-31', 9500, 9500, 5, 3, 0.1, 'Active'),
(3, 2, 3, '2026-02-01', '2026-11-30', 7000, 7000, 7, 2, 0.08, 'Active'),
(4, 1, 5, '2026-03-01', '2026-09-30', 5500, 5500, 10, 2, 0.05, 'Active');

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

CREATE TABLE `payments` (
  `payment_id` int(11) NOT NULL,
  `lease_id` int(11) NOT NULL,
  `amount_paid` double NOT NULL,
  `payment_date` date NOT NULL,
  `payment_status` varchar(20) NOT NULL,
  `penalty_amount` double DEFAULT 0,
  `outstanding_balance` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`payment_id`, `lease_id`, `amount_paid`, `payment_date`, `payment_status`, `penalty_amount`, `outstanding_balance`) VALUES
(5, 2, 9500, '2026-01-04', 'Paid', 0, 0),
(7, 4, 3000, '2026-03-12', 'Pending', 0, 2500),
(8, 3, 3000, '2026-02-15', 'Pending', 560, 4560);

-- --------------------------------------------------------

--
-- Table structure for table `properties`
--

CREATE TABLE `properties` (
  `property_id` int(11) NOT NULL,
  `property_type` varchar(20) NOT NULL,
  `building_size` double NOT NULL,
  `address` varchar(255) NOT NULL,
  `town` varchar(100) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `market_value` double NOT NULL,
  `monthly_rental_cost` double NOT NULL,
  `availability_status` varchar(20) NOT NULL,
  `plot_size` double DEFAULT NULL,
  `unit_number` varchar(20) DEFAULT NULL,
  `floor_level` int(11) DEFAULT NULL,
  `elevator_access` tinyint(1) DEFAULT NULL,
  `backyard` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `properties`
--

INSERT INTO `properties` (`property_id`, `property_type`, `building_size`, `address`, `town`, `location`, `market_value`, `monthly_rental_cost`, `availability_status`, `plot_size`, `unit_number`, `floor_level`, `elevator_access`, `backyard`) VALUES
(1, 'House', 120, '12 Independece Ave', 'Windhoek', 'CBD', 950000, 8500, 'Available', 400, NULL, NULL, 0, 0),
(2, 'Townhouse', 95, '45 Sam Nujoma Drive', 'Windhoek', 'CBD', 780000, 7200, 'Available', NULL, 'T03', NULL, 0, 1),
(3, 'Flat', 70, 'Flat 2, Peneyambeko Courts', 'Oshakati', 'Oshakati East', 620000, 5500, 'Occupied', NULL, 'F07', 2, 1, 0),
(4, 'House', 180, '45 Nelson Mandela', 'Orandjemund', 'Naraville', 1200000, 9500, 'Available', 600, NULL, NULL, 0, 0),
(5, 'Townhouse', 80, '5 Lagoon Estate', 'Walvis Bay', 'Quisebmund', 680000, 6500, 'Occupied', NULL, '12', NULL, 0, 0),
(6, 'Flat', 70, 'Flat 8, Ocean View Apartments', 'Swakopmund', 'Mondessa', 480000, 5200, 'Available', NULL, '8', 2, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `tenants`
--

CREATE TABLE `tenants` (
  `tenant_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `id_passport` varchar(50) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `email_address` varchar(100) NOT NULL,
  `current_status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tenants`
--

INSERT INTO `tenants` (`tenant_id`, `first_name`, `last_name`, `id_passport`, `phone_number`, `email_address`, `current_status`) VALUES
(1, 'Tommy', 'Shelby', '99010100001', '0812556600', 'shelby@email.com', 'Active'),
(2, 'Maria', 'Dapilashimwe', '001156979', '0814562341', 'mariatula21@yahoo.com', 'Active'),
(3, 'Penge', 'Fernandes', '092145579', '0814736656', 'shiwa@gmail.com', 'Blacklisted'),
(7, 'Melissa', 'Shikongo', '95050500002', '0818765432', 'melissa@gmail.com', 'Active'),
(9, 'Daniel', 'Shikongo', '01020345678', '0812345678', 'daniel.shikongo@gmail.com', 'Active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `leases`
--
ALTER TABLE `leases`
  ADD PRIMARY KEY (`lease_id`),
  ADD KEY `tenant_id` (`tenant_id`),
  ADD KEY `property_id` (`property_id`);

--
-- Indexes for table `payments`
--
ALTER TABLE `payments`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `lease_id` (`lease_id`);

--
-- Indexes for table `properties`
--
ALTER TABLE `properties`
  ADD PRIMARY KEY (`property_id`);

--
-- Indexes for table `tenants`
--
ALTER TABLE `tenants`
  ADD PRIMARY KEY (`tenant_id`),
  ADD UNIQUE KEY `id_passport` (`id_passport`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `leases`
--
ALTER TABLE `leases`
  MODIFY `lease_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `payments`
--
ALTER TABLE `payments`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `properties`
--
ALTER TABLE `properties`
  MODIFY `property_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `tenants`
--
ALTER TABLE `tenants`
  MODIFY `tenant_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `leases`
--
ALTER TABLE `leases`
  ADD CONSTRAINT `leases_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `tenants` (`tenant_id`),
  ADD CONSTRAINT `leases_ibfk_2` FOREIGN KEY (`property_id`) REFERENCES `properties` (`property_id`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`lease_id`) REFERENCES `leases` (`lease_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
