package common

import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

class DropdownHelper {

    @Keyword
    def selectIfMultiple(TestObject dropdown, String value, String fieldName) {

        WebUI.waitForElementVisible(dropdown, 10)
        WebUI.waitForElementClickable(dropdown, 10)

        WebElement element = WebUiCommonHelper.findWebElement(dropdown, 10)
        Select select = new Select(element)

        List<WebElement> options = select.getOptions().findAll {
            it.getAttribute("value") != null && !it.getAttribute("value").trim().isEmpty()
        }

        KeywordUtil.logInfo("${fieldName} options available: ${options.size()}")

        if (options.size() == 1) {
            String selectedValue = select.getFirstSelectedOption().getText().trim()
            KeywordUtil.markPassed("Only one ${fieldName} available. Auto-selected: ${selectedValue}")
        } else {
            select.selectByVisibleText(value)   // pure Selenium — no internal frame switch-out
            String selectedValue = select.getFirstSelectedOption().getText().trim()
            KeywordUtil.markPassed("${fieldName} selected successfully: ${selectedValue}")
        }

        WebUI.switchToDefaultContent()   // safe reset, doesn't need to re-locate the iframe
    }
}
