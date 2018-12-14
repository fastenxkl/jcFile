// 文件上传
jQuery(function() {

    var zNodes =[
        { id:1, pId:0, name:"文件操作", file:"",open:"True"},
        // { id:11, pId:1, name:"首页", file:"upload",open:1},
        { id:11, pId:1, name:"操作", file:"queryAndDelete",open:1}
        ];
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
        callback: {
            onClick:function(e, id, node){
                clickNode = node.file;
                var zTree = $.fn.zTree.getZTreeObj("menuTree");
                if (node.isParent) {
                    zTree.expandNode();
                } else {
                    addTabs(node.name, node.file,node.rel);
                }
            }
        }
    };



    //添加一个选项卡面板
    function addTabs(title, url, icon){
        if(!$('#tabs').tabs('exists', title)){
            $('#tabs').tabs('add',{
                title:title,
                //href : url,
                content:'<iframe id='+title+' src="'+url+'" frameBorder="0" border="0" scrolling="no" style="width: 100%; height: 100%;"/>',
                closable:true
            });
        } else {
            $('#tabs').tabs('select', title);
        }
    }
    function closeTab(title){
        if($('#tabs').tabs('exists', title)){
            $('#tabs').tabs('close',title);
        }
    }

    /*function delFile(name) {
        alert(name + "is delete!");
        click="delFile(' + file.name  + ')"
    }*/
    var init = function(){
        $.fn.zTree.init($("#menuTree"), setting, zNodes);
    }

    init();
});
