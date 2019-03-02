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
			$sql = "select id_menu from menu where nama = '".post($con, $_POST['nama'])."'";
			$result=mysqli_query($con,$sql) or die(mysqli_error($con));			
			$ada = mysqli_num_rows($result);
			if($ada == 0){
				$sql = "select concat('M',lpad(ifnull(max(mid(id_menu,2,length(id_menu)-1)),0)+1,9,0)) id_menu from menu";
				$result=mysqli_query($con,$sql) or die(mysqli_error($con));
				$row = mysqli_fetch_array($result);

				if(isset($_POST['image_data'])){
					$ImageData = $_POST['image_data'];
					$ImageName = post($con, $row['id_menu']).".jpg";
					$ImagePath = "uploads/menu/".$ImageName;
					file_put_contents($ImagePath,base64_decode($ImageData));				
				}else{
					$ImageName = "";
				}
				
				$sql = "
					INSERT INTO `menu`(`id_menu`, `nama`, `harga`, `keterangan`, `gambar`, `jenis`) VALUES 
					('".post($con, $row['id_menu'])."','".post($con, $_POST['nama'])."','".post($con, $_POST['harga'])."','".post($con, $_POST['keterangan'])."',
					'".$ImageName."','".post($con, $_POST['jenis'])."')
				"; 
				$result=mysqli_query($con,$sql) or die(mysqli_error($con));
				$response['error'] = false;
				$response['message'] = 'Berhasil Input';
			}else{
				$response['error'] = true;
				$response['message'] = 'Nama menu sudah ada';
			}
		}
	}else{
		$response['error'] = true;
		$response['message'] = 'Invalid Request';
	}
	mysqli_close($con) or die(mysqli_error($con));
	echo json_encode($response);

?>