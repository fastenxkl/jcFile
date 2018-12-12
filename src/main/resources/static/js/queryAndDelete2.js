// 文件上传
jQuery(function() {

    var pathDir, custNo, chipVersion, batchNo,fileIp;
    var selectHost=selectHostForJs="";//select选中路径
    var successNum = 0,failNum = 0;uploadState=false;
    var clickNode = "upload";
    var dataTemp = {
        "custNo": "",
        "chipVersion": "",
        "batchNo": "",
        "fileDir": "",
        "selectHost": ""
    }
    var zNodes =[
        { id:1, pId:0, name:"文件操作", file:"",open:"True"},
        { id:11, pId:1, name:"上传", file:"upload",open:1},
        { id:11, pId:1, name:"删除", file:"queryAndDelete",open:1}
        ];
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        },
        view: {
            selectedMulti: false,
            fontCss : {"font-size":"20px"},
            showTitle: true
        },
        callback: {
            onClick:function(e, id, node){
                //TODO
                // if (clickNode == node.file) {
                //     // if (clickNode == "upload"){
                //     $("#custNo").val($("#custNo").val());
                //     $("#chipVersion").val(333);
                //     $("#batchNo").val(222);
                //     $("#hostIp option:selected").text(000);
                //         // $("#custNo").val(dataTemp.custNo);
                //         // $("#chipVersion").val(dataTemp.chipVersion);
                //         // $("#batchNo").val(dataTemp.batchNo);
                //         // $("#hostIp option:selected").text(dataTemp.selectHost);
                //     // }
                // }
                clickNode = node.file;
                var zTree = $.fn.zTree.getZTreeObj("menuTree");
                if (node.isParent) {
                    zTree.expandNode();
                } else {
                    var tab = $('#tabs').tabs('getTab', 0);  // 取得第一个tab
                    $('#tabs').tabs('update', {
                        tab: tab,
                        options: {
                            title: node.name,
                        }
                    });
                    $("#calendarTab").attr("src",node.file);

                    // $.ajax({
                    //     url:node.file,
                    //     data:{"data":dataTemp},
                    //     error: function () {//请求失败处理函数
                    //         alert('请求失败');
                    //     },
                    //     success:function(data){
                    //         // var jsondata= eval(data);
                    //         alert(data);
                    //     }
                    // })
                }
            }
        }
    };

    var $ = jQuery,
        $list = $('#thelist'),
        $btn = $('#ctlBtn'),
        state = 'pending',
        uploader;
    uploader = WebUploader.create({

        // 不压缩image
        resize: false,

        // swf文件路径
        swf: '/js/Uploader.swf',

        // 文件接收服务端。
        server: 'http://localhost:8011/upload/uploadFiles',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#picker',
        threads: 20,
    });

    // $.fn.zTree.init($("#menuTree"), setting, zNodes);
    //文件目录结构查询
    //TODO
    var queryFileAjax = function(urlParam,path,directoryFlag,treeId){
        $.ajax({
            url:urlParam,
            type: 'post',
            dataType: 'json',
            data: {path:path},
            success: function (data) {
                zNodes = [];
                var dataTmp = data;
                analyzeData(dataTmp,directoryFlag);
                var test = JSON.stringify(zNodes);
                $.fn.zTree.init($("#" + treeId), setting, zNodes);
            },
            error: function () {
                alert("success")
            }
        })
    }


    //select绑定
    $("#hostIp").on('change',function() {
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


        })

    $("#" + selectHostForJs).on('change',function() {
        $("#fileDir").val($("#"+ selectHostForJs + " option:selected").text());
    })

    //文件上传


    var analyzeData = function (dataTmp,directoryFlag) {

        var obj = {id:dataTmp.id,pId:dataTmp.pid,name:dataTmp.name,flieFlag:dataTmp.flieFlag};

        if((directoryFlag && dataTmp.parent) || !directoryFlag) {
            zNodes.push(obj);
        }

        if (dataTmp.parent) {
            for (var i=0;i<dataTmp.children.length;i++){
                var datasTmp = dataTmp.children[i];
                //TODO
                analyzeData(datasTmp,directoryFlag);
            }

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
    $("#picker").click(function () {
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

    })
    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
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
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
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
    });

    uploader.on( 'uploadSuccess', function( file ,response) {

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
    });

    uploader.on( 'uploadError', function( file ) {
        $( '#'+file.id ).find('p.state').text('上传出错');
        failNum ++;
    });

    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').fadeOut();
    });

    uploader.on( 'all', function( type ) {
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
            //TODO
            // if(!uploadState){
            //     $("#successNum").text("");
            //     $("#failNum").text("");
            // }
            // queryFileAjax("queryFile","/mapping/test/1",false,"menuTree");
        }
//TODO
        var file_dir0 = $("#file_dir0").text();
        // queryFileAjax("queryFile",file_dir0,true,"menuTree0");
    });

    $btn.on( 'click', function() {
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
    });

    uploader.on( 'uploadBeforeSend', function( block, data ) {
        // block为分块数据。

        // file为分块对应的file对象。
        var file = block.file;


        // 修改data可以控制发送哪些携带数据。
        data.custNo = $("#custNo").val(),//$("#custNo").val()
        data.chipVersion = $("#chipVersion").val(),//$("#custNo").val()
        data.batchNo = $("#batchNo").val()//$("#custNo").val()
        data.fileDir = $("#"+ selectHostForJs + " option:selected").text()//$("#fileDir").val()
        data.selectHost = selectHost;
    });




    /*function delFile(name) {
        alert(name + "is delete!");
        click="delFile(' + file.name  + ')"
    }*/
    var init = function(){
        $.fn.zTree.init($("#menuTree"), setting, zNodes);
        if (clickNode != "upload") {
            $("#chipVersionDel").val(333);
            $("#batchNoDel").val(222);
        }

    }
    init();
});


// 图片上传demo
/*
jQuery(function() {
    var $ = jQuery,
        $list = $('#fileList'),
        // 优化retina, 在retina下这个值是2
        ratio = window.devicePixelRatio || 1,

        // 缩略图大小
        thumbnailWidth = 100 * ratio,
        thumbnailHeight = 100 * ratio,

        // Web Uploader实例
        uploader;

    // 初始化Web Uploader
    uploader = WebUploader.create({

        // 自动上传。
        auto: false,

        // swf文件路径
        swf: '/js/Uploader.swf',

        // 文件接收服务端。
        server: 'http://webuploader.duapp.com/server/fileupload.php',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#filePicker',

        // 只允许选择文件，可选。
        /!*accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/!*'
        }*!/
    });

    // 当有文件添加进来的时候
    uploader.on( 'fileQueued', function( file ) {
        var $li = $(
            '<div id="' + file.id + '" class="file-item thumbnail">' +
            '<img>' +
            '<div class="info">' + file.name + '</div>' +
            '</div>'
            ),
            $img = $li.find('img');

        $list.append( $li );

        // 创建缩略图
        uploader.makeThumb( file, function( error, src ) {
            if ( error ) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }

            $img.attr( 'src', src );
        }, thumbnailWidth, thumbnailHeight );
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on( 'uploadProgress', function( file, percentage ) {
        var $li = $( '#'+file.id ),
            $percent = $li.find('.progress span');

        // 避免重复创建
        if ( !$percent.length ) {
            $percent = $('<p class="progress"><span></span></p>')
                .appendTo( $li )
                .find('span');
        }

        $percent.css( 'width', percentage * 100 + '%' );
    });

    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on( 'uploadSuccess', function( file ) {
        $( '#'+file.id ).addClass('upload-state-done');
    });

    // 文件上传失败，现实上传出错。
    uploader.on( 'uploadError', function( file ) {
        var $li = $( '#'+file.id ),
            $error = $li.find('div.error');

        // 避免重复创建
        if ( !$error.length ) {
            $error = $('<div class="error"></div>').appendTo( $li );
        }

        $error.text('上传失败');
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on( 'uploadComplete', function( file ) {
        $( '#'+file.id ).find('.progress').remove();
    });
});*/
