<?php
require('config.php');
session_start();

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

ob_clean();
flush();

$data = json_decode(file_get_contents("php://input"), true);
$teacherID = $_SESSION['teacherID'];
$teacherName = $data['name'];
$teacherEmail = $data['email'];

if (!$teacherID) {
    echo json_encode(["success" => false, "error" => "User not logged in"]);
    exit();
}

$sql = "UPDATE teacher SET teacherName = ?, teacherEmail = ? WHERE teacherID = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("ssi", $teacherName, $teacherEmail, $teacherID);

if ($stmt->execute()) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "error" => "Database update failed"]);
}

$stmt->close();
$con->close();
?>
