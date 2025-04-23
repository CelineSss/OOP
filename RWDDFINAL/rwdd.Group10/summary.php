<?php
session_start();
require('config.php');

$attemptID = $_GET['attemptID'];
$query = "SELECT s.questionID, s.studentAnswer, s.isCorrect, q.questionText 
          FROM summary s 
          JOIN questions q ON s.questionID = q.id 
          WHERE s.attemptID = '$attemptID'";
$result = mysqli_query($conn, $query);

echo "<h2>Quiz Summary</h2>";
echo "<ul>";
while ($row = mysqli_fetch_assoc($result)) {
    echo "<li>{$row['questionText']} - Your Answer: <strong>{$row['studentAnswer']}</strong> ";
    echo $row['isCorrect'] ? "<span style='color: green;'>✔ Correct</span>" : "<span style='color: red;'>✘ Incorrect</span>";
    echo "</li>";
}
echo "</ul>";
?>
