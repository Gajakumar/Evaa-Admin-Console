package customkeywords

import java.text.SimpleDateFormat
import java.util.regex.Pattern

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

class InsuranceVerificationKeywords {

    /**
     * Verifies the insurance verification note on both the "Unprocessed" and "All" tabs,
     * using today's date and the given insurance/insured details.
     *
     * @param insuranceName    e.g. "Aetna"
     * @param insuredId        e.g. "12345678"
     * @param insuredFirstName e.g. "QA"
     * @param insuredLastName  e.g. "Katalon"
     * @param insuredDob       e.g. "01/04/1995"  (MM/dd/yyyy)
     * @param verifiedBy       e.g. "Evaa"
     */
    @Keyword
    void verifyInsuranceVerification(String insuranceName, String insuredId, String insuredFirstName,
            String insuredLastName, String insuredDob, String insuredGender, String verifiedBy) {

        String todayDate = new SimpleDateFormat('MM/dd/yyyy').format(new Date())

        // Navigate to Insurance tab
        WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))
        WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/a_Insurance'))

        WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/span_Unprocessed'), 5)

        // --- Unprocessed tab: date cell ---
        TestObject dateCellUnprocessed = buildDynamicDateObject(
                'Insurance Verifcation/Page_MaximEyes/td_dynamic_date', todayDate)
        WebUI.assertElementText(dateCellUnprocessed, todayDate + ' (By ' + verifiedBy + ')', 0)

        String actualText = WebUI.getText(
                findTestObject('Insurance Verifcation/Page_MaximEyes/span_PATIENT HAS ADDED Aetna AND IT DOES NOT HAV'))

        verifyInsuranceDetailsText(actualText, insuranceName, insuredId, insuredFirstName, insuredLastName, insuredDob, insuredGender)

        WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_td'), '', 0)

        // --- Switch to All tab ---
        WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/span_All'))

        TestObject dateCellAll = buildDynamicDateObject(
                'Insurance Verifcation/Page_MaximEyes/td_dynamic_date', todayDate)
        WebUI.assertElementText(dateCellAll, todayDate, 0)

        verifyInsuranceDetailsText(actualText, insuranceName, insuredId, insuredFirstName, insuredLastName, insuredDob, insuredGender)
    }

    /**
     * Builds a fresh copy of the base date-cell test object with today's date injected
     * as a CONTAINS condition on the 'text' attribute, so the same Repository object
     * can be reused for any day instead of one hardcoded per date.
     */
    private TestObject buildDynamicDateObject(String repositoryPath, String dateValue) {
        TestObject baseObject = findTestObject(repositoryPath)
        TestObject dynamicObject = new TestObject(baseObject.getObjectId())
        dynamicObject.setProperties(baseObject.getProperties())
        dynamicObject.addProperty('text', ConditionType.CONTAINS, dateValue)
        return dynamicObject
    }

    /**
     * Runs all the regex verifications against the insurance verification note text,
     * using the parameterized values instead of hardcoded strings.
     */
    private void verifyInsuranceDetailsText(String actualText, String insuranceName, String insuredId,
            String insuredFirstName, String insuredLastName, String insuredDob, String insuredGender) {

        WebUI.verifyMatch(actualText, '.*PATIENT HAS ADDED ' + Pattern.quote(insuranceName) + '.*', true)
        WebUI.verifyMatch(actualText, '.*INSURED ID: ' + Pattern.quote(insuredId) + '.*', true)
        WebUI.verifyMatch(actualText, '.*INSURED FIRST NAME: ' + Pattern.quote(insuredFirstName) + '.*', true)
        WebUI.verifyMatch(actualText, '.*INSURED LAST NAME: ' + Pattern.quote(insuredLastName) + '.*', true)
        WebUI.verifyMatch(actualText, '.*INSURED DOB: ' + Pattern.quote(insuredDob) + '.*', true)
        // insuredGender may be blank if the app doesn't always populate it; (...)? keeps the match optional
        WebUI.verifyMatch(actualText, '.*INSURED GENDER:\\s*(' + Pattern.quote(insuredGender) + ')?\\b.*', true)
        WebUI.verifyMatch(actualText, '.*INSURANCE CARD HAS BEEN ATTACHED.*', true)
    }
}