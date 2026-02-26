package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import org.openqa.selenium.WebElement

class InputUtils {

	@Keyword
	def clearAndSetInput(TestObject to, String value) {

		WebElement element = WebUI.findWebElement(to, 10)

		WebUI.executeJavaScript(
				'''
            var input = arguments[0];
            var newValue = arguments[1];
            var type = input.getAttribute('type');

            var nativeSetter = Object.getOwnPropertyDescriptor(
                window.HTMLInputElement.prototype,
                'value'
            ).set;

            // -------- CLEAR --------
            nativeSetter.call(input, '');
            input.dispatchEvent(new Event('input', { bubbles: true }));
            input.dispatchEvent(new Event('change', { bubbles: true }));
            input.dispatchEvent(new Event('blur', { bubbles: true }));

            // -------- SET --------
            if (type === 'date' && newValue) {

                var year, month, day;

                // yyyy-MM-dd
                if (/^\\d{4}-\\d{2}-\\d{2}$/.test(newValue)) {
                    var p = newValue.split('-');
                    year = p[0]; month = p[1]; day = p[2];
                }
                // MM/dd/yyyy
                else if (/^\\d{2}\\/\\d{2}\\/\\d{4}$/.test(newValue)) {
                    var p = newValue.split('/');
                    month = p[0]; day = p[1]; year = p[2];
                } else {
                    console.warn('Unsupported date format:', newValue);
                    return;
                }

                var dateObj = new Date(year, month - 1, day);

                if ('valueAsDate' in input) {
                    input.valueAsDate = dateObj;
                } else {
                    nativeSetter.call(input, year + '-' + month.padStart(2,'0') + '-' + day.padStart(2,'0'));
                }

            } else {
                nativeSetter.call(input, newValue);
            }

            input.dispatchEvent(new Event('input', { bubbles: true }));
            input.dispatchEvent(new Event('change', { bubbles: true }));
            input.dispatchEvent(new Event('blur', { bubbles: true }));
            ''',
				[element, value]
				)
	}
}
