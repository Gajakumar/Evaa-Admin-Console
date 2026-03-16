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
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.testobject.TestObject
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import common.TableDataHelper
import common.TDHelper
import common.TdInactiveUsers
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import common.TableDataHelper
import common.TableDataHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import verification.CrossSystemVerifier

TableDataHelper helper = new TableDataHelper()
TDHelper helperA = new TDHelper()
TdInactiveUsers helperB = new TdInactiveUsers()
CrossSystemVerifier verify = new CrossSystemVerifier()

// ======================
// MAXIMEYES LOGIN
// ======================
WebUI.callTestCase(
    findTestCase('Test Cases/Common/Maximeyes Login'),
    [:],
    FailureHandling.STOP_ON_FAILURE
)


WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Office Admin Button'))
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Business Administration'))

// ======================
// READ BUSINESS
// ======================
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Businesses'))

println "BUSINESS TABLE XPATH = " +
findTestObject("Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Buisness Table")
	.findPropertyValue("xpath")

helper.readAndStore(
    "business",
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Buisness Table'),
    0
)


WebUI.click(
	findTestObject(
		'Object Repository/MaximEyes Business/Buisness name',
		['businessName':'Frequency Online']
	)
)

Map fields = [
	
	"Business Name":[
	findTestObject('Object Repository/MaximEyes Business/input_Business Name_txtBusinessName'),
	findTestObject('Object Repository/Evaa Business Records/input__businessName')
	],
	
	"Website":[
	findTestObject('Object Repository/MaximEyes Business/input_Website_txtWebsite'),
	findTestObject('Object Repository/Evaa Business Records/input_Website_website')
	],
	
	"Address Line 1":[
	findTestObject('Object Repository/MaximEyes Business/input_Address_PL_Main_Line1_c84d6056'),
	findTestObject('Object Repository/Evaa Business Records/input__addressLineOne')
	],
	
	"City":[
	findTestObject('Object Repository/MaximEyes Business/input_City, State_PL_Main_CityName_c84d6056'),
	findTestObject('Object Repository/Evaa Business Records/input__city')
	],
	
	"Zip Code":[
	findTestObject('Object Repository/MaximEyes Business/input_Zip Code_PR_Info_Address_Main_ZipCode_aa58c9'),
	findTestObject('Object Repository/Evaa Business Records/input_Zip Code_zip')
	],
	
	"Phone":[
	findTestObject('Object Repository/MaximEyes Business/input_Phone_PL_Main_Phone_c84d6056'),
	findTestObject('Object Repository/Evaa Business Records/input__phoneNumber')
	],
	
	"DBA":[
		findTestObject('Object Repository/MaximEyes Business/input_Business Name DBA_txtBusinessNameDBA'),
		findTestObject('Object Repository/Evaa Business Records/input__phoneNumber')
		],
		
	"Website":[
		findTestObject('Object Repository/MaximEyes Business/input_Website_txtWebsite'),
		findTestObject('Object Repository/Evaa Business Records/input_Website_website')
			],
		
	"Federal Tax ID":[
		findTestObject('Object Repository/MaximEyes Business/input_Federal Tax ID_txtTaxId'),
		findTestObject('Object Repository/Evaa Business Records/input_Federal Tax Identification Number for_9843b8')
		],
		
	"Address (Line 2)":[
		findTestObject('Object Repository/MaximEyes Business/input_Address (Line 2)_PL_Main_Line2_c84d6056'),
		findTestObject('Object Repository/Evaa Business Records/input_Address Line 2_addressLineTwo')
		],
		
	"Country":[
		findTestObject('Object Repository/MaximEyes Business/country'),
		findTestObject('Object Repository/Evaa Business Records/button_Select Country')
			],
			
	"Ext":[
		findTestObject('Object Repository/MaximEyes Business/input_Ext_PL_Main_PhoneExtension_c84d6056'),
		findTestObject('Object Repository/Evaa Business Records/input_Extension_extension')
			],
	
	]
	
	verify.captureFields(fields)
	
	CustomKeywords.'verification.CrossSystemVerifier.captureMaximEyesLocations'(
		findTestObject('Object Repository/MaximEyes Business/Location Table Rows')
	)
	
WebUI.click(findTestObject('Object Repository/MaximEyes Business/button__dialog-close-button btn-close 18d6f'))

WebUI.click(findTestObject('Maximeye.com/Page_MaximEyes/Page_MaximEyes/Page_MaximEyes/span_Show Inactive'))

helper.readAndStore(
	"inactive business",
	findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Buisness Table'),
	0
)



// ======================
// READ LOCATIONS
// ======================
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Locations'))

helper.readAndStore(
    "locations",
    findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Location Table'),
    0
)

WebUI.click(findTestObject('Maximeye.com/Page_MaximEyes/Page_MaximEyes/Page_MaximEyes/span_Show Inactive_1'))

helper.readAndStore(
	"inactive locations",
	findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Location Table'),
	0
)
// ======================
// READ PROVIDERS
// ======================
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Providers'))

helper.readAndStore(
    "providers",
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Provider Table'),
    0
)

WebUI.click(findTestObject('Maximeye.com/Page_MaximEyes/Page_MaximEyes/Page_MaximEyes/input_SHOW_INACTIVE_Providers'))

helper.readAndStore(
	"inactive providers",
	findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Provider Table'),
	0
)

//helper.readAndStoreProviders("providers", findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Provider Table'))

// ======================
// READ USERS
// ======================
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes/Users and Permissions'))


helperA.readAndStoreMaxUsers(
    "Users",
    findTestObject("Object Repository/Maximeyes Evaa Login/Page_MaximEyes/User table"),
    1, // First Name
    2, // Last Name
    0, // Login
    findTestObject("Object Repository/Maximeyes Evaa Login/Page_MaximEyes/Pagignation Next Btn")
)

WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes/Users and Permissions'))

WebUI.delay(2)
WebUI.click(findTestObject('Maximeye.com/Page_MaximEyes/Page_MaximEyes/span_Show Inactive Users'))
WebUI.delay(2)

helperB.readAndStoreMaxInactiveUsers(
	"MAX_INACTIVE",
	findTestObject("Object Repository/Maximeyes Evaa Login/Page_MaximEyes/User table"),
	1,
	2,
	0,
	6,  // Active column
	findTestObject("Object Repository/Maximeyes Evaa Login/Page_MaximEyes/Pagignation Next Btn")
)


// ======================
// EVAA LOGIN
// ======================
WebUI.callTestCase(
    findTestCase('Test Cases/Common/Navigate to Evaa Admin'),
    [:],
    FailureHandling.STOP_ON_FAILURE
)

WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Login'))
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/button_MaximEyes'))

WebUI.setText(
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_Sign In With MaximEyes_Username'),
    'GajakumarA'
)

WebUI.setEncryptedText(
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_Sign In With MaximEyes_Password'),
    'cvW8qx4B2o1WegCEDy41Xg=='
)

WebUI.setText(
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_https_MaximEyeURL'),
    'qa10evaa'
)



WebUI.click(
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/button_Login')
)

WebUI.executeJavaScript(
	"document.body.style.zoom='80%'",
	null
)


// ======================
// LOCATIONS – COMPARE + VIEW (EDITABLE → NO READ-ONLY CHECK)
// ======================

TestObject settingBtn = findTestObject("Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Refer a Colleague_btn px-2 d-flex ga_038e76")

WebUI.waitForElementClickable(settingBtn, 5)
WebUI.click(settingBtn)
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Locations'))

helper.compareAndClickView(
    "locations",
    findTestObject('Object Repository/Evaa Locations/Location Table Evaa'),
    0,
    ".//button[normalize-space()='View']",
    false
)

WebUI.click(findTestObject('Evaa Buisness/Page_Unified Admin/input_Show Inactive_1'))

helper.compareAndClickView(
	"inactive locations",
	findTestObject('Object Repository/Evaa Locations/Location Table Evaa'),
	0,
	".//button[normalize-space()='View']",
	false
)


// ======================
// BUSINESS – COMPARE + VIEW (READ-ONLY)
// ======================
TestObject buisnessTable = findTestObject("Object Repository/Evaa Buisness/Buisness Table Evaa")
WebUI.waitForElementClickable(settingBtn, 5)
WebUI.click(settingBtn)
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/button_Business'))
WebUI.delay(5)
WebUI.waitForElementVisible(buisnessTable, 90, FailureHandling.CONTINUE_ON_FAILURE)

helper.compareAndClickView(
    "business",
    findTestObject('Object Repository/Evaa Buisness/Buisness Table Evaa'),
    0,
    ".//button[normalize-space()='View']",
    true
)

WebUI.click(
	findTestObject(
		'Object Repository/Evaa Business Records/LocName View Btn',
		['locationName':'Frequency Online']
	)
)

verify.verifyFields(fields)

// 
CustomKeywords.'verification.CrossSystemVerifier.captureEvaaLocations'(
	findTestObject('Object Repository/Evaa Business Records/Location Table')
)

// Compare
CustomKeywords.'verification.CrossSystemVerifier.compareLocations'()

WebUI.click(findTestObject('Object Repository/Evaa Buisness/button_Cancel'))

WebUI.click(findTestObject('Evaa Buisness/Page_Unified Admin/input_Show Inactive'))

helper.compareAndClickView(
	"inactive business",
	findTestObject('Object Repository/Evaa Buisness/Buisness Table Evaa'),
	0,
	".//button[normalize-space()='View']",
	true
)

// ======================
// PROVIDERS – COMPARE + VIEW (READ-ONLY)
// ======================
WebUI.click(findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/span_Providers'))
WebUI.delay(5)

helper.compareAndClickView(
    "providers",
    findTestObject('Object Repository/Evaa Providers/Providers Table Evaa'),
    0,
    ".//button[normalize-space()='View']",
    true
)

WebUI.click(findTestObject('Evaa Buisness/Page_Unified Admin/input_Show Inactive_2'))

helper.compareAndClickView(
	"inactive providers",
	findTestObject('Object Repository/Evaa Providers/Providers Table Evaa'),
	0,
	".//button[normalize-space()='View']",
	true
)

// ======================
// USER – COMPARE 
// ======================
WebUI.click(findTestObject('Object Repository/Users/Page_Unified Admin/button_Users'))
WebUI.delay(3)


Map usersEvaa = helperA.getEvaaUsers(findTestObject("Object Repository/Users/Page_Unified Admin/User Table Evaa"))

println(usersEvaa)

helperA.compareUsers("Users", usersEvaa)

WebUI.click(findTestObject('Users/Page_Unified Admin/Page_Unified Admin/input_toggleShowInactive'))

Map evaaInactive = helperB.getEvaaInactiveUsers(
	findTestObject("Object Repository/Users/Page_Unified Admin/User Table Evaa")
)

helperB.compareInctiveUsers("MAX_INACTIVE", evaaInactive)

CustomKeywords.'common.TableDataHelper.assertAll'()


