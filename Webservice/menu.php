<?php
	require('konek.php');
	$response = array(
		'error' => true,
		'message' => 'Harap Coba kembali'
	);

	$sql = "
	select menu.*
	from menu
	";
	$result=mysqli_query($con,$sql);

	$nilai = array();
	while($row = mysqli_fetch_array($result)){
		$temp = array(
			"id_menu"	=> $row['id_menu'],
			"nama"		=> ucwords($row['nama']),
			"gambar"	=> $row['gambar'],
			"jenis"		=> $row['jenis'],
			"harga"		=> $row['harga'],
			"jumlah"	=> 0
		);
		$nilai[] = $temp;
	}
	$response['error'] = false;
	$response['message'] = $nilai;

	mysqli_close($con);
	echo json_encode($response);
