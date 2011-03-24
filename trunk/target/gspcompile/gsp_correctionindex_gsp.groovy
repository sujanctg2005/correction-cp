import org.codehaus.groovy.grails.plugins.metadata.GrailsPlugin
import org.codehaus.groovy.grails.web.pages.GroovyPage
import org.codehaus.groovy.grails.web.taglib.*
import org.codehaus.groovy.grails.web.taglib.exceptions.GrailsTagException
import org.springframework.web.util.*
import grails.util.GrailsUtil

class gsp_correctionindex_gsp extends GroovyPage {
public String getGroovyPageFileName() { "/WEB-INF/grails-app/views/index.gsp" }
public Object run() {
def params = binding.params
def request = binding.request
def flash = binding.flash
def response = binding.response
def out = binding.out
def codecOut = binding.codecOut
registerSitemeshPreprocessMode(request)
printHtmlPart(0)
body1 = new GroovyPageTagBody(this,binding.webRequest, {
printHtmlPart(1)
body2 = createClosureForHtmlPart(2)
invokeTag('captureTitle','sitemesh',4,[:],body2)
printHtmlPart(1)
})
invokeTag('captureHead','sitemesh',5,[:],body1)
printHtmlPart(3)
body1 = createClosureForHtmlPart(4)
invokeTag('captureBody','sitemesh',22,[:],body1)
printHtmlPart(5)
}
public static final Map JSP_TAGS = new HashMap()
protected void init() {
	this.jspTags = JSP_TAGS
}
public static final String CONTENT_TYPE = 'text/html;charset=UTF-8'
public static final long LAST_MODIFIED = 1299841796000L
public static final String DEFAULT_CODEC = null
}
