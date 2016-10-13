<?php
	include '../defines.php'; 
	$response = array();
	 
	// checando dados recebidos
	if (isset($_POST["local_id"])) {
	    $local_id = $_POST['local_id'];

	    //Criando conexão com o banco
	    $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);

	    // Checando conexão
	    if ($conn->connect_error) {
	        die("Connection failed: " . $conn->connect_error);
	    }

	    //$sql = "SELECT *FROM ".DB_TABLE_OBJETO." WHERE ".LOCAL_ID." = $local_id";
	    //Apenas para mostrar os tres ultimos
	    $sql = "SELECT *FROM ".DB_TABLE_OBJETO." WHERE ".LOCAL_ID." = $local_id"." order by ". KEY_ID." DESC LIMIT 3";
		$result = $conn->query($sql);

	    // checando se o resultado for vazio
	   if ($result->num_rows > 0) {
	        $response["objetos"] = array();
	     
	        while ($row = $result->fetch_array(MYSQL_ASSOC)) {
	            // temp user array
	            $objetos = array();
	            $objetos[KEY_ID] = $row[KEY_ID];
	            $objetos[OBJECT_NAME] = utf8_encode($row[OBJECT_NAME]);
	            $objetos[FRASE] = utf8_encode($row[FRASE]);
	            $objetos[LATITUDE] = $row[LATITUDE];
	            $objetos[LONGITUDE] = $row[LONGITUDE];

	            //IMAGEM//////////////////////////////////////////////////////////////////////////////////
	            $im = file_get_contents($row[IMAGE_PATH_RESIZED]);
	            $objetos['imagem'] = base64_encode($im); 
	            //IMAGEM//////////////////////////////////////////////////////////////////////////////////

	            // push single product into final response array
	            array_push($response["objetos"], $objetos);
	        }
	        // success
	        $response["success"] = 1;
	     
	        // echoing JSON response
	        echo json_encode($response);

        }else{
            // Nenhum objeto cadastrado nesse local
            $response["success"] = 0;
            $response["message"] = "Nenhum objeto cadastrado nesse local";
 
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