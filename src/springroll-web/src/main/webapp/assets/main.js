require.config({
    paths: {
        "jquery": "vendor/jquery/jquery-3.1.1.min",
        "jquery.cometd": "vendor/cometd/jquery.cometd",
        "messenger.cometd": "lib/framework/messenger.cometd",
        "Application": "lib/framework/Application",
        "marionette": "vendor/marionette/backbone.marionette.min",
        "underscore": "vendor/marionette/underscore",
        "backbone": "vendor/marionette/backbone",
        "backbone.radio": "vendor/marionette/backbone.radio",
    }
});
require(['Application', 'messenger.cometd'],function(Application){

    $.ajaxSetup({ cache: false });

    //Application.start();

    $("#Test1").click(function(){
        //Application.CometD.publish('/service/hello', { name: 'World' });
        $.get("api/testPipelineSimple", function(data, status){
                //alert("Data: " + data + "\nStatus: " + status);
        });
    });
    $("#Test2").click(function(){
        $.get("api/testCompetingThreads", function(data, status){
//                alert("Data: " + data + "\nStatus: " + status);
        });
    });
    $("#Test3").click(function(){
        data = '{"approved":true, "notificationId":1,"reviewStepId":1}';
        $.ajax(
            {
                url: '/api/sr/reviewaction',
                type: 'POST',
                data: data,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function (msg) {
//                            alert(msg);
                }
            }
        );
    });
    $("#Test4").click(function(){
        data = '{"notificationId":1}';
        $.ajax(
            {
                url: '/api/sr/notificationack',
                type: 'POST',
                data: data,
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                success: function (msg) {
//                            alert(msg);
                }
            }
        );
    });
});
