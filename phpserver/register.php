<?php
require 'db.php';
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *");

// Проверим, что данные пришли
if (!isset($_POST['username']) || !isset($_POST['password'])) {
    echo json_encode(['status' => 'error', 'message' => 'Поля username и password обязательны']);
    exit;
}

$username = $_POST['username'];
$password = password_hash($_POST['password'], PASSWORD_DEFAULT);

try {
    $pdo = new PDO("pgsql:host=localhost;dbname=sorryformath", "postgres", "1234");
    $stmt = $pdo->prepare("INSERT INTO users (username, password) VALUES (?, ?)");
    $stmt->execute([$username, $password]);

    echo json_encode(['status' => 'success']);
} catch (PDOException $e) {
    echo json_encode(['status' => 'error', 'message' => $e->getMessage()]);
}
?>
