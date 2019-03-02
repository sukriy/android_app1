<?php
	require('konek.php');
	
	if(isset($_POST['username']) || isset($_POST['password'])){
		$sql = "
			select * 
			from account 
			where username = '".post($con, $_POST['username'])."' and password = '".post($con, $_POST['password'])."'
		";
		$result=mysqli_query($con,$sql) or die(mysqli_error($con));
		$rowcount=mysqli_num_rows($result);
		$row = mysqli_fetch_array($result);
		
		if($rowcount == 1){
			$response['error'] = false;
			$response['message'] = $row;
		}else{
			$response['error'] = true;
			$response['message'] = 'Account tidak terdaftar';
		}		
	}else{
		$response['error'] = true;
		$response['message'] = 'Parameter not valid';
	}

	mysqli_close($con);
	echo json_encode($response);
