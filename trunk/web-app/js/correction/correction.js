var ajaxUrl="/Correction/correction/";  //base url, add "action" to end of url.
var originalVal="~NULL~";
var interminVal="";
var editedVal="";
var hasError=false;
var delImage = "/Correction/images/correction/erase.png";
var revertImage = "/Correction/images/correction/undo.png"
var sessionThreshold = 3600; // in secs
var sessionTimeout;
var sessionAboutToExpire=false;
var t;
var t2;

$(document).ready(function() {
	$(".errorTable tr:even").addClass("alt");
	
   // bind 'myForm' and provide a simple callback function 
	  $('#correctionREM').ajaxForm({ 
	        // dataType identifies the expected content type of the server response 
	        dataType:'jsonp', 
	        success:postSubmitForm
	    });
	  
	 //ajax submission of form..	
	  $('#correction').ajaxForm({
          //alert("Thank you for your comment!");
		  dataType:  'jsonp', 
		  //json: 'callback',
		  success : function(data, status, XMLHttpRequest){
			  //alert("back from call:"+status);
			  this.response = {};
			  response = data;
			  //alert("data>>>>>>>>>>:"+data.STUFF);
			  postSubmitForm(response);
		  }
		  //success: postSubmitForm
		  //postSubmitForm(success.STUFF);
      }); 
});


//Called by onBody of renderui.jsp. Sets up the page with form visible and errors hidden 
//and sets the state of the tabs.
function setUp(mode,maxInactive) {
	sessionTimeout=maxInactive;
	//alert("session timeout:"+maxInactive+" seconds to 10 mins to test.. remove this in correction.js");
	//TODO: remove this later!
	//sessionTimeout=600; use for testing...
	toggleDiv('content','contentTab');
	setEditMode(mode);
	setSessionTimeout();
}

// ----------- Session time out warning and timers  --------------------
function setSessionTimeout(){
	t = setTimeout(function(){ alertUserOfPendingTimeout() },(sessionTimeout * 1000)- (sessionThreshold * 1000));
	t2 = setTimeout(function() { alertUserOfTimeout() },(sessionTimeout * 1000));
}

//this should be called on page load and when ever an Ajax call is done...
function resetSessionTimeout() {
	clearTimeout(t);
	clearTimeout(t2);
	//alert("cleared time out, set about to expire to false");
	sessionAboutToExpire=false;
	t  = setTimeout(function(){ alertUserOfPendingTimeout() },(sessionTimeout * 1000)- (sessionThreshold * 1000));
	t2 = setTimeout(function() { alertUserOfTimeout() },(sessionTimeout * 1000));
}

function alertUserOfPendingTimeout(){
	sessionAboutToExpire = true;
	//alert('Your session will time out in '+sessionThreshold/60+' minutes due to inactivity!');
}

function alertUserOfTimeout(){
	setEditMode(false)
	
	//alert('Your session has timed out due to inactivity!');
}

//---------------- Disabling/enabling of form input --------------------------
// used when the form is Read-only
function setEditMode(mode){
	if(mode==true)
		enableAllInput();
	else
		disableAllInput();
}

//used when the correction page is in Read-Only mode.
function disableAllInput() {
	$(':input').attr('disabled', true);
	$(':input').addClass("formInputDisabled");
	$('.modelnodeheadernodelist').css('display', 'none');
	$('.modelnodeheaderadd').html('&nbsp;');
	$('.modelpropertydelete').html('&nbsp;');
	$('.propertyrowdelete').html('&nbsp;');
}

//not currently used, but here in case it is needed later..
function enableAllInput(){
	$(':input').attr('enabled', true);
}
//----------------- End of disable/enable form input -----------------

//----------------- Toggling of Node Visibility ----------------------

function toggleNodeVisibility(node,nodeid) {
	//alert("nodeid:"+nodeid);
	var nodeInfo 	 = nodeid.split("-");
	var nodeName 	 = nodeInfo[0];
	var id			 = nodeInfo[1];
	//var currentHeight= nodeInfo[2];
	keepSessionAlive();
	currentState=$(node).html();
	//alert("current state:"+currentState+"  currentHeight:"+currentHeight);
	if(currentState=='-'){
		$(node).html("+");
		//var height=$('#'+id).height();
		//$('#'+id).height("20");
		//alert("setting "+id+" to be hidden...");
		$('#DIV'+id).slideUp("slow");
		$('#DIV'+id).attr('disabled', true);
		//alert("setting node "+id);
		//$(node).attr('id',nodeName+":"+id+":"+height);
	} else {
		$(node).html("-");
		//$(node).height(currentHeight);
		//$('#'+id).height(currentHeight);
		$('#DIV'+id).attr('disabled', false);
		$('#DIV'+id).slideDown("slow");
		//scroll the expanded node to top of page (note that amount of scroll depends on position of current node- browser may determine final position)
		$('html,body').animate({scrollTop: $(node).offset().top},'slow');
	}
	//alert(currentState);
}

//not used yet
function showAllDivs() {
	$('#1').children().show();
}
//not used yet
function hideAllDivs(){
	$('#1').children().hide();
}
//----------------- Form Validation Functions -----------------------

//called by onFocus
function setOriginalValue(val) {
		originalVal=val;  //capture the original before editing.
}

//called by onBlur in the form
//A short time out is used to allow page to be ready.
//Code regarding original value commented out, in case it is needed to be re-introduced.
function setEditedValue(val,datatype,inputField) {
	keepSessionAlive();
	editedVal=val;
	if(validateFormInput(datatype)) {
		hasError = false;
		//alert('valid input, a string, no change, a or blank value entered');
		//originalValue='~NULL~'; //start over again.
	}else {
		hasError = true;
		alert(val+' is not valid for the type: '+datatype+'\nPlease correct.');
		//inputField.value=originalVal;
		//originalValue='~NULL~';
		setTimeout(function(){$('#'+inputField.select())},100);		
	}
}

//Validate the field tabbed from. 
function validateFormInput(datatype) {	
	//alert(originalVal+' - '+datatype+' - '+editedVal);
	/* if(originalVal!= editedVal) {
		if(editedVal == "" || datatype=="String") {
			return true;
		} */
		//alert("validating using:"+datatype+"  for:"+editedVal);
		if(editedVal == "") {
			return true; // there is not a required attribute in the UFC as yet!
		}
		if(datatype == 'Amount' || datatype == 'Double' || datatype == 'Integer') {
			if(isNaN(editedVal)){
				return false;
			}
		} else if (datatype == 'Date') {
			try {
				var theDate = new Date(editedVal);
				if(theDate=='Invalid Date') {
					return false;
				}
				return true;
			}catch(err) {
				return false;
			}
		} else if (datatype == 'Boolean') {
			if(editedVal.toLowerCase()=='false' || editedVal.toLowerCase()=='true'){
				return true;
			} else {
				return false;
			}
		}else if(datatype=='Time'){
			return timeValidation(editedVal);
		}
		return true;
/*	} else {  //no change	
		return true;
	} */
}

function timeValidation(value){

    // regular expression to match required time format
    re =  /^(\d{1,2}):(\d{2}):(\d{2})(.000)?$/;


    if(value != '') {
        if(regs = value.match(re)) {
          if(regs[3]) {
            if(regs[1] < 1 || regs[1] > 12) {
              
              return false;
            }
          } else {
            if(regs[1] > 23) {
            
              return false;      
            }
          }
          if(regs[2] > 59) {
           
            return false;      
          }
          if(regs[3] > 59) {
            
              return false;      
            }
        } else {
          //alert("Invalid time format: " + value);
         
          return false;
        }
    }
    
    return true;
}

//---------------------- End Form Validation functions ----------

//-------------------- Form Submission -----------------------

//make sure you have an event listener in the document.onready area for this formName!!

var downloadFile=false;
function downloadDataFile(){
	downloadFile =true;
	
	$("#mask").mask("Waiting...");

	submitForm('correction');
	
	
}

function submitForm(formName) {
	enableAllInput(); //so it all gets submitted, including hidden divs.
	resetSessionTimeout();
	$('#'+formName).submit(); 
}

//function to handle post from processing. For now, it just passes a status msessage (JSON).
function postSubmitForm(data) {
	
	if(downloadFile){
		location.href="downloadFile"
	}
	$("#mask").unmask();

	downloadFile=false;
}

//-------------- End Form Submission -----------------------

//------------ Keep alive function.. so session doesnt expire when user is active, but not with server --------
function keepSessionAlive(){
	if(sessionAboutToExpire==true){
		ajaxCall('dummy=dummy','keepSessionAliveResponse',"keepAlive");
	}
	//else { alert('no need to do keep-alive'); }
}

function keepSessionAliveResponse() {
	//alert("session kept alive...");
	resetSessionTimeout();
}

//---------------------- Node/Property Retrieval ---------------

//Called by "on select" of the "Add.." Select list at a node level....
//The input is the new node identifier(option value="[input]", which should tell the backend where to find
//the node in the model..  
//A formatted child node should be returned, which includes all editable properties and..
//A select list of available nodes and properties.
function addNodeRequest(input,nodeid){
	//input = the option value- the identifier for the node that needs created on the parent in the model.
	if(input=="") return; //in case they select 'Add..'
	var nodeInfo 	 = nodeid.split("-");
	var nodeName 	 = nodeInfo[0];
	var id			 = nodeInfo[1]; //this id (with the correct prefix, identifies the current div and the child div enclosing the props-where the new node or prop goes.
	//get the current padding of the parent. 
	//The backend should provide the child padding and apply.
	var padInfo = $("#NODELBL-"+id).css("padding-left").split("px");
	var pad = padInfo[0];

	//alert("The Current padding for NODELBL-"+id+":"+pad);
	
	//arg 1 -input-should be the querystring, 
	//arg 2 -call back function
	//arg 3 - appends to the url, the "action" in the controller to be invoked.
	var querystring="pad="+pad+"&selected="+input+"&parentnode="+id;
	ajaxCall(querystring,'addNodeResponse',"addNode");
}

//this is the response handler for the add node ajax call.
function addNodeResponse(data,querystring,nodeid){
	//alert("response: addNodeResponse called with:"+data.node+" put in DIV"+data.divid);
	resetSessionTimeout();
	if(data.error != null)
	{
		alert(data.error);
		return;
	}
	$("#DIVPROP-"+data.divid).append(data.node);
}


//----------------- Node Delete/Revert -------------------
function toggleDeleteRevertNode(id,div) {
	//alert("Delete node:"+id);
	
	var divInfo=id.split("-");
	var targetDiv=divInfo[1];
	//$('#'+id).html("xxx");
	//$(div).text("zzz");
	//alert("looking at:HID-"+targetDiv);
	//($('"#W"+num').attr('disabled',false)) 
	//("#E"+num).is(':disabled'))
	
	//if the hidden field at the node level is disabled, cannot use the Delete/Revert functionality.
	if( $("#HID-"+targetDiv).is(':disabled') ){
		alert("the current div is disabled?");
		return;
	}
	
	//fragility point here.. "erase" is part of the image name.
	if($(div).html().indexOf("erase") > -1) {
		$(div).html("<img onmouseover=\"style.cursor='pointer';\" height=\"22px\" width=\"22px\" src=\"/Correction/images/correction/undo.png\"></img>");
		deleteNode(targetDiv);
	} else {
		$(div).html("<img onmouseover=\"style.cursor='pointer';\" height=\"15px\" width=\"15px\" src=\"/Correction/images/correction/erase.png\"></img>");
		revertNode(targetDiv);
	}
}
//$("ul.topnav > li").css("border", "3px double red");
function deleteNode(div) {
	//alert("delete node called.");
	$("#DIV"+div).attr('disabled', true);//delete later?
	$("#DIV"+div).addClass("divDeleted");
	$('#DIV'+div+' :input').attr('disabled', true);
	//$('#SEL-'+div+' :input').attr('disabled', true);
	$('#SEL-'+div).attr('disabled', true);
	$('#NODELBL-'+div).addClass('divDeleted');

	
	//	$('#DIV'+div+' :div').attr('disabled', true); //testing
//	$('#DIV'+div).each(function () { //doesnt work...
  //      this.onclick = undefined;
//	});
}

function revertNode(div) {
	$("#DIV"+div).attr('disabled', false); //deletet later?
	$('#DIV'+div+' :input').attr('disabled', false);
	$("#DIV"+div).removeClass("divDeleted");
	//$('#SEL-'+div+' :input').attr('disabled', false);
	$('#SEL-'+div).attr('disabled', false);
	$('#NODELBL-'+div).removeClass('divDeleted'); //IE ignoring..
	$('#NODELBL-'+div).removeAttr('divDeleted');  //IE works.
	
}
 //----------------------- AJAX functions --------------------
//function ajaxCall(criteria,callback,nodeid,action ){
function ajaxCall(querystring,callback,action ){
	//alert('ajaxRetrieve called with '+querystring);
	  //var currentClaimSearchURL = portalServerUrl +'/.UI_OAClaimsReview_Portlet/lniECSClaimsView?claimId='+claimId+'&operation='+operation;	
	  $.ajax({
		  type: "get",
		  url: ajaxUrl+action+"?"+querystring,
		  dataType: "jsonp",
		  cache: false,
		  jsonp: 'jsonp_callback',
		  success : function(data, status, XMLHttpRequest){
			  //alert("back from call:"+status);
			  this.response = {};
			  response = data;
			  //responseString = JSON.stringify(response);
			  //alert(response.STUFF);
			  if(functionIsAllowed(callback)){
				  eval(''+callback+'(data,querystring)');
			  }else { 
				  alert("Function is not allowed!!!"); 
			  }
		  }					
	  }); 
}
	

function remove(data,criteria){
	alert("delete handler-test ajax:"+data.STUFF);
}

function add(data,criteria){
	alert("add handler-test ajax:"+data.STUFF);
}

//Limit what may be evaluated by "eval()"
function functionIsAllowed(callback) {
	if(callback=="addNodeResponse" || callback=="keepSessionAliveResponse" || callback=="add" || callback=="remove") {
		return true;
	} else {
		return false;
	}
}
//-------------- End AJAX Functions ---------------------

//--------------- Error Navigation ----------------------

function navigateError(errorid) {
	toggleDiv('content','contentTab');
	setTimeout(function(){showError(errorid)},750);
}

function showError(errorid) {

	location.hash = "#" + errorid;
}

   // location.hash = "#" + hash;

//--------------- End Error Navigation --------------------

//---------------- Toggle tabs and divs -------------------

function toggleDiv(divID,tabID){
	//alert("showing div:"+divID);
	keepSessionAlive();
	$('#content').hide();
	$('#errorList').hide();
	$('#contentTab').css("background-color","#EBE6E7");
	$('#errorTab').css("background-color","#EBE6E7");
	$('#contentTab').css("color","#A8A2A3");
	$('#errorTab').css("color","#A8A2A3");
	$('#'+divID).slideDown("slow");
	$('#'+tabID).css("background-color","#E0DEDF");
	$('#'+tabID).css("color","#000000");
}


//not currently used... 
function ajax(criteria,callback) {
	alert("called ajaxTest..."+criteria+" -"+callback);
	$.getJSON(ajaxUrl+'?criteria='+criteria+'&jsonp_callback=?', callback);
}
