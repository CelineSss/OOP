<?php
require('config.php');
session_start();

if (isset($_POST['LogIn'])) {
    if (isset($_POST['parentID'], $_POST['parentPW'])) {
        // Use mysqli to avoid SQL injection
        $parentID = mysqli_real_escape_string($con, $_POST['parentID']);
        $parentPW = mysqli_real_escape_string($con, $_POST['parentPW']);

        $parentQuery = "SELECT * FROM `parent` WHERE parentID = '$parentID' AND parentPW = '$parentPW'";
        $parentResult = mysqli_query($con, $parentQuery);

        if ($parentResult) {
            $parent_info = mysqli_num_rows($parentResult);
            if ($parent_info > 0) {
                $_SESSION['parentID'] = $parentID;
                $_SESSION['LogIn'] = true;

                echo "<script>
                    alert('Login successful!');
                    window.location.href = 'HomePage.P.html';
                </script>";
            } else {
                echo "<script>
                    alert('Wrong username or password. Please try again.');
                    window.location.href = 'LOGIN.P.html';
                </script>";
            }
        } else {
            echo "<script>
                alert('Database query failed. Please try again later.');
                window.location.replace('LOGIN.P.html');
            </script>";
        }
    } else {
        echo "<script>
            alert('Please fill in all fields.');
            window.location.replace('LOGIN.P.html');
        </script>";
    }
} else {
    echo "<script>
        alert('Invalid request!');
        window.location.replace('LOGIN.P.html');
    </script>";
}
?>
