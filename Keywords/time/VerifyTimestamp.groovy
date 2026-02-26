package time

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable


import java.text.SimpleDateFormat

//public class VerifyTimestamp {
//
//	/**
//	 * Reusable keyword to verify UI timestamp with system time.
//	 *
//	 * @param to                TestObject of the timestamp element
//	 * @param pattern           Timestamp pattern ex: "dd/MM/yyyy | HH:mm:ss z"
//	 * @param allowedSeconds    Allowed difference in seconds
//	 */
//	@Keyword
//	def verifyTimestamp(TestObject to, String pattern, int allowedSeconds) {
//
//		// 1️⃣ Get UI timestamp text
//		String uiTime = WebUI.getText(to).trim()
//		println("📌 UI Timestamp: " + uiTime)
//
//		// 2️⃣ Prepare date formatter
//		SimpleDateFormat sdf = new SimpleDateFormat(pattern)
//
//		// 3️⃣ Parse UI timestamp
//		Date uiDate = sdf.parse(uiTime)
//
//		// 4️⃣ Get current time
//		Date now = new Date()
//		String nowFormatted = sdf.format(now)
//		Date currentDate = sdf.parse(nowFormatted)
//
//		// 5️⃣ Compare difference
//		long diffMs = Math.abs(uiDate.getTime() - currentDate.getTime())
//		long diffSec = diffMs / 1000
//
//		println("⏱ Difference: " + diffSec + " seconds")
//
//		// 6️⃣ Validate
//		if (diffSec <= allowedSeconds) {
//			println("✅ Timestamp is valid (within " + allowedSeconds + " seconds)")
//			return true
//		} else {
//			println("❌ Timestamp mismatch! Difference: " + diffSec + " seconds")
//			WebUI.verifyEqual(false, true)   // force fail
//		}
//	}
//}

public class VerifyTimestamp {
	
		/**
		 * Verify UI timestamp with system time using tolerance
		 */
		@Keyword
		def verifyTimestamp(TestObject to, String pattern, int allowedSeconds) {
	
			// 1️⃣ Read UI timestamp
			String uiTime = WebUI.getText(to).trim()
			println("📌 UI Timestamp: " + uiTime)
	
			// 2️⃣ Formatter WITH timezone (match your app)
			SimpleDateFormat sdf = new SimpleDateFormat(pattern)
			sdf.setTimeZone(TimeZone.getTimeZone("GMT")) // 🔥 critical fix
	
			// 3️⃣ Parse UI time
			Date uiDate = sdf.parse(uiTime)
	
			// 4️⃣ Current system time in SAME timezone
			Date now = new Date()
	
			long diffMs = Math.abs(uiDate.getTime() - now.getTime())
			long diffSec = diffMs / 1000
	
			println("🕒 System Time (GMT): " + sdf.format(now))
			println("⏱ Difference: " + diffSec + " seconds")
	
			// 5️⃣ Validation
			if (diffSec <= allowedSeconds) {
				println("✅ Timestamp is valid (within ${allowedSeconds} seconds)")
				return true
			}
	
			println("❌ Timestamp mismatch! Allowed=${allowedSeconds}s, Actual=${diffSec}s")
			return false
		}
	}
