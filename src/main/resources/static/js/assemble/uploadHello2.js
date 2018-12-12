/*
var uploader = WebUploader.create({

    // swf文件路径
    // swf: BASE_URL + '/js/Uploader.swf',
    swf: '/js/Uploader.swf',

    // 文件接收服务端。
    server: 'http://webuploader.duapp.com/server/fileupload.php',

    // 选择文件的按钮。可选。
    // 内部根据当前运行是创建，可能是input元素，也可能是flash.
    pick: '#picker',

    // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
    resize: false
});

// 当有文件被添加进队列的时候
uploader.on( 'fileQueued', function( file ) {
    $list.append( '<div id="' + file.id + '" class="item">' +
        '<h4 class="info">' + file.name + '</h4>' +
        '<p class="state">等待上传...</p>' +
        '</div>' );
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
            '</div>').appendTo( $li ).find('.progress-bar');
    }

    $li.find('p.state').text('上传中');

    $percent.css( 'width', percentage * 100 + '%' );
});*/

/**
 *
 */

/*todo:
 1. query 获取上传host
 2. 组件化
 */

$(function() {


    /*init webuploader*/
    //注意，这一步的变量提取是官网的资料中缺少的
    var $list = $('#thelist');

    var uploader = WebUploader.create({
        //是否自动上传，如果为false，则在之前的html代码中需要再设置一个button来进行上传
        auto: false,
        // swf文件路径（根据你自己的工程目录进行设置）
        swf: '/js/Uploader.swf',
        // 文件接收服务端（路由）
        server: 'http://localhost:8080/upload/test',
        pick: {
            // 选择文件的按钮。可选
            id: '#picker',
            //不允许单次上传时同时选择多个文件
            multiple: true
        },
        // //允许上传的文件总数量为1
        // fileNumLimit: 1,
        chunked:true,
        chunkSize:5242880,
    });

    /**
     * 验证文件格式、数量以及文件大小
     */
    /*uploader.on("error", function(type) {
        if (type == "Q_TYPE_DENIED") {
            alert("请上传xlsx格式文件");
        } else if (type == "F_EXCEED_SIZE") {
            alert("文件大小不能超过8M");
        } else if (type == "Q_EXCEED_NUM_LIMIT") {
            alert("一种资源只能上传一个文件");
        }
    });*/


    // 当有文件被添加进队列的时候
    uploader.on('fileQueued', function(file) {
        $list.append('<span id="' + file.id + '" class="item">' +
            '<span class="info">' + file.name + '</span>' +
            '<p class="state">等待上传...</p>' +
            '</span>');
    });

    // 文件上传过程中创建进度条实时显示。
    uploader.on('uploadProgress', function(file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress .progress-bar');

        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<div class="progress progress-striped active">' +
                '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                '</div>' +
                '</div>').appendTo($li).find('.progress-bar');
        }

        $li.find('p.state').text('上传中');

        $percent.css('width', percentage * 100 + '%');
    });
    // 文件上传成功
    uploader.on('uploadSuccess', function(file) {
        $('#' + file.id).find('p.state').text('已上传');
    });

    //文件上传是否成功的状态
    var flag = true;
    //根据服务器返回的数据判断文件是否上传成功
    //这个函数会接收来自服务器的文件上传是否成功的状态，这个事件发生在‘uploadError’之前
    //data为服务器返回的数据，是个对象，如果服务器返回的是json格式，那么正和我们的意。如果不是json格式，response._raw里面可以拿到原始数据。所以，webuploader对于后端返回的数据格式是没有要求的
    uploader.on("uploadAccept", function(file, data) {
        if (data.op_result == "0") {
            // 通过return false来告诉组件，此文件上传有错。
            flag = false;
            return false;
        }
    });
    // 文件上传失败，显示上传出错
    uploader.on('uploadError', function(file) {
        $('#' + file.id).find('p.state').text('上传出错');
    });

    // 完成上传完
    uploader.on('uploadComplete', function(file) {
        $('#' + file.id).find('.progress').fadeOut();
    });


});