<?php
require('config.php');

if (!isset($_GET['quizID']) || !isset($_GET['studentID'])) {
    die("<p style='color: red;'>Invalid request</p>");
}

$quizID = intval($_GET['quizID']);
$studentID = intval($_GET['studentID']);

$stmt = $con->prepare("
    SELECT score, durationTaken, attemptTimestamp
    FROM attempt
    WHERE quizID = ? AND studentID = ?
    ORDER BY attemptTimestamp DESC
    LIMIT 1
");
$stmt->bind_param("ii", $quizID, $studentID);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    echo "<h2>Quiz Attempt Details</h2>";
    echo "<table border='1' cellpadding='8' cellspacing='0'>";
    echo "<tr><th>Quiz ID</th><td>" . htmlspecialchars($quizID) . "</td></tr>";
    echo "<tr><th>Score</th><td>" . htmlspecialchars($row['score']) . "</td></tr>";
    echo "<tr><th>Total Questions</th><td>3</td></tr>"; 
    echo "<tr><th>Time Taken</th><td>" . htmlspecialchars($row['durationTaken']) . " seconds</td></tr>";
    echo "<tr><th>Attempt Timestamp</th><td>" . htmlspecialchars($row['attemptTimestamp']) . "</td></tr>";
    echo "</table>";
} else {
    echo "<p style='color: red;'>No attempts found</p>";
}

$stmt->close();
$con->close();
?>
