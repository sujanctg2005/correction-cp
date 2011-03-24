<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=us-ascii">
<html>
<head>
<title>Test File Upload</title>
</head>

<body>

    <form name="upload" method="GET" action="http://localhost:8080/Correction/correction/renderui">
	<b>Upload a file:</b>
	<br/><input type="file" name="fragment" size="100"> (not activated)
	<br/>URL to get XML ID from:<input name="xmlurl" type=text size="50" value="file:///C:/india/">
	<br/>XML ID (file name)<input name ="xmlid" type="text" size="30" value="UCFWithErrors.xml">
	<br/>Call back URL: <input type=text name="callbackurl" size=50 value="http://localhost:8080/postback">
	<br/>User Info: <input type=text name="userinfo" value="MFOWLER">
	<br/>View Mode:<select name="viewmode">
		<option value="EDIT" selected>Edit</option>
		<option value="READ">Read</option>
		</select>
	<br/><input type=submit>
</form>
</body>
</html>
