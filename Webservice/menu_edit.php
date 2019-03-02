<?php
	require('konek.php');
	$response = array(
		'error' => true,
		'message' => 'Harap Coba kembali'
	);
	$array = array('Makanan','Minuman','Dessert');
	
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		if(empty($_POST['nama'])){
			$response['error'] = true;
			$response['message'] = 'Nama Menu tidak boleh kosong';
		}else if(empty($_POST['harga'])){
			$response['error'] = true;
			$response['message'] = 'Harga tidak boleh kosong';
		}else if(empty($_POST['jenis'])){
			$response['error'] = true;
			$response['message'] = 'Jenis tidak boleh kosong';
		}else if(byk(255, $_POST['nama'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Nama 255';
		}else if(byk(11, $_POST['harga'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Harga 11';
		}else if(byk(1000, $_POST['keterangan'])){
			$response['error'] = true;
			$response['message'] = 'Panjang max Keterangan 1000';
		}else if(!in_array($_POST['jenis'], $array)){
			$response['error'] = true;
			$response['message'] = 'Jenis tidak ditemukan';			
		}else{
			$sql = "select id_menu from menu where id_menu<>'".post($con, explode(" ",$_POST['id_menu'])[2])."' and nama = '".post($con, $_POST['nama'])."'";
			$result=mysqli_query($con,$sql) or die(mysqli_error($con));
			$ada = mysqli_num_rows($result);
			if($ada == 0){
				if(isset($_POST['image_data'])){
					$ImageData = $_POST['image_data'];
					$ImageName = post($con, explode(" ",$_POST['id_menu'])[2]).".jpg";
					$ImagePath = "uploads/menu/".$ImageName;
					file_put_contents($ImagePath,base64_decode($ImageData));				

					$sql = "
						UPDATE `menu` SET `nama`='".post($con, $_POST['nama'])."',`harga`='".post($con, $_POST['harga'])."',
						`keterangan`='".post($con, $_POST['keterangan'])."',`jenis`='".post($con, $_POST['jenis'])."',
						`gambar`='".$ImageName."'
						WHERE `id_menu`='".post($con, explode(" ",$_POST['id_menu'])[2])."'";
				}else{
					$sql = "
						UPDATE `menu` SET `nama`='".post($con, $_POST['nama'])."',`harga`='".post($con, $_POST['harga'])."',
						`keterangan`='".post($con, $_POST['keterangan'])."',`jenis`='".post($con, $_POST['jenis'])."'
						WHERE `id_menu`='".post($con, explode(" ",$_POST['id_menu'])[2])."'";
				}
				mysqli_query($con,$sql) or die(mysqli_error($con));
				$response['error'] = false;
				$response['message'] = 'Berhasil Edit';
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