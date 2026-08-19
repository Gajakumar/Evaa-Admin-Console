package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions


class SchedulerKeywords {

    // =========================================================
    // Scheduler Configuration
    // =========================================================

    static final int START_HOUR = 8
    static final int MIN_PER_ROW = 5

    static final String CELL_ID_PREFIX =
            'scheduler_containerBlock_DXCntv0_'


    // =========================================================
    // Convert Time to Scheduler Row Index
    // =========================================================

    static int timeToRowIndex(String timeStr) {

        if (timeStr == null || timeStr.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    'Time cannot be empty.'
            )
        }

        Date time

        try {

            if (timeStr.toUpperCase().contains('AM') ||
                timeStr.toUpperCase().contains('PM')) {

                time = new java.text.SimpleDateFormat(
                        'hh:mm a'
                ).parse(timeStr.toUpperCase())

            } else {

                time = new java.text.SimpleDateFormat(
                        'HH:mm'
                ).parse(timeStr)
            }

        } catch (Exception e) {

            throw new IllegalArgumentException(
                    "Invalid time format: ${timeStr}. " +
                    "Use HH:mm or hh:mm AM/PM."
            )
        }

        Calendar calendar =
                Calendar.getInstance()

        calendar.setTime(time)

        int hour =
                calendar.get(Calendar.HOUR_OF_DAY)

        int minute =
                calendar.get(Calendar.MINUTE)

        int totalMinutes =
                ((hour - START_HOUR) * 60) + minute

        if (totalMinutes < 0) {

            throw new IllegalArgumentException(
                    "Time ${timeStr} is before scheduler start time ${START_HOUR}:00."
            )
        }

        if (totalMinutes % MIN_PER_ROW != 0) {

            throw new IllegalArgumentException(
                    "Time ${timeStr} is not aligned to the 5-minute scheduler grid."
            )
        }

        return totalMinutes / MIN_PER_ROW
    }


    // =========================================================
    // Get Scheduler Cell
    // =========================================================

    static WebElement getSchedulerCell(String timeStr) {

        WebDriver driver =
                DriverFactory.getWebDriver()

        JavascriptExecutor js =
                (JavascriptExecutor) driver

        int rowIndex =
                timeToRowIndex(timeStr)

        String cellId =
                CELL_ID_PREFIX + rowIndex

        println "Scheduler Cell: ${cellId}"

        WebElement cell =
                driver.findElement(
                        By.id(cellId)
                )

        js.executeScript(
                '''
                arguments[0].scrollIntoView({
                    block: 'center',
                    inline: 'nearest'
                });
                ''',
                cell
        )

        WebUI.delay(1)

        return cell
    }


    // =========================================================
    // Get Element at Scheduler Time
    // =========================================================

    static WebElement getElementAtTime(String timeStr) {

        WebDriver driver =
                DriverFactory.getWebDriver()

        JavascriptExecutor js =
                (JavascriptExecutor) driver

        WebElement cell =
                getSchedulerCell(timeStr)

        Map coordinates =
                (Map) js.executeScript(
                        '''
                        var rect =
                            arguments[0].getBoundingClientRect();

                        return {
                            x: rect.left + rect.width / 2,
                            y: rect.top + rect.height / 2
                        };
                        ''',
                        cell
                )

        double x =
                coordinates.x as double

        double y =
                coordinates.y as double

        println "Click position for ${timeStr}: X=${x}, Y=${y}"

        WebElement element =
                (WebElement) js.executeScript(
                        '''
                        return document.elementFromPoint(
                            arguments[0],
                            arguments[1]
                        );
                        ''',
                        x,
                        y
                )

        if (element == null) {

            throw new IllegalStateException(
                    "Unable to find element at ${timeStr}."
            )
        }

        return element
    }


    // =========================================================
    // Right Click Appointment
    // =========================================================

    @Keyword
    static void rightClickAppointmentAtTime(
            String apptTime
    ) {

        WebDriver driver =
                DriverFactory.getWebDriver()

        WebElement element =
                getElementAtTime(apptTime)

        println "Right-clicking appointment at ${apptTime}"

        new Actions(driver)
                .moveToElement(element)
                .pause(300)
                .contextClick()
                .perform()

        WebUI.delay(1)
    }


    // =========================================================
    // Click Reschedule Appointment
    // =========================================================

    private static void clickRescheduleAppointment() {

        TestObject rescheduleOption =
                new TestObject('rescheduleOption')

        /*
         * Based on the actual DevExpress HTML:
         *
         * <li class="dxm-item ...">
         *     <div class="dxm-content ...">
         *         <span class="dx-vam">
         *             Reschedule Appointment
         *         </span>
         *     </div>
         * </li>
         *
         * Avoid using DXI2 because the menu index can change.
         */

        rescheduleOption.addProperty(
                'xpath',
                ConditionType.EQUALS,
                "//li[contains(@class,'dxm-item')]" +
                "[.//span[normalize-space()='Reschedule Appointment']]" +
                "//div[contains(@class,'dxm-content')]"
        )

        WebUI.waitForElementVisible(
                rescheduleOption,
                10
        )

        WebUI.waitForElementClickable(
                rescheduleOption,
                10
        )

        println 'Clicking Reschedule Appointment'

        WebUI.click(rescheduleOption)

        WebUI.delay(1)
    }


    // =========================================================
    // Click OFFICE
    // =========================================================

    private static void clickOfficeButton() {

        TestObject officeButton =
                new TestObject('officeButton')

        officeButton.addProperty(
                'id',
                ConditionType.EQUALS,
                'btnOfficeRescPtApptFrmPopup'
        )

        WebUI.waitForElementVisible(
                officeButton,
                10
        )

        WebUI.waitForElementClickable(
                officeButton,
                10
        )

        println 'Clicking OFFICE'

        WebUI.click(officeButton)

        WebUI.delay(1)
    }


    // =========================================================
    // Double Click Destination Slot
    // =========================================================

    @Keyword
    static void doubleClickSlotAtTime(
            String targetTime
    ) {

        WebDriver driver =
                DriverFactory.getWebDriver()

        /*
         * Get the actual scheduler cell instead of relying
         * on whatever child element happens to be at the
         * center of the cell.
         */

        WebElement cell =
                getSchedulerCell(targetTime)

        println "Double-clicking destination slot: ${targetTime}"

        new Actions(driver)
                .moveToElement(cell)
                .pause(300)
                .doubleClick()
                .perform()

        WebUI.delay(1)
    }


    // =========================================================
    // Click Save
    // =========================================================

    private static void clickSaveButton() {

        TestObject saveButton =
                new TestObject('saveButton')

        saveButton.addProperty(
                'xpath',
                ConditionType.EQUALS,
                "//span[normalize-space()='Save Appointment']"
        )

        WebUI.waitForElementVisible(
                saveButton,
                10
        )

        WebUI.waitForElementClickable(
                saveButton,
                10
        )

        println 'Clicking Save'

        WebUI.click(saveButton)

        WebUI.delay(1)
    }


    // =========================================================
    // COMPLETE RESCHEDULE FLOW
    // =========================================================

    @Keyword
    static void rescheduleAppointment(
            String apptTime,
            String targetTime
    ) {

        println ''
        println '================================================'
        println '       APPOINTMENT RESCHEDULE FLOW'
        println '================================================'
        println "Current Appointment : ${apptTime}"
        println "New Appointment     : ${targetTime}"
        println '================================================'


        // -----------------------------------------------------
        // Step 1: Right-click current appointment
        // -----------------------------------------------------

        rightClickAppointmentAtTime(
                apptTime
        )


        // -----------------------------------------------------
        // Step 2: Select Reschedule Appointment
        // -----------------------------------------------------

        clickRescheduleAppointment()


        // -----------------------------------------------------
        // Step 3: Select OFFICE
        // -----------------------------------------------------

        clickOfficeButton()


        // -----------------------------------------------------
        // Step 4: Double-click destination slot
        // -----------------------------------------------------

        doubleClickSlotAtTime(
                targetTime
        )


        // -----------------------------------------------------
        // Step 5: Save appointment
        // -----------------------------------------------------

        clickSaveButton()


        println ''
        println '================================================'
        println '       RESCHEDULE FLOW COMPLETED'
        println '================================================'
    }
}