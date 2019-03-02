<?php
	require('konek.php');
	$response = array( 
		'error' => true,
		'message' => 'Harap Coba kembali'
	);
	if(isset($_POST['id_menu'])){
		$sql = "Select * from menu where id_menu='".post($con, $_POST['id_menu'])."'";
		$result=mysqli_query($con,$sql) or die(mysqli_error($con));
		
		if(mysqli_num_rows($result)==1){
			$response['error'] = false;
			$response['message'] = mysqli_fetch_array($result);
		}else{
			$response['error'] = true;
			$response['message'] = "Data Tidak ditemukan";
		}		
	}else{
		$response['error'] = true;
		$response['message'] = "Invalid Parameter";
	}
	mysqli_close($con);
	echo json_encode($response);
