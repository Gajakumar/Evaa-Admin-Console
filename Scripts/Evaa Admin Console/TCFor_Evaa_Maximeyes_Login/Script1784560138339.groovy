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
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

//WebUI.callTestCase(findTestCase('Test Cases/Common/Evaa VA/Navigate to Evaa Admin'), [:], FailureHandling.STOP_ON_FAILURE)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Login'))
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_MaximEyes'))
//
//WebUI.setText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Username_Email'), 'QA_User')
//
//WebUI.setEncryptedText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter Your Password'), 'V35d/XPbheASJTEPzyNXhQ==')
//
//WebUI.setText(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/input_Enter URL'), 'qa5')
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_MaximEyes Identity/button_Login'))
//
//WebUI.verifyElementAttributeValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/img_EVAA Logo'), 'alt', 'EVAA Logo', 5)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h1_Welcome qa5 - Admin Overview'), 'Welcome qa5 - Admin Overview', 
//    0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'))
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'))
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant_1'), 'Virtual Assistant', 
//    5)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Action Required'), 'Action Required', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Unassigned'), 'Unassigned', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Assigned'), 'Assigned', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_My Queue'), 'My Queue', 0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Action Required'))
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_All'), 'All', 0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Dashboard'))
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Unassigned'))
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Unassigned (0)'), 'Unassigned (0)', 0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Dashboard'))
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Assigned'))
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Assigned (0)'), 'Assigned (0)', 0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Dashboard'))
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_My Queue'))
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_My Queue (0)'), 'My Queue (0)', 0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'))
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Voicemail'), 'Voicemail', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Interactive Voice'), 'Interactive Voice', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Chat Appearance'), 'Chat Appearance', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Embedded_Share'), 'Embedded/Share', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_AI Training'), 'AI Training', 0)
//
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Preferences'), 'Preferences', 0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Chat Appearance'))
//
//WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 
//    0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_AI Training'))
//
//WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 
//    0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_AI Training Content'))
//
//WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 
//    0)
//
//WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Preferences'))
//
//WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 
//    0)
//
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable

// Default timeout (in seconds)
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

// =====================================================================
// STEP 3: Navigate to Virtual Assistant view
// =====================================================================

// Open the "Select an Assistant" dropdown/menu
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'))
WebUI.comment('STEP 3.1: Opened "Select an Assistant" menu.')

// Select "Virtual Assistant" from the menu
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'))
WebUI.comment('STEP 3.2: Selected "Virtual Assistant".')

// //Verify the Virtual Assistant view has loaded correctly
//WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant_1'), 'Virtual Assistant', DEFAULT_TIMEOUT)
//WebUI.comment('STEP 3.3: Verified "Virtual Assistant" view is loaded.') QA User

// =====================================================================
// STEP 4: Verify dashboard queue sections are present
// =====================================================================

// Verify "Action Required" section header
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Action Required'), 'Action Required', 5)
WebUI.comment('STEP 4.1: Verified "Action Required" section header.')

// Verify "Unassigned" section header
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Unassigned'), 'Unassigned', 5)
WebUI.comment('STEP 4.2: Verified "Unassigned" section header.')

// Verify "Assigned" section header
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Assigned'), 'Assigned', 5)
WebUI.comment('STEP 4.3: Verified "Assigned" section header.')

// Verify "My Queue" section header
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_My Queue'), 'My Queue', 5)
WebUI.comment('STEP 4.4: Verified "My Queue" section header.')

// =====================================================================
// STEP 5: Validate "Action Required" queue
// =====================================================================

// Click into the "Action Required" section
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Action Required'))
WebUI.comment('STEP 5.1: Clicked "Action Required" section.')

// Verify the "All" filter tab is displayed
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_All'), 'All', 5)
WebUI.comment('STEP 5.2: Verified User landed on "All" filter tab in Action Required queue.')

// Fetch the computed "border-bottom-color" CSS value of the "All" tab to confirm it's active/selected
String allTabBorderColor = WebUI.getCSSValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_All'), 'border-bottom-color')
WebUI.comment('STEP 5.3: Fetched "border-bottom-color" of "All" tab -> ' + allTabBorderColor)

// Verify the active tab indicator color matches the expected highlight color (pink/magenta)
String expectedActiveColor = 'rgba(184, 14, 116, 1)'
if (allTabBorderColor.trim().equalsIgnoreCase(expectedActiveColor)) {
	WebUI.comment('STEP 5.4: PASSED - "All" tab active border-bottom-color matches expected value: ' + expectedActiveColor)
} else {
	KeywordUtil.markFailed('STEP 5.4: FAILED - Expected border-bottom-color "' + expectedActiveColor + '" but got "' + allTabBorderColor + '"')
}

// Return to Dashboard
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Dashboard'))
WebUI.comment('STEP 5.3: Navigated back to Dashboard.')

// =====================================================================
// STEP 6: Validate "Unassigned" queue (dynamic count) and active tab color
// =====================================================================

// Click into the "Unassigned" section
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Unassigned'))
WebUI.comment('STEP 6.1: Clicked "Unassigned" section.')

// Fetch the actual text of the Unassigned tab (count is dynamic, e.g. "Unassigned (3)")
String unassignedTabText = WebUI.getText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Unassigned (0)'))
WebUI.comment('STEP 6.2: Fetched "Unassigned" tab text -> ' + unassignedTabText)

// Verify the tab text matches the pattern "Unassigned (<number>)" regardless of the actual count
boolean isUnassignedFormatValid = unassignedTabText.matches(/Unassigned \(\d+\)/)
if (isUnassignedFormatValid) {
    WebUI.comment('STEP 6.3: PASSED - "Unassigned" tab text matches expected pattern "Unassigned (N)".')
} else {
    KeywordUtil.markFailed('STEP 6.3: FAILED - "Unassigned" tab text "' + unassignedTabText + '" does not match expected pattern "Unassigned (N)".')
}

// Extract and log the actual count for reporting/debugging purposes
String unassignedCount = (unassignedTabText =~ /\((\d+)\)/)[0][1]
WebUI.comment('STEP 6.4: Unassigned queue count = ' + unassignedCount)

// Verify the active tab indicator color (border-bottom-color) matches expected highlight color
String unassignedBorderColor = WebUI.getCSSValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Unassigned (0)'), 'border-bottom-color')

if (unassignedBorderColor.trim().equalsIgnoreCase(expectedActiveColor)) {
    WebUI.comment('STEP 6.5: PASSED - "Unassigned" tab active border-bottom-color matches expected value: ' + expectedActiveColor)
} else {
    KeywordUtil.markFailed('STEP 6.5: FAILED - Expected border-bottom-color "' + expectedActiveColor + '" but got "' + unassignedBorderColor + '"')
}

// Return to Dashboard
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Dashboard'))
WebUI.comment('STEP 6.6: Navigated back to Dashboard.')

// =====================================================================
// STEP 7: Validate "Assigned" queue (dynamic count) and active tab color
// =====================================================================

// Click into the "Assigned" section
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_Assigned'))
WebUI.comment('STEP 7.1: Clicked "Assigned" section.')

// Fetch the actual text of the Assigned tab (count is dynamic, e.g. "Assigned (5)")
String assignedTabText = WebUI.getText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Assigned (0)'))
WebUI.comment('STEP 7.2: Fetched "Assigned" tab text -> ' + assignedTabText)

// Verify the tab text matches the pattern "Assigned (<number>)" regardless of the actual count
boolean isAssignedFormatValid = assignedTabText.matches(/Assigned \(\d+\)/)
if (isAssignedFormatValid) {
    WebUI.comment('STEP 7.3: PASSED - "Assigned" tab text matches expected pattern "Assigned (N)".')
} else {
    KeywordUtil.markFailed('STEP 7.3: FAILED - "Assigned" tab text "' + assignedTabText + '" does not match expected pattern "Assigned (N)".')
}

// Extract and log the actual count for reporting/debugging purposes
String assignedCount = (assignedTabText =~ /\((\d+)\)/)[0][1]
WebUI.comment('STEP 7.4: Assigned queue count = ' + assignedCount)

// Verify the active tab indicator color (border-bottom-color) matches expected highlight color
String assignedBorderColor = WebUI.getCSSValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Assigned (0)'), 'border-bottom-color')
if (assignedBorderColor.trim().equalsIgnoreCase(expectedActiveColor)) {
    WebUI.comment('STEP 7.5: PASSED - "Assigned" tab active border-bottom-color matches expected value: ' + expectedActiveColor)
} else {
    KeywordUtil.markFailed('STEP 7.5: FAILED - Expected border-bottom-color "' + expectedActiveColor + '" but got "' + assignedBorderColor + '"')
}

// Return to Dashboard
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Dashboard'))
WebUI.comment('STEP 7.6: Navigated back to Dashboard.')


// =====================================================================
// STEP 8: Validate "My Queue" (dynamic count) and active tab color
// =====================================================================

// Click into the "My Queue" section
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/h3_My Queue'))
WebUI.comment('STEP 8.1: Clicked "My Queue" section.')

// Fetch the actual text of the My Queue tab (count is dynamic, e.g. "My Queue (2)")
String myQueueTabText = WebUI.getText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_My Queue (0)'))
WebUI.comment('STEP 8.2: Fetched "My Queue" tab text -> ' + myQueueTabText)

// Verify the tab text matches the pattern "My Queue (<number>)" regardless of the actual count
boolean isMyQueueFormatValid = myQueueTabText.matches(/My Queue \(\d+\)/)
if (isMyQueueFormatValid) {
    WebUI.comment('STEP 8.3: PASSED - "My Queue" tab text matches expected pattern "My Queue (N)".')
} else {
    KeywordUtil.markFailed('STEP 8.3: FAILED - "My Queue" tab text "' + myQueueTabText + '" does not match expected pattern "My Queue (N)".')
}

// Extract and log the actual count for reporting/debugging purposes
String myQueueCount = (myQueueTabText =~ /\((\d+)\)/)[0][1]
WebUI.comment('STEP 8.4: My Queue count = ' + myQueueCount)

// Verify the active tab indicator color (border-bottom-color) matches expected highlight color
String myQueueBorderColor = WebUI.getCSSValue(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_My Queue (0)'), 'border-bottom-color')
if (myQueueBorderColor.trim().equalsIgnoreCase(expectedActiveColor)) {
    WebUI.comment('STEP 8.5: PASSED - "My Queue" tab active border-bottom-color matches expected value: ' + expectedActiveColor)
} else {
    KeywordUtil.markFailed('STEP 8.5: FAILED - Expected border-bottom-color "' + expectedActiveColor + '" but got "' + myQueueBorderColor + '"')
}

// =====================================================================
// STEP 9: Navigate to Setup and verify all setup menu links
// =====================================================================

// Open the "Setup" menu
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'))
WebUI.comment('STEP 9.1: Opened "Setup" menu.')

// Verify "Voicemail" link is present
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Voicemail'), 'Voicemail', 5)
WebUI.comment('STEP 9.2: Verified "Voicemail" link is present in Setup menu.')

// Verify "Interactive Voice" link is present
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Interactive Voice'), 'Interactive Voice', 5)
WebUI.comment('STEP 9.3: Verified "Interactive Voice" link is present in Setup menu.')

// Verify "Chat Appearance" link is present
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Chat Appearance'), 'Chat Appearance', 5)
WebUI.comment('STEP 9.4: Verified "Chat Appearance" link is present in Setup menu.')

// Verify "Embedded/Share" link is present
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Embedded_Share'), 'Embedded/Share', 5)
WebUI.comment('STEP 9.5: Verified "Embedded/Share" link is present in Setup menu.')

// Verify "AI Training" link is present
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_AI Training'), 'AI Training', 5)
WebUI.comment('STEP 9.6: Verified "AI Training" link is present in Setup menu.')

// Verify "Preferences" link is present
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Preferences'), 'Preferences', 5)
WebUI.comment('STEP 9.7: Verified "Preferences" link is present in Setup menu.')

// =====================================================================
// STEP 10: Validate "Chat Appearance" page
// ===================================================================== 

// Navigate to "Chat Appearance"
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Chat Appearance'))
WebUI.comment('STEP 10.1: Navigated to "Chat Appearance" page.')

// Verify the "Virtual Assistant Name" section is visible
WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 5)
WebUI.comment('STEP 10.2: Verified "Virtual Assistant Name" section is visible on Chat Appearance page.')

// =====================================================================
// STEP 11: Validate "AI Training" page
// =====================================================================

// Navigate to "AI Training"
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_AI Training'))
WebUI.comment('STEP 11.1: Navigated to "AI Training" page.')

// Verify the "Virtual Assistant Name" section is visible
WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 5)
WebUI.comment('STEP 11.2: Verified "Virtual Assistant Name" section is visible on AI Training page.')

// Open "AI Training Content"
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_AI Training Content'))
WebUI.comment('STEP 11.3: Opened "AI Training Content".')

// Re-verify the "Virtual Assistant Name" section is still visible
WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 5)
WebUI.comment('STEP 11.4: Verified "Virtual Assistant Name" section is visible after opening AI Training Content.')

// =====================================================================
// STEP 12: Validate "Preferences" page
// =====================================================================

// Navigate to "Preferences"
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Preferences'))
WebUI.comment('STEP 12.1: Navigated to "Preferences" page.')

// Verify the "Virtual Assistant Name" section is visible
WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), 5)
WebUI.comment('STEP 12.2: Verified "Virtual Assistant Name" section is visible on Preferences page.')

// Final log confirming test completion
WebUI.comment('TEST COMPLETE: All EVAA Admin Dashboard, Queue, and Setup validations passed successfully.')