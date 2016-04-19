<?php
    include '../defines.php';

    $response = array();
     
    // checando dados recebidos
    if (isset($_POST['nome_local']) && isset($_POST['creator_id'])) {
     
        $name = $_POST['nome_local'];
        $criador = $_POST['creator_id'];
         
        // criando conexão
        $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);
        
        // Checando conexão
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $sql = "INSERT INTO ".DB_TABLE_LOCAL."(".LOCAL_NAME.", ".CREATOR_ID.") VALUES ('$name', '$criador')";
     
        // checando se a inserção aconteceu
        if ($conn->query($sql) ) {
            $response["success"] = 1;
            $response["message"] = "Local criado com sucesso.";
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