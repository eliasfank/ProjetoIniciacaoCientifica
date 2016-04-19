<?php
	 header('Content-Type: application/json; charset=utf-8');
	include '../defines.php'; 
	$response = array();
	 
	// checando dados recebidos
	if (isset($_POST["creator_id"])) {
	    $creator_id = $_POST['creator_id'];

	    //Criando conexão com o banco
	    $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);

	    // Checando conexão
	    if ($conn->connect_error) {
	        die("Connection failed: " . $conn->connect_error);
	    }

	    $sql = "SELECT *FROM ".DB_TABLE_LOCAL." WHERE ".CREATOR_ID." = $creator_id";
	    $result = $conn->query($sql);

	    // checando se o resultado for vazio
	   if ($result->num_rows > 0) {
	        $response["locais"] = array();
	     
	        while ($row = $result->fetch_array(MYSQL_ASSOC)) {
	            // temp user array
	        	$locais = array();
	            $locais[KEY_ID] = utf8_encode($row[KEY_ID]);
	            $locais[LOCAL_NAME] = utf8_encode($row[LOCAL_NAME]);
	     
	            // push single product into final response array
	            array_push($response["locais"], $locais);
	        }
	        // success
	        $response["success"] = 1;
	     
	        // echoing JSON response
	        echo json_encode($response);

	    }else{
	        // Nenhum local cadastrado
	        $response["success"] = 0;
	        $response["message"] = "Nenhum local cadastrado.";
	     
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