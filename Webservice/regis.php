<?php
	require('konek.php');
	$response = array(
		'error' => true,
		'message' => 'Harap Coba kembali'
	);
	
	if(isset($_POST['username']) || isset($_POST['password']) || isset($_POST['email'])){
		$sql = "select id_account from account where username = '".post($con, $_POST['username'])."' or email = '".post($con, $_POST['email'])."'";
		$result=mysqli_query($con,$sql);
		$ada = mysqli_num_rows($result);
		if($ada == 0){
			$sql = "select concat('A',lpad(ifnull(max(mid(id_account,2,length(id_account)-1)),0)+1,9,0)) id_account from account";
			$result=mysqli_query($con,$sql);
			$row = mysqli_fetch_array($result);
			
			$sql = "
				INSERT INTO `account`(`id_account`, `nama_lengkap`, `username`, `password`, `level`, `alamat`, `notlpn`, `email`, `gambar`, `saldo`) VALUES (
				'".post($con, $row['id_account'])."','".post($con, $_POST['nama_lengkap'])."','".post($con, $_POST['username'])."','".post($con, $_POST['password'])."',
				'".post($con, 'Pelanggan')."','".post($con, $_POST['alamat'])."','".post($con, $_POST['notlpn'])."','".post($con, $_POST['email'])."','',0)
			"; 
			if(mysqli_query($con,$sql)){
				$response['error'] = false;
				$response['message'] = $row['id_account'];
			}else{
				$response['error'] = true;
				$response['message'] = 'Terjadi Kesalahan';
			}				
		}else{
			$response['error'] = true;
			$response['message'] = 'Username / Email sudah ada';
		}
	}else{
		$response['error'] = true;
		$response['message'] = 'Invalid Request';
	}
	mysqli_close($con);
	echo json_encode($response);

?>