<?php
	require('konek.php');
	$response = array(
		'error' => true,
		'message' => 'Harap Coba kembali'
	);
	$array = array('Pelanggan','Staff','Manager');
	
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		if(empty($_POST['nama_lengkap'])){
			$response['error'] = true;
			$response['message'] = 'Nama Lengkap tidak boleh kosong';
		}else if(empty($_POST['username'])){
			$response['error'] = true;
			$response['message'] = 'Username tidak boleh kosong';
		}else if(empty($_POST['password'])){
			$response['error'] = true;
			$response['message'] = 'Password tidak boleh kosong';
		}else if(empty($_POST['email'])){
			$response['error'] = true;
			$response['message'] = 'Email tidak boleh kosong';
		}else if($_POST['password'] != $_POST['password0']){
			$response['error'] = true;
			$response['message'] = 'Password not matched';
		}else if(!filter_var($_POST['email'], FILTER_VALIDATE_EMAIL)) {
			$response['error'] = true;
			$response['message'] = 'Email tidak valid';
		}else if(byk(255, $_POST['nama_lengkap'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Nama Lengkap 255';
		}else if(byk(255, $_POST['username'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Username 255';
		}else if(byk(255, $_POST['password'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Password 255';
		}else if(byk(255, $_POST['alamat'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Alamat 255';
		}else if(byk(255, $_POST['notlpn'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Notlpn 255';
		}else if(byk(255, $_POST['email'])){ 
			$response['error'] = true;
			$response['message'] = 'Panjang max Email 255';
		}else if(!in_array($_POST['level'], $array)){
			$response['error'] = true;
			$response['message'] = 'Level tidak ditemukan';			
		}else{
			$sql = "select id_account from account where username = '".post($con, $_POST['username'])."' or email = '".post($con, $_POST['email'])."'";
			$result=mysqli_query($con,$sql) or die(mysqli_error($con));			
			$ada = mysqli_num_rows($result);
			if($ada == 0){
				$sql = "select concat('A',lpad(ifnull(max(mid(id_account,2,length(id_account)-1)),0)+1,9,0)) id_account from account";
				$result=mysqli_query($con,$sql) or die(mysqli_error($con));
				$row = mysqli_fetch_array($result);

				if(isset($_POST['image_data'])){
					$ImageData = $_POST['image_data'];
					$ImageName = post($con, $row['id_account']).".jpg";
					$ImagePath = "uploads/account/".$ImageName;
					file_put_contents($ImagePath,base64_decode($ImageData));				
				}else{
					$ImageName = "";
				}
				
				$sql = "
					INSERT INTO `account`(`id_account`, `nama_lengkap`, `username`, `password`, `level`, `alamat`, `notlpn`, `email`, `gambar`, `saldo`) VALUES (
					'".post($con, $row['id_account'])."','".post($con, $_POST['nama_lengkap'])."','".post($con, $_POST['username'])."','".post($con, $_POST['password'])."',
					'".post($con, $_POST['level'])."','".post($con, $_POST['alamat'])."','".post($con, $_POST['notlpn'])."','".post($con, $_POST['email'])."','".$ImageName."',0)
				"; 
				$result=mysqli_query($con,$sql) or die(mysqli_error($con));
				$response['error'] = false;
				$response['message'] = 'Berhasil Input';
			}else{
				$response['error'] = true;
				$response['message'] = 'Username / Email sudah ada';
			}
		}
	}else{
		$response['error'] = true;
		$response['message'] = 'Invalid Request';
	}
	mysqli_close($con) or die(mysqli_error($con));
	echo json_encode($response);

?>