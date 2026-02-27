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

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import common.TableDataHelper
import common.TableDataHelper
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

TableDataHelper helper = new TableDataHelper()

// ======================
// MAXIMEYES LOGIN
// ======================
WebUI.callTestCase(
    findTestCase('Test Cases/Common/Maximeyes Login'),
    [:],
    FailureHandling.STOP_ON_FAILURE
)


//WebUI.navigateToUrl("https://thomasmoorevm.maximeyes.com/")
//
//WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/UserName'), "ChelseaN")
//
//WebUI.setText(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Password'), "Test@123")
//
//WebUI.click(
//	findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Login Button')
//)

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

// ======================
// READ LOCATIONS
// ======================
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Locations'))

helper.readAndStore(
    "locations",
    findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/Location Table'),
    0
)

// ======================
// READ PROVIDERS
// ======================
WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Providers'))

//helper.readAndStore(
//    "providers",
//    findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Provider Table'),
//    0
//)

helper.readAndStoreProviders("providers", findTestObject('Object Repository/Maximeyes Evaa Login/Page_Unified Admin/Provider Table'))

// ======================
// LOGOUT MAXIMEYES
// ======================
//WebUI.click(findTestObject('Object Repository/Maximeye.com/Page_MaximEyes/a_Logout'))

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

//WebUI.setText(
//	findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_Sign In With MaximEyes_Username'),
//	'ChelseaN'
//)
//
//WebUI.setText(
//	findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_Sign In With MaximEyes_Password'),
//	'Test@123'
//)
//
//WebUI.setText(
//	findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/input_https_MaximEyeURL'),
//	'thomasmoorevm'
//)

WebUI.click(
    findTestObject('Object Repository/Maximeyes Evaa Login/Page_MaximEyes Identity/button_Login')
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


CustomKeywords.'common.TableDataHelper.assertAll'()


