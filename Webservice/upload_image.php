<?php
require('konek.php');

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	$DefaultId = 0;
	$ImageData = $_POST['image_data'];
	$ImageName = $_POST['image_tag'];
	$ImagePath = "uploads/$ImageName.jpg";
	$ServerURL = "shadi/$ImagePath";
	$InsertSQL = "INSERT INTO imageupload (image_path,image_name) values('$ServerURL','$ImageName')";

	if(mysqli_query($con, $InsertSQL)){
		file_put_contents($ImagePath,base64_decode($ImageData));
		echo "Your Image Has Been Uploaded.";
	}

	mysqli_close($con);
}else{
	echo "Please Try Again";
}

?>