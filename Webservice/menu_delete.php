<?php
	require('konek.php');
	$response = array( 
		'error' => true,
		'message' => 'Harap Coba kembali'
	);
	
	if(isset($_POST['id_menu'])){
		$sql = "delete from menu where id_menu = '".post($con, $_POST['id_menu'])."'";
		$result=mysqli_query($con,$sql);

		$response['error'] = false;
		$response['message'] = 'Berhasil';
	}
	
	mysqli_close($con);		
	echo json_encode($response);
