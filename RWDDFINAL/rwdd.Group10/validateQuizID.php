<!--Validate entered QuizID with QuizID in the database -->
<?php
session_start();
require('config.php');

if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

if (isset($_POST['quizID'])) {
    $quizID = $_POST['quizID'];
    $quizID = $con->real_escape_string($quizID);

    $query = "SELECT quizID FROM quiz WHERE quizID = '$quizID'";
    $result = $con->query($query);

    if ($result === false) {
        echo "Error with query: " . $con->error;
    } else {
        if ($result->num_rows > 0) {
            header("Location: ReadyMenu.php?quizID=$quizID");
            exit();
        } else {
            echo "<script type='text/javascript'>
                    alert('QuizID not found. Please try again.');
                    window.location.href = 'HomePage.S.html';
                  </script>";
        }
    }
}
$con->close();
?>
