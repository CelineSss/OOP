<?php
require('config.php');
session_start();

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

ob_clean();
flush();

$data = json_decode(file_get_contents("php://input"), true);
$studentID = $_SESSION['studentID'];
$studentName = $data['name'];
$studentEmail = $data['email'];

if (!$studentID) {
    echo json_encode(["success" => false, "error" => "User not logged in"]);
    exit();
}

$sql = "UPDATE student SET studentName = ?, studentEmail = ? WHERE studentID = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("ssi", $studentName, $studentEmail, $studentID);

if ($stmt->execute()) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "error" => "Database update failed"]);
}

$stmt->close();
$con->close();
?>
