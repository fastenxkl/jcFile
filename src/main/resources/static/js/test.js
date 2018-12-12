$(function () {
    $("#menu_container").mouseover(function () {
        $("#menu").show();
        $("#menu_container").css("width","160px")

    })
    $("#menu_container").mouseout(function () {
        $("#menu_container").css("width","20px")
        $("#menu").hide()
    })

});