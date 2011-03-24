package org.apache.jsp.WEB_002dINF.grails_002dapp.views.correction;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.edifecs.elm.ucf.domain.Model;
import com.edifecs.correction.utility.CorrectionUIBean;

public final class renderui_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write(" \r\n");
      out.write("\r\n");
      out.write("\r\n");
      com.edifecs.correction.utility.CorrectionUIBean uiBean = null;
      synchronized (session) {
        uiBean = (com.edifecs.correction.utility.CorrectionUIBean) _jspx_page_context.getAttribute("uiBean", PageContext.SESSION_SCOPE);
        if (uiBean == null){
          uiBean = new com.edifecs.correction.utility.CorrectionUIBean();
          _jspx_page_context.setAttribute("uiBean", uiBean, PageContext.SESSION_SCOPE);
        }
      }
      out.write("\r\n");
      out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> \r\n");
      out.write("<html><head><title>Exception Correction</title>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/Correction/js/correction/jquery-1.5.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/Correction/js/correction/floating-1.5.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/Correction/js/correction/jquery.qtip-1.0.0-rc3.min.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/Correction/js/correction/jquery.form.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/Correction/js/correction/overlib.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"/Correction/js/correction/correction.js\"></script>\r\n");
      out.write("\r\n");
      out.write(" <script type=\"text/javascript\">  \r\n");
      out.write("     floatingMenu.add('floatdiv',  \r\n");
      out.write("         {  \r\n");
      out.write("             // Represents distance from left or right browser window  \r\n");
      out.write("             // border depending upon property used. Only one should be  \r\n");
      out.write("             // specified.  \r\n");
      out.write("             // targetLeft: 0,  \r\n");
      out.write("             targetRight: 1,  \r\n");
      out.write("   \r\n");
      out.write("             // Represents distance from top or bottom browser window  \r\n");
      out.write("             // border depending upon property used. Only one should be  \r\n");
      out.write("             // specified.  \r\n");
      out.write("             targetTop: 6,  \r\n");
      out.write("             // targetBottom: 0,  \r\n");
      out.write("   \r\n");
      out.write("             // Uncomment one of those if you need centering on  \r\n");
      out.write("             // X- or Y- axis.  \r\n");
      out.write("             // centerX: true,  \r\n");
      out.write("             // centerY: true,  \r\n");
      out.write("   \r\n");
      out.write("             // Remove this one if you don't want snap effect  \r\n");
      out.write("             snap: true  \r\n");
      out.write("             });  \r\n");
      out.write(" </script>  \r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<link rel=\"stylesheet\" href=\"/Correction/css/correction/correction.css\" type=\"text/css\"></link>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("  \r\n");
      out.write("<body bgcolor=\"white\" onload=\"setUp(");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((com.edifecs.correction.utility.CorrectionUIBean)_jspx_page_context.findAttribute("uiBean")).isEditMode())));
      out.write(',');
      out.print(session.getMaxInactiveInterval() );
      out.write(");\">\r\n");
      out.write("<div id=\"overDiv\" style=\"position:absolute; visibility:hidden; z-index:1000;\"></div>\r\n");
      out.write("\r\n");
      out.write("  <a name=\"top\"></a>\r\n");
      out.write("<div class=\"pageDiv\">\r\n");
      out.write("  <div id=\"floatdiv\" style=\"  \r\n");
      out.write("     position:absolute;  \r\n");
      out.write("     width:13px;height:8px;top:10px;right:10px;  \r\n");
      out.write("     padding:10px;  \r\n");
      out.write("     border:0px solid #2266AA;  \r\n");
      out.write("     z-index:100\">  \r\n");
      out.write("<a style=\"text-decoration:none;\" href=\"#top\"><font color=\"gray\">Top</font></a>  \r\n");
      out.write(" </div> \r\n");
      out.write("\r\n");
      out.write("<div class=\"tabHolder\">\r\n");
      out.write("\t<div class=\"contentTab\" id=\"contentTab\" onmouseover=\"style.cursor='pointer'\" onClick=\"toggleDiv('content','contentTab');\">");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((com.edifecs.correction.utility.CorrectionUIBean)_jspx_page_context.findAttribute("uiBean")).getContentTabLabel())));
      out.write("</div>\r\n");
      out.write("\t<div class=\"errorTab\" id=\"errorTab\" onmouseover=\"style.cursor='pointer'\" onClick=\"toggleDiv('errorList','errorTab');\">");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((com.edifecs.correction.utility.CorrectionUIBean)_jspx_page_context.findAttribute("uiBean")).getErrorTabLabel())));
      out.write("</div>\r\n");
      out.write("\t<div class=\"contentTab\" id=\"saveTab\" onmouseover=\"style.cursor='pointer'\" onClick=\"submitForm('correction');\">SAVE</div>\r\n");
      out.write("\t<div class=\"contentTab\" id=\"downloadTab\" onmouseover=\"style.cursor='pointer'\" onclick=\"\"><a href=\"downloadFile\">download</div>\r\n");
      out.write("</div>\r\n");
      out.write("<span class=\"clearFloat\"></span>\r\n");
      out.write("<div class=\"pageHeader\">\r\n");
      out.write("<br/>");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((com.edifecs.correction.utility.CorrectionUIBean)_jspx_page_context.findAttribute("uiBean")).getHeaderInfo())));
      out.write("<br/>\r\n");
      out.write("</div>\r\n");
      out.write("<span class=\"clearFloat\"></span>\r\n");
      out.write("<div class=\"content\" style=\"float:left\" id=\"content\">");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((com.edifecs.correction.utility.CorrectionUIBean)_jspx_page_context.findAttribute("uiBean")).getContentFormData())));
      out.write("</div>\r\n");
      out.write("<div class=\"errorList\" style=\"float:left\" id=\"errorList\">");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((com.edifecs.correction.utility.CorrectionUIBean)_jspx_page_context.findAttribute("uiBean")).getErrorListData())));
      out.write("</div>\r\n");
      out.write("<span class=\"clearFloat\"></span>\r\n");
      out.write("</div>\r\n");
      out.write("</body></html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
