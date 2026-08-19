import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.util.KeywordUtil


String firstName   = 'QA'
String lastName    = 'Katalon'

// STEP 1: Login to MaximEyes
KeywordUtil.logInfo('Step 1: Logging into MaximEyes')
WebUI.navigateToUrl(GlobalVariable.MaxUrlQA5)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), GlobalVariable.QA5Username)
WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), GlobalVariable.QA5Password)
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button'))

TestObject otpField = findTestObject('SSO/Page_MaximEyes/input_CODE')

// Wait for OTP field to appear (max 10 seconds)
if (WebUI.waitForElementVisible(otpField, 10, FailureHandling.OPTIONAL)) {

	String otp = CustomKeywords.'utils.GmailOTPReader.getVerificationCode'(
		GlobalVariable.MyEmail_Id,
		GlobalVariable.Email_Key
	)

	println("OTP = " + otp)

	WebUI.setText(otpField, otp)
	WebUI.click(findTestObject('SSO/Page_MaximEyes/input_btnEmailVerify'))
	
	WebUI.assertElementVisible(findTestObject('SSO/Page_MaximEyes/ul_Patient'), 10)

} else {
	println("OTP field is not visible. Skipping OTP fetch and entry.")
}

// STEP 2: Search for the patient booked in Part 1
KeywordUtil.logInfo("Step 2: Searching for patient '${firstName} ${lastName}'")
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/a_imgFindPatient'))
WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_First Name_Preferred'), firstName)
WebUI.setText(findTestObject('MaximeyesAppt/Page_MaximEyes/input_Last Name'), lastName)
WebUI.click(findTestObject('MaximeyesAppt/Page_MaximEyes/input_btnSearchPatient'))

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/a_dropdown-toggle menu-large recentmodule'))

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/a_Insurance'))


WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/span_Unprocessed'), 0)

WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_08_02_2026 (By Evaa)'), '08/02/2026 (By Evaa)', 
    0)



String actualText = WebUI.getText(
    findTestObject('Insurance Verifcation/Page_MaximEyes/span_PATIENT HAS ADDED Aetna AND IT DOES NOT HAV')
)

WebUI.verifyMatch(actualText, '.*PATIENT HAS ADDED Aetna.*', true)
WebUI.verifyMatch(actualText, '.*INSURED ID: 12345678.*', true)
WebUI.verifyMatch(actualText, '.*INSURED FIRST NAME: QA.*', true)
WebUI.verifyMatch(actualText, '.*INSURED LAST NAME: Katalon.*', true)
WebUI.verifyMatch(actualText, '.*INSURED DOB: 01/04/1995.*', true)
//WebUI.verifyMatch(actualText, '.*INSURED GENDER:\\s*(Male)?\\b.*', true)
WebUI.verifyMatch(actualText, '.*INSURANCE CARD HAS BEEN ATTACHED.*', true)


WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_td'), '', 0)

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/span_All'))



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_08_02_2026'), '08/02/2026', 0)



WebUI.verifyMatch(actualText, '.*PATIENT HAS ADDED Aetna.*', true)
WebUI.verifyMatch(actualText, '.*INSURED ID: 12345678.*', true)
WebUI.verifyMatch(actualText, '.*INSURED FIRST NAME: QA.*', true)
WebUI.verifyMatch(actualText, '.*INSURED LAST NAME: Katalon.*', true)
WebUI.verifyMatch(actualText, '.*INSURED DOB: 01/04/1995.*', true)
//WebUI.verifyMatch(actualText, '.*INSURED GENDER:\\s*(Male)?\\b.*', true)
WebUI.verifyMatch(actualText, '.*INSURANCE CARD HAS BEEN ATTACHED.*', true)

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/span_PATIENT HAS ADDED Aetna AND IT DOES NOT HAV_1'))



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Date Submited _ 08_02_2026'), 'Date Submited :  08/02/2026', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Insurance Name _ Aetna'), 'Insurance Name :  Aetna', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Insured Name _ QA Katalon'), 'Insured Name :  QA Katalon ', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Insured ID _ 12345678'), 'Insured ID :  12345678', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Insured DOB _ 01_04_1995'), 'Insured DOB :  01/04/1995', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Insured Gender _'), 'Insured Gender :  ', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/h6_Pt relationship to insured _ Select'), 'Pt relationship to insured :  Select', 
    0)



WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/img_img'), 0)



WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/img_img_1'), 0)

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/span_mif-circle-plus font20 fg-purple line-heigh'))

WebUI.setText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_Search in data grid'), 'Aetna')

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/span_Aetna'))

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/span_Aetna_1'))

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/input_btnInsSave'))



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/div_Aetna (S_M)'), 'Aetna (S/M)', 0)


WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/span_ACTIVE (08_02_2028)'), 'ACTIVE (08/02/2028)', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_CoverageStartDate'), '', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_CoverageEndDate'), '', 0)

WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/select_ddlPatientRelationshipToInsured'), '---Select---\nSelf\nSpouse\nChild\nOther\n', 
    0)

WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_InsurancePlanInsuredID'), '', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_First Name'), '', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_Last Name'), '', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_InsuredDOB'), '', 0)



WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/input_GenderMale'), 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/input_txtPhone'), '', 0)



WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/img_insFrontImg'), 0)



WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/img_insBackImg'), 0)

WebUI.click(findTestObject('Insurance Verifcation/Page_MaximEyes/a_ui-id-24'))



WebUI.assertElementPresent(findTestObject('Insurance Verifcation/Page_MaximEyes/a_Aetna-Front-20260802164721630.png'), 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/span_Insurance Card'), 'Insurance Card', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_08_02_2026_1'), '08/02/2026', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_08_02_2026_2'), '08/02/2026', 0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/a_Aetna-Back-20260802164721630.png'), 'Aetna-Back-20260802164721630.png', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/span_Insurance Card_1'), 'Insurance Card', 
    0)



WebUI.assertElementText(findTestObject('Insurance Verifcation/Page_MaximEyes/td_08_02_2026_3'), '08/02/2026', 0)

