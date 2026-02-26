package common

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import org.openqa.selenium.WebElement
import org.openqa.selenium.By

import com.kms.katalon.core.testobject.ConditionType


public class TableDataHelper {

    // ==========================
    // DATA STORES
    // ==========================
    static Map<String, List<String>> store = [:]
    static List<String> softFailures = []

    // ==========================
    // SOFT ASSERT HELPERS
    // ==========================
    static void addSoftFailure(String message) {
        KeywordUtil.logInfo("❌ SOFT ASSERT → ${message}")
        softFailures.add(message)
    }

    static List<String> getSoftFailures() {
        return softFailures
    }

    static void assertAll() {
        if (!softFailures.isEmpty()) {
            throw new com.kms.katalon.core.exception.StepFailedException(
                "❌ SOFT ASSERT FAILURES (${softFailures.size()}):\n" +
                softFailures.join("\n")
            )
        }
        KeywordUtil.logInfo("✅ ALL SOFT ASSERTS PASSED")
    }

    // ==========================
    // SAFE CLICK
    // ==========================
    def safeClick(WebElement element) {
        WebUI.executeJavaScript(
            "arguments[0].scrollIntoView({block:'center'});",
            [element]
        )
        WebUI.delay(1)
        WebUI.executeJavaScript(
            "arguments[0].click();",
            [element]
        )
    }

    // ==========================
    // READ-ONLY VALIDATION
    // ==========================
    def verifyReadOnlyPage() {

        KeywordUtil.logInfo("🔎 Verifying read-only business fields")

        TestObject fieldsObj = new TestObject().addProperty(
            "xpath",
            ConditionType.EQUALS,
            "//input[not(@type='checkbox') and not(@type='file')] | //textarea | //select"
        )

        List<WebElement> fields =
            WebUiCommonHelper.findWebElements(fieldsObj, 5)

        fields.each { el ->

            String ref =
                el.getAttribute("name") ?:
                el.getAttribute("id") ?:
                "unknown-field"

            // Skip known non-data controls
            if (ref.toLowerCase().contains("search") ||
                ref.toLowerCase().contains("filter")) {
                return
            }

            if (el.isEnabled()) {
                addSoftFailure("Editable field found: ${ref}")
            }
        }

        KeywordUtil.logInfo("✅ Read-only validation finished")
    }

    // ==========================
    // READ & STORE TABLE DATA
    // ==========================
    @Keyword
    def readAndStore(String key, TestObject tableObj, int columnIndex) {

        WebElement table =
            WebUiCommonHelper.findWebElement(tableObj, 20)

        List<WebElement> rows =
            table.findElements(By.xpath(".//tbody/tr"))

        List<String> values = rows.collect {
            def cells = it.findElements(By.tagName("td"))
            if (cells.size() > columnIndex) {
                def txt = cells[columnIndex].getText().trim()
                if (
    txt.equalsIgnoreCase("Location Name") ||
    txt.equalsIgnoreCase("Business Name") ||
    txt.equalsIgnoreCase("Last Name")
) {
    return null
}
return txt
            }
            return null
        }.findAll { it != null }.unique()

        store[key] = values
        KeywordUtil.logInfo("📥 STORED ${key}: ${values}")
    }

    // ==========================
    // COMPARE + CLICK VIEW
    // ==========================
    @Keyword
    def compareAndClickView(
        String key,
        TestObject tableObj,
        int columnIndex,
        String viewBtnXpath,
        boolean verifyReadOnly = true
    ) {

        if (!store.containsKey(key)) {
            addSoftFailure("${key} has no stored baseline data")
            return
        }

        WebElement table =
            WebUiCommonHelper.findWebElement(tableObj, 20)

        List<WebElement> rows =
            table.findElements(By.xpath(".//tbody/tr"))

        List<String> evaaValues = rows.collect {
            def cells = it.findElements(By.tagName("td"))
            if (cells.size() > columnIndex) {
                def txt = cells[columnIndex].getText().trim()
                return txt ? txt : null
            }
            return null
        }.findAll { it != null }.unique()

        List<String> matched   = evaaValues.intersect(store[key])
        List<String> unmatched = evaaValues - store[key]
        List<String> missing   = store[key] - evaaValues

        if (!missing.isEmpty())
            addSoftFailure("${key} missing records: ${missing}")

        KeywordUtil.logInfo("✅ MATCHED ${key}: ${matched}")
        KeywordUtil.logInfo("⚠️ UNMATCHED ${key}: ${unmatched}")

        // CLICK VIEW ONLY FOR MATCHED
        matched.each { value ->

            WebElement freshTable =
                WebUiCommonHelper.findWebElement(tableObj, 20)

            WebElement row = freshTable
                .findElements(By.xpath(".//tbody/tr"))
                .find {
                    it.findElements(By.tagName("td"))[columnIndex]
                        .getText().trim() == value
                }

            if (!row) {
                addSoftFailure("Row not found for ${key}: ${value}")
                return
            }

            WebElement viewBtn =
                row.findElement(By.xpath(viewBtnXpath))

            safeClick(viewBtn)
            WebUI.delay(2)

            if (verifyReadOnly)
                verifyReadOnlyPage()

            // Close using X
            WebUI.click(findTestObject(
                'Object Repository/Evaa Locations/Page_Unified Admin/Close Button'
            ))
            WebUI.delay(1)

            // Optional discard popup
            TestObject discardYes =
                findTestObject(
                    'Object Repository/Evaa Locations/Page_Unified Admin/Discard Changes Yes Button'
                )

            if (WebUI.verifyElementPresent(
                discardYes, 2, FailureHandling.OPTIONAL)) {
                WebUI.click(discardYes)
                WebUI.delay(1)
            }
        }
    }
}
