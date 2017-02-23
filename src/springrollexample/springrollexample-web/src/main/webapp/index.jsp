<!DOCTYPE html>
<html lang = "en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>SpringRoll</title>
    <script>
        var JSessionId = '<%=session.getId()%>';
    </script>
</head>

<body>
<div id = "app"></div>
<script src = "assets/generated/sindex.js"></script>
</body>

</html>