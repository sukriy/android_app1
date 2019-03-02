<?php
	require('konek.php');
	$response = array( 
		'error' => true,
		'message' => 'Harap Coba kembali'
	);
	
	if(isset($_POST['id_account'])){
		$sql = "delete from account where id_account = '".post($con, $_POST['id_account'])."'";
		$result=mysqli_query($con,$sql);

		$response['error'] = false;
		$response['message'] = 'Berhasil';
	}
	
	mysqli_close($con);		
	echo json_encode($response);
