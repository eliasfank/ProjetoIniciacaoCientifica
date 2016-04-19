<?php
    include '../defines.php';
    // checando dados recebidos
    if (isset($_POST['nome']) && isset($_POST['frase']) && isset($_POST['local_id']) && isset($_POST['latitude']) && isset($_POST['longitude']) && isset($_POST['altitude']) && isset($_POST['imagem'])) {
     
        $name = $_POST['nome'];
        $frase = $_POST['frase'];
        $local_id = $_POST['local_id'];
        $latitude = $_POST['latitude'];
        $longitude = $_POST['longitude'];
        $altitude = $_POST['altitude'];
        $imagem = base64_decode($_POST['imagem']);
        file_put_contents('ObjectImages/'.$name.'.png', $imagem);
        $image_path = 'ObjectImages/'.$name.'.png';

        // criando conexão
        $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);
        
        // Checando conexão
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $sql = "INSERT INTO " . DB_TABLE_OBJETO . "(".LOCAL_ID.", ".OBJECT_NAME.", ".FRASE.", ".LATITUDE.", ".LONGITUDE.", ".ALTITUDE.", ".IMAGE_PATH.") 
                            VALUES('$local_id', '$name', '$frase', '$latitude', '$longitude', '$altitude', '$image_path')";
     
        // checando se a inserção aconteceu
        if ($conn->query($sql) ) {
            $response["success"] = 1;
            $response["message"] = "Objeto criado com sucesso.";
            // echoing JSON response
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "Oops! Um erro ocorreu.".$conn->error;;
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
