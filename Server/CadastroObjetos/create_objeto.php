
<?php
	include '../defines.php';
	include '../resizeImage.php';
	if (isset($_POST['nome']) && isset($_POST['frase']) && isset($_POST['local_id']) && isset($_POST['latitude']) && isset($_POST['longitude'])){
		$name = utf8_decode($_POST['nome']);
        $frase = utf8_decode($_POST['frase']);
        $local_id = $_POST['local_id'];
        $latitude = $_POST['latitude'];
        $longitude = $_POST['longitude'];

		$image_path = "ObjectImages/Normal/" . basename($_FILES['image']['name']);
        $image_path_gray = "ObjectImages/Gray/" . basename($_FILES['image']['name']);
        $image_path_resized = "ObjectImages/NormalResized/" . basename($_FILES['image']['name']);
        $image_path_resized_gray = "ObjectImages/GrayResized/" . basename($_FILES['image']['name']);

        $image = $_FILES['image']['tmp_name'];

		// criando conexão
        $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);
        
        // Checando conexão
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }

        $sql = "INSERT INTO " . DB_TABLE_OBJETO . "(".LOCAL_ID.", ".OBJECT_NAME.", ".FRASE.", ".LATITUDE.", ".LONGITUDE.", ".IMAGE_PATH.", ".IMAGE_PATH_RESIZED.", ".IMAGE_PATH_RESIZED_GRAY.") 
                            VALUES('$local_id', '$name', '$frase', '$latitude', '$longitude', 
                                   '$image_path', '$image_path_resized', '$image_path_resized_gray')";
     
        // checando se a inserção aconteceu
        if ($conn->query($sql) ) {
            echo "Objeto criado com sucesso.";
        } else {
            echo "Oops! Um erro ocorreu.".$conn->error;;
        }
        $conn->close();

		try {
		    //throw exception if can't move the file
		    if (!move_uploaded_file($image, $image_path) ) { //&& !move_uploaded_file($image_gray, $image_path_gray) 
		        throw new Exception('Could not move file');
		    }
            //para redimensionar a imagem
            smart_resize_image($image_path, null, MAX_RESIZE_WIDTH, MAX_RESIZE_HEIGHT, true, $image_path_resized, false, true, 100);
            //para converter para gray scale e redimensionar a imagem
            $im = imagecreatefromjpeg($image_path);
            imagefilter($im, IMG_FILTER_GRAYSCALE);
            imagejpeg($im, $image_path_gray);
            smart_resize_image($image_path_gray, null, 300, 300, true, $image_path_resized_gray, false, true, 100);
		    echo "The file " . basename($_FILES['image']['name']) .
		    " has been uploaded";
		} catch (Exception $e) {
		    die('File did not upload: ' . $e->getMessage());
		}

	}else{
		echo "Falta algum campo obrigatorio";
	}
?>
