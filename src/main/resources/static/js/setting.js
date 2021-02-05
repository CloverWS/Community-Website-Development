$(function () {
    $("#uploadForm").submit(upload);
});

function upload() {

    $.ajax({
        url: "http://upload-z1.qiniup.com",
        method: "post",
        // processData是否把表单的内容转成字符串，默认true，但是因为我们要传文件，所以false
        processData: false,
        // contentType 不让jquery去设置文件类型
        contentType: false,
        data: new FormData($("#uploadForm")[0]),
        success: function (data) {
            if (data && data.code == 0){
                // 更新头像访问路径
                $.post(
                    CONTEXT_PATH + "/user/header/url",
                    {"fileName":$("input[name='key']").val()},
                    function (data) {
                        data = $.parseJSON(data);
                        if (data.code == 0){
                            window.location.reload();
                        }else{
                            alert(data.msg);
                        }
                    }
                );
            }else{
                alert("上传失败！");
            }
        }
    });

    return false;
}