<?php
require('config.php');
session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['parentName'], $_POST['parentPW'], $_POST['parentEmail'], $_POST['studentIDs'])) {
        $parentName = mysqli_real_escape_string($con, $_POST['parentName']);
        $parentPW = mysqli_real_escape_string($con, $_POST['parentPW']);
        $parentEmail = mysqli_real_escape_string($con, $_POST['parentEmail']);

        $parentQuery = "INSERT INTO parent (parentName, parentPW, parentEmail) VALUES ('$parentName', '$parentPW', '$parentEmail')";
        if (mysqli_query($con, $parentQuery)) {
            $parentID = mysqli_insert_id($con);
            if (isset($_POST['studentIDs'])) {
                $studentIDs = explode(',', $_POST['studentIDs']);
                
                $studentIDs = array_map('trim', $studentIDs);

                foreach ($studentIDs as $studentID) {
                    $studentID = mysqli_real_escape_string($con, $studentID);
                    $studentQuery = "UPDATE student SET parentID = '$parentID' WHERE studentID = '$studentID'";
                    mysqli_query($con, $studentQuery);
                }
            }

            echo "<script>
                    alert('Registration successful! Your Parent ID is: $parentID');
                    window.location.replace('LOGIN.P.html');
                  </script>";
        } else {
            echo "<script>
                    alert('Error occurred during registration. Please try again later.');
                    window.location.replace('Register.P.php');
                  </script>";
        }
    } else {
        echo "<script>
                alert('Please fill in all the required fields.');
                window.location.replace('Register.P.php');
              </script>";
    }
}
?>
