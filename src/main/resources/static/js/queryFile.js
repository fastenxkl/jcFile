// 文件上传
$(function () {
    $("#queryTest").click(function () {
        alert(11111);
        $.ajax({
            url:"queryFile",
            type: 'post',
            // data: {stripid: $("#stripid").val(),waferId:$("#waferId").val()},
            dataType: 'json',
            success: function (data) {
                zNodes = [];
                // zNodes = [{
                //     "id": "\\172.17.255.93\mapping\test",
                //     "pId": null,
                //     "name": "test/",
                //     open:true
                // }, {
                //     "id": "\\172.17.255.93\mapping\test\2",
                //     "pId": "\\172.17.255.93\mapping\test",
                //     "name": "2/"
                // }, {
                //     "id": "\\172.17.255.93\mapping\test\2\3",
                //     "pId": "\\172.17.255.93\mapping\test\2",
                //     "name": "3/"
                // }]
                var dataTmp = data;
                analyzeData(dataTmp);
                var test = JSON.stringify(zNodes);
                $.fn.zTree.init($("#menuTree"), setting, zNodes);
            },
            error: function () {
                alert("success")
            }
        })
    });
    var zNodes =[
            // { id:1, pId:0, name:"数据查询", file:"",open:"True"},
            // { id:11, pId:1, name:"批次信息查询", file:"materialTrack/index",open:1},
            // { id:12, pId:1, name:"批次作业记录查询", file:"materialTrack/materialHistory",open:1},
            // { id:13, pId:1, name:"批次框架数据查询", file:"materialTrack/materialAolotFrame",open:1},
            // { id:14, pId:1, name:"批次FTP记录查询", file:"ftpTask/index",open:1},
            // { id:15, pId:1, name:"打码机上传记录", file:"mappingInitLog/index",open:1},
            // { id:16, pId:1, name:"机台作业批次查询", file:"mappingLog/index",open:1},
            // { id:17, pId:1, name:"2008机台批次上传查询", file:"stripFileTask/index",open:1},
            // // { id:18, pId:1, name:"导出空管数量批次", file:"mappingLot2D/index",open:1},
            // { id:2, pId:0, name:"系统配置", file:"",open:"True"},
            // { id:21, pId:2, name:"调机料管理", file:"testMaterial/index",open:1},
            // { id:22, pId:2, name:"设备装片数量设置", file:"stripSetting/index",open:1},
            // { id:23, pId:2, name:"设备信息查询", file:"deviceInfo/index",open:1},
            // { id:24, pId:2, name:"系统设置", file:"jsp/sysSetting/sysSetting.jsp",open:1}
            // { id:25, pId:2, name:"WaferMap系统设置", file:"waferMap/waferSetting",open:1},
            // { id:3, pId:0, name:"WaferMap系统", file:"",open:"True"},
            // { id:31, pId:3, name:"WaferMap在库管理", file:"waferMap/index",open:1},
            // { id:32, pId:3, name:"WaferMap转换记录", file:"waferMap/transferRecord",open:1},
            // { id:33, pId:3, name:"WaferMap设备下载记录", file:"waferMap/downLoadRcord",open:1},
            // { id:34, pId:3, name:"WaferMap设备ReMap记录", file:"waferMap/remapRecord",open:1},
            // { id:35, pId:3, name:"WaferMap详情", file:"waferMap/showWaferMapDetail?waferId=001",open:1}
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
                var zTree = $.fn.zTree.getZTreeObj("menuTree");
                if(node.isParent) {
                    zTree.expandNode();
                } else {
                    // addTabs(node.name, node.file,node.rel);
                    alert(node.id);
                }
            }
        }
    };

    var analyzeData = function (dataTmp) {
        var obj = {id:dataTmp.id,pId:dataTmp.pid,name:dataTmp.name,open:true};
        zNodes.push(obj);
        if (dataTmp.parent) {
            for (var i=0;i<dataTmp.children.length;i++){
                var datasTmp = dataTmp.children[i];
                //TODO
                analyzeData(datasTmp);
            }

        }
    }
    // $.fn.zTree.init($("#menuTree"), setting, zNodes);
    // var zTree = $.fn.zTree.getZTreeObj("menuTree");
})


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
