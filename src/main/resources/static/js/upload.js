// 文件上传
jQuery(function() {

    var pathDir, custNo, chipVersion, batchNo,fileIp;
    var selectHost=selectHostForJs="";//select选中路径
    var successNum = 0,failNum = 0;uploadState=false;
    var dataTemp = {
        "custNo": "",
        "chipVersion": "",
        "batchNo": "",
        "fileDir": "",
        "selectHost": ""
    }

    var $ = jQuery,
        $list = $('#thelist'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        uploader;


    //select change事件
    var hostChange = function(){
        selectHost = selectHostForJs ="";
        var reg = new RegExp("\\.","g");
        $("#hostIp option").each(function () {
            $(this).text();
            $("#"+$(this).text().replace(reg,"\\.")).hide();
        })
        selectHost = $("#hostIp option:selected").text();
        selectHostForJs = selectHost.replace(reg,"\\.");
        if(selectHost == "请选择"){
            $("#selectTmp").show();
        } else {
            $("#selectTmp").hide();
            $("#"+selectHostForJs).show();
        }
    }


    //删除文件
    var delFile = function (file) {
        // $("#"+file.id).css("display","none");
        $("#"+file.id).remove();
        uploader.removeFile( file ,true);

    }

    var showFileDirFn = function() {
        if (selectHost == "请选择" || selectHost =="") {
            alert("请选择上传ftp服务器！");
            return;
        } else {
            pathDir = $("#"+ selectHostForJs + " option:selected").val();
        }
        fileDir = $("#"+ selectHostForJs + " option:selected").text() + "/" + $("#custNo").val() + "/" + $("#chipVersion").val() + "/" + $("#batchNo").val();
        fileIp = $("#hostIp option:selected").text();
        $("#filePath").text(fileIp + "/" + fileDir );
        $("#successNum").text("");
        $("#failNum").text("");
    }

    var pickerClick = function() {
        showFileDirFn();
        if (uploadState) {
            successNum = 0
            failNum = 0
            var fileList = uploader.getFiles();
            for (var i=0;i<fileList.length;i++) {
                delFile(fileList[i]);
            }
            uploadState = false;
        }
    }

    var btnClickFn = function() {
        // zNodes[2].file = "queryAndDelete?custNo=" + $("#custNo").val();
        showFileDirFn();
        custNo = $("#custNo").val();
        chipVersion = $("#chipVersion").val();
        batchNo = $("#batchNo").val();

        if (pathDir == 0) {
            alert("请选择文件夹父级目录！");
            return;
        }
        if(custNo == null || custNo == "") {
            alert("客户三位码不能为空！");
            return;
        }
        if(chipVersion == null || chipVersion == "") {
            alert("芯片型号不能为空！");
            return;
        }
        if(batchNo == null || batchNo == "") {
            alert("批次号不能为空！");
            return;
        }
        if ( state === 'uploading' ) {
            uploader.stop();
        } else {
            uploader.upload();
        }
    };

    //失焦事件
    var inputBlur = function(){
        dataTemp.custNo = $("#custNo").val();
        dataTemp.chipVersion = $("#chipVersion").val();
        // zNodes[2].file = "queryAndDelete?custNo=" + dataTemp.custNo +"&chipVersion" + dataTemp.chipVersion;
    }

    // 文件新增
    var fileQueuedFn = function( file ) {
        $list.append( '<div id="' + file.id + '" class="item">' +
            '<input type="button" id="' + file.id + '_delBtn" class="btn btn-default del_file_button" value="取消上传"></input>' +
            '<lable class="info">文件名：' + file.name + '&nbsp</lable>' +
            // '<lable class="info">&nbsp文件大小：' + file.size + 'KB</lable>' +
            '<p class="state">等待上传...</p>' +
            '</div>' );

        $("#"+file.id+"_delBtn").click(function () {
            if(confirm("你确认要取消对文件" + file.name +"的上传吗？")){
                delFile(file);
            }
        });
    };

    // 创建进度条实时显示。
    var uploadProgressFn = function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>' +
                + percentage * 100
            ).appendTo( $li ).find('.progress-bar');
        }
        $li.find('p.state').text('上传中');
        $percent.css( 'width', percentage * 100 + '%' );
    };

    //文件上传成功
    var uploadSuccessFn = function( file ,response) {
        if(response.fileState=="success"){
            $( '#'+file.id ).find('p.state').text('已上传');
            $( '#'+file.id ).addClass('upload-state-done');
            $( '#'+file.id + '_delBtn').attr("disabled",true);
            successNum ++;
        } else {
            $( '#'+file.id ).find('p.state').text('上传出错');
            failNum ++;
        }
        //TODO
        uploadState=true;
    };

    //文件上传状态
    var uploadStateFn = function( type ) {
        if ( type === 'startUpload' ) {
            state = 'uploading';
        } else if ( type === 'stopUpload' ) {
            state = 'paused';
        } else if ( type === 'uploadFinished' ) {
            state = 'done';
        }
        if ( state === 'uploading' ) {
            $btn.text('暂停上传');
        } else {
            $btn.text('开始上传');
        }
        if(state == 'done'){
            if (successNum != 0) {
                $("#successNum").show();
                $("#successNum").text("成功上传" + successNum + "个文件！");
            }

            if (failNum != 0) {
                $("#failNum").show();
                $("#failNum").text(failNum + "个文件上传失败！");
            }
        }
    };

    var uploadBeforeSendFn = function( block, data ) {
        // block为分块数据。
        // file为分块对应的file对象。
        var file = block.file;

        // 修改data可以控制发送哪些携带数据。
        data.custNo = $("#custNo").val(),//$("#custNo").val()
            data.chipVersion = $("#chipVersion").val(),//$("#custNo").val()
            data.batchNo = $("#batchNo").val()//$("#custNo").val()
        data.fileDir = $("#"+ selectHostForJs + " option:selected").text()//$("#fileDir").val()
        data.selectHost = selectHost;
    };

    var uploaderLoad = function() {
        //创建uploader对象
        uploader = WebUploader.create({
            resize: false,
            swf: '/js/Uploader.swf',
            server: 'http://localhost:8011/upload/uploadFiles',
            pick: '#picker',
            threads: 20,
        });
        //添加事件监听
        uploader.on( 'fileQueued', fileQueuedFn);
        uploader.on( 'uploadProgress', uploadProgressFn);
        uploader.on( 'uploadSuccess', uploadSuccessFn);
        uploader.on( 'uploadError', function( file ) {
            $( '#'+file.id ).find('p.state').text('上传出错');
            failNum ++;
        });
        uploader.on( 'uploadComplete', function( file ) {
            $( '#'+file.id ).find('.progress').fadeOut();
        });
        uploader.on( 'all', uploadStateFn);
        uploader.on( 'uploadBeforeSend', uploadBeforeSendFn);
    };

    var init = function() {
        uploaderLoad();
        //加载事件
        $("#hostIp").on('change',hostChange);
        $("#" + selectHostForJs).on('change',function() {
            $("#fileDir").val($("#"+ selectHostForJs + " option:selected").text());
        })
        $btn.on( 'click', btnClickFn);
        $("#picker").click(pickerClick);
        $("#custNo").blur(inputBlur);
        $("#chipVersion").blur(inputBlur);

    };

    init();

});
