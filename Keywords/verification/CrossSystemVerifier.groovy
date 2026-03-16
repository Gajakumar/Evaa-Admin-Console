package verification

import storage.DataStore
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.util.KeywordUtil
import storage.TableStore
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import org.openqa.selenium.WebElement
import org.openqa.selenium.By

class CrossSystemVerifier {

    @Keyword
    def captureFields(Map fieldMap) {

        fieldMap.each { fieldName, objects ->

            TestObject maxObj = objects[0]

            String value = WebUI.getAttribute(maxObj, "value")?.trim()

            DataStore.data.put(fieldName, value)

            KeywordUtil.logInfo("Stored MaximEyes -> ${fieldName} : ${value}")
        }
    }

    @Keyword
    def verifyFields(Map fieldMap) {

        fieldMap.each { fieldName, objects ->

            TestObject evaaObj = objects[1]

            String expected = DataStore.data.get(fieldName)
            String actual = WebUI.getAttribute(evaaObj, "value")?.trim()

            if(expected == actual) {
                KeywordUtil.markPassed("✔ ${fieldName} matched : ${actual}")
            }
            else {
                KeywordUtil.markFailed("✘ ${fieldName} mismatch | Max: ${expected} | EVAA: ${actual}")
            }
        }
    }
	
	@Keyword
	def captureMaximEyesLocations(TestObject tableRows) {
	
		List<WebElement> rows =
			WebUiCommonHelper.findWebElements(tableRows, 10)
	
		rows.each { row ->
	
			List<WebElement> cols = row.findElements(By.tagName("td"))
	
			String locationName = cols[0].getText().trim()
	
			TableStore.maximeyesLocations.add(locationName)
		}
	
		println("MaximEyes Locations -> " + TableStore.maximeyesLocations)
	}
	
	@Keyword
	def captureEvaaLocations(TestObject tableRows) {
	
		List<WebElement> rows =
			WebUiCommonHelper.findWebElements(tableRows, 10)
	
		rows.each { row ->
	
			List<WebElement> cols = row.findElements(By.tagName("td"))
	
			String locationName = cols[0].getText().trim()
	
			TableStore.evaaLocations.add(locationName)
		}
	
		println("EVAA Locations -> " + TableStore.evaaLocations)
	}
	
	@Keyword
	def compareLocations() {
	
		if (TableStore.maximeyesLocations == TableStore.evaaLocations) {
	
			println("✅ Locations Match")
	
		} else {
	
			println("❌ Locations Mismatch")
	
			println("MaximEyes: " + TableStore.maximeyesLocations)
			println("EVAA: " + TableStore.evaaLocations)
		}
	}

}
