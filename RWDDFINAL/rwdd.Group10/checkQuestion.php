<?php
require('config.php');

if (!isset($_GET['quizID']) || !isset($_GET['questionID'])) {
    die("<p style='color: red;'>Invalid request</p>");
}

$quizID = intval($_GET['quizID']);
$questionID = intval($_GET['questionID']);

$stmt = $con->prepare("
    SELECT questionText, answer, type
    FROM questions
    WHERE quizID = ? AND questionID = ?
");

$stmt->bind_param("ii", $quizID, $questionID);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo "<h2>Question Details</h2>";
    echo "<table border='1' cellpadding='8' cellspacing='0'>";
    echo "<tr><th>Quiz ID</th><td>" . htmlspecialchars($quizID) . "</td></tr>";
    echo "<tr><th>Question ID</th><td>" . htmlspecialchars($questionID) . "</td></tr>";
    echo "<tr><th>Question</th><td>" . htmlspecialchars($row['questionText']) . "</td></tr>";
    echo "<tr><th>Answer</th><td>" . htmlspecialchars($row['answer']) . "</td></tr>";
    echo "<tr><th>Type</th><td>" . htmlspecialchars($row['type']) . "</td></tr>";
    echo "</table>";
} else {
    echo "<p style='color: red;'>No question found</p>";
}

$stmt->close();
$con->close();
?>
