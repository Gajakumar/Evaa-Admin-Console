package ui

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class FormValidationKeywords {

    @Keyword
    def submitForm(TestObject submitBtn) {
        WebUI.waitForElementClickable(submitBtn, 10)
        WebUI.click(submitBtn)
    }

    @Keyword
    def verifyErrorText(TestObject errorObj, String expectedText) {
        WebUI.waitForElementVisible(errorObj, 10)
        WebUI.verifyElementText(errorObj, expectedText)
    }
	
	
}
