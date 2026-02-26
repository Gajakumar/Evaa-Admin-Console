

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


public class VariableStories {

	@Keyword
	def setItemToLOS(String key, Object value) {
		WebUI.executeJavaScript(
				"window.localStorage.setItem('${key}', ${JsonOutput.toJson(value)});",
				null
				)
	}

	@Keyword
	def Object getItemFromLOS(String key) {
		def json = WebUI.executeJavaScript(
				"return window.localStorage.getItem('${key}');",
				null
				)
		return json ? new JsonSlurper().parseText(json) : null
	}
}