<?php
require('config.php'); 
session_start();

if (isset($_POST['reset'])) {
    $parentID = htmlspecialchars(trim($_POST['parentID']));
    $newPassword = trim($_POST['newPassword']);
    $confirmPassword = trim($_POST['confirmPassword']);

    if ($newPassword !== $confirmPassword) {
        echo "<script>showPopUpMessage('Passwords do not match. Please try again.', 'error');</script>";
        exit;
    }

    if (!preg_match('/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d\-_\.@]{8,}$/', $newPassword)) {
        echo "<script>showPopUpMessage('Password does not meet the requirements. Please try again.', 'error');</script>";
        exit;
    }

    if ($stmt = $con->prepare("UPDATE parent SET parentPW = ? WHERE parentID = ?")) {
        $stmt->bind_param('ss', $newPassword, $parentID); 

        if ($stmt->execute()) {
            echo "<script>
                alert('Reset Password Successfully!');
                window.location.replace('LOGIN.P.html');
            </script>";
        } else {
            echo "<script>
                alert('Failed to Reset Password! Please try again.');
                window.location.replace('forgot_pw.P.html');
            </script>";
        }
        $stmt->close();
    } else {
        echo "<script>showPopUpMessage('Error preparing query. Please try again later.', 'error');</script>";
    }
    $con->close();
}
?>
