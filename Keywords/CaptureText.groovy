import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

class CaptureText {

	@Keyword
	def storeInnerTextToGlobal(String xpathLocator, String globalVarName) {

		// Create dynamic TestObject
		TestObject obj = new TestObject("dynamicObject")
		obj.addProperty("xpath", ConditionType.EQUALS, xpathLocator)

		// Capture inner text
		String text = WebUI.getText(obj)

		// Store value in requested GlobalVariable
		GlobalVariable.metaClass."$globalVarName" = text

		WebUI.comment("Stored value [" + text + "] in GlobalVariable." + globalVarName)
	}
}