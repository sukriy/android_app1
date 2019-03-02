<?php
require('konek.php');
date_default_timezone_set("Asia/Bangkok");
$tgl=date("Y-m-d H:i:s");

mysqli_autocommit($con,FALSE);

$sql = "select concat('P',lpad(ifnull(max(mid(id_pesan,2,length(id_pesan)-1)),0)+1,9,0)) id_pesan from pesan";
$result=mysqli_query($con, $sql) or die('test1 = '.mysqli_error($con));
$row = mysqli_fetch_assoc($result);

$data = json_decode($_POST['data']);
$total = 0;

foreach ($data as $key => $value) {
    $temp = (str_replace(',', '', $value->harga) * str_replace(',', '', $value->jmlh));
    $total += $temp;
    $sql = "
      INSERT INTO `pesan_detail`(`id_pesan`, `id_menu`, `harga`, `qty`, `total`) VALUES (
      '".post($con, $row['id_pesan'])."','".post($con, $value->id_menu)."','".post($con, $value->harga)."',
      '".post($con, $value->jmlh)."','".post($con, $temp)."')
    ";
    // echo $sql;
    $result=mysqli_query($con, $sql) or die('test2 = '.mysqli_error($con));
}

$tgl_dpt_meja='';
if($_POST['meja'] == '' && $_POST['jenis'] != 'take away'){
  $sql = "
    select
    case when meja = 'meja_2' then 2
    when meja = 'meja_1' then 1
    else 0
    end meja, tgl_input, id_pesan, 1 posisi
    from pesan
    where flag <> 3 and makan = 'dine in'
    union
    select *
    from (
        SELECT `id_sensor`, max(tgl_input) tgl_input, `nilai`, `posisi`
        FROM `sensor_data`
        group by id_sensor
    )t0
    where id_sensor not in (
        select
        case when meja = 'meja_2' then 2
        when meja = 'meja_1' then 1
        else 0 end
        from pesan
        where flag <> 3
    )";
  $result=mysqli_query($con, $sql) or die('test3 = '.mysqli_error($con));

  $temp = array();
  while ($row0 = mysqli_fetch_assoc($result)) {
      if($row0['posisi'] == 0){
        $_POST['meja'] = 'meja_'.$row0['meja'];
        $tgl_dpt_meja = $tgl;
        break;
      }
  }
}else if($_POST['meja'] != ''){
  $tgl_dpt_meja = $tgl;
}

if($tgl_dpt_meja == ''){
  $flag = 1;
  $tgl_dpt_meja = '0000-00-00 00:00:00';
}else {
  $flag = 2;
}
$sql = "
  INSERT INTO `pesan`(`id_pesan`, `tgl_input`, `user_input`, `tgl_dpt_meja`, `pembayaran`, `total`, `makan`, `meja`, `flag`) VALUES
  (
  '".post($con, $row['id_pesan'])."','".$tgl."','".post($con, $_POST['id_account'])."','".post($con, $tgl_dpt_meja)."',
  '".post($con, $_POST['pembayaran'])."','".post($con, $total)."','".post($con, $_POST['jenis'])."','".post($con, $_POST['meja'])."','".post($con, $flag)."'
  )
";
// echo $sql;
$result=mysqli_query($con, $sql) or die('test4 = '.$sql);

if (strtolower($_POST['pembayaran']) == 'saldo') {
    $sql = "
      UPDATE `account`
      SET saldo = saldo - ".post($con, $total)."
      WHERE id_account<>'a000000003' and `id_account`='".post($con, $_POST['id_account'])."'
      ";
      // echo $sql;
      $result=mysqli_query($con, $sql) or die('test5 = '.mysqli_error($con));

    $sql = "
      select saldo
      from account
      where id_account = '".post($con, $_POST['id_account'])."'
    ";
    $result=mysqli_query($con, $sql) or die('test6 = '.mysqli_error($con));
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
}else{
  $response['error'] = false;
  $response['message'] = 'Berhasil Pesan';
  mysqli_commit($con);
}
echo json_encode($response);
exit();
