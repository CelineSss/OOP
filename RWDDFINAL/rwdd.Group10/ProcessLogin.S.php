<?php
require('config.php');
session_start();

if (isset($_POST['LogIn'])) {
    if (isset($_POST['studentID'], $_POST['studentPW'])) {
        $studentID = mysqli_real_escape_string($con, $_POST['studentID']);
        $studentPW = mysqli_real_escape_string($con, $_POST['studentPW']);

        $studentQuery = "SELECT * FROM `student` WHERE studentID = '$studentID' AND studentPW = '$studentPW'";
        $studentResult = mysqli_query($con, $studentQuery);

        if ($studentResult) {
            $student_info = mysqli_num_rows($studentResult);
            if ($student_info > 0) {
                $_SESSION['studentID'] = $studentID;
                $_SESSION['LogIn'] = true;
                echo "<script>
                    alert('Login successful!');
                    window.location.href = 'HomePage.S.html';
                </script>";
            } else {
                echo "<script>
                    alert('Wrong username or password. Please try again.');
                    window.location.href = 'LOGIN.S.html';
                </script>";
            }
        } else {
            echo "<script>
                alert('Database query failed. Please try again later.');
                window.location.replace('LOGIN.S.html');
            </script>";
        }
    } else {
        echo "<script>
            alert('Please fill in all fields.');
            window.location.replace('LOGIN.S.html');
        </script>";
    }
} else {
    echo "<script>
        alert('Invalid request!');
        window.location.replace('LOGIN.S.html');
    </script>";
}
?>
