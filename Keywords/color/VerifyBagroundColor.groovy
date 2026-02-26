

package color

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.model.FailureHandling

class VerifyBagroundColor {

	/**
	 * Verifies CSS color (text, background, border, etc.)
	 * @param object TestObject
	 * @param cssProperty color | background-color | border-color
	 * @param expectedRGB e.g. "rgb(126, 46, 239)"
	 */
	@Keyword
	def verifyBagroundColor(TestObject object, String cssProperty, String expectedRGB) {

		String actual = WebUI.getCSSValue(object, cssProperty).trim()

		// Normalize rgba → rgb
		if (actual.startsWith("rgba")) {
			actual = actual
					.replace("rgba", "rgb")
					.replaceAll(",\\s*[0-9.]+\\)", ")")
		}

		KeywordUtil.logInfo("CSS Property : " + cssProperty)
		KeywordUtil.logInfo("Expected     : " + expectedRGB)
		KeywordUtil.logInfo("Actual       : " + actual)

		WebUI.verifyEqual(
				actual,
				expectedRGB,
				FailureHandling.STOP_ON_FAILURE
				)
	}
}
