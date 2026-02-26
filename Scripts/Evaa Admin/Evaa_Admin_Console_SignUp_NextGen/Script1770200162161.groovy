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
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

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
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import org.apache.commons.lang3.RandomStringUtils
import java.util.UUID
import java.util.Arrays
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import java.util.UUID

// ===================== TEST DATA =====================
String urlevva = RandomStringUtils.randomAlphanumeric(5) + ".evaa.ai"
GlobalVariable.randomEmail = "user_${UUID.randomUUID().toString().substring(0,8)}@testmail.com"
println("Generated Email: " + GlobalVariable.randomEmail)
//String randomEmail ="gajakumara@first-insight.com"
println(GlobalVariable.randomEmail)

String random9Digit = String.valueOf((long)(Math.random() * 900000000) + 100000000)
println random9Digit

// ===================== LOGIN TO EVAA ADMIN =====================
WebUI.callTestCase(findTestCase('Test Cases/Common/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.verifyElementText(
	findTestObject("Object Repository/Evaa Admin Start Page/h1_Meet EVAA AI"),
	"Meet EVAA AI"
)

WebUI.click(findTestObject("Object Repository/Evaa Admin Start Page/button_Sign Up"))

WebUI.verifyElementText(
	findTestObject("Object Repository/Welcome page/Welcome to EVAA AI Text"),
	"Welcome to EVAA AI"
)

// ===================== PRODUCT SELECTION =====================

CustomKeywords.'common.ProductSelection.selectProductByName'('NextGen')

// ===================== ACCOUNT SIGN UP =====================
WebUI.setText(
	findTestObject("Object Repository/Account Sign Up Page/input_Sign Up to Your Account_Email"),
	GlobalVariable.randomEmail
)

WebUI.click(findTestObject("Object Repository/Account Sign Up Page/button_Next"))


// ===================== ORGANIZATION DETAILS =====================

WebUI.callTestCase(
	findTestCase('Test Cases/Common/Enter Organisation Details'),
	[
		orgName   : 'FIC',
		taxId     :  random9Digit,
		phone     : '(123) 456-7899',
		email     : GlobalVariable.randomEmail,
		address   : '2433 Wimbledon Drive',
		city      : 'Zionsville',
		state     : 'Alaska State',
		zip       : '46077',
		specialty : 'Sp Type Ophthalmology',
		website   : urlevva
	]
)


// ===================== CREATE ACCOUNT =====================
WebUI.verifyElementText(
	findTestObject("Object Repository/Create Your EVAA Account Page/Create Your EVAA Account Header Text"),
	"Create Your EVAA Account"
)

WebUI.setText(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_First Name"),
	"HarryTest"
)

WebUI.setText(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_Last Name"),
	"BrookTest"
)

WebUI.verifyElementAttributeValue(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_Email Address"),
	"value",
	GlobalVariable.randomEmail,
	10
)

WebUI.setEncryptedText(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_Create New Password"),
	"cvW8qx4B2o1WegCEDy41Xg=="
)

WebUI.setEncryptedText(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_Confirm Password"),
	"cvW8qx4B2o1WegCEDy41Xg=="
)

WebUI.click(findTestObject("Object Repository/Create Your EVAA Account Page/button_Next"))

// ===================== AGREEMENT & PAYMENT =====================
WebUI.delay(5)

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/button_I Accept"))

WebUI.click(findTestObject(
    'Object Repository/Page_Unified Admin/button_Add To Cart',
    ['productName' : 'EVAA Intelli-scan']
))

WebUI.waitForElementVisible(
	findTestObject("Object Repository/Page_Unified Admin/span_Item added to cart successfully_1"),
	3,
	FailureHandling.CONTINUE_ON_FAILURE
)

WebUI.verifyElementText(
	findTestObject("Object Repository/Page_Unified Admin/span_Item added to cart successfully_1"),
	"Item added to cart successfully"
)

WebUI.verifyElementText(
	findTestObject("Object Repository/Page_Unified Admin/span_1"),
	"1"
)

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/button_Go to Cart1"))
WebUI.click(findTestObject("Object Repository/Page_Unified Admin/button_Checkout"))

WebUI.setText(
	findTestObject("Object Repository/Page_Unified Admin/input_Electronic Signature_signature-input_9"),
	"Harry"
)

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/input_Electronic Signature_agree-terms"))

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/button_Sign Agreement  Continue"))

WebUI.verifyElementText(
	findTestObject("Object Repository/Page_Unified Admin/span_Agreement signed successfully. You wil_1bf869_1"),
	"Agreement signed successfully. You will receive a copy via email after payment confirmation."
)

TestObject cardNumber = findTestObject("Object Repository/Page_Unified Admin/input_Card number_payment-numberInput")

WebUI.waitForElementClickable(cardNumber, 5)


WebUI.setText(
	findTestObject("Object Repository/Page_Unified Admin/input_Card number_payment-numberInput"),
	"4242 4242 4242 4242"
)

WebUI.setText(
	findTestObject("Object Repository/Page_Unified Admin/input_MM  YY_payment-expiryInput"),
	"12 / 32"
)

WebUI.setText(
	findTestObject("Object Repository/Page_Unified Admin/input_Security code_payment-cvcInput"),
	"333"
)

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/button_Pay Now"))

// ===================== LOGIN AFTER PAYMENT =====================
WebUI.waitForElementVisible(
	findTestObject("Object Repository/Page_Unified Admin/email_id"),
	10
)

WebUI.verifyElementText(
	findTestObject("Object Repository/Page_Unified Admin/email_id"),
	GlobalVariable.randomEmail
)

////verify email
//
//CustomKeywords.'email.EmailVerification.verifyAgreementEmailWithRetry'(
//	"EVAA Intelli-scan",
//	"\$24.00/yr",
//	"\$2.00/mo",
//	"Harry",
//	180,   // max wait in seconds
//	10     // poll interval in seconds
//)

TestObject loginBtn = findTestObject("Object Repository/Page_Unified Admin/button_CLICK TO LOGIN")

WebUI.scrollToElement(loginBtn, 10)
WebUI.waitForElementClickable(loginBtn, 5)
WebUI.click(loginBtn)

WebUI.delay(2)
//WebUI.switchToWindowIndex(1)

WebUI.setText(
	findTestObject("Object Repository/Page_MaximEyes Identity/input_Sign In to Your Account_Username"),
	GlobalVariable.randomEmail
)

WebUI.setEncryptedText(
	findTestObject("Object Repository/Page_MaximEyes Identity/input_Sign In to Your Account_Password"),
	"cvW8qx4B2o1WegCEDy41Xg=="
)

WebUI.click(findTestObject("Object Repository/Page_MaximEyes Identity/button_Login"))
WebUI.delay(5)



// ===================== VERIFY ACCOUNT =====================
String actualProductPurchased = WebUI.getText(
	findTestObject("Object Repository/demo/Page_Unified Admin/div_Intelliscan1 Providers")
).replaceAll("\\s+", "").trim()

WebUI.verifyMatch(actualProductPurchased, "Intelliscan1Providers", false)

String expUrl = urlevva.replace(".", "")

WebUI.verifyElementText(
	findTestObject("Object Repository/demo/Page_Unified Admin/span_Account qaevaaai  Organization FIC"),
	"Account: " + expUrl + " • Organization: FIC"
)

WebUI.executeJavaScript(
	"document.body.style.zoom='80%'",
	null
)

// ===================== VERIFY ORGANIZATION & BUSINESS =====================
TestObject settingBtn = findTestObject("Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76")

WebUI.waitForElementClickable(settingBtn, 5)
WebUI.click(settingBtn)
WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_OrganizationBusiness"))

WebUI.verifyElementAttributeValue(
	findTestObject("Object Repository/demo/Page_Unified Admin/input__organizationName"),
	"value",
	"FIC",
	10
)

WebUI.verifyElementText(
	findTestObject("Object Repository/demo/Page_Unified Admin/span_United States"),
	"United States"
)

WebUI.verifyElementAttributeValue(
	findTestObject("Object Repository/demo/Page_Unified Admin/input__addressLine1"),
	"value",
	"2433 Wimbledon Drive",
	10
)

WebUI.verifyElementAttributeValue(
	findTestObject("Object Repository/demo/Page_Unified Admin/input__city"),
	"value",
	"Zionsville",
	10
)

WebUI.verifyElementText(
	findTestObject("Object Repository/demo/Page_Unified Admin/span_Alaska"),
	"Alaska"
)

WebUI.verifyElementAttributeValue(
	findTestObject("Object Repository/demo/Page_Unified Admin/input__zipCode"),
	"value",
	"46077",
	10
)

// ===================== ADD BUSINESS =====================
WebUI.setText(findTestObject("Object Repository/demo/Page_Unified Admin/input__taxId"), "123456789")

TestObject businessLeftPane = findTestObject("Object Repository/Practice Login/Page_Unified Admin/span_Business")

WebUI.waitForElementClickable(businessLeftPane, 5)
WebUI.click(businessLeftPane)

WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_Add New Business"))

WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input__businessName"), "Revolution")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input_Patient-friendly name used by EVAA fo_e9cec2"), "DBA Name")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input_Website_website"), "https://qa8.maximeyes.com/")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input__addressLineOne"), "Street 01")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input_Address Line 2_addressLineTwo"), "Unit 99")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input__city"), "Zionsville")

WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_Select State (1)"))
WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/span_Alaska"))

WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input_Zip Code_zip"), "46077")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input__phoneNumber"), "(123) 456-7899")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input__taxId"), "123456789")
WebUI.setText(findTestObject("Object Repository/Practice Login/Page_Unified Admin/input_National Provider Identifier for your_0e94e2"), "999999999")

// ===================== SAVE BUSINESS =====================


TestObject saveBtn = findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_Save Changes")
WebUI.delay(10)

WebUI.scrollToElement(saveBtn, 10)
WebUI.waitForElementClickable(saveBtn, 5)
WebUI.click(saveBtn)


// ----------------------------------------------------
// Navigate to Locations and Create New Location
// ----------------------------------------------------
//TestObject settingBtn = findTestObject("Object Repository/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76")

WebUI.waitForElementClickable(settingBtn, 5)
WebUI.click(settingBtn)

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Locations'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add New Location'))

// Select Business
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Select business'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/span_Revolution'))

// Enter Location Details
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__name'), 'Test Location')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Doing Business As_dbaName'), 'DBA Name')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Phone Number_phone'), '(123) 456-7899')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Fax Number_faxNumber'), '(123) 456-7899')

// Verify Auto-Populated Fields
WebUI.verifyElementAttributeValue(
	findTestObject('Object Repository/Page_Unified Admin/input_Tax ID_taxId'),
	'value',
	'123456789',
	10
)

WebUI.verifyElementAttributeValue(
	findTestObject('Object Repository/Page_Unified Admin/input_Group NPI_groupNpiId'),
	'value',
	'999999999',
	10
)

// Enter Address Details
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Address Line 1_address'), 'Street 01')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Address Line 2_address2'), 'Unit 09')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_City_city'), 'Zionsville')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Select state'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Alaska'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_ZIP Code_zip'), '46077')

// Set Closed Days
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/input_-_closed-Saturday'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/input_-_closed-Sunday'))

// ----------------------------------------------------
// Add Location ID
// ----------------------------------------------------

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add ID'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/svg'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Commercial'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Value_form-control_4'), '1234')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Verified'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/span_Verified'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/textarea_Optional Note'), 'Optional Note')

// Save Location ID
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save'))

// Verify Location ID Details
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Commercial'), 'Commercial')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_1234'), '1234')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Verified_1'), 'Verified')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Optional Note'), 'Optional Note')

// Save Location
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save Changes'))

// Verify Location Summary
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Test Location'), 'Test Location')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Revolution_1'), 'Revolution')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Street 01'), 'Street 01')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_(123) 456-7899'), '(123) 456-7899')
WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Active'), 'Active')

// ----------------------------------------------------
// Add New Provider
// ----------------------------------------------------

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Providers'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add New Provider'))

// Enter Provider Details
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__first_name'), 'Test')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Middle Name_middle_name'), 'P')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__last_name'), 'Provider')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Suffix_suffix'), 'Sr.')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Credentials_credentials'), 'MD')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Specialty_specialty'), 'Ophthalmology')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__NPI'), '4444444444')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__email'), 'gajakumara@first-insight.com')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Phone Number_phone'), '(123) 456-7899')

// ----------------------------------------------------
// Add Provider ID
// ----------------------------------------------------

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add ID_1'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Select ID type'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_NPI'))

WebUI.doubleClick(findTestObject('Object Repository/Page_Unified Admin/div_ID Value'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/input__form-control p-2'))
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__form-control p-2_10'), '4444444444')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Verified_1'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Verified_2'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/textarea_Additional Notes'), 'Additional Notes')

// Save Provider
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save_1'))

// Assign Location to Provider
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/input_Select the locations where this provi_0580e6'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save Changes_1'))

// ----------------------------------------------------
// Edit Provider
// ----------------------------------------------------

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Edit_1'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__first_name'), 'Test Edit')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__last_name'), 'Provider Edit')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Specialty_specialty'), 'Ophthalmology Edit')
WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__email'), 'Test@gmail.com')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Update Provider'))

// ----------------------------------------------------
// Verify Updated Provider Details
// ----------------------------------------------------

WebUI.verifyElementText(
	findTestObject('Object Repository/Page_Unified Admin/span_Sr. Test Edit Provider Edit'),
	'Sr. Test Edit Provider Edit'
)

WebUI.verifyElementText(
	findTestObject('Object Repository/Page_Unified Admin/span_Ophthalmology Edit'),
	'Ophthalmology Edit'
)

WebUI.verifyElementText(
	findTestObject('Object Repository/Page_Unified Admin/span_4444444444'),
	'4444444444'
)