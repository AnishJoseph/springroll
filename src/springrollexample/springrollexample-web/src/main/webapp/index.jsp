<!DOCTYPE html>
<html lang = "en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>SpringRoll</title>
    <script>
        var JSessionId = '<%=session.getId()%>';
    </script>
</head>

<body>
<div id = "app"></div>

<%--For production uncomment out the line below and comment out the URL based script --%>
<%--<script src = "assets/generated/index.js"></script>--%>
<script src = "http://localhost:9080/assets/generated/index.js"></script>

<iframe id="ifmcontentstoprint" style="height: 0px; width: 0px; position: absolute"></iframe>

</body>

</html>