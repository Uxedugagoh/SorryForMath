<?php
$host = 'localhost';
$db = 'sorryformath';
$user = 'postgres';
$pass = '1234';

try {
    $pdo = new PDO("pgsql:host=$host;dbname=$db", $user, $pass);
} catch (PDOException $e) {
    die("Ошибка подключения: " . $e->getMessage());
}
?>  