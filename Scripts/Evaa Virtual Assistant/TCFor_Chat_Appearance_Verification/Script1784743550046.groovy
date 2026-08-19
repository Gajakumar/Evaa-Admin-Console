import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
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


/* =============================================================
 * TEST DATA / CONFIGURATION
 * ============================================================= */

// --- Timeouts ---
int DEFAULT_TIMEOUT      = 10
int SHORT_TIMEOUT        = 5
int ZERO_TIMEOUT         = 0

// --- URLs ---
String QA5_URL            = 'https://qa5.eyeclinic.ai/'
String EVAA_WINDOW_TITLE  = 'Evaa AI'

// --- Custom Assistant values (values set during the test) ---
String customAssistantName      = 'Katalon Vartual Assistant'
String customWelcomeMessage     = 'Hello i am your katalon Assitant'
String customButtonLabel        = 'Book Appointment'
String customThemeColorName     = 'Royal Magenta (RM)'
String customIconPosition       = 'Left'
String customChatIconPositionTxt = 'Chat Icon - Position: Left'

// --- Default / Reset ("factory") values expected after RESET ---
String defaultAssistantName        = 'Evaa Virtual Assistant'
String defaultWelcomeMessage       = "Hello, I'm EVAA, --- I can help book appointments, check your order status, or answer many questions about the practice!"
String defaultThemeColorName       = 'Royal Magenta (RM)'
String defaultThemeColorRgb        = 'rgb(184, 14, 116)'
String defaultIconPositionLabel    = 'None'
String defaultButtonLabelBook      = 'Book Appointment'
String defaultButtonLabelOrder     = 'Order Status'
String defaultChatIconPositionTxt  = 'Chat Icon - Position: Right'
String defaultIconPositionActual   = 'Right'

// --- Colors used for verification ---
String colorAfterSetup   = 'rgb(1, 52, 153)'
String colorAfterReset   = 'rgb(195, 29, 118)'

/* =============================================================
 * STEP 1: LOGIN
 * ============================================================= */
WebUI.comment('STEP 1: Logging in to Maximeyes as QA5 user.')
WebUI.callTestCase(
	findTestCase('Test Cases/Common/Maximeyes/Evaa Maximeyes Login With QA5 User'),
	[:],
	FailureHandling.STOP_ON_FAILURE
)
WebUI.comment('STEP 1: Login completed successfully.')

/* =============================================================
 * STEP 2: OPEN VIRTUAL ASSISTANT
 * ============================================================= */
WebUI.comment('STEP 2.1: Opening the "Select an Assistant" dropdown/menu.')
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Select an Assistant'))
WebUI.comment('STEP 2.1: Opened "Select an Assistant" menu.')

WebUI.comment('STEP 2.2: Selecting "Virtual Assistant" from the menu.')
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant'))
WebUI.comment('STEP 2.2: Selected "Virtual Assistant".')

WebUI.comment('STEP 2.3: Verifying the Virtual Assistant view has loaded correctly.')
WebUI.assertElementText(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Virtual Assistant_1'), 'QA User', DEFAULT_TIMEOUT)
WebUI.comment('STEP 2.3: Verified "QA User" view is loaded.')

/* =============================================================
 * STEP 3: NAVIGATE TO CHAT APPEARANCE SETTINGS
 * ============================================================= */
WebUI.comment('STEP 3.1: Opening the "Setup" menu.')
WebUI.waitForElementClickable(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'), DEFAULT_TIMEOUT)
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/button_Setup'))
WebUI.comment('STEP 3.1: Opened "Setup" menu.')

WebUI.comment('STEP 3.2: Navigating to "Chat Appearance" page.')
WebUI.click(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/a_Chat Appearance'))
WebUI.comment('STEP 3.2: Navigated to "Chat Appearance" page.')

WebUI.comment('STEP 3.3: Verifying the "Virtual Assistant Name" section is visible.')
WebUI.assertElementVisible(findTestObject('Evaa Maximeyes Login/Page_Evaa AI/div_Virtual Assistant NameShown in the chat head'), SHORT_TIMEOUT)
WebUI.comment('STEP 3.3: Verified "Virtual Assistant Name" section is visible on Chat Appearance page.')

/* =============================================================
 * STEP 4: CUSTOMIZE CHAT APPEARANCE (using variables, not hard-coded text)
 * ============================================================= */
WebUI.comment('STEP 4.1: Setting custom assistant name to "' + customAssistantName + '".')
WebUI.setText(findTestObject('Chat Appearance/Page_Evaa AI/input_Evaa Virtual Assistant'), customAssistantName)

WebUI.comment('STEP 4.2: Setting custom welcome message to "' + customWelcomeMessage + '".')
WebUI.setText(findTestObject('Chat Appearance/Page_Evaa AI/textarea_Hello, Im EVAA, - I can help book appo'), customWelcomeMessage)

WebUI.comment('STEP 4.3: Setting custom action button label to "' + customButtonLabel + '".')
WebUI.setText(findTestObject('Chat Appearance/Page_Evaa AI/textarea_Book Appointment, Order Status'), customButtonLabel)

WebUI.comment('STEP 4.4: Selecting theme color "' + customThemeColorName + '".')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/button_Royal Magenta (RM)'))

WebUI.comment('STEP 4.5: Opening additional style/position dropdown.')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/div_radix-_r52'))

WebUI.comment('STEP 4.6: Selecting chat icon position "' + customIconPosition + '".')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/button_Left'))

WebUI.comment('STEP 4.7: Selecting avatar/style option "None".')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/button_None'))

WebUI.comment('STEP 4.8: Opening secondary style dropdown.')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/div_radix-_r5e'))

WebUI.comment('STEP 4.9: Saving the Chat Appearance changes.')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/button_SAVE CHANGES'))
WebUI.comment('STEP 4: Chat Appearance customized and saved with name="' + customAssistantName +
	'", welcome="' + customWelcomeMessage + '", button="' + customButtonLabel +
	'", color="' + customThemeColorName + '", position="' + customIconPosition + '".')

/* =============================================================
 * STEP 5: VERIFY CUSTOMIZED SETTINGS ON THE SETUP/ADMIN PAGE
 * ============================================================= */
WebUI.comment('STEP 5.1: Waiting for and verifying the header now shows the custom assistant name.')
TestObject headerText = findTestObject('Chat Appearance/Page_Evaa AI/span_Katalon Vartual Assistant')
WebUI.waitForElementVisible(headerText, DEFAULT_TIMEOUT)
WebUI.assertElementText(headerText, customAssistantName, SHORT_TIMEOUT)

WebUI.comment('STEP 5.2: Verifying the custom welcome message text.')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/p_Hello i am your katalon Assitant'), customWelcomeMessage, SHORT_TIMEOUT)

WebUI.comment('STEP 5.3: Verifying the custom action button label.')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/button_Book Appointment'), customButtonLabel, SHORT_TIMEOUT)

WebUI.comment('STEP 5.4: Verifying the custom assistant container is visible.')
WebUI.assertElementVisible(findTestObject('Chat Appearance/Page_Evaa AI/div_Katalon Vartual Assistant'), SHORT_TIMEOUT)

WebUI.comment('STEP 5.5: Verifying the chat icon position label reflects "Left".')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/p_Chat Icon - Position_ right'), customChatIconPositionTxt, SHORT_TIMEOUT)
WebUI.comment('STEP 5: Verified all customized Chat Appearance settings on the admin page.')

/* =============================================================
 * STEP 6: VERIFY CUSTOMIZATIONS ON THE LIVE EVAA CHAT WIDGET
 * ============================================================= */
WebUI.comment('STEP 6.1: Opening a new tab and navigating to the live site: ' + QA5_URL)
WebUI.newTab('')
WebUI.navigateToUrl(QA5_URL)

WebUI.comment('STEP 6.2: Waiting for the "Push to talk" chat icon to be visible.')
TestObject pushToTalk = findTestObject('Chat Appearance/Evaa React/img_Push to talk')
WebUI.waitForElementVisible(pushToTalk, DEFAULT_TIMEOUT)

WebUI.comment('STEP 6.3: Verifying the chat icon position is "' + customIconPosition + '".')
CustomKeywords.'common.UIValidation.verifyElementProperty'(pushToTalk, 'Position', customIconPosition)

WebUI.comment('STEP 6.4: Verifying the widget welcome message matches the custom text.')
String welcomeMessages = WebUI.getText(findTestObject('Chat Appearance/Evaa React/div_welcomeMessages')).replaceAll('X$', '').trim()
WebUI.verifyEqual(welcomeMessages, customWelcomeMessage)

WebUI.comment('STEP 6.5: Clicking the chat icon to open the widget.')
WebUI.click(pushToTalk)

WebUI.comment('STEP 6.6: Verifying the widget header shows the custom assistant name.')
TestObject header = findTestObject('Chat Appearance/Evaa React/h1_Katalon Vartual Assistant')
WebUI.waitForElementVisible(header, DEFAULT_TIMEOUT)
WebUI.assertElementText(header, customAssistantName, SHORT_TIMEOUT)

WebUI.comment('STEP 6.7: Verifying the widget header background color reflects the selected theme.')
CustomKeywords.'common.UIValidation.verifyElementProperty'(
	findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/header_Evaa Virtual Assistant'),
	'BackgroundColor',
	colorAfterSetup
)

WebUI.comment('STEP 6.8: Verifying the widget welcome message paragraph text.')
WebUI.assertElementText(findTestObject('Chat Appearance/Evaa React/p_Hello i am your katalon Assitant'), customWelcomeMessage, SHORT_TIMEOUT)

WebUI.comment('STEP 6.9: Verifying the widget action button label and clicking it.')
WebUI.assertElementText(findTestObject('Chat Appearance/Evaa React/button_Book Appointment'), customButtonLabel, SHORT_TIMEOUT)
WebUI.click(findTestObject('Chat Appearance/Evaa React/button_Book Appointment'))
WebUI.comment('STEP 6: Verified custom appearance is correctly reflected on the live Evaa chat widget.')

/* =============================================================
 * STEP 7: RESET CHAT APPEARANCE TO DEFAULTS AND VERIFY (ADMIN PAGE)
 * ============================================================= */
WebUI.comment('STEP 7.1: Switching back to the "' + EVAA_WINDOW_TITLE + '" window.')
WebUI.switchToWindowTitle(EVAA_WINDOW_TITLE)

WebUI.comment('STEP 7.2: Clicking the "RESET" button to restore default Chat Appearance settings.')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/button_RESET'))

WebUI.comment('STEP 7.3: Verifying the assistant name input reset to the default value.')
WebUI.verifyElementAttributeValue(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/input_Evaa Virtual Assistant'),
	'defaultValue',
	defaultAssistantName,
	SHORT_TIMEOUT
)

WebUI.comment('STEP 7.4: Verifying the welcome message textarea reset to the default text.')
WebUI.assertElementText(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/textarea_Hello, Im EVAA, - I can help book appo'),
	defaultWelcomeMessage,
	ZERO_TIMEOUT
)

WebUI.comment('STEP 7.5: Verifying the default theme color label.')
WebUI.assertElementText(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/div_Royal Magenta (RM)'),
	defaultThemeColorName,
	ZERO_TIMEOUT
)

WebUI.comment('STEP 7.6: Verifying the default position button background color.')
CustomKeywords.'common.UIValidation.verifyElementProperty'(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/button_Right'),
	'BackgroundColor',
	defaultThemeColorRgb
)

WebUI.comment('STEP 7.7: Verifying the "None" style option label after reset.')
WebUI.assertElementText(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/button_None'),
	defaultIconPositionLabel,
	ZERO_TIMEOUT
)

WebUI.comment('STEP 7.8: Verifying the header shows the default assistant name.')
WebUI.assertElementText(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/span_Evaa Virtual Assistant'),
	defaultAssistantName,
	ZERO_TIMEOUT
)

WebUI.comment('STEP 7.9: Verifying the default welcome message paragraph text.')
WebUI.assertElementText(
	findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/p_Hello, Im EVAA, - I can help book appointment'),
	defaultWelcomeMessage,
	ZERO_TIMEOUT
)

WebUI.comment('STEP 7.10: Verifying the default "Book Appointment" and "Order Status" button labels.')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/button_Book Appointment'), defaultButtonLabelBook, ZERO_TIMEOUT)
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/button_Order Status'), defaultButtonLabelOrder, ZERO_TIMEOUT)

WebUI.comment('STEP 7.11: Verifying the chat icon position label reflects "Right" after reset.')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Page_Evaa AI/p_Chat Icon - Position_ right'), defaultChatIconPositionTxt, ZERO_TIMEOUT)
WebUI.comment('STEP 7: Verified all Chat Appearance settings were restored to their default values.')

/* =============================================================
 * STEP 8: VERIFY RESET SETTINGS ON THE LIVE EVAA CHAT WIDGET
 * ============================================================= */
WebUI.comment('STEP 8.1: Switching back to the live site tab and refreshing it.')
WebUI.switchToWindowUrl(QA5_URL)
WebUI.refresh()
WebUI.waitForElementVisible(pushToTalk, DEFAULT_TIMEOUT)

WebUI.comment('STEP 8.2: Verifying the chat icon position is "' + defaultIconPositionActual + '" after reset.')
CustomKeywords.'common.UIValidation.verifyElementProperty'(pushToTalk, 'Position', defaultIconPositionActual)

WebUI.comment('STEP 8.3: Verifying the widget welcome message matches the default text.')
String welcomeMessages1 = WebUI.getText(findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/div_welcomeMessages')).replaceAll('X$', '').trim()
WebUI.verifyEqual(welcomeMessages1, defaultWelcomeMessage)

WebUI.comment('STEP 8.4: Clicking the chat icon to open the widget.')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/img_Push to talk'))
WebUI.waitForElementVisible(header, DEFAULT_TIMEOUT)

WebUI.comment('STEP 8.5: Verifying the widget header shows the default assistant name.')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/h1_Evaa Virtual Assistant'), defaultAssistantName, ZERO_TIMEOUT)

WebUI.comment('STEP 8.6: Verifying the widget welcome message paragraph text (normalized whitespace).')
String actualTextmsg = WebUI.getText(
	findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/p_Hello, Im EVAA, - I can help book appointment')
).replaceAll('\\s+', ' ').trim()
String expectedTextmsg = defaultWelcomeMessage.replaceAll('\\s+', ' ').trim()
WebUI.verifyEqual(actualTextmsg, expectedTextmsg)

WebUI.comment('STEP 8.7: Clicking the "Book Appointment" button on the widget.')
WebUI.click(findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/button_Book Appointment'))

WebUI.comment('STEP 8.8: Verifying the widget header background color reflects the default theme.')
CustomKeywords.'common.UIValidation.verifyElementProperty'(
	findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/header_Evaa Virtual Assistant'),
	'BackgroundColor',
	colorAfterReset
)

WebUI.comment('STEP 8.9: Verifying the "Book Appointment" and "Order Status" button labels on the widget.')
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/button_Book Appointment'), defaultButtonLabelBook, ZERO_TIMEOUT)
WebUI.assertElementText(findTestObject('Chat Appearance/Page_Evaa AI/Chat Appearance/button_Order Status'), defaultButtonLabelOrder, ZERO_TIMEOUT)
WebUI.comment('STEP 8: Verified all default Chat Appearance settings are correctly reflected on the live Evaa chat widget.')

WebUI.comment('TEST COMPLETE: Chat Appearance customization and reset flow verified successfully end-to-end.')