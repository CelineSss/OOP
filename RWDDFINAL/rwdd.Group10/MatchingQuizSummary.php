<!--Show Quiz Summary after student completed their quiz-->
<?php
session_start();
require('config.php');

if (!isset($_GET['attemptID']) || !is_numeric($_GET['attemptID'])) {
    die("Error: Missing attempt ID.");
}

$attemptID = intval($_GET['attemptID']);

$query = "SELECT studentID, attemptTimestamp, durationTaken, score, quizID FROM attempt WHERE attemptID = ?";
$stmt = $con->prepare($query);
if (!$stmt) {
    die("Error preparing attempt query: " . $con->error);
}
$stmt->bind_param("i", $attemptID);
$stmt->execute();
$result = $stmt->get_result();
$attemptData = $result->fetch_assoc();
$stmt->close();

// ✅ Check if attempt data exists
if (!$attemptData) {
    die("Error: No attempt data found.");
}

$studentID = $attemptData['studentID'];
$quizID = $attemptData['quizID'];
$durationTaken = $attemptData['durationTaken'];
$score = $attemptData['score'];
$timestamp = $attemptData['attemptTimestamp'];

$query = "SELECT COUNT(*) AS questionID FROM matchingattempt WHERE attemptID = ?";
$stmt = $con->prepare($query);
if (!$stmt) {
    die("Error preparing totalQuestions query: " . $con->error);
}
$stmt->bind_param("i", $attemptID);
$stmt->execute();
$result = $stmt->get_result();
$totalData = $result->fetch_assoc();
$totalQuestions = $totalData['questionID']; 
$stmt->close();

$query = "SELECT ma.matchingID, mp.subject, mp.correctAnswer, ma.stuAnswer, ma.isProper
          FROM matchingattempt ma
          JOIN matchingpair mp ON ma.matchingID = mp.matchingID
          WHERE ma.attemptID = ?";
$stmt = $con->prepare($query);
if (!$stmt) {
    die("Error preparing matchingattempt query: " . $con->error);
}
$stmt->bind_param("i", $attemptID);
$stmt->execute();
$result = $stmt->get_result();

$answers = [];
while ($row = $result->fetch_assoc()) {
    $answers[] = $row;
}
$stmt->close();
$con->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Matching Quiz Summary</title>
    <link rel="stylesheet" href="matchingSummary.css">
</head>
<body>
    <header class="header">
        <a href="HomePage.S.html" class="home">
            <div class="left-section">
                <img src="images/logo2.png" alt="Logo" class="logo">
            </div>
        </a>
    </header>

    <div class="summary-container">
        <div class="summary-header">🎉Quiz Complete🎉</div>

        <div class="info-container">
            <p><strong>Attempt ID:</strong> <?php echo $attemptID; ?></p>
            <p><strong>Student ID:</strong> <?php echo $studentID; ?></p>
            <p><strong>Quiz ID:</strong> <?php echo $quizID; ?></p>
        </div>
        
        <div class="score-section">
            <div class="score-circle">
                <p><?php echo $score . '/' . $totalQuestions; ?></p>
            </div>

            <div class="stats">
                <div class="stat-item">
                    <p><strong>Duration Taken:</strong> <?php echo $durationTaken; ?> seconds</p>
                    <p><strong>Timestamp:</strong> <?php echo $timestamp; ?></p>
                </div>
            </div>
        </div>
        
        <h3>Question Overview:</h3>
            <table>
                <tr>
                    <th>Prompt</th>
                    <th>Correct Answer</th>
                    <th>Your Answer</th>
                    <th>Result</th>
                </tr>
                <?php foreach ($answers as $row): ?>
                    <tr class="<?php echo ($row['isProper'] == 1) ? 'correct' : 'incorrect'; ?>">
                        <td><?php echo htmlspecialchars($row['subject']); ?></td>
                        <td><?php echo htmlspecialchars($row['correctAnswer']); ?></td>
                        <td><?php echo htmlspecialchars($row['stuAnswer']); ?></td>
                        <td><?php echo ($row['isProper'] == 1) ? '✅ Correct' : '❌ Incorrect'; ?></td>
                    </tr>
                <?php endforeach; ?>
            </table>
        <br>

        <form action="MatchingQuiz.php" method="GET">
                <input type="hidden" name="quizID" value="<?php echo $quizID; ?>">
                <button class="action-button" type="submit">🔄 Redo Quiz</button>
        </form>
    </div>
</body>
</html>
