package color

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject

class VerifyColor {

	@Keyword
	def verifyColor(TestObject object, String expectedRGB) {

		// Get CSS color from object
		String actual = WebUI.getCSSValue(object, 'color')

		// Convert rgba → rgb
		if (actual.contains("rgba")) {
			actual = actual.replace("rgba", "rgb").replaceAll(",\\s?1\\)", ")")
		}

		// Cleanup spaces
		actual = actual.trim()

		println("Expected: " + expectedRGB)
		println("Actual: " + actual)

		// Verification
		WebUI.verifyEqual(actual, expectedRGB)
	}
}
