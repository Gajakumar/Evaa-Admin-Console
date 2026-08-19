package common

import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.annotation.Keyword
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

class SchedulerKeywords {

    @Keyword
    def doubleClickTimeSlot(String hr, String min) {
        WebDriver driver = DriverFactory.getWebDriver()
        String minuteText = (hr == "12" && min == "00") ? "PM" : min

        String xpath
        if (min == "00") {
            xpath = "//td[contains(@class,\"dxsc-tr-hourItem\")]/span[text()=\"${hr}\"]" +
                    "/parent::td/following-sibling::td[contains(@id,\"DXCntv0_\")]"
        } else {
            xpath = "//td[contains(@class,\"dxsc-tr-hourItem\")]/span[text()=\"${hr}\"]" +
                    "/parent::td/parent::tr/following-sibling::tr[td/span[text()=\"${minuteText}\"]][1]" +
                    "/td[contains(@id,\"DXCntv0_\")]"
        }

        WebElement slot = driver.findElement(By.xpath(xpath))
        new Actions(driver).moveToElement(slot).doubleClick().perform()
    }

    @Keyword
    def doubleClickTimeSlotFromString(String time) {
        // Matches formats like "11:30 AM", "11:30AM", "9:05 PM", "12:00 PM"
        def matcher = time.trim() =~ /(?i)(\d{1,2}):(\d{2})\s*(AM|PM)?/
        if (!matcher.find()) {
            throw new Exception("Invalid time format: ${time}. Expected e.g. '11:30 AM'")
        }
        String hr     = matcher.group(1)
        String min    = matcher.group(2)
        String amOrPm = matcher.group(3)?.toUpperCase()

        doubleClickTimeSlot(hr, min)
    }
}