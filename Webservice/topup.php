<?php
	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");
		
	mysqli_autocommit($con,FALSE);
	
	$sql = "select concat('T',lpad(ifnull(max(mid(id_topup,2,length(id_topup)-1)),0)+1,9,0)) id_topup from topup";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));
	$row = mysqli_fetch_array($result);
	
	$sql = "
		INSERT INTO `topup`(`id_topup`, `user_input`, `tgl_input`, `nominal`, `flag`) VALUES 
		('".post($con, $row['id_topup'])."','".post($con, $_POST['id_account'])."','".$tgl."',".post($con, $_POST['nominal']).",'".post($con, 1)."')
	";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));

	$sql = "
		UPDATE `account` 
		SET saldo = saldo + ".post($con, $_POST['nominal'])."
		WHERE `id_account`='".post($con, $_POST['id_account'])."'";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));
	$response['error'] = false;
	$response['message'] = 'Berhasil Update';	
	
	if(empty($response)){
		$response['error'] = true;
		$response['message'] = 'Harap coba kembali';	
		mysqli_rollback($con);
	}else{
		mysqli_commit($con);
	}	
	mysqli_close($con);
	echo json_encode($response);