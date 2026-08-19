package common

import org.openqa.selenium.Dimension
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class UIValidation {

	@Keyword
	def verifyElementProperty(TestObject object,
			String property,
			String expectedValue,
			int timeout = 10) {

		KeywordUtil.logInfo("====================================================")
		KeywordUtil.logInfo("UI Validation Started")
		KeywordUtil.logInfo("Property : ${property}")
		KeywordUtil.logInfo("Expected : ${expectedValue}")
		KeywordUtil.logInfo("====================================================")

		WebUI.waitForElementVisible(object, timeout)

		switch(property.toLowerCase()) {

			//---------------------------------------------------
			// Position
			//---------------------------------------------------
			case "position":

				verifyPosition(object, expectedValue, timeout)
				break

			//---------------------------------------------------
			// Background Color
			//---------------------------------------------------
			case "backgroundcolor":

				verifyCssProperty(object,
						"background-color",
						expectedValue)
				break

			//---------------------------------------------------
			// Text Color
			//---------------------------------------------------
			case "textcolor":

				verifyCssProperty(object,
						"color",
						expectedValue)
				break

			//---------------------------------------------------
			// Font Size
			//---------------------------------------------------
			case "fontsize":

				verifyCssProperty(object,
						"font-size",
						expectedValue)
				break

			//---------------------------------------------------
			// Font Weight
			//---------------------------------------------------
			case "fontweight":

				verifyCssProperty(object,
						"font-weight",
						expectedValue)
				break

			//---------------------------------------------------
			// Width
			//---------------------------------------------------
			case "width":

				verifyCssProperty(object,
						"width",
						expectedValue)
				break

			//---------------------------------------------------
			// Height
			//---------------------------------------------------
			case "height":

				verifyCssProperty(object,
						"height",
						expectedValue)
				break

			//---------------------------------------------------
			// Border Color
			//---------------------------------------------------
			case "bordercolor":

				verifyCssProperty(object,
						"border-color",
						expectedValue)
				break

			//---------------------------------------------------
			// Opacity
			//---------------------------------------------------
			case "opacity":

				verifyCssProperty(object,
						"opacity",
						expectedValue)
				break

			//---------------------------------------------------
			// Visibility
			//---------------------------------------------------
			case "visible":

				boolean actual = WebUI.verifyElementVisible(
						object,
						com.kms.katalon.core.model.FailureHandling.OPTIONAL)

				WebUI.verifyEqual(
						actual,
						Boolean.parseBoolean(expectedValue))

				KeywordUtil.markPassed("Visibility verified.")
				break

			default:

				KeywordUtil.markFailedAndStop(
						"Unsupported Property : ${property}")
		}
	}

	//---------------------------------------------------
	// Position Verification
	//---------------------------------------------------

	private void verifyPosition(TestObject object,
			String expected,
			int timeout) {

		WebElement element =
				WebUiCommonHelper.findWebElement(object, timeout)

		Dimension size =
				DriverFactory.getWebDriver()
				.manage()
				.window()
				.getSize()

		int pageWidth = size.getWidth()

		int x = element.getLocation().getX()

		boolean isRight = x > pageWidth/2

		KeywordUtil.logInfo("Browser Width : ${pageWidth}")
		KeywordUtil.logInfo("Element X : ${x}")

		if(expected.equalsIgnoreCase("Right")) {

			WebUI.verifyEqual(isRight,true)

		}else {

			WebUI.verifyEqual(isRight,false)
		}

		KeywordUtil.markPassed("Position verified.")
	}

	//---------------------------------------------------
	// Generic CSS Verification
	//---------------------------------------------------

	private void verifyCssProperty(TestObject object,
			String cssProperty,
			String expectedValue) {

		String actualValue =
				WebUI.getCSSValue(object, cssProperty)

		KeywordUtil.logInfo(cssProperty +
				" : " + actualValue)

		// Normalize both values before comparison
String actual = actualValue.trim()
String expected = expectedValue.trim()

// Convert rgba(r,g,b,1) -> rgb(r,g,b)
actual = actual.replaceAll(/rgba\((\d+),\s*(\d+),\s*(\d+),\s*1(\.0)?\)/, 'rgb($1, $2, $3)')

// Remove extra spaces
actual = actual.replaceAll(/\s+/, " ")
expected = expected.replaceAll(/\s+/, " ")

KeywordUtil.logInfo("Actual   : ${actual}")
KeywordUtil.logInfo("Expected : ${expected}")

WebUI.verifyEqual(actual, expected)
		

		KeywordUtil.markPassed(cssProperty + " verified.")
	}
}