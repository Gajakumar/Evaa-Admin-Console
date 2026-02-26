package common

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.regex.*

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

class ToastTimeVerifier {

	@Keyword
	def verifyGmtTimeToast(TestObject toastObj, int toleranceMinutes = 5) {

		WebUI.waitForElementVisible(toastObj, 10)

		// 1️⃣ Normalize toast text
		String rawToast = WebUI.getText(toastObj)
		String toastText = rawToast.replaceAll("\\s+", " ").trim()

		println "================ TOAST TEXT ================"
		println toastText
		println "==========================================="

		// 2️⃣ Match BOTH formats: 13:59 OR 12:59 PM
		Pattern timePattern = Pattern.compile(
				"(\\d{1,2}:\\d{2})(?:\\s?(AM|PM))?",
				Pattern.CASE_INSENSITIVE
				)

		Matcher matcher = timePattern.matcher(toastText)

		assert matcher.find() :
		"❌ Time not found in toast. Actual text:\n${toastText}"

		String timePart = matcher.group(1)
		String amPmPart = matcher.group(2)

		println "🕒 Extracted Time: $timePart ${amPmPart ?: '(24h format)'}"

		// 3️⃣ Parse time
		LocalTime toastTime
		if (amPmPart) {
			toastTime = LocalTime.parse(
					"$timePart $amPmPart",
					DateTimeFormatter.ofPattern("hh:mm a")
					)
		} else {
			toastTime = LocalTime.parse(
					timePart,
					DateTimeFormatter.ofPattern("HH:mm")
					)
		}

		ZonedDateTime toastGmt = ZonedDateTime.of(
				LocalDate.now(ZoneId.of("GMT")),
				toastTime,
				ZoneId.of("GMT")
				)

		ZonedDateTime nowGmt = ZonedDateTime.now(ZoneId.of("GMT"))

		long diffMinutes = Math.abs(
				ChronoUnit.MINUTES.between(toastGmt, nowGmt)
				)

		println "⏱ GMT Time Difference: ${diffMinutes} minutes"

		// 4️⃣ Assertions
		assert toastText.contains("Your account was locked at")
		assert toastText.contains("15 minutes from the lock time")

		assert diffMinutes <= toleranceMinutes :
		"❌ GMT time mismatch. Difference = ${diffMinutes} minutes"
	}
}
