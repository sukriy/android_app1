<?php

	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");

	mysqli_autocommit($con,FALSE);

  $sql = "
    update account set
    saldo = saldo + (select total from pesan where id_pesan = '".$_POST['id_order']."')
		WHERE id_account<>'a000000003' and `id_account`=(select user_input from pesan where id_pesan = '".post($con, $_POST['id_order'])."')
  ";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));

  $sql = "delete from pesan where id_pesan = '".$_POST['id_order']."'";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));

  $sql = "delete from pesan_detail where id_pesan = '".$_POST['id_order']."'";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));

	$response['error'] = false;
	$response['message'] = "Berhasil Hapus";

	if(empty($response)){
		$response['error'] = true;
		$response['message'] = 'Harap coba kembali';
		mysqli_rollback($con);
	}else{
		mysqli_commit($con);
	}

	mysqli_close($con);
	echo json_encode($response);
