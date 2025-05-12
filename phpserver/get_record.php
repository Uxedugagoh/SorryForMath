<?php
require 'sorryformath/db.php';

header('Content-Type: text/plain');

$username = $_GET['username'] ?? '';

if ($username) {
    $stmt = $pdo->prepare("SELECT record FROM users WHERE username = ?");
    $stmt->execute([$username]);
    if ($row = $stmt->fetch()) {
        echo $row['record'];
    } else {
        echo "0";
    }
} else {
    echo "0";
}
?>
