<?php
    include '../defines.php';

    $response = array();
     
    // checando dados recebidos
    if (isset($_POST['email']) && isset($_POST['pass'])) {
     
        $email = $_POST['email'];
        $pass = $_POST['pass'];


        $hashed_pass = md5($pass);
         
        // criando conexão
        $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);
        
        // Checando conexão
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $sql = "INSERT INTO ".DB_TABLE_USER."(".USER_EMAIL.", ".USER_PASS.") VALUES ('$email', '$hashed_pass')";
     
        // checando se a inserção aconteceu
        if ($conn->query($sql) ) {
            $response["success"] = 1;
            $response["message"] = "Usuario criado com sucesso";
            // echoing JSON response
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "Oops! Um erro ocorreu.";
            // echoing JSON response
            echo json_encode($response);
        }
        $conn->close();
    } else {
        $response["success"] = -1;
        $response["message"] = "Falta algum campo obrigatorio";
        // echoing JSON response
        echo json_encode($response);
    }
?>