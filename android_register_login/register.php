<?php

if ($_SERVER['REQUEST_METHOD'] =='POST'){

    $name = $_POST['name'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $organisation = $_POST['organisation'];
    $password = $_POST['password'];

    $password = password_hash($password, PASSWORD_DEFAULT);

    require_once 'connect.php';

    $sql = "INSERT INTO users_table (name, email, phone, organisation, password) VALUES ('$name', '$email', '$phone', '$organisation', '$password')";

    if ( mysqli_query($conn, $sql) ) {
        $result["success"] = "1";
        $result["message"] = "success";

        echo json_encode($result);
        mysqli_close($conn);

    } else {

        $result["success"] = "0";
        $result["message"] = "error";

        echo json_encode($result);
        mysqli_close($conn);
    }
}

?>