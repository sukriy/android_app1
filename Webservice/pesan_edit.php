<?php
	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");

	mysqli_autocommit($con,FALSE);

  $sql = "
    UPDATE `account`
    SET saldo = saldo + (select sum(total) from pesan_detail where id_pesan = '".post($con, $_POST['id_pesan'])."')
    WHERE id_account<>'a000000003' and `id_account`=(select user_input from pesan where id_pesan = '".post($con, $_POST['id_pesan'])."')
    ";
		// echo $sql;
    $result=mysqli_query($con, $sql) or die(mysqli_error($con));

    $sql = "DELETE from pesan_detail WHERE id_pesan = '".post($con, $_POST['id_pesan'])."'";
		// echo $sql;
    $result=mysqli_query($con, $sql) or die(mysqli_error($con));

    $data = json_decode($_POST['data']);
    $total = 0;
    foreach ($data as $key => $value) {
        $temp = (str_replace(',', '', $value->harga) * str_replace(',', '', $value->jmlh));
        $total += $temp;
        $sql = "
          INSERT INTO `pesan_detail`(`id_pesan`, `id_menu`, `harga`, `qty`, `total`) VALUES (
          '".post($con, $_POST['id_pesan'])."','".post($con, $value->id_menu)."','".post($con, $value->harga)."',
          '".post($con, $value->jmlh)."','".post($con, $temp)."')
        ";
				// echo $sql;
		    $result=mysqli_query($con, $sql) or die(mysqli_error($con));
    }

  $sql = "
    UPDATE pesan set
    total = (select sum(total) from pesan_detail where id_pesan = '".post($con, $_POST['id_pesan'])."')
    where id_pesan = '".post($con, $_POST['id_pesan'])."'
  ";
	// echo $sql;
	$result=mysqli_query($con, $sql) or die(mysqli_error($con));

  $sql = "
    UPDATE `account`
    SET saldo = saldo - (select sum(total) from pesan_detail where id_pesan = '".post($con, $_POST['id_pesan'])."')
    WHERE id_account<>'a000000003' and `id_account`=(select user_input from pesan where id_pesan = '".post($con, $_POST['id_pesan'])."')
    ";
		// echo $sql;
    $result=mysqli_query($con, $sql) or die(mysqli_error($con));

  $sql = "
    select saldo
    from account 
		WHERE `id_account`=(select user_input from pesan where id_pesan = '".post($con, $_POST['id_pesan'])."')";
  $result=mysqli_query($con, $sql) or die(mysqli_error($con));
  $check = mysqli_fetch_assoc($result);

  if ($check['saldo'] < 0) {
      $response['error'] = true;
      $response['message'] = 'Saldo tidak cukup';
      mysqli_rollback($con);
  }else{
    $response['error'] = false;
    $response['message'] = 'Berhasil Pesan';
    mysqli_commit($con);
  }

  echo json_encode($response);
  exit();
