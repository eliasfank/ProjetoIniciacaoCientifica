<?php
    include '../defines.php'; 
    $response = array();
     
    // checando dados recebidos
    if (isset($_POST["email"]) && isset($_POST["pass"])) {
        $email = $_POST['email'];
        $pass = $_POST['pass'];

        $hashed_pass = md5($pass);

        //Criando conexão com o banco
        $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);

        // Checando conexão
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $sql = "SELECT *FROM ".DB_TABLE_USER." WHERE ".USER_EMAIL."='$email' AND ".USER_PASS."='$hashed_pass'";
        

        if ($result = $conn->query($sql)) {

                    // checando se o resultado for vazio
           if ($result->num_rows > 0) {
                $row = $result->fetch_array(MYSQL_ASSOC);
                // success
                $response["success"] = 1;
                $response["message"] = "Usuario entrou com sucesso";
                $response[CREATOR_ID] = $row[KEY_ID];
             
                // echoing JSON response
                echo json_encode($response);

            }else{
                // Nenhum local cadastrado
                $response["success"] = 0;
                $response["message"] = "Nenhum usuario com essas credenciais.";
                // echoing JSON response
                echo json_encode($response);
            }

        }else {
            $response["success"] = 0;
            $response["message"] = "Oops! Um erro ocorreu.".$conn->error;;
            // echoing JSON response
            echo json_encode($response);
        }


        $conn->close();
    } else {
        // required field is missing
        $response["success"] = -1;
        $response["message"] = "Falta algum campo obrigatorio.";
     
        // echoing JSON response
        echo json_encode($response);
    }

?>