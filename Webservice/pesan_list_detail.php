<?php
	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");

	mysqli_autocommit($con,FALSE);

	$sql = "
		select pesan.*, pesan_detail.*, ucase(menu.nama) nama, account.nama_lengkap
		from pesan
		left join pesan_detail on pesan.id_pesan = pesan_detail.id_pesan
		left join menu on menu.id_menu = pesan_detail.id_menu
		left join account on account.id_account = pesan.user_input
		where pesan.id_pesan = '".$_POST['id_pesan']."'
	";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));

	$nilai = array();
	while($row = mysqli_fetch_array($result, MYSQLI_ASSOC)){
		$nilai[] = $row;
	}
	$response['error'] = false;
	$response['message'] = $nilai;

	if(empty($response)){
		$response['error'] = true;
		$response['message'] = 'Harap coba kembali';
		mysqli_rollback($con);
	}else{
		mysqli_commit($con);
	}

	mysqli_close($con);
	echo json_encode($response);
