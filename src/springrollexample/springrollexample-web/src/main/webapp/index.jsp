<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
        <h1>Hello World - SpringRoll Example</h1>
        <button id="Test1">Exception  Test </button>
        <button id="Test2">Competing Threads  Test </button>
        <button id="Test3">Approve</button>
</body>
<script>
    $(document).ready(function(){
        $("#Test1").click(function(){
            $.get("api/testPipelineSimple", function(data, status){
//                alert("Data: " + data + "\nStatus: " + status);

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
                            alert(msg);
                        }
                    }
            );
        });
    });
</script>

</html>