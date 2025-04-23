<?php
require('config.php');

if (isset($_POST['register'])) {
    $studentName = $_POST['studentName'];
    $studentPW = $_POST['studentPW'];
    $gender = $_POST['gender'];
    $studentEmail = $_POST['studentEmail'];
    $parentID = isset($_POST['parentID']) ? $_POST['parentID'] : NULL;

    if (!filter_var($studentEmail, FILTER_VALIDATE_EMAIL)) {
        echo "<script>alert('Invalid email format.');</script>";
        echo "<script>window.location='Register.S.php';</script>";
        exit;
    }

    if (!preg_match("/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d\-_\.@]{8,}$/", $studentPW)) {
        echo "<script>alert('Password must be at least 8 characters long, and contain both letters and numbers.');</script>";
        echo "<script>window.location='Register.S.php';</script>";
        exit;
    }

    if ($studentPW !== $_POST['confirmPassword']) {
        echo "<script>alert('Passwords do not match.');</script>";
        echo "<script>window.location='Register.S.php';</script>";
        exit;
    }

    $stmt = $con->prepare("INSERT INTO student (studentName, studentPW, gender, studentEmail, parentID) VALUES (?, ?, ?, ?, ?)");

    if ($stmt === false) {
        die('MySQL prepare error: ' . $con->error);
    }

    if ($parentID === NULL) {
        $stmt->bind_param("sssss", $studentName, $studentPW, $gender, $studentEmail, $parentID);
    } else {
        $stmt->bind_param("ssssi", $studentName, $studentPW, $gender, $studentEmail, $parentID);
    }

    if ($stmt->execute()) {
        $studentID = $con->insert_id;
        echo "<script>alert('Registration successful! Your student ID is: " . $studentID . "');</script>";
        echo "<script>window.location='LOGIN.S.html';</script>";
    } else {
        echo "<script>alert('Failed to register. Error: " . $stmt->error . "');</script>";
        echo "<script>window.location='Register.S.php';</script>";
    }
    $stmt->close();
}
?>
