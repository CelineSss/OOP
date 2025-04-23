-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 06, 2025 at 11:22 AM
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
-- Database: `quizzing_you`
--

-- --------------------------------------------------------

--
-- Table structure for table `attempt`
--

CREATE TABLE `attempt` (
  `attemptID` int(50) NOT NULL,
  `studentID` int(50) NOT NULL,
  `attemptTimestamp` timestamp(6) NOT NULL DEFAULT current_timestamp(6),
  `durationTaken` int(50) NOT NULL,
  `score` int(10) NOT NULL,
  `quizID` int(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `matchingattempt`
--

CREATE TABLE `matchingattempt` (
  `attemptID` int(50) NOT NULL,
  `matchingID` int(50) NOT NULL,
  `stuAnswer` varchar(100) DEFAULT NULL,
  `isProper` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `matchingpair`
--

CREATE TABLE `matchingpair` (
  `matchingID` int(11) NOT NULL,
  `questionID` int(50) NOT NULL,
  `subject` varchar(50) NOT NULL,
  `correctAnswer` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `matchingpair`
--

INSERT INTO `matchingpair` (`matchingID`, `questionID`, `subject`, `correctAnswer`) VALUES
(1, 3, 'He', 'is'),
(2, 3, 'They', 'are'),
(3, 6, 'Break the ice', 'Start a conversation'),
(4, 6, 'Under the weather', 'Feeling sick'),
(5, 9, 'I', 'eat'),
(6, 9, 'She', 'eats'),
(7, 12, 'Dog', 'Animal'),
(8, 12, 'Rose', 'Flower'),
(9, 15, 'I ___ to school every day.', 'go'),
(10, 15, 'She ___ a beautiful dress', 'has'),
(11, 18, 'Under the weather', 'Feeling sick'),
(12, 18, 'Hit the nail on the head', 'To do something perfectly'),
(13, 21, 'I ___ ( ) to the park.', 'walk'),
(14, 21, 'He ___ ( ) to school yesterday.', 'walked'),
(15, 24, 'Elephant', 'A big animal'),
(16, 24, 'Daisy', 'A flower'),
(17, 27, 'What is your name', 'Question mark (?)'),
(18, 27, 'I love playing soccer', 'Period (.)'),
(19, 30, 'When pigs fly', 'Something impossible'),
(20, 30, 'On top of the world ', 'Feeling very happy'),
(21, 33, 'He ___ ( ) to the shop now', 'is walking'),
(22, 33, 'We ___ ( ) to the mountains last week.', 'walked'),
(23, 36, 'Brave', 'Small'),
(24, 36, 'Tiny', 'Courageous'),
(25, 39, 'The boy ___ ( ) his lunch quickly.', 'eats'),
(26, 39, 'The children ___ ( ) in the park.', 'play'),
(27, 42, 'Break a leg', 'Good luck'),
(28, 42, 'Spill the beans', 'Reveal a secret'),
(29, 45, 'Past tense of \"run\"', 'ran'),
(30, 45, 'Future tense of \"eat\"', 'will eat'),
(31, 48, 'Happy', 'Joyful'),
(32, 48, 'Small', 'Tiny'),
(33, 51, 'She has been singing', 'Present Perfect Continuous'),
(34, 51, 'They played basketball', 'Past Tense'),
(35, 54, 'Piece of cake', 'Very easy'),
(36, 54, 'Out of the blue', 'Something unexpected'),
(37, 57, 'He ___ ( ) in the garden every morning.', 'works'),
(38, 57, 'We ___ ( ) our project before the deadline.', 'completed'),
(39, 60, 'Brave', 'Cowardly'),
(40, 60, 'Wide', 'Narrow'),
(41, 63, 'She runs because she loves to stay fit', 'Independent Clause'),
(42, 63, 'Although it was raining, we went hiking', 'Dependent Clause'),
(43, 66, 'Hit the sack', 'Go to bed'),
(44, 66, 'Under the weather', 'Feeling sick'),
(45, 69, 'She was singing', 'Past Continuous'),
(46, 69, 'They will have finished', 'Future Perfect'),
(47, 72, 'Quick', 'Fast'),
(48, 72, 'Bright', 'Clever');

-- --------------------------------------------------------

--
-- Table structure for table `parent`
--

CREATE TABLE `parent` (
  `parentID` int(50) NOT NULL,
  `parentName` varchar(100) NOT NULL,
  `parentPW` varchar(100) NOT NULL,
  `parentEmail` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `parent`
--

INSERT INTO `parent` (`parentID`, `parentName`, `parentPW`, `parentEmail`) VALUES
(1, 'Ali', 'Ali@1234', 'ali@gmail.com'),
(2, 'Ariana', 'ariana@1234', 'ariana@gmail.com'),
(3, 'Jin', 'jin@1111', 'jin@gmail.com'),
(4, 'Jin', 'jin@111', 'jin@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `question`
--

CREATE TABLE `question` (
  `questionID` int(50) NOT NULL,
  `type` varchar(50) NOT NULL,
  `quizID` int(50) NOT NULL,
  `answer` varchar(100) NOT NULL,
  `questionText` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `question`
--

INSERT INTO `question` (`questionID`, `type`, `quizID`, `answer`, `questionText`) VALUES
(1, 'Fill in the Blank', 1, 'am', 'I ___ (is/am/are) a student. '),
(2, 'Fill in the Blank', 1, 'has', 'She ___ (has/have) a pet cat'),
(3, 'Matching', 1, '', NULL),
(4, 'Fill in the Blank', 2, 'heavily', 'It’s raining ___ (heavily/lightly). '),
(5, 'Fill in the Blank', 2, 'cake', 'A piece of ___ (cake/pie) means something is easy'),
(6, 'Matching', 2, '', NULL),
(7, 'Fill in the Blank', 3, 'run', 'I like to ___ (run/running) in the park'),
(8, 'Fill in the Blank', 3, 'eats', 'He ___ (eat/eats) an apple every day'),
(9, 'Matching', 3, '', NULL),
(10, 'Fill in the Blank', 4, 'bright', 'The sun is __ (bright/hot). '),
(11, 'Fill in the Blank', 4, 'apple', 'An ___ (apple/orange) is red. '),
(12, 'Matching', 4, '', ''),
(13, 'Fill in the Blank', 5, 'was', 'She ___ (was/were) happy yesterday. '),
(14, 'Fill in the Blank', 5, 'have', 'We ___ (has/have) two dogs. '),
(15, 'Matching', 5, '', NULL),
(16, 'Fill in the Blank', 6, 'piece of cake', 'The test was a ___ (piece of cake/cup of tea). '),
(17, 'Fill in the Blank', 6, 'cover', 'Don’t judge a book by its ___ (cover/story). '),
(18, 'Matching', 6, '', NULL),
(19, 'Fill in the Blank', 7, 'runs', 'He ___ (runs/run) every morning. '),
(20, 'Fill in the Blank', 7, 'cooked', 'She ___ (cooked/cook) dinner last night. '),
(21, 'Matching', 7, '', NULL),
(22, 'Fill in the Blank', 8, 'ocean', 'The ___ (ocean/forest) is full of water. '),
(23, 'Fill in the Blank', 8, 'banana', 'A ___ (banana/grape) is yellow. '),
(24, 'Matching', 8, '', NULL),
(25, 'Fill in the Blank', 9, 'were', 'They ___ (was/were) at the park yesterday. '),
(26, 'Fill in the Blank', 9, 'brushes', 'He always ___ (brushes/brush) his teeth before bed. '),
(27, 'Matching', 9, '', NULL),
(28, 'Fill in the Blank', 10, 'under the weather', 'I’m feeling a bit ___ (under the weather/in the clouds) today.'),
(29, 'Fill in the Blank', 10, 'words', 'Actions speak louder than ___ (words/sounds). '),
(30, 'Matching', 10, '', NULL),
(31, 'Fill in the Blank', 11, 'swim', 'They ___ (swim/swimming) in the pool every weekend.'),
(32, 'Fill in the Blank', 11, 'writes', 'She ___ (writes/writing) a letter to her friend. '),
(33, 'Matching', 11, '', NULL),
(34, 'Fill in the Blank', 12, 'elephant', 'The ___ (tiger/elephant) is the largest land animal.'),
(35, 'Fill in the Blank', 12, 'rainbow', 'The ___ (rainbow/cloud) has many colors. '),
(36, 'Matching', 12, '', ''),
(37, 'Fill in the Blank', 13, 'does', 'She ___ (does/do) her homework every evening.'),
(38, 'Fill in the Blank', 13, 'were', 'If I ___ (was/were) a bird, I would fly high. '),
(39, 'Matching', 13, '', NULL),
(40, 'Fill in the Blank', 14, 'blessing', 'The new rule is a ___ (blessing/curse) in disguise. '),
(41, 'Fill in the Blank', 14, 'feet', 'He got cold ___ (feet/hands) before the big performance.'),
(42, 'Matching', 14, '', NULL),
(43, 'Fill in the Blank', 15, 'writes', 'She ___ (writes/writing) beautifully in her journal. '),
(44, 'Fill in the Blank', 15, 'watched', 'We ___ (watched/watching) a movie last night.'),
(45, 'Matching', 15, '', ''),
(46, 'Fill in the Blank', 16, 'caterpillar', 'A ___ (caterpillar/butterfly) turns into a butterfly. '),
(47, 'Fill in the Blank', 16, 'eagle', 'The ___ (eagle/sparrow) is known for its sharp vision. '),
(48, 'Matching', 16, '', NULL),
(49, 'Fill in the Blank', 17, 'studies', 'If he ___ (studies/study) hard, he will pass the test. '),
(50, 'Fill in the Blank', 17, 'was', 'The cake ___ (was/were) baked by my grandmother. '),
(51, 'Matching', 17, '', NULL),
(52, 'Fill in the Blank', 18, 'nail', 'He hit the ___ (nail/wall) on the head with his answer. '),
(53, 'Fill in the Blank', 18, 'on', 'The teacher told us to keep an eye ___ (on/off) our homework. '),
(54, 'Matching', 18, '', NULL),
(55, 'Fill in the Blank', 19, 'had', 'By the time we arrived, the movie ___ (had/has) started. '),
(56, 'Fill in the Blank', 19, 'will', 'They ___ (will/would) finish their homework tomorrow. '),
(57, 'Matching', 19, '', NULL),
(58, 'Fill in the Blank', 20, 'lion', 'The ___ (lion/deer) is called the king of the jungle. '),
(59, 'Fill in the Blank', 20, 'ant', 'The ___ (ant/bee) is known for being hard-working. '),
(60, 'Matching', 20, '', NULL),
(61, 'Fill in the Blank', 21, 'had', 'By the time we arrived, they ___ (had/have) already eaten. '),
(62, 'Fill in the Blank', 21, 'had', 'If I ___ (had/have) known about the test, I would have studied more.'),
(63, 'Matching', 21, '', NULL),
(64, 'Fill in the Blank', 22, 'cat', 'He let the ___ (cat/dog) out of the bag by revealing the secret. '),
(65, 'Fill in the Blank', 22, 'edge', 'She was on the ___ (top/edge) of her seat during the thrilling movie. '),
(66, 'Matching', 22, '', NULL),
(67, 'Fill in the Blank', 23, 'has', 'She ___ (has/have) been working on the project since morning. '),
(68, 'Fill in the Blank', 23, 'will', 'By this time tomorrow, they ___ (will/would) have reached their destination. '),
(69, 'Matching', 23, '', NULL),
(70, 'Fill in the Blank', 24, 'volcano', 'The ___ (volcano/mountain) erupted, sending ash into the sky. '),
(71, 'Fill in the Blank', 24, 'mystery', 'The detective solved the ___ (mystery/story) with great skill. '),
(72, 'Matching', 24, '', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `quiz`
--

CREATE TABLE `quiz` (
  `quizID` int(50) NOT NULL,
  `category` varchar(50) NOT NULL,
  `teacherID` int(50) DEFAULT NULL,
  `totalMarks` int(50) NOT NULL,
  `grade` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz`
--

INSERT INTO `quiz` (`quizID`, `category`, `teacherID`, `totalMarks`, `grade`) VALUES
(1, 'Grammar', NULL, 5, 1),
(2, 'Idiom', NULL, 5, 1),
(3, 'Verb', NULL, 5, 1),
(4, 'Vocabulary', NULL, 5, 1),
(5, 'Grammar', NULL, 5, 2),
(6, 'Idiom', NULL, 5, 2),
(7, 'Verb', NULL, 5, 2),
(8, 'Vocabulary', NULL, 5, 2),
(9, 'Grammar', NULL, 5, 3),
(10, 'Idiom', NULL, 5, 3),
(11, 'Verb', NULL, 5, 3),
(12, 'Vocabulary', NULL, 5, 3),
(13, 'Grammar', NULL, 5, 4),
(14, 'Idiom', NULL, 5, 4),
(15, 'Verb', NULL, 5, 4),
(16, 'Vocabulary', NULL, 5, 4),
(17, 'Grammar', NULL, 5, 5),
(18, 'Idiom', NULL, 5, 5),
(19, 'Verb', NULL, 5, 5),
(20, 'Vocabulary', NULL, 5, 5),
(21, 'Grammar', NULL, 5, 6),
(22, 'Idiom', NULL, 5, 6),
(23, 'Verb', NULL, 5, 6),
(24, 'Vocabulary', NULL, 5, 6);

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `studentID` int(11) NOT NULL,
  `studentName` varchar(100) NOT NULL,
  `studentPW` varchar(100) NOT NULL,
  `gender` enum('Male','Female') NOT NULL,
  `studentEmail` varchar(100) NOT NULL,
  `parentID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`studentID`, `studentName`, `studentPW`, `gender`, `studentEmail`, `parentID`) VALUES
(1, 'Jennie', 'jennie@1234', 'Female', 'jennie@gmail.com', 1),
(2, 'Lisa', 'lalisa@1234', 'Female', 'lalisa@gmail.com', 4),
(3, 'Rose', 'Rose@1234', 'Female', 'rose@gmail.com', 2),
(4, 'jimin', 'jimin@111', 'Male', 'jimin@gmail.com', 4);

-- --------------------------------------------------------

--
-- Table structure for table `summary`
--

CREATE TABLE `summary` (
  `attemptID` int(50) NOT NULL,
  `questionID` int(50) NOT NULL,
  `studentAnswer` varchar(100) DEFAULT NULL,
  `isCorrect` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `teacherID` int(50) NOT NULL,
  `teacherName` varchar(100) NOT NULL,
  `teacherPW` varchar(100) NOT NULL,
  `gender` varchar(10) NOT NULL,
  `teacherEmail` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`teacherID`, `teacherName`, `teacherPW`, `gender`, `teacherEmail`) VALUES
(1, 'woozi', '$2y$10$awz9W9J22ruF3og4hESHTubYQqE4oon8pJ3LTF0oGxjfHsuz5EIWq', 'male', 'woozi@gmail.com'),
(2, 'woozi', 'woozi@1234', 'male', 'woozi@gmail.com'),
(3, 'woozi', 'woozi@1234', 'male', 'woozi@gmail.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attempt`
--
ALTER TABLE `attempt`
  ADD PRIMARY KEY (`attemptID`),
  ADD KEY `studentID` (`studentID`),
  ADD KEY `quizID` (`quizID`);

--
-- Indexes for table `matchingattempt`
--
ALTER TABLE `matchingattempt`
  ADD PRIMARY KEY (`attemptID`,`matchingID`),
  ADD KEY `matchingID` (`matchingID`);

--
-- Indexes for table `matchingpair`
--
ALTER TABLE `matchingpair`
  ADD PRIMARY KEY (`matchingID`),
  ADD KEY `questionID` (`questionID`);

--
-- Indexes for table `parent`
--
ALTER TABLE `parent`
  ADD PRIMARY KEY (`parentID`);

--
-- Indexes for table `question`
--
ALTER TABLE `question`
  ADD PRIMARY KEY (`questionID`),
  ADD KEY `quizID` (`quizID`);

--
-- Indexes for table `quiz`
--
ALTER TABLE `quiz`
  ADD PRIMARY KEY (`quizID`),
  ADD KEY `teacherID` (`teacherID`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`studentID`),
  ADD KEY `parentID` (`parentID`);

--
-- Indexes for table `summary`
--
ALTER TABLE `summary`
  ADD PRIMARY KEY (`attemptID`,`questionID`),
  ADD KEY `FK_summary_question` (`questionID`);

--
-- Indexes for table `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`teacherID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attempt`
--
ALTER TABLE `attempt`
  MODIFY `attemptID` int(50) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `matchingpair`
--
ALTER TABLE `matchingpair`
  MODIFY `matchingID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=49;

--
-- AUTO_INCREMENT for table `parent`
--
ALTER TABLE `parent`
  MODIFY `parentID` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `question`
--
ALTER TABLE `question`
  MODIFY `questionID` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=73;

--
-- AUTO_INCREMENT for table `quiz`
--
ALTER TABLE `quiz`
  MODIFY `quizID` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT for table `student`
--
ALTER TABLE `student`
  MODIFY `studentID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `teacher`
--
ALTER TABLE `teacher`
  MODIFY `teacherID` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `attempt`
--
ALTER TABLE `attempt`
  ADD CONSTRAINT `attempt_ibfk_1` FOREIGN KEY (`studentID`) REFERENCES `student` (`studentID`),
  ADD CONSTRAINT `attempt_ibfk_2` FOREIGN KEY (`quizID`) REFERENCES `quiz` (`quizID`);

--
-- Constraints for table `matchingattempt`
--
ALTER TABLE `matchingattempt`
  ADD CONSTRAINT `matchingattempt_ibfk_1` FOREIGN KEY (`attemptID`) REFERENCES `attempt` (`attemptID`),
  ADD CONSTRAINT `matchingattempt_ibfk_2` FOREIGN KEY (`matchingID`) REFERENCES `matchingpair` (`matchingID`);

--
-- Constraints for table `matchingpair`
--
ALTER TABLE `matchingpair`
  ADD CONSTRAINT `matchingpair_ibfk_1` FOREIGN KEY (`questionID`) REFERENCES `question` (`questionID`);

--
-- Constraints for table `question`
--
ALTER TABLE `question`
  ADD CONSTRAINT `question_ibfk_1` FOREIGN KEY (`quizID`) REFERENCES `quiz` (`quizID`);

--
-- Constraints for table `quiz`
--
ALTER TABLE `quiz`
  ADD CONSTRAINT `quiz_ibfk_1` FOREIGN KEY (`teacherID`) REFERENCES `teacher` (`teacherID`);

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`parentID`) REFERENCES `parent` (`parentID`);

--
-- Constraints for table `summary`
--
ALTER TABLE `summary`
  ADD CONSTRAINT `FK_summary_attempt` FOREIGN KEY (`attemptID`) REFERENCES `attempt` (`attemptID`),
  ADD CONSTRAINT `FK_summary_question` FOREIGN KEY (`questionID`) REFERENCES `question` (`questionID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
