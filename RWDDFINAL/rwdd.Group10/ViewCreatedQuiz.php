<?php
session_start();
require('config.php');

if (!isset($_SESSION['teacherID'])) {
    header("Location: login.html");
    exit();
}

$teacherID = $_SESSION['teacherID']; 

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

$query = "SELECT q.quizID,q.category,q.teacherID,q.grade,que.type,mp.subject,mp.correctAnswer
          FROM quiz q
          JOIN question que ON q.quizID = que.quizID
          JOIN matchingpair mp ON que.questionID = mp.questionID
          WHERE q.teacherID = '$teacherID'"; 

$result = $con->query($query);

if ($result === false) {
    echo "Error with query: " . $con->error;
    exit();
}

$quizzes = [];
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $quizzes[] = $row;
    }
} else {
    $quizzes = null; 
}
$con->close();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Created Quizzes</title>
    <link rel="stylesheet" href="ViewCreatedQuiz.css">
</head>
<body>
    <header class="header">
        <a href="HomePage.T.html" class="home">
            <div class="left-section">
                <img src="images/logo2.png" alt="Logo" class="logo">
            </div>
        </a>
        <div class="right-section">
            <div class="icons">
                <img src="images/user.icon.png" alt="User">
                <img src="images/setting.icon.png" alt="Settings">
            </div>
        </div>
    </header>
    <h2>View Created Quizzes</h2>

    <?php if ($quizzes !== null): ?>
        <table border="1" class="viewresults">
            <thead>
                <tr>
                    <th>Quiz ID</th>
                    <th>Teacher ID</th>
                    <th>Type</th>
                    <th>Category</th>
                    <th>Grade</th>
                    <th>Prompt</th>
                    <th>Answer</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($quizzes as $quiz): ?>
                    <tr>
                        <td><?php echo htmlspecialchars($quiz['quizID']); ?></td>
                        <td><?php echo htmlspecialchars($quiz['teacherID']); ?></td>
                        <td><?php echo htmlspecialchars($quiz['type']); ?></td>
                        <td><?php echo htmlspecialchars($quiz['category']); ?></td>
                        <td><?php echo htmlspecialchars($quiz['grade']); ?></td>
                        <td><?php echo htmlspecialchars($quiz['subject']); ?></td>
                        <td><?php echo htmlspecialchars($quiz['correctAnswer']); ?></td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
        <br>
        <input type="button" class="print-btn" value="Print" onclick="window.print();">
    <?php else: ?>
        <p>No quizzes found.</p>
    <?php endif; ?>

</body>
</html>
