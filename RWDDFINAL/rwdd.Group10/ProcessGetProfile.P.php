<?php
require('config.php');
session_start();

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

ob_clean();
flush();

if (!isset($_SESSION['parentID'])) {
    echo json_encode(["error" => "User not logged in"]);
    exit();
}

$parentID = $_SESSION['parentID'];

$sql = "SELECT parentID, parentName, parentEmail FROM parent WHERE parentID = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("s", $parentID);
$stmt->execute();
$result = $stmt->get_result();

if ($row = $result->fetch_assoc()) {
    error_log("Fetched Data: " . print_r($row, true));
    echo json_encode($row);
} else {
    echo json_encode(["error" => "parent not found"]);
}

$stmt->close();
$con->close();
?>
