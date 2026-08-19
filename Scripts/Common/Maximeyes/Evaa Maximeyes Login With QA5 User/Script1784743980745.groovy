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

int DEFAULT_TIMEOUT = 10

//=====================================================================
//STEP 1: Navigate to EVAA Admin and log in via MaximEyes Identity
//=====================================================================

// Navigate to the EVAA Admin page (common/reusable test case)
WebUI.callTestCase(findTestCase('Test Cases/Common/Evaa VA/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)
WebUI.comment('STEP 1.1: Navigated to EVAA Admin page.')

// Wait for and click the Login button on the EVAA AI landing page
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'))
WebUI.comment('STEP 1.2: Clicked "Login" button on EVAA AI page.')

// Wait for and click the "MaximEyes" identity provider option
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_MaximEyes'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_MaximEyes'))
WebUI.comment('STEP 1.3: Selected "MaximEyes" as Practice')

// Enter username on the MaximEyes Identity login form
WebUI.waitForElementVisible(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Username_Email'), DEFAULT_TIMEOUT)
WebUI.setText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Username_Email'), 'QA_User')
WebUI.comment('STEP 1.4: Entered username "QA_User".')

// Enter encrypted password
WebUI.setEncryptedText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Password'), 'V35d/XPbheASJTEPzyNXhQ==')
WebUI.comment('STEP 1.5: Entered password (encrypted).')

// Enter the target environment URL/tenant identifier
WebUI.setText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter URL'), 'qa5')
WebUI.comment('STEP 1.6: Entered environment/tenant URL "qa5".')

// Submit the login form
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_Login'))
WebUI.comment('STEP 1.7: Submitted login form.')

// =====================================================================
// STEP 2: Verify successful login and landing on Admin Overview page
// =====================================================================

// Verify EVAA logo is present with the correct alt text (confirms page loaded correctly)
WebUI.waitForElementPresent(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), DEFAULT_TIMEOUT)
WebUI.verifyElementAttributeValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), 'alt', 'EVAA Logo', DEFAULT_TIMEOUT)
WebUI.comment('STEP 2.1: Verified EVAA logo is present with alt text "EVAA Logo".')

// Verify the Admin Overview welcome header text
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h1_Welcome qa5 - Admin Overview'), 'Welcome qa5 - Admin Overview', 5)
WebUI.comment('STEP 2.2: Verified "Welcome qa5 - Admin Overview" header text.')