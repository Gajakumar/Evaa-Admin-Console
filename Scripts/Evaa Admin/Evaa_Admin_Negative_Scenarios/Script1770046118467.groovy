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
import org.apache.commons.lang3.RandomStringUtils as RandomStringUtils
import java.util.UUID as UUID
import java.util.Arrays as Arrays
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import ui.FormValidationKeywords

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.testobject.TestObject
import ui.FormValidationKeywords as Validate
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

def verifyError(TestObject obj, String expectedMsg) {
	WebUI.waitForElementVisible(obj, 5)
	WebUI.verifyElementText(obj, expectedMsg)
}

String randomEmail = "user_" + UUID.randomUUID().toString().substring(0, 8) + "@testmail.com"
println(randomEmail)

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
	''
)

WebUI.click(findTestObject("Object Repository/Account Sign Up Page/button_Next"))

WebUI.verifyElementText(
	findTestObject("Object Repository/Error Messages/Email is required"),
	"Email is required"
)

WebUI.setText(
	findTestObject("Object Repository/Account Sign Up Page/input_Sign Up to Your Account_Email"),
	randomEmail
)

WebUI.click(findTestObject("Object Repository/Account Sign Up Page/button_Next"))

// ===================== ORGANIZATION DETAILS =====================

// ================================
// Test Object Declarations
// ================================
TestObject btnNext      = findTestObject('Object Repository/Organization Details Page/button_Next')

TestObject errLanguage  = findTestObject('Object Repository/Error Messages/Please select a language')
TestObject errOrgName   = findTestObject('Object Repository/Error Messages/Please enter your organization name')
TestObject errTaxId     = findTestObject('Object Repository/Error Messages/Please enter Tax ID')
TestObject errPhone     = findTestObject('Object Repository/Error Messages/Please enter your phone number')
TestObject errAddress   = findTestObject('Object Repository/Error Messages/Please enter your business address')
TestObject errCity      = findTestObject('Object Repository/Error Messages/Please enter your city')
TestObject errState     = findTestObject('Object Repository/Error Messages/Please select a state')
TestObject errZip       = findTestObject('Object Repository/Error Messages/Please enter ZIP code')
TestObject errSpecialty = findTestObject('Object Repository/Error Messages/Please select a specialty type')
TestObject errSubdomain = findTestObject('Object Repository/Error Messages/Please enter a website or subdomain')

// ================================
// Negative Scenario
// ================================
FormValidationKeywords validation = new FormValidationKeywords()
validation.submitForm(btnNext)

validation.verifyErrorText(errLanguage,  'Please select a language.')
validation.verifyErrorText(errOrgName,   'Please enter your organization name.')
validation.verifyErrorText(errTaxId,     'Please enter Tax ID.')
validation.verifyErrorText(errPhone,     'Please enter your phone number.')
validation.verifyErrorText(errAddress,   'Please enter your business address.')
validation.verifyErrorText(errCity,      'Please enter your city.')
validation.verifyErrorText(errState,     'Please select a state.')
validation.verifyErrorText(errZip,       'Please enter ZIP code.')
validation.verifyErrorText(errSpecialty, 'Please select a specialty type.')
validation.verifyErrorText(errSubdomain, 'Please enter a website or subdomain.')

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/Phone Number"),
	"(123) 456"
)

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/Tax ID_TaxId input"),
	'1234'
)


validation.submitForm(btnNext)

validation.verifyErrorText(errPhone,     'Enter a valid 10-digit phone number.')
validation.verifyErrorText(errTaxId,     'Tax ID must be exactly 9 digits.')

// ===================== ORGANIZATION DETAILS =====================
String urlevva = RandomStringUtils.randomAlphanumeric(5) + ".evaa.ai"
WebUI.callTestCase(
	findTestCase('Test Cases/Common/Enter Organisation Details'),
	[
		orgName   : 'FIC',
		taxId     :  random9Digit,
		phone     : '(123) 456-7899',
		email     : randomEmail,
		address   : '2433 Wimbledon Drive',
		city      : 'Zionsville',
		state     : 'Alaska State',
		zip       : '46077',
		specialty : 'Sp Type Ophthalmology',
		website   : urlevva
	]
)


// ================================
// Test Object Declarations
// ================================

TestObject btnNextCreateEvaaAcc        = findTestObject('Object Repository/Create Your EVAA Account Page/button_Next')

TestObject errFirstName   = findTestObject('Object Repository/Error Messages/Please enter your first name')
TestObject errLastName    = findTestObject('Object Repository/Error Messages/Please enter your last name')
TestObject errPassword    = findTestObject('Object Repository/Error Messages/Please enter a password')
TestObject errConfirmPwd  = findTestObject('Object Repository/Error Messages/Please confirm your password')

// ================================
// Negative Scenario
// ================================

validation.submitForm(btnNextCreateEvaaAcc)

validation.verifyErrorText(errFirstName,  'Please enter your first name.')
validation.verifyErrorText(errLastName,   'Please enter your last name.')
validation.verifyErrorText(errPassword,   'Please enter a password.')
validation.verifyErrorText(errConfirmPwd, 'Please confirm your password.')

// ================================
//Password Validation
// ================================
TestObject txtPassword   = findTestObject('Object Repository/Create Your EVAA Account Page/input_Create New Password')
TestObject txtConfirmPwd   = findTestObject('Object Repository/Create Your EVAA Account Page/input_Confirm Password')

WebUI.setText(txtPassword, 'abc123')

validation.submitForm(btnNextCreateEvaaAcc)
validation.verifyErrorText(
	errPassword,
	'Please fix: At least 8 characters, At least 1 uppercase letter, At least 1 special character'
)

WebUI.setText(txtPassword, 'Password1!')
WebUI.setText(txtConfirmPwd, 'Password2!')

validation.submitForm(btnNextCreateEvaaAcc)

validation.verifyErrorText(
	errConfirmPwd,
	'Confirm password does not match.'
)

WebUI.setText(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_First Name"),
	"Matt"
)

WebUI.setText(
	findTestObject("Object Repository/Create Your EVAA Account Page/input_Last Name"),
	"Henry"
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
	"Matt"
)

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/input_Electronic Signature_agree-terms"))

WebUI.click(findTestObject("Object Repository/Page_Unified Admin/button_Sign Agreement  Continue"))

WebUI.verifyElementText(
	findTestObject("Object Repository/Page_Unified Admin/span_Agreement signed successfully. You wil_1bf869_1"),
	"Agreement signed successfully. You will receive a copy via email after payment confirmation."
)

TestObject cardNumber = findTestObject("Object Repository/Page_Unified Admin/input_Card number_payment-numberInput")

WebUI.waitForElementClickable(cardNumber, 5)

// -------------------- PAYMENT ERRORS --------------------
WebUI.click(findTestObject('Negative Scenarios/Page_Unified Admin/button_Pay Now'))


//WebUI.verifyElementText(findTestObject('Page_Unified Admin/p_Field-numberError'), 'Your card number is incomplete.')
//
//WebUI.verifyElementText(findTestObject('Page_Unified Admin/p_Field-expiryError'), 'Your card’s expiry date is incomplete.')
//
//WebUI.verifyElementText(findTestObject('Page_Unified Admin/p_Field-cvcError'), 'Your card’s security code is incomplete.')
//
//WebUI.verifyElementText(findTestObject('Page_Unified Admin/div_Your card number is incomplete'), 'Your card number is incomplete.')

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

// -------------------- LOGIN ERRORS --------------------


TestObject loginBtn = findTestObject("Object Repository/Page_Unified Admin/button_CLICK TO LOGIN")

WebUI.scrollToElement(loginBtn, 10)
WebUI.waitForElementClickable(loginBtn, 5)
WebUI.click(loginBtn)

WebUI.click(findTestObject("Object Repository/Page_MaximEyes Identity/button_Login"))

verifyError(
	findTestObject('Negative Scenarios/Page_MaximEyes Identity/span_Username-error'),
	'The Username field is required.'
)

verifyError(
	findTestObject('Negative Scenarios/Page_MaximEyes Identity/span_Password-error'),
	'The Password field is required.'
)

WebUI.setText(
	findTestObject("Object Repository/Page_MaximEyes Identity/input_Sign In to Your Account_Username"),
	randomEmail
)

WebUI.setEncryptedText(
	findTestObject("Object Repository/Page_MaximEyes Identity/input_Sign In to Your Account_Password"),
	"cvW8qx4B2o1WegCEDy41Xg=="
)

WebUI.click(findTestObject("Object Repository/Page_MaximEyes Identity/button_Login"))
WebUI.delay(5)

// -------------------- ORGANIZATION DETAILS --------------------



WebUI.executeJavaScript(
	"document.body.style.zoom='80%'",
	null
)

WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76"))
WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_OrganizationBusiness"))


TestObject businessLeftPane = findTestObject("Object Repository/Practice Login/Page_Unified Admin/span_Business")

WebUI.waitForElementClickable(businessLeftPane, 5)
WebUI.click(businessLeftPane)

WebUI.click(findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_Add New Business"))

TestObject saveBtn = findTestObject("Object Repository/Practice Login/Page_Unified Admin/button_Save Changes")
WebUI.delay(10)

WebUI.scrollToElement(saveBtn, 10)
WebUI.waitForElementClickable(saveBtn, 5)
WebUI.click(saveBtn)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_Please fill all required fields'),
	'Please fill all required fields'
)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_Business name is required'),
	'Business name is required'
)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_Address Line 1 is required'),
	'Address Line 1 is required'
)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_City is required'),
	'City is required'
)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_State is required'),
	'State is required'
)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_Phone number is required'),
	'Phone number is required'
)

verifyError(
	findTestObject('Negative Scenarios/Page_Unified Admin/div_Tax ID is required'),
	'Tax ID is required'
)

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


WebUI.delay(10)

WebUI.scrollToElement(saveBtn, 10)
WebUI.waitForElementClickable(saveBtn, 5)
WebUI.click(saveBtn)


// -------------------- LOCATION --------------------

TestObject settingBtn = findTestObject("Object Repository/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76")

WebUI.waitForElementClickable(settingBtn, 5)
WebUI.click(settingBtn)

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Locations'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add New Location'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save Changes'))

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Please fill in all required fields'), 'Please fill in all required fields')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Business is required'), 'Business is required')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Location Name is required'), 'Location Name is required')

WebUI.click(findTestObject('errors/Page_Unified Admin/button_Cancel'))

//-------------------Provider--------------------------

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Providers'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add New Provider'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save Changes_1'))

//WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Please fill in all required fields'), 'Please fill in all required fields')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_First name is required'), 'First name is required')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Last name is required'), 'Last name is required')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_NPI number is required'), 'NPI number is required')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Email is required'), 'Email is required')

WebUI.verifyElementText(findTestObject('errors/Page_Unified Admin/div_Select at least one practice location'), 'Select at least one practice location')

WebUI.click(findTestObject('errors/Page_Unified Admin/button_Cancel'))

