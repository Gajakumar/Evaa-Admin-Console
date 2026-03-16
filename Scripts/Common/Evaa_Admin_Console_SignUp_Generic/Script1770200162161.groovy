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
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword as WebUIAbstractKeyword
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.apache.commons.lang3.RandomStringUtils as RandomStringUtils
import java.util.UUID as UUID
import java.util.Arrays as Arrays
import com.kms.katalon.core.webui.common.WebUiCommonHelper as WebUiCommonHelper

// ===================== TEST DATA =====================

Random rand = new Random()

String urlevva = RandomStringUtils.randomAlphanumeric(5) + '.evaa.ai'
String random9Digit = (100000000 + rand.nextInt(900000000)).toString()

println(random9Digit)

String threeDigit = String.format('%03d', rand.nextInt(1000))
GlobalVariable.randomEmail = "gajakumara+" + threeDigit + "@first-insight.com"

println(GlobalVariable.randomEmail)

String randomPhone = String.format("(%03d) %03d-%04d",
		rand.nextInt(900)+100,
		rand.nextInt(900)+100,
		rand.nextInt(9000)+1000)


// ===================== LOGIN TO EVAA ADMIN =====================

WebUI.callTestCase(findTestCase('Test Cases/Common/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)

TestObject meetHeader = findTestObject('Object Repository/Evaa Admin Start Page/h1_Meet EVAA AI')
WebUI.waitForElementVisible(meetHeader,10)
WebUI.verifyElementText(meetHeader,"Meet EVAA AI")

TestObject signUpBtn = findTestObject('Object Repository/Evaa Admin Start Page/button_Sign Up')
WebUI.waitForElementClickable(signUpBtn,10)
WebUI.click(signUpBtn, FailureHandling.STOP_ON_FAILURE)

TestObject welcomeText = findTestObject('Object Repository/Welcome page/Welcome to EVAA AI Text')
WebUI.waitForElementVisible(welcomeText,10)
WebUI.verifyElementText(welcomeText,EvaaWelcomeText, FailureHandling.STOP_ON_FAILURE)


// ===================== PRODUCT SELECTION =====================

CustomKeywords.'common.ProductSelection.selectProductByName'(practiceName)


// ===================== ACCOUNT SIGN UP =====================

TestObject emailField = findTestObject('Object Repository/Account Sign Up Page/input_Sign Up to Your Account_Email')

WebUI.waitForElementVisible(emailField,10)
WebUI.setText(emailField, GlobalVariable.randomEmail)

WebUI.click(findTestObject('Object Repository/Account Sign Up Page/button_Next'))


// ===================== ORGANIZATION DETAILS =====================

WebUI.callTestCase(findTestCase('Test Cases/Common/Enter Organisation Details'),
[
('orgName'):'FIC',
('taxId'):random9Digit,
('phone'):randomPhone,
('email'):GlobalVariable.randomEmail,
('address'):'2433 Wimbledon Drive',
('city'):'Zionsville',
('state'):'Alaska State',
('zip'):'46077',
('specialty'):'Sp Type Ophthalmology',
('website'):urlevva
])


// ===================== CREATE ACCOUNT =====================

TestObject createHeader = findTestObject('Object Repository/Create Your EVAA Account Page/Create Your EVAA Account Header Text')

WebUI.waitForElementVisible(createHeader,10)
WebUI.verifyElementText(createHeader,'Create Your EVAA Account')

WebUI.setText(findTestObject('Object Repository/Create Your EVAA Account Page/input_First Name'),'Harry')
WebUI.setText(findTestObject('Object Repository/Create Your EVAA Account Page/input_Last Name'),'Brook')

WebUI.verifyElementAttributeValue(
findTestObject('Object Repository/Create Your EVAA Account Page/input_Email Address'),
'value',
GlobalVariable.randomEmail,
10, FailureHandling.STOP_ON_FAILURE)

WebUI.setEncryptedText(findTestObject('Object Repository/Create Your EVAA Account Page/input_Create New Password'),
'cvW8qx4B2o1WegCEDy41Xg==')

WebUI.setEncryptedText(findTestObject('Object Repository/Create Your EVAA Account Page/input_Confirm Password'),
'cvW8qx4B2o1WegCEDy41Xg==')

WebUI.click(findTestObject('Object Repository/Create Your EVAA Account Page/button_Next'))


// ===================== AGREEMENT =====================

TestObject acceptBtn = findTestObject('Object Repository/Create Your EVAA Account Page/Page_Unified Admin/button_I Accept')

if(WebUI.waitForElementClickable(acceptBtn,10,FailureHandling.OPTIONAL)){
	WebUI.click(acceptBtn)
}


// ===================== ADD PRODUCT =====================

TestObject addToCart = findTestObject(
'Object Repository/Browse Product Page/Add To Cart as per Product name',
[('productName'):'plan2']
)

WebUI.waitForElementClickable(addToCart,10)
WebUI.click(addToCart)

TestObject cartSuccess = findTestObject('Object Repository/Page_Unified Admin/span_Item added to cart successfully_1')

WebUI.waitForElementVisible(cartSuccess,10)
WebUI.verifyElementText(cartSuccess,'Item added to cart successfully')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_1'),'2')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Go to Cart1'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Checkout'))


// ===================== SIGN AGREEMENT =====================

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Electronic Signature_signature-input_9'),'Harry')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/input_Electronic Signature_agree-terms'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Sign Agreement  Continue'))

TestObject agreementSuccess = findTestObject('Object Repository/Page_Unified Admin/span_Agreement signed successfully. You wil_1bf869_1')

WebUI.waitForElementVisible(agreementSuccess,10)

WebUI.verifyElementText(agreementSuccess,
'Agreement signed successfully. You will receive a copy via email after payment confirmation.')


// ===================== PAYMENT =====================

TestObject cardNumber = findTestObject('Object Repository/Page_Unified Admin/input_Card number_payment-numberInput')

WebUI.waitForElementVisible(cardNumber,15)

WebUI.setText(cardNumber,'4242 4242 4242 4242')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_MM  YY_payment-expiryInput'),'12 / 32')

WebUI.setText(findTestObject('Page_Unified Admin/input_Security code_payment-cvcInput'),'333')

WebUI.selectOptionByValue(findTestObject('Payment Page/Page_Unified Admin/select_Select Country'),'IN',false)

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Pay Now'))


// ===================== LOGIN AFTER PAYMENT =====================

TestObject emailId = findTestObject('Object Repository/Page_Unified Admin/email_id')

WebUI.waitForElementVisible(emailId,20)
WebUI.verifyElementText(emailId,GlobalVariable.randomEmail)

//CustomKeywords.'email.EmailVerification.verifyAgreementEmailWithRetry'(
//"EVAA Intelli-scan",
//"\$24.00/yr",
//"\$2.00/mo",
//"Harry",
//180,
//10
//)

TestObject loginBtn = findTestObject('Object Repository/Page_Unified Admin/button_CLICK TO LOGIN')
WebUI.scrollToElement(loginBtn, 10)
WebUI.waitForElementClickable(loginBtn,15)
WebUI.click(loginBtn)


// ===================== VERIFY ORGANIZATION & BUSINESS =====================

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/main_Welcome fic - Admin OverviewMonitor setup s'),
	5)

WebUI.verifyElementText(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/h1_Welcome fic - Admin Overview'), 'Welcome FIC - Admin Overview')

TestObject settingBtn = findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76')

WebUI.waitForElementClickable(settingBtn, 10)
WebUI.click(settingBtn)

TestObject orgBusinessBtn = findTestObject('Object Repository/Practice Login/Page_Unified Admin/button_OrganizationBusiness')
WebUI.waitForElementClickable(orgBusinessBtn, 10)
WebUI.click(orgBusinessBtn)


// -------- VERIFY ORG DETAILS --------

WebUI.verifyElementAttributeValue(findTestObject('Object Repository/demo/Page_Unified Admin/input__organizationName'),
		'value','FIC',10)

WebUI.verifyElementText(findTestObject('Object Repository/demo/Page_Unified Admin/span_United States'),
		'United States')

WebUI.verifyElementAttributeValue(findTestObject('Object Repository/demo/Page_Unified Admin/input__addressLine1'),
		'value','2433 Wimbledon Drive',10)

WebUI.verifyElementAttributeValue(findTestObject('Object Repository/demo/Page_Unified Admin/input__city'),
		'value','Zionsville',10)

WebUI.verifyElementText(findTestObject('Object Repository/demo/Page_Unified Admin/span_Alaska'),
		'Alaska')

WebUI.verifyElementAttributeValue(findTestObject('Object Repository/demo/Page_Unified Admin/input__zipCode'),
		'value','46077',10)

WebUI.verifyElementAttributeValue(findTestObject('Object Repository/demo/Page_Unified Admin/input__taxId'),
		'value',random9Digit,10,FailureHandling.CONTINUE_ON_FAILURE)

//WebUI.verifyElementAttributeValue(findTestObject('Object Repository/Organization Details Page/input_Phone_phone'),
//		'value',randomPhone,10,FailureHandling.CONTINUE_ON_FAILURE)

String expectedPhone = randomPhone.replaceAll("[^0-9]", "")

WebUI.verifyElementAttributeValue(
	findTestObject('Object Repository/Organization Details Page/input_Phone_phone'),
	'value',
	expectedPhone,
	10,
	FailureHandling.CONTINUE_ON_FAILURE
)

WebUI.executeJavaScript(
	"document.body.style.zoom='80%'",
	null
)


// ===================== ADD BUSINESS =====================

TestObject businessLeftPane = findTestObject('Object Repository/Practice Login/Page_Unified Admin/span_Business')

WebUI.waitForElementClickable(businessLeftPane,10)
WebUI.click(businessLeftPane)

TestObject addBusinessBtn = findTestObject('Object Repository/Practice Login/Page_Unified Admin/button_Add New Business')
WebUI.waitForElementClickable(addBusinessBtn,10)
WebUI.click(addBusinessBtn)

WebUI.waitForElementVisible(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input__businessName'),10)

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input__businessName'),'Revolution')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input_Patient-friendly name used by EVAA fo_e9cec2'),
		'DBA Name')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input_Website_website'),
		'https://qa8.maximeyes.com/')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input__addressLineOne'),'Street 01')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input_Address Line 2_addressLineTwo'),'Unit 99')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input__city'),'Zionsville')

WebUI.click(findTestObject('Object Repository/Practice Login/Page_Unified Admin/button_Select State (1)'))
WebUI.click(findTestObject('Object Repository/Practice Login/Page_Unified Admin/span_Alaska'))

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input_Zip Code_zip'),'46077')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input__phoneNumber'),'(123) 456-7899')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input__taxId'),'123456789')

WebUI.setText(findTestObject('Object Repository/Practice Login/Page_Unified Admin/input_National Provider Identifier for your_0e94e2'),
		'999999999')


// ===================== SAVE BUSINESS =====================

TestObject saveBtn = findTestObject('Practice Login/Page_Unified Admin/button_Save Changes')

WebUI.scrollToElement(saveBtn,5)
WebUI.waitForElementClickable(saveBtn,10)
WebUI.click(saveBtn)


// ===================== NAVIGATE TO LOCATIONS =====================

WebUI.waitForElementClickable(settingBtn,10)
WebUI.click(settingBtn)

TestObject locationsBtn = findTestObject('Object Repository/Page_Unified Admin/button_Locations')
WebUI.waitForElementClickable(locationsBtn,10)
WebUI.click(locationsBtn)

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add New Location'))


// ===================== SELECT BUSINESS =====================

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Select business'))
WebUI.click(findTestObject('Object Repository/Page_Unified Admin/span_Revolution'))


// ===================== ENTER LOCATION DETAILS =====================

WebUI.waitForElementVisible(findTestObject('Object Repository/Page_Unified Admin/input__name'),10)

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__name'),'Test Location')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Doing Business As_dbaName'),'DBA Name')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Phone Number_phone'),'(123) 456-7899')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Fax Number_faxNumber'),'(123) 456-7899')


// -------- VERIFY AUTO POPULATED --------

//WebUI.verifyElementAttributeValue(findTestObject('Object Repository/Page_Unified Admin/input_Tax ID_taxId'),
//		'value',random9Digit,10,FailureHandling.CONTINUE_ON_FAILURE)

WebUI.verifyElementAttributeValue(findTestObject('Object Repository/Page_Unified Admin/input_Group NPI_groupNpiId'),
		'value','999999999',10)


// ===================== ADDRESS =====================

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Address Line 1_address'),'Street 01')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Address Line 2_address2'),'Unit 09')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_City_city'),'Zionsville')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Select state'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Alaska'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_ZIP Code_zip'),'46077')


// ===================== LOCATION HOURS =====================

CustomKeywords.'common.LocationHours.setWeeklySchedule'([
('Monday') : [('start'):'09:00',('end'):'18:00'],
('Tuesday'): [('start'):'09:00',('end'):'18:00'],
('Wednesday'): [('start'):'09:30',('end'):'17:30'],
('Thursday'): [('start'):'09:00',('end'):'18:00'],
('Friday'): [('start'):'09:00',('end'):'16:00'],
('Saturday'): [('closed'):true],
('Sunday'): [('closed'):true]
])


// ===================== ADD LOCATION ID =====================

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add ID'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/svg'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Commercial'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Value_form-control_4'),'1234')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Verified'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/span_Verified'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/textarea_Optional Note'),'Optional Note')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save'))


// ===================== VERIFY LOCATION ID =====================

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Commercial'),'Commercial')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_1234'),'1234')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Verified_1'),'Verified')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Optional Note'),'Optional Note')


// ===================== SAVE LOCATION =====================

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save Changes'))


// ===================== VERIFY LOCATION SUMMARY =====================

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Test Location'),'Test Location')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Revolution_1'),'Revolution')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Street 01'),'Street 01')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_(123) 456-7899'),'(123) 456-7899')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Active'),'Active')


// ===================== ADD PROVIDER =====================

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Providers'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add New Provider'))

WebUI.waitForElementVisible(findTestObject('Object Repository/Page_Unified Admin/input__first_name'),10)

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__first_name'),'Test')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Middle Name_middle_name'),'P')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__last_name'),'Provider')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Suffix_suffix'),'Sr.')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Credentials_credentials'),'MD')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Specialty_specialty'),'Ophthalmology')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__NPI'),'4444444444')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__email'),'gajakumara@first-insight.com')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Phone Number_phone'),'(123) 456-7899')


// ===================== PROVIDER ID =====================

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Add ID_1'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Select ID type'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_NPI'))

WebUI.doubleClick(findTestObject('Object Repository/Page_Unified Admin/div_ID Value'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__form-control p-2_10'),'4444444444')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Verified_1'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Verified_2'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/textarea_Additional Notes'),'Additional Notes')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save_1'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/input_Select the locations where this provi_0580e6'))

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Save Changes_1'))


// ===================== EDIT PROVIDER =====================

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Edit_1'))

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__first_name'),'Test Edit')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__last_name'),'Provider Edit')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input_Specialty_specialty'),'Ophthalmology Edit')

WebUI.setText(findTestObject('Object Repository/Page_Unified Admin/input__email'),'Test@gmail.com')

WebUI.click(findTestObject('Object Repository/Page_Unified Admin/button_Update Provider'))


// ===================== VERIFY UPDATED PROVIDER =====================

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Sr. Test Edit Provider Edit'),
		'Sr. Test Edit Provider Edit')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_Ophthalmology Edit'),
		'Ophthalmology Edit')

WebUI.verifyElementText(findTestObject('Object Repository/Page_Unified Admin/span_4444444444'),
		'4444444444')
