<?php
	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");

	mysqli_autocommit($con,FALSE);

	$sql = "
	select *, account.nama_lengkap
	from pesan
	left join account on account.id_account=pesan.user_input
	where
	case when 'pelanggan' = (select level from account where id_account='".post($con, $_POST['id_account'])."')
	then pesan.user_input = '".post($con, $_POST['id_account'])."'
	else 1 end
	order by tgl_input desc
	";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));

	$nilai = array();
	while($row = mysqli_fetch_array($result)){
		$temp = array(
			"id_pesan"		=> $row['id_pesan'],
			"nama_lengkap"		=> $row['nama_lengkap'],
			"tgl_input"		=> $row['tgl_input'],
			"total"			=> $row['total'],
			"pembayaran"	=> $row['pembayaran'],
			"makan"			=> $row['makan']
		);
		$nilai[] = $temp;
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
