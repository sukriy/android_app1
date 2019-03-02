<?php
	require_once('konek.php');

	$sql = "Select * from account";
	$result=mysqli_query($con,$sql) or die('masalah');

	$nilai = array();
	while($row = mysqli_fetch_array($result)){
		array_push($nilai, array(
			"id_account"	=> $row['id_account'],
			"username"		=> $row['username']
		));
	}
	pre(json_encode(array('nilai'=>$nilai)));

	mysqli_close($con);