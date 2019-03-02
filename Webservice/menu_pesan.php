<?php
	require('konek.php');
	$response = array(
		'error' => true,
		'message' => 'Harap Coba kembali'
	);

  if($_POST['action'] == 'edit'){
		$sql = "
		select menu.*, ifnull(pesan_detail.qty,0) jmlh
		from menu
		left join pesan_detail on pesan_detail.id_menu=menu.id_menu and pesan_detail.id_pesan='".$_POST['id_pesan']."'
		";
  }else{
		$sql = "
		select menu.*, 0 jmlh
		from menu
		";
	}
	$result=mysqli_query($con,$sql);

	$nilai = array();

	while($row = mysqli_fetch_array($result)){
		$temp = array(
			"id_menu"	=> $row['id_menu'],
			"nama"		=> ucwords($row['nama']),
			"gambar"	=> $row['gambar'],
			"jenis"		=> $row['jenis'],
			"harga"		=> $row['harga'],
			"jumlah"	=> $row['jmlh']
		);
		$nilai[] = $temp;
	}
	$response['error'] = false;
	$response['message'] = $nilai;

	mysqli_close($con);
	echo json_encode($response);
