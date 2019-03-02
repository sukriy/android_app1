<?php
	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");

  $sql = "
    select flag
    from pesan
    where user_input = '".post($con, $_POST['id_account'])."' and flag<>3
    limit 1";
	$result=mysqli_query($con,$sql) or die(mysqli_error($con));
  $row = mysqli_fetch_assoc($result);

	$txt = ' ';
  if($row['flag'] == 1){
    $sql = "
      select posisi
      from (
          select @i:=@i+1 as posisi, user_input, id_pesan
          from pesan, (select @i:=0) posisi
          where flag=1
          order by tgl_input asc
      )t
      where user_input = '".post($con, $_POST['id_account'])."'
    ";
    $result=mysqli_query($con,$sql) or die(mysqli_error($con));
    $row = mysqli_fetch_assoc($result);
    $txt = 'Waiting list ke - '.$row['posisi'];
  }else if($row['flag'] == 2){
    $txt = "Pesanan dalam tahap proses";
  }else{

	}
  $response['error'] = false;
  $response['message'] = $txt;

  mysqli_close($con);
  echo json_encode($response);
