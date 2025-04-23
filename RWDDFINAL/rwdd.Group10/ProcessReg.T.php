<?php
require('config.php');

if (isset($_POST['register'])) {
    $teacherName = $_POST['teacherName'];
    $teacherPW = $_POST['teacherPW'];
    $gender = $_POST['gender'];
    $teacherEmail = $_POST['teacherEmail'];

    if (!filter_var($teacherEmail, FILTER_VALIDATE_EMAIL)) {
        echo "<script>alert('Invalid email format.'); window.location='REG.php';</script>";
        exit;
    }

    if (!preg_match("/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d\-_\.@]{8,}$/", $teacherPW)) {
        echo "<script>alert('Password must be at least 8 characters long, and contain both letters and numbers.'); window.location='REG.php';</script>";
        exit;
    }

    if ($teacherPW !== $_POST['confirmPassword']) {
        echo "<script>alert('Passwords do not match.'); window.location='REG.php';</script>";
        exit;
    }

    $stmt = $con->prepare("INSERT INTO teacher (teacherName, teacherPW, gender, teacherEmail) VALUES (?, ?, ?, ?)");
    if ($stmt === false) {
        die('MySQL prepare error: ' . $con->error);
    }

    $stmt->bind_param("ssss", $teacherName, $teacherPW, $gender, $teacherEmail);
    if ($stmt->execute()) {
        $teacherID = $con->insert_id;
        echo "<script>
            alert('Registration successful! Your Teacher ID is: $teacherID');
            window.location='LOGIN.T.html';
        </script>";
    } else {
        echo "<script>
            alert('Registration failed. Error: " . $stmt->error . "');
            window.location='Register.T.php';
        </script>";
    }
    $stmt->close();
}
?>
