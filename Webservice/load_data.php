<?php
    require('konek.php');

    if (isset($_POST['id_account'])) {
        $sql = "
select account.*, case when pesan.flag in (1,2) then 1 else 0 end pesanan
from account
left join pesan on pesan.user_input=account.id_account and pesan.flag in (1,2)
where id_account = '".post($con, $_POST['id_account'])."'";
        $result=mysqli_query($con, $sql) or die(mysqli_error($con));
        $rowcount=mysqli_num_rows($result);
        $row = mysqli_fetch_array($result);

        if ($rowcount == 1) {
            $response['error'] = false;
            $response['message'] = $row;
        } else {
            $response['error'] = true;
            $response['message'] = 'Account tidak terdaftar';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Parameter not valid';
    }

    mysqli_close($con);
    echo json_encode($response);
