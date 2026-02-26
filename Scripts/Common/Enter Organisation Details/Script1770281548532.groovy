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

WebUI.verifyElementText(
	findTestObject("Object Repository/Organization Details Page/Organization Details Header Text"),
	"Organization Details"
)

WebUI.click(findTestObject("Object Repository/Organization Details Page/Select Language"))
WebUI.click(findTestObject("Object Repository/Organization Details Page/Language English"))

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/Organization Name"),
	orgName
)

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/Tax ID_TaxId input"),
	taxId
)

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/Phone Number"),
	phone
)

WebUI.verifyElementAttributeValue(
	findTestObject("Object Repository/Organization Details Page/Email Address Input"),
	"value",
	email,
	10
)

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/input_Address_Address"),
	address
)

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/input_City_City"),
	city
)

WebUI.click(findTestObject("Object Repository/Organization Details Page/Select state"))
WebUI.click(findTestObject("Object Repository/Organization Details Page/${state}"))

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/input_ZIP Code"),
	zip
)

WebUI.click(findTestObject("Object Repository/Organization Details Page/Select your specialty type"))
WebUI.click(findTestObject("Object Repository/Organization Details Page/${specialty}"))

WebUI.setText(
	findTestObject("Object Repository/Organization Details Page/Website or Preferred EVAA Subdomain"),
	website
)

WebUI.click(findTestObject("Object Repository/Organization Details Page/button_Next"))
