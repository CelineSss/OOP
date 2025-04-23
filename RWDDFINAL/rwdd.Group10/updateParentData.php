<?php
require('config.php');
session_start();

header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

ob_clean();
flush();

$data = json_decode(file_get_contents("php://input"), true);
$parentID = $_SESSION['parentID'];
$parentName = $data['name'];
$parentEmail = $data['email'];

if (!$parentID) {
    echo json_encode(["success" => false, "error" => "User not logged in"]);
    exit();
}

$sql = "UPDATE parent SET parentName = ?, parentEmail = ? WHERE parentID = ?";
$stmt = $con->prepare($sql);
$stmt->bind_param("ssi", $parentName, $parentEmail, $parentID);

if ($stmt->execute()) {
    echo json_encode(["success" => true]);
} else {
    echo json_encode(["success" => false, "error" => "Database update failed"]);
}

$stmt->close();
$con->close();
?>
