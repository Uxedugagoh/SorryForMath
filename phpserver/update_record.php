<?php
require 'sorryformath/db.php';

$username = $_POST['username'];
$score = (int)$_POST['score'];

$stmt = $pdo->prepare("SELECT record FROM users WHERE username = ?");
$stmt->execute([$username]);
$row = $stmt->fetch();

if ($row) {
    $currentRecord = (int)$row['record'];
    if ($score > $currentRecord) {
        $update = $pdo->prepare("UPDATE users SET record = ? WHERE username = ?");
        $update->execute([$score, $username]);
    }
    echo "updated";
} else {
    echo "user_not_found";
}
?>
