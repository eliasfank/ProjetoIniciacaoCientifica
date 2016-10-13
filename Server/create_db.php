<?php

    include 'defines.php';

    $SCRIPT_CREATE_TABLE_USER =
                "CREATE TABLE IF NOT EXISTS " . DB_TABLE_USER . " ("
                        . KEY_ID . " INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                        . USER_EMAIL . " VARCHAR(120) NOT NULL,"
                        . USER_PASS . " VARCHAR(120) NOT NULL"
                        . ")CHARACTER SET utf8 COLLATE utf8_general_ci;";

    $SCRIPT_CREATE_TABLE_LOCAL =
                "CREATE TABLE IF NOT EXISTS " . DB_TABLE_LOCAL . " ("
                        . KEY_ID . " INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                        . LOCAL_NAME . " VARCHAR(120) NOT NULL, "
                        . CREATOR_ID . " INT(10) UNSIGNED NOT NULL"
                        . ")CHARACTER SET utf8 COLLATE utf8_general_ci;";

    $SCRIPT_CREATE_TABLE_OBJETO =
                "CREATE TABLE IF NOT EXISTS " . DB_TABLE_OBJETO . " ("
                        . KEY_ID . " INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                        . LOCAL_ID . " INT(10) UNSIGNED NOT NULL, "
                        . OBJECT_NAME . " VARCHAR(120) NOT NULL,"
                        . FRASE . " VARCHAR(120) NOT NULL,"
                        . LATITUDE . " VARCHAR(120) NOT NULL,"
                        . LONGITUDE . " VARCHAR(120) NOT NULL,"
                        . IMAGE_PATH . " VARCHAR(120) NOT NULL,"
                        . IMAGE_PATH_GRAY . " VARCHAR(120) NOT NULL,"
                        . IMAGE_PATH_RESIZED . " VARCHAR(120) NOT NULL,"
                        . IMAGE_PATH_RESIZED_GRAY . " VARCHAR(120) NOT NULL"
                        . ")CHARACTER SET utf8 COLLATE utf8_general_ci;";

    $SCRIPT_CREATE_TABLE_RESULTADO =
                "CREATE TABLE IF NOT EXISTS " . DB_TABLE_RESULTADO . " ("
                        . KEY_ID . " INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY, "
                        . LON_USER . " VARCHAR(120) NOT NULL,"
                        . LAT_USER . " VARCHAR(120) NOT NULL,"
                        . DISTANCIA_METROS . " DOUBLE NOT NULL,"
                        . RESULTADO . " INT(10) UNSIGNED,"
                        . SCRIPT . " MEDIUMTEXT"
                        . ")CHARACTER SET utf8 COLLATE utf8_general_ci;";

                        /*CREATE TABLE IF NOT EXISTS tabela_resultado (
                        	chave INT(10) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                        	lon_user VARCHAR(120) NOT NULL,
                        	lat_user VARCHAR(120) NOT NULL,
                        	distancia_metros DOUBLE NOT NULL,
                        	resultado INT(10) UNSIGNED,
                        	script MEDIUMTEXT)
                        CHARACTER SET utf8 COLLATE utf8_general_ci;
						*/

    //Conectando ao servidor e criando base
    $conn = new mysqli(SERVERNAME, USERNAME, PASS);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 
    $sql = "CREATE DATABASE IF NOT EXISTS ". DB_NAME ." DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;";
    if ($conn->query($sql) === TRUE) {
        echo "Database created successfully <br>";
    } else {
        echo "Error creating database: " . $conn->error;
    }

    // Conexão a base de dados
    $conn = new mysqli(SERVERNAME, USERNAME, PASS, DB_NAME);
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    } 

    //Deletando as tabelas
    if (!$conn->query("DROP TABLE IF EXISTS ".DB_TABLE_USER) ||
        !$conn->query("DROP TABLE IF EXISTS ".DB_TABLE_LOCAL) ||
        !$conn->query("DROP TABLE IF EXISTS ".DB_TABLE_OBJETO) ||
        !$conn->query("DROP TABLE IF EXISTS ".DB_TABLE_RESULTADO)){
        echo "Error deleting tables: " . $conn->error;
    }else{
        echo "Table deleted successfully <br>";
    }

    //Criação das tabelas
    if ($conn->query($SCRIPT_CREATE_TABLE_USER) === TRUE) {
        echo "User table created successfully <br>";
    } else {
        echo "Error creating table: " . $conn->error;
    }
    if ($conn->query($SCRIPT_CREATE_TABLE_LOCAL) === TRUE) {
        echo "Local table created successfully <br>";
    } else {
        echo "Error creating table: " . $conn->error;
    }
    if ($conn->query($SCRIPT_CREATE_TABLE_OBJETO) === TRUE) {
        echo "Object table created successfully <br>";
    } else {
        echo "Error creating table: " . $conn->error;
    }
    if ($conn->query($SCRIPT_CREATE_TABLE_RESULTADO) === TRUE) {
        echo "Resultado table created successfully <br>";
    } else {
        echo "Error creating table: " . $conn->error;
    }

    $conn->close();
    
    //Limpando as pastas de imagens
    /*
    removePasta("CadastroObjetos/ObjectImages/Normal/");
    removePasta("CadastroObjetos/ObjectImages/NormalResized/");
    removePasta("CadastroObjetos/ObjectImages/Gray/");
    removePasta("CadastroObjetos/ObjectImages/GrayResized/");
    removePasta("Recognizer/RecievedImages/Normal/");
    removePasta("Recognizer/RecievedImages/NormalResized/");
    removePasta("Recognizer/RecievedImages/Gray/");
    removePasta("Recognizer/RecievedImages/GrayResized/");
    function removePasta($pasta){
		foreach (glob($pasta."*.jpg") as $filename) {
		   unlink($filename);
		}
	}
	echo "Imagens removidas das pastas";
	*/
?>
