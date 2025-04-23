<?php
require('config.php');
session_start();

if (isset($_POST['LogIn'])) {
    if (isset($_POST['teacherID'], $_POST['teacherPW'])) {
        $teacherID = mysqli_real_escape_string($con, $_POST['teacherID']);
        $teacherPW = mysqli_real_escape_string($con, $_POST['teacherPW']);

        $teacherQuery = "SELECT * FROM `teacher` WHERE teacherID = '$teacherID' AND teacherPW = '$teacherPW'";
        $teacherResult = mysqli_query($con, $teacherQuery);

        if ($teacherResult) {
            $teacher_info = mysqli_num_rows($teacherResult);
            if ($teacher_info > 0) {
                $_SESSION['teacherID'] = $teacherID;
                $_SESSION['LogIn'] = true;
                echo "<script>
                    alert('Login successful!');
                    window.location.href = 'HomePage.T.html';
                </script>";
            } else {
                echo "<script>
                    alert('Wrong username or password. Please try again.');
                    window.location.href = 'LOGIN.T.html';
                </script>";
            }
        } else {
            echo "<script>
                alert('Database query failed. Please try again later.');
                window.location.replace('LOGIN.T.html');
            </script>";
        }
    } else {
        echo "<script>
            alert('Please fill in all fields.');
            window.location.replace('LOGIN.T.html');
        </script>";
    }
} else {
    echo "<script>
        alert('Invalid request!');
        window.location.replace('LOGIN.T.html');
    </script>";
}
?>
