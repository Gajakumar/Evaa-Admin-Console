package common

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.*
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.WebElement
import org.openqa.selenium.Keys

class LocationHours {

    @Keyword
    def setWeeklySchedule(Map<String, Map> schedule) {

        schedule.each { day, values ->

            boolean isClosed = values.get("closed", false)

            // ===== Closed Checkbox =====
            TestObject closedCheckbox = new TestObject()
            closedCheckbox.addProperty(
                    "id",
                    ConditionType.EQUALS,
                    "closed-" + day
            )

            if (isClosed) {

                if (!WebUI.verifyElementChecked(closedCheckbox, 1, com.kms.katalon.core.model.FailureHandling.OPTIONAL)) {
                    WebUI.check(closedCheckbox)
                }

            } else {

                // If previously closed → uncheck first
                if (WebUI.verifyElementChecked(closedCheckbox, 1, com.kms.katalon.core.model.FailureHandling.OPTIONAL)) {
                    WebUI.uncheck(closedCheckbox)
                }

                String startTime = values.get("start")
                String endTime   = values.get("end")

                // ===== Locate Start Time Input =====
                TestObject startInput = new TestObject()
                startInput.addProperty(
                        "xpath",
                        ConditionType.EQUALS,
                        "//div[text()='" + day + "']/following::input[@type='time'][1]"
                )

                // ===== Locate End Time Input =====
                TestObject endInput = new TestObject()
                endInput.addProperty(
                        "xpath",
                        ConditionType.EQUALS,
                        "//div[text()='" + day + "']/following::input[@type='time'][2]"
                )

                // ===== Set Values (React Safe) =====
                setReactTime(startInput, startTime)
                setReactTime(endInput, endTime)

                // ===== Wait Until Persisted =====
                WebUI.waitForElementAttributeValue(startInput, "value", startTime, 10)
                WebUI.waitForElementAttributeValue(endInput, "value", endTime, 10)
            }
        }
    }


    /**
     * React-safe setter for controlled inputs
     */
private void setReactTime(TestObject to, String timeValue) {

    WebElement element = WebUiCommonHelper.findWebElement(to, 10)

    String script =
        "var input = arguments[0];" +
        "var value = arguments[1];" +
        "var nativeInputValueSetter = " +
        "Object.getOwnPropertyDescriptor(" +
        "window.HTMLInputElement.prototype, 'value').set;" +
        "nativeInputValueSetter.call(input, value);" +
        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
        "input.dispatchEvent(new Event('change', { bubbles: true }));"

    DriverFactory.getWebDriver().executeScript(script, element, timeValue)
}
}