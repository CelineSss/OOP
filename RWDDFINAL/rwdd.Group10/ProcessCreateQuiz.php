<?php
session_start();
require('config.php');

if (!isset($_SESSION['teacherID'])) {
    echo "<script>alert('You must log in first.'); window.location.href='LOGIN.T.html';</script>";
    exit();
}

$teacherID = $_SESSION['teacherID']; 

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $category = $_POST['category'];
    $totalMarks = (int)$_POST['totalMarks'];
    $grade = (int)$_POST['grade'];

    $con->begin_transaction();

    try {
        // INSERT INTO quiz
        $stmt = $con->prepare("INSERT INTO quiz (category, teacherID, totalMarks, grade) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("siii", $category, $teacherID, $totalMarks, $grade);
        $stmt->execute();
        $quizID = $stmt->insert_id;

        // INSERT INTO question
        $questionType = "Matching";
        $answer = "None";
        $stmt = $con->prepare("INSERT INTO question (type, quizID, answer, questionText) VALUES (?, ?, ?, NULL)");
        $stmt->bind_param("sis", $questionType, $quizID, $answer);
        $stmt->execute();
        $questionID = $stmt->insert_id;

        // INSERT INTO matchingpair
        if (!empty($_POST['prompts']) && !empty($_POST['responses'])) {
            $stmt = $con->prepare("INSERT INTO matchingpair (questionID, subject, correctAnswer) VALUES (?, ?, ?)");
            for ($i = 0; $i < count($_POST['prompts']); $i++) {
                $subject = trim($_POST['prompts'][$i]);
                $correctAnswer = trim($_POST['responses'][$i]);

                if (!empty($subject) && !empty($correctAnswer)) {
                    $stmt->bind_param("iss", $questionID, $subject, $correctAnswer);
                    $stmt->execute();
                }
            }
        }

        $con->commit();

        echo "<script>
            alert('Quiz successfully created! Your Quiz ID is: $quizID');
            window.location.href='HomePage.T.html';
        </script>";
        exit();

    } catch (Exception $e) {
        $con->rollback();
        die("Error: " . $e->getMessage());
    }
} else {
    echo "<script>alert('Invalid request.'); window.location.href='createquiz.html';</script>";
}
?>
