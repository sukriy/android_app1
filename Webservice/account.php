<?php
	require('konek.php');
	$response = array(
		'error' => true,
		'message' => 'Harap Coba kembali'
	);

	$sql = "Select * from account";
	$result=mysqli_query($con,$sql);

	$nilai = array();
	while($row = mysqli_fetch_array($result)){
		$temp = array(
			"id_account"	=> $row['id_account'],
			"username"		=> ucwords($row['username']),
			"gambar"		=> $row['gambar'],
			"level"		=> $row['level']
		);
		$nilai[] = $temp;
	}
	$response['error'] = false;
	$response['message'] = $nilai;

	mysqli_close($con);
	echo json_encode($response);
