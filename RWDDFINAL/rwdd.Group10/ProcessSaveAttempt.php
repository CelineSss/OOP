<!--Process file: Save attempt of the students-->
<!--Currently able to save with matching quiz-->
<?php
session_start();
require('config.php'); 

if (!isset($_POST['answers']) || empty(trim($_POST['answers']))) {
    die("❌ Error: Missing data for answers. Check JavaScript or form submission.");
}

$quizID = intval($_POST['quizID']);
$studentID = intval($_POST['studentID']);
$score = intval($_POST['score']);
$durationTaken = intval($_POST['durationTaken']);
$answersRaw = trim($_POST['answers']); 

if (empty($answersRaw)) {
    die("Error: Missing data for answers.");
}

$answers = [];
$pairs = explode(",", $answersRaw); 
foreach ($pairs as $pair) {
    $data = explode(":", $pair);
    if (count($data) === 2) {
        $matchingID = intval($data[0]);
        $userAnswer = trim($data[1]);
        $answers[$matchingID] = $userAnswer;
    }
}

$query = "INSERT INTO attempt (studentID, attemptTimestamp, durationTaken, score, quizID) VALUES (?, NOW(), ?, ?, ?)";
$stmt = $con->prepare($query);
if (!$stmt) {
    die("Error preparing attempt query: " . $con->error);
}

$stmt->bind_param("iiii", $studentID, $durationTaken, $score, $quizID);
if (!$stmt->execute()) {
    die("Error inserting attempt: " . $stmt->error);
}

$attemptID = $stmt->insert_id; 
$stmt->close();

$query = "INSERT INTO matchingattempt (attemptID, matchingID, stuAnswer, isProper) VALUES (?, ?, ?, ?)";
$stmt = $con->prepare($query);
if (!$stmt) {
    die("Error preparing matchingattempt query: " . $con->error);
}

foreach ($answers as $matchingID => $userAnswer) {
    $checkQuery = "SELECT correctAnswer FROM matchingpair WHERE matchingID = ?";
    $checkStmt = $con->prepare($checkQuery);
    $checkStmt->bind_param("i", $matchingID);
    $checkStmt->execute();
    $checkResult = $checkStmt->get_result();
    $row = $checkResult->fetch_assoc();
    $correctAnswer = $row ? $row['correctAnswer'] : '';
    $isProper = ($userAnswer === $correctAnswer) ? 1 : 0;

    $stmt->bind_param("iisi", $attemptID, $matchingID, $userAnswer, $isProper);
    if (!$stmt->execute()) {
        die("Error inserting into matchingattempt: " . $stmt->error);
    }
}

$stmt->close();
$con->close();

echo "Quiz attempt saved successfully!";
header("Location: MatchingQuizSummary.php?attemptID=" . $attemptID);
exit();

?>
