// 文件上传
jQuery(function() {

    var uploader,pathDir, custNo, chipVersion, batchNo,fileIp;
    var selectHost=selectHostForJs="";//select选中路径
    var successNum = 0,failNum = 0;deleteState=false;
    var fileList = [],treeParentNodes = [];
    var rMenu = $("#rMenu"),state = 'pending';
    var clickCallback = function(e, id, node){
        clickNode = node.file;
        var zTree = $.fn.zTree.getZTreeObj("fileTreeList");
        // if (node.isParent) {
        //     zTree.expandNode();
        // }
    };
    var $btn = $('#ctlBtn');
    var checkCallback = function(e, id, node) {
        fileList = [];
        var zTree = $.fn.zTree.getZTreeObj("fileTreeList");
        var nodes = zTree.getCheckedNodes(true);
        for (var i=0;i<nodes.length;i++){
            if (nodes[i].flieFlag) {
                fileList.push(nodes[i]);
            }
        }
        // if (node.checked) {
        //     fileList.push(node.path);
        // } else {
        //     fileList.splice(node.path)
        // }
        // alert(fileList.toString());
        // alert(node.tId + ", " + node.name + "," + node.checked +"node.path" + node.path);
    };

    var showRMenu = function (type, x, y) {
        // $("#rMenu ul").show();
        if (type=="root") {
            $("#m_del").hide();
            $("#m_upload").show();
            $("#m_create").show();
            $("#m_refresh").show();
        } else if (type=="node") {
            $("#m_del").show();
            $("#m_upload").show();
            $("#m_create").hide();
            $("#m_refresh").hide();
        } else {
            $("#m_del").hide();
            $("#m_upload").show();
            $("#m_create").hide();
            $("#m_refresh").show();
        }
        rMenu.css({"top":y+"px", "left":x+"px", "visibility":"visible"});

        $("body").bind("mousedown", onBodyMouseDown);
    }

    var hideRMenu = function () {
        if (rMenu) rMenu.css({"visibility": "hidden"});
        $("body").unbind("mousedown", onBodyMouseDown);
    }

    var onBodyMouseDown = function (event) {
        if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
            rMenu.css({"visibility" : "hidden"});
        }
    }

    var rightClickCalback = function(event, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("fileTreeList");
        treeParentNodes = [];
        var nodes = zTree.getNodes()[0];//获取主节点
        nodes = nodes.children;
        if (nodes && nodes.length > 0) {
            for (var i = 0; i<nodes.length; i++) {
                if (nodes[i].isParent) {
                    treeParentNodes.push(nodes[i]);
                }
            }
        }

        var mX = event.clientX-30,
            mY = event.clientY+10;
        fileList = [];
        if (!treeNode && event.target.tagName.toLowerCase() != "button" && $(event.target).parents("a").length == 0) {
            zTree.cancelSelectedNode();
            showRMenu("root", mX, mY);
        } else if (treeNode && !treeNode.noR) {
            zTree.selectNode(treeNode);
            if (treeNode.flieFlag) {
                fileList.push(treeNode);
                showRMenu("node", mX, mY);
            } else {
                showRMenu("parent", mX, mY);
            }
        }
    }

    var onDblClickCalback = function (event, treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("fileTreeList");
        if (treeNode.pId == null) {
            treeNode.path = treeNode.path.substring(0,treeNode.path.lastIndexOf("/"));
            treeNode.path = treeNode.path.substring(0,treeNode.path.lastIndexOf("/")+1);
            // alert(treeNode.path);
        }
        if (treeNode.isParent) {
            queryFileAjax("queryFile",treeNode.path,false,"fileTreeList");
        }
    }
    
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        },
        async: {
            enable: true
        },
        view: {
            selectedMulti: false,
            fontCss : {"font-size":"20px"},
            showTitle: true
        },
        // check: {
        //     enable: true,
        //     chkStyle: "checkbox",
        //     chkboxType: { "Y": "ps", "N": "ps" },
        //     radioType: "all"
        // },
        callback: {
            onClick : clickCallback,
            onCheck: checkCallback,
            onRightClick: rightClickCalback,
            onDblClick: onDblClickCalback
        }
    };

    var analyzeData = function (dataTmp,directoryFlag) {
        var obj = {"id":dataTmp.id,"pId":dataTmp.pid,"name":dataTmp.name,"path":dataTmp.path,"flieFlag":dataTmp.fileFlag,"open":dataTmp.parent,isParent:dataTmp.parent};

        if (obj.pId == null) {
            obj.name='..返回上一级'
        }
        if((directoryFlag && dataTmp.parent) || !directoryFlag) {
            zNodes.push(obj);
        }

        if (dataTmp.parent) {
            if (dataTmp.children != null) {
                for (var i=0;i<dataTmp.children.length;i++){
                    var datasTmp = dataTmp.children[i];
                    //TODO
                    if(datasTmp.name.substr(datasTmp.name.length -1 ) != "$") {
                        analyzeData(datasTmp,directoryFlag);
                    }
                }
            }


        }
    }

    // $.fn.zTree.init($("#menuTree"), setting, zNodes);
    //文件目录结构查询
    //TODO
    var queryFileAjax = function(urlParam,path,directoryFlag,treeId){
        var hostIp = $("#hostIp option:selected").text();
        $("#uploader_list").css({"visibility":"hidden"});
        $.ajax({
            url: urlParam,
            type: "post",
            dataType: "json",
            async: false,
            data: {
                "hostIp": hostIp,
                "path": path
            },
            success: function (data) {
                zNodes = [];
                var dataTmp = data;
                if (data.children == null) {
                    alert("查无此路径，请联系管理员！")
                }
                analyzeData(dataTmp,directoryFlag);
                if (zNodes.length <=1) {
                    $("#dellBtn").attr("disabled",true);
                } else {
                    $("#dellBtn").attr("disabled",false);
                }
                $.fn.zTree.init($("#" + treeId), setting, zNodes);
                path = path.substring(path.indexOf("@")+1,path.length);
                $("#filePath").text(path);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                               alert(XMLHttpRequest.status);
                               alert(XMLHttpRequest.readyState);
                               alert(textStatus);
            }
        })
    }

    var hostChangeFn = function() {
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
    };

    var delFileAjaxFn = function (urlParam,dataTmp) {
        // alert(dataTmp[0].name + "被袁敏超吃了！略略略……");
        var filePathList = [];
        var fileNameList = [];
        if (dataTmp.length > 0) {
            for (var i=0; i<dataTmp.length;i++) {
                filePathList.push(dataTmp[i].path);
                fileNameList.push(dataTmp[i].name);
            }
        }
        $.ajax({
            url: urlParam,
            type: "post",
            dataType: "json",
            async: false,
            data: {
                "filePathList": filePathList,
                "fileNameList": fileNameList,
                "ftpPath": "/" + $("#custNo").val() + "/" + new Date().getFullYear() + "/" + $("#chipVersion").val() + "/" +$("#batchNo").val(),
            },
            success: function (data) {
                if (data.success) {
                    alert(data.success);
                } else {
                    alert(data.error);
                }
                queryFileAjax("queryFile",$("#filePath").text(),false,"fileTreeList");
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
                alert(XMLHttpRequest.readyState);
                alert(textStatus);
            }
        })
    }
    var delFileFn = function() {
        if(fileList.length > 0) {
            var deleteSureState = confirm("是否删除所选文件？");
            if (deleteSureState) {
                delFileAjaxFn("deleteFiles",fileList);
            } else {
                return;
            }

        } else {
            alert("请选择所需删除的文件");
        }
    }

    //删除文件
    var delFile = function (file) {
        // $("#"+file.id).css("display","none");
        $("#"+file.id).remove();
        uploader.removeFile( file ,true);
    }

    var showFileDirFn = function() {
        selectHost = $("#hostIp option:selected").text();
        if (selectHost == "请选择" || selectHost =="") {
            alert("请选择上传ftp服务器！");
            return;
        } else {
            // pathDir = $("#"+ selectHostForJs + " option:selected").val();
            pathDir = "/";
        }
        // fileDir = $("#"+ selectHostForJs + " option:selected").text() + pathDir + $("#custNo").val() + "/" + $("#chipVersion").val() + "/" + $("#batchNo").val();
        fileIp = $("#hostIp option:selected").text();
        $("#filePath").text(fileIp + "/" );
        $("#successNum").text("");
        $("#failNum").text("");
    }

    var btnClickFn = function() {
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
        queryFileAjax("queryFile","/",false,"fileTreeList");
    };



    var m_upload = function () {
        // alert("执行上传操作！");
        // $("#uploader_pick").css({"visibility":"visible"});
    }
    var m_refresh = function () {
        // alert("执行刷新操作！");
        var path =$("#filePath").text();
        queryFileAjax("queryFile",path,false,"fileTreeList");
        hideRMenu();
    }
    var m_create = function () {
        alert("执行新建操作！");
        $('#myModal').modal('show');
    }
    var m_del = function () {
        // alert("执行删除操作！")
        hideRMenu();
        if(fileList.length > 0) {
            var deleteSureState = confirm("是否删除所选文件？");
            if (deleteSureState) {
                delFileAjaxFn("deleteFiles",fileList);
            } else {
                return;
            }
        } else {
            alert("请选择所需删除的文件");
        }
        // hideRMenu();
    }
    
    var ajaxCreateDir = function (urlParam,path,fileName) {
        var hostIp = $("#hostIp option:selected").text();
        $.ajax({
            url: urlParam,
            type: "post",
            dataType: "json",
            async: false,
            data: {
                "hostIp": hostIp,
                "path": path,
                "fileName": fileName
            },
            success: function (data) {
                if (data.success) {
                    alert(data.success);
                } else {
                    alert(data.message);
                }
                queryFileAjax("queryFile",path,false,"fileTreeList");
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.status);
                alert(XMLHttpRequest.readyState);
                alert(textStatus);
            }
        })
    }
    
    var addDirFn = function () {
        var state = true;
        if (treeParentNodes.length > 0) {
            for (var i=0; i<treeParentNodes.length;i++) {
                if(treeParentNodes[i].name == $("#newDirName").val()) {
                    alert("文件名重复！");
                    state = false;
                }
            }
        }

        if (state) {
            // var zTree = $.fn.zTree.getZTreeObj("fileTreeList");
            // var newNode = {name:$("#newDirName").val(),isParent:true}
            // zTree.addNodes(zTree.getNodes()[0],-1,newNode);
            var path = $("#filePath").text();
            fileName = $("#newDirName").val();
            ajaxCreateDir("createDir",path,fileName);
            $('#myModal').modal('hide');
        }
    }

    var keyupFn = function (event) {
        if(event.keyCode ==13){
            addDirFn();
        }
    }

    // 文件新增
    var fileQueuedFn = function( file ) {
        $("#uploader_list").css({"visibility":"visible"});
        $list.append( '<div id="' + file.id + '" class="item">' +
            '<input type="button" id="' + file.id + '_delBtn" class="btn btn-default del_file_button" value="取消上传"></input>' +
            '<lable class="info">文件名：' + file.name + '&nbsp</lable>' +
            // '<lable class="info">&nbsp文件大小：' + file.size + 'KB</lable>' +
            '<p class="state">等待上传...</p>' +
            '</div>' );
        hideRMenu();
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
            queryFileAjax("queryFile",$("#filePath").text(),false,"fileTreeList");
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
        // if ( state === 'uploading' ) {
        //     $btn.text('暂停上传');
        // } else {
        //     $btn.text('开始上传');
        // }
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
        var filePath = $("#filePath").text();
        // 修改data可以控制发送哪些携带数据。
        data.custNo = $("#custNo").val(),//$("#custNo").val()
        data.chipVersion = $("#chipVersion").val(),//$("#custNo").val()
        data.batchNo = $("#batchNo").val()//$("#custNo").val()
        data.fileDir = ""//$("#fileDir").val()
        data.selectHost = $("#hostIp option:selected").text();
        data.path = $("#filePath").text();
    };

    var uploaderLoad = function() {
        $list = $("#uploader_pick");
        //创建uploader对象
        uploader = WebUploader.create({
            resize: false,
            swf: '/js/Uploader.swf',
            server: 'http://localhost:8011/upload/uploadFiles',
            // server: 'http://172.17.253.80:8011/upload/uploadFiles',
            pick: {id:'#m_upload'},
            threads: 20,
            duplicate :true,
            auto:true
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

    var init = function(){
        uploaderLoad();
        // $("#hostIp").on('change',hostChangeFn);
        $("#queryBtn").on( 'click', btnClickFn);
        $("#dellBtn").on('click',delFileFn);
        $("#m_upload").on('click',m_upload);
        $("#m_refresh").on('click',m_refresh);
        $("#m_create").on('click',m_create);
        $("#m_del").on('click',m_del);
        $("#addDir").on('click',addDirFn);
        $("#newDirName").on('keyup',keyupFn);

        
        // $("#" + selectHostForJs).on('change',function() {
        //     $("#fileDir").val($("#"+ selectHostForJs + " option:selected").text());
        // })

    }
    init();
});

