<?php
require('config.php');
session_start();

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

ob_clean();
flush();

if (!isset($_SESSION['teacherID'])) {
    echo json_encode(["error" => "User not logged in"]);
    exit();
}

$teacherID = $_SESSION['teacherID'];

$sql = "SELECT teacherID, teacherName, teacherEmail FROM teacher WHERE teacherID = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("s", $teacherID);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    error_log("Fetched Data: " . print_r($row, true));
    echo json_encode($row);
} else {
    echo json_encode(["error" => "teacher not found"]);
}

$stmt->close();
$con->close();
?>
