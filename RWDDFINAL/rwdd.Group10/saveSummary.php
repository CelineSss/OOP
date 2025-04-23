<?php
session_start();
require('config.php');

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $studentID = $_POST['studentID'];
    $quizID = $_POST['quizID'];

    $attemptTimestamp = date("Y-m-d H:i:s");
    $durationTaken = $_POST['timeTaken'];
    $score = $_POST['score'];

    $stmt = $conn->prepare("INSERT INTO attempt (studentID, attemptTimestamp, durationTaken, score, quizID) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("issii", $studentID, $attemptTimestamp, $durationTaken, $score, $quizID);
    $stmt->execute();
    $attemptID = $stmt->insert_id;
    $stmt->close();

    // Insert answers into summary table
    $totalQuestions = $_POST['totalQuestions'];

    for ($i = 1; $i <= $totalQuestions; $i++) {
        $question = $_POST["question$i"];
        $studentAnswer = $_POST["answer$i"];
        $isCorrect = ($_POST["correct$i"] == "true") ? 1 : 0;

        $stmt = $conn->prepare("INSERT INTO summary (attemptID, questionID, studentAnswer, isCorrect) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("issi", $attemptID, $question, $studentAnswer, $isCorrect);
        $stmt->execute();
        $stmt->close();
    }

    echo "Summary saved successfully!";
}
?>
