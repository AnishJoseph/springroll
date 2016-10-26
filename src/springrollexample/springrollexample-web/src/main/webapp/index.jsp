<%@ page import="java.util.Properties" %>
<%@ page import="java.io.InputStream" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>SpringRoll</title>
    <script src="springroll.js"></script>
</head>
<body>
        <div id="root-element" style="width: 100%; height: 100vh;"/>
<%
    InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("springroll.properties");
    Properties props = new Properties();
    props.load(stream);
    String uiDateFormatJs = props.getProperty("ui.date.format.js");
    if(uiDateFormatJs == null)uiDateFormatJs = "dd/mm/yyyy";

%>
        <script>
            window.UIProperties = {};
            window.UIProperties.uiDateFormatJs = '<%=uiDateFormatJs%>';
        </script>
</body>
</html>