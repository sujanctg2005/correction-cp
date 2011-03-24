<%@ page import="com.edifecs.elm.ucf.domain.Model" %> 
<%@ page import="com.edifecs.correction.utility.CorrectionUIBean" %>

<jsp:useBean id="uiBean" class="com.edifecs.correction.utility.CorrectionUIBean" scope="session" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html><head><title>Exception Correction</title>
<script type="text/javascript" src="/Correction/js/correction/jquery-1.5.js"></script>
<script type="text/javascript" src="/Correction/js/correction/floating-1.5.js"></script>
<script type="text/javascript" src="/Correction/js/correction/jquery.qtip-1.0.0-rc3.min.js"></script>
<script type="text/javascript" src="/Correction/js/correction/jquery.form.js"></script>
<script type="text/javascript" src="/Correction/js/correction/overlib.js"></script>
<script type="text/javascript" src="/Correction/js/correction/correction.js"></script>

<link href="/Correction/css/correction/jquery.loadmask.css" rel="stylesheet" type="text/css" />
<script type='text/javascript' src='/Correction/js/correction/jquery.loadmask.js'></script>


 <script type="text/javascript">  
     floatingMenu.add('floatdiv',  
         {  
             // Represents distance from left or right browser window  
             // border depending upon property used. Only one should be  
             // specified.  
             // targetLeft: 0,  
             targetRight: 1,  
   
             // Represents distance from top or bottom browser window  
             // border depending upon property used. Only one should be  
             // specified.  
             targetTop: 6,  
             // targetBottom: 0,  
   
             // Uncomment one of those if you need centering on  
             // X- or Y- axis.  
             // centerX: true,  
             // centerY: true,  
   
             // Remove this one if you don't want snap effect  
             snap: true  
             });  
 </script>  


<link rel="stylesheet" href="/Correction/css/correction/correction.css" type="text/css"></link>


</head>
  
<body bgcolor="white" onload="setUp(<jsp:getProperty name="uiBean" property="editMode"/>,<%=session.getMaxInactiveInterval() %>);">
<div id="overDiv" style="position:absolute; visibility:hidden; z-index:1000;"></div>

  <a name="top"></a>
<div class="pageDiv">
  <div id="floatdiv" style="  
     position:absolute;  
     width:13px;height:8px;top:10px;right:10px;  
     padding:10px;  
     border:0px solid #2266AA;  
     z-index:100">  
<a style="text-decoration:none;" href="#top"><font color="gray">Top</font></a>  
 </div> 

<div id="mask" >
<div class="tabHolder">
	<div class="contentTab" id="contentTab" onmouseover="style.cursor='pointer'" onClick="toggleDiv('content','contentTab');"><jsp:getProperty name="uiBean" property="contentTabLabel"/></div>
	<div class="errorTab" id="errorTab" onmouseover="style.cursor='pointer'" onClick="toggleDiv('errorList','errorTab');"><jsp:getProperty name="uiBean" property="errorTabLabel"/></div>
	<div class="contentTab" id="downloadTab" onmouseover="style.cursor='pointer'" onclick="downloadDataFile();">download</div>
</div>
<span class="clearFloat"></span>
<div class="pageHeader">
<br/><jsp:getProperty name="uiBean" property="headerInfo"/><br/>
</div>
</div>


<span class="clearFloat"></span>
<div class="content" style="float:left" id="content"><jsp:getProperty name="uiBean" property="contentFormData"/></div>
<div class="errorList" style="float:left" id="errorList"><jsp:getProperty name="uiBean" property="errorListData"/></div>
<span class="clearFloat"></span>
</div>
</body></html>