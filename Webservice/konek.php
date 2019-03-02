<?php
	$servername = "localhost";
	$username = "id8119546_sa";
	$password = "tester1234";
	$db = "id8119546_sa";
	$base_url = "http://prores.000webhostapp.com/";

	$con = mysqli_connect($servername, $username, $password, $db) or die("Connection failed: ".mysqli_connect_error());

	function post($con, $str){
		if(is_numeric(str_replace('%','',$str)))
			$str = str_replace('%','',$str);
		return mysqli_real_escape_string($con, strtolower(trim($str)));
	}
	function byk($byk, $str){
		if($str == "") return false;
		if(strlen(trim($str)) > $byk)
			return true;
		else
			return false;
	}
	function cetak_tglwkt($str){
		if($str=='0000-00-00 00:00:00' || $str==''){
			return '';
		}else{
			$temp0 = explode(' ',$str);
			$temp = explode('-',$temp0[0]);
			return $temp[2].'-'.$temp[1].'-'.$temp[0].' '.$temp0[1];
		}
	}
?>