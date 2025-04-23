<?php
require('config.php');
session_start();

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

ob_clean();
flush();

error_reporting(E_ALL);
ini_set('display_errors', 1);

if (!isset($_SESSION['studentID'])) {
    echo json_encode(["error" => "User not logged in"]);
    exit();
}

$studentID = $_SESSION['studentID'];

$sql = "SELECT studentID, studentName, studentEmail FROM student WHERE studentID = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("s", $studentID);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    error_log("Fetched Data: " . print_r($row, true));
    echo json_encode($row);
} else {
    echo json_encode(["error" => "Student not found"]);
}




$stmt->close();
$con->close();
?>
