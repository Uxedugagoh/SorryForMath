<?php
require 'db.php';

header("Content-Type: application/json");

$username = $_POST['username'] ?? '';
$password = $_POST['password'] ?? '';

if (empty($username) || empty($password)) {
    echo json_encode(["status" => "error", "message" => "Empty credentials"]);
    exit;
}

$stmt = $pdo->prepare("SELECT password FROM users WHERE username = ?");
$stmt->execute([$username]);

if ($row = $stmt->fetch()) {
    if (password_verify($password, $row['password'])) {
        echo json_encode(["status" => "success"]);
    } else {
        echo json_encode(["status" => "invalid"]);
    }
} else {
    echo json_encode(["status" => "not_found"]);
}
?>
