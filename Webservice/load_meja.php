<?php
    require('konek.php');

    $mapping = "
      select sensor.id_sensor meja,
      case
          when (posisi_sensor = 1) then 1
      	when tgl_dpt_meja is null then 0
      else
      	case when abs(TIMESTAMPDIFF(MINUTE, tgl_dpt_meja, addtime(now(), '07:00:00'))) < 5 THEN 1
          else 0
          end
      end posisi
      FROM
      (
        SELECT `id_sensor`, tgl_input, `nilai`, `posisi` posisi_sensor
        FROM `sensor_data`
        where tgl_input = (select max(tgl_input) from sensor_data)
      )sensor
      left join pesan on REPLACE(pesan.meja,'meja_','') = sensor.id_sensor and pesan.flag = 2 and pesan.makan = 'dine in'
    ";

    //update pesan proses menjadi selesai
    $sql = "
      select count(id_pesan) byk
      from pesan
      inner join (".$mapping.")mapping on concat('meja_',mapping.meja)=pesan.meja and mapping.posisi=0
      WHERE flag = 2;
    ";
    $result=mysqli_query($con, $sql) or die('test = '.mysqli_error($con));
    $row = mysqli_fetch_assoc($result);
    if($row['byk'] > 0){
      $sql = "
        UPDATE pesan
        inner join (".$mapping.")mapping on concat('meja_',mapping.meja)=pesan.meja and mapping.posisi=0
        SET pesan.meja = concat('meja_',mapping.meja), flag = 3
        WHERE flag = 2;
      ";
      $result=mysqli_query($con, $sql) or die('test = '.mysqli_error($con));
    }

    //update waiting list menjadi proses
    $sql = "select * from (".$mapping.")mapping where posisi = 0";
    $result=mysqli_query($con, $sql) or die(mysqli_error($con));
    while ($row = mysqli_fetch_assoc($result)) {
        $sql = "
          select count(id_pesan) byk
          from pesan
          where flag = 1
        ";
        $result=mysqli_query($con, $sql) or die('test = '.mysqli_error($con));
        $row = mysqli_fetch_assoc($result);

        if($row['byk'] > 0){
          $sql = "SET @meja = (select concat('meja_',meja) from (".$mapping.")mapping where posisi = 0 limit 1);";
          $result0=mysqli_query($con, $sql) or die('test2 = '.mysqli_error($con));

          $sql = "
            update pesan
            set meja = @meja, tgl_dpt_meja = addtime(now(), '07:00:00'), flag = 2
            where flag = 1 and @meja<>''
            order by pesan.tgl_dpt_meja asc
            limit 1
          ";
          $result0=mysqli_query($con, $sql) or die('test3 = '.mysqli_error($con));
        }
    }

    $sql = "select * from (".$mapping.")mapping order by meja asc";
    $result=mysqli_query($con, $sql) or die('test4 = '.mysqli_error($con));

    $temp = array();
    while ($row = mysqli_fetch_assoc($result)) {
        $temp[] = $row;
    }

    $response['error'] = false;
    $response['message'] = $temp;

    echo json_encode($response);
    mysqli_close($con);
