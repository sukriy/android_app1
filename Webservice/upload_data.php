<?php
	require('konek.php');
	date_default_timezone_set("Asia/Bangkok");
	$tgl=date("Y-m-d H:i:s");

	foreach($_GET as $key=>$value){
		if(abs(floatval($value)) > 0.15){
			$flag = 1;
		}else {
			$flag = 0;
		}
		$sql = "
		INSERT INTO `sensor_data`
		(`id_sensor`, `tgl_input`, `nilai`, `posisi`) VALUES
		('".post($con, $key)."','".$tgl."','".post($con, $value)."','".post($con, $flag)."')";
		// echo $sql.'<br>';
		$result=mysqli_query($con,$sql) or die(mysqli_error($con));
	}
	echo 'success';
	exit();
