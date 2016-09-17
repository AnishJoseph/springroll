<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
</head>
<body>
        <h1>Hello World - SpringRoll Example</h1>
        <button name="Test1">Desscription </button>
</body>
<script>
    $(document).ready(function(){
        $("button").click(function(){
            $.get("api/testPipelineSimple", function(data, status){
                alert("Data: " + data + "\nStatus: " + status);
            });
        });
    });
</script>

</html>