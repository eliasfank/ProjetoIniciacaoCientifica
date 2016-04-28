<?php
    include '../defines.php';
    include '../resizeImage.php';
    date_default_timezone_set('America/Sao_Paulo');
    class Objeto{
    	public $id=0;
    	public $latitude=0;
    	public $longitude=0;
    	public $frase=0;
    	public $distancia=0;
	}
    $response = array();
     
    // checando dados recebidos
    if (isset($_POST['latitude']) && isset($_POST['longitude'])) {
     
        $latitude = $_POST['latitude'];
        $longitude = $_POST['longitude'];

        $nomeimagem = 'RecievedImages/Normal/recievedOn_'.date("Y.m.d_").date("H.i.s").'.jpg';
        $nomeimagem_resized = 'RecievedImages/NormalResized/recievedOn_'.date("Y.m.d_").date("H.i.s").'.jpg';
        $nomeimagem_gray = 'RecievedImages/Gray/recievedOn_'.date("Y.m.d_").date("H.i.s").'.jpg';
        $nomeimagem_gray_resized = 'RecievedImages/GrayResized/recievedOn_'.date("Y.m.d_").date("H.i.s").'.jpg';

        $distancia = 0;
        $idObjeto = 0;

        try {
		    //Exceção caso nao seja possivel mover o arquivo
		    if (!move_uploaded_file($_FILES['imagem']['tmp_name'], $nomeimagem)) {
		        throw new Exception('Could not move file');
		    }

		    //para criar os varios formatos da imagem recebida
		    $im = imagecreatefromjpeg($nomeimagem);
            imagefilter($im, IMG_FILTER_GRAYSCALE);
            imagejpeg($im, $nomeimagem_gray);
            smart_resize_image($nomeimagem, null, MAX_RESIZE_WIDTH, MAX_RESIZE_HEIGHT, true, $nomeimagem_resized, false, true, 100);
            smart_resize_image($nomeimagem_gray, null, MAX_RESIZE_WIDTH, MAX_RESIZE_HEIGHT, true, $nomeimagem_gray_resized, false, true, 100);
           
		} catch (Exception $e) {
		    die('File did not upload: ' . $e->getMessage());
		}
        
        // criando conexão
        $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);
        
        // Checando conexão
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        //$sql = "SELECT * FROM ".DB_TABLE_OBJETO." ORDER BY sqrt(pow($longitude - ".LONGITUDE.", 2)+pow($latitude - ".LATITUDE.", 2)) LIMIT 10";
		
		//$sql = "SELECT *, (6371 *acos(cos(radians($latitude)) *cos(radians(latitude)) *cos(radians(longitude) - radians(longitude)) +sin(radians($latitude)) *sin(radians(latitude)))) AS distance FROM ". DB_TABLE_OBJETO ." HAVING distance <= 0.01";

		//$sql = "SELECT *, sqrt( pow(($latitude - latitude),2) + pow(($longitude - longitude),2)) as distance FROM ".DB_TABLE_OBJETO." HAVING distance <= 0.001";

		$sql = "SELECT *, (111.045* DEGREES(ACOS(COS(RADIANS($latitude))
                 * COS(RADIANS(latitude))
                 * COS(RADIANS($longitude) - RADIANS(longitude))
                 + SIN(RADIANS($latitude))
                 * SIN(RADIANS(latitude)))))*1000 AS distance FROM 
                 ".DB_TABLE_OBJETO." HAVING distance <=10";

		$result = $conn->query($sql);
		
		if ($result->num_rows > 0) {
	        $parametros = "";
	        $chave = [];
	        $valor = [];

	        while ($row = $result->fetch_array(MYSQL_ASSOC)) {
				$tempString = " ../CadastroObjetos/".$row[IMAGE_PATH_RESIZED_GRAY];
	            $parametros = $parametros.$tempString;
	            $last = substr(strrchr($tempString, "/"), 1 );

	            $obj = new Objeto();
	            $obj->id = $row[KEY_ID];
	            $obj->longitude = $row[LONGITUDE];
	            $obj->latitude = $row[LATITUDE];
	            $obj->frase = $row[FRASE];
	            $obj->distancia = $row['distance'];

	            array_push($chave, $last);
	            array_push($valor, $obj);
	        }
	        
	        //Esse vai ser o par: nome da imagem e objeto com varias informações
	        $mapeado = array_combine($chave, $valor);
	        
	        //Precisamos mostrar onde o php deve procurar as bibliotecas
			putenv("LD_LIBRARY_PATH=/usr/local/lib");
			
			$scriptName = " features_detection.py";
			#$scriptName = " opencv_correlation.py";
			#$scriptName = " opencv_chi-squared.py";
			#$scriptName = " opencv_intersection.py";
			
			$ender_imagem = $nomeimagem_gray_resized;
			$command = 'python'.$scriptName." ".$ender_imagem.$parametros;
			
			//Executando atraves do shell o script python
			exec($command, $output);

			if($output[1] > 10){
				$distancia = $mapeado[$output[0]]->distancia;
		    	$idObjeto = $mapeado[$output[0]]->id;
				//Enviando resposta para o android
		        echo utf8_encode($mapeado[$output[0]]->frase);
	    	}else{
	    		echo "Objeto não identificado, tente novamente.";
	    	}

	    }else{

	        echo "Nenhum objeto cadastrado neste local ou no raio informado.";
	    }

	    //Armazenando o resultado na base

		$sql = "INSERT INTO ". DB_TABLE_RESULTADO ." (".LON_USER.",".LAT_USER.",".DISTANCIA_METROS.",".RESULTADO.") VALUES($longitude,$latitude,$distancia,$idObjeto);";
		$conn->query($sql);

	    $conn->close();
	} else {

		echo "Falta algum campo obrigatorio.";

	}
?>
