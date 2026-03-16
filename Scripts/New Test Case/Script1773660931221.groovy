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

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/button_Select an Assistant'))

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/button_Scribe'))

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/a_Dashboard'))

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/div_Enterprise Optical Group - ScribeAI-powered'), 
    0)

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/span_Recorded Sessions'))

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/div_Enterprise Optical Group - ScribeAI-powered'), 
    0)

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/span_Templates'))

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/div_Enterprise Optical Group - ScribeAI-powered'), 
    0)

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/span_Setup'))

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/span_Storage'))

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/div_Enterprise Optical Group - ScribeAI-powered'), 
    0)

WebUI.click(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/span_Default Preferences'))

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/div_Enterprise Optical Group - ScribeAI-powered'), 
    0)

WebUI.rightClick(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/main_Welcome fic - Admin OverviewMonitor setup s'))

WebUI.verifyElementPresent(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/main_Welcome fic - Admin OverviewMonitor setup s'), 
    0)

WebUI.verifyElementText(findTestObject('Evaa Admin Start Page/Page_Evaa Admin/h1_Welcome fic - Admin Overview'), 'Welcome FIC - Admin Overview')

