package custom

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.driver.DriverFactory

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

/**
 * Custom keyword library for the DevExpress ASPxScheduler used on the schedule page.
 *
 * Usage from a Test Case:
 *   CustomKeywords.'custom.SchedulerActions.rescheduleAppointment'('09:30', '11:00')
 *
 * Times are 24-hour "HH:mm" strings, on 5-minute boundaries, within the
 * scheduler's visible hours (this page renders 08:00 - 16:55).
 */
class SchedulerActions {

    // Prefix used by DevExpress for the per-5-minute time ruler cells.
    private static final String CELL_ID_PREFIX = "scheduler_containerBlock_DXCntv0_"

    // The scheduler starts its grid at 8 AM (see dxo.AddVerticalContainer(0,108,new Date(...,8)...))
    private static final int START_HOUR = 8

    /**
     * Reschedules whichever appointment currently sits at fromTime to toTime,
     * by simulating a native mouse drag (DevExpress uses mousedown/mousemove/
     * mouseup, not HTML5 drag events, so Selenium Actions works here).
     *
     * @param fromTime "HH:mm" - the appointment's current start time
     * @param toTime   "HH:mm" - the desired new start time
     */
    @Keyword
    def rescheduleAppointment(String fromTime, String toTime) {
        WebDriver driver = DriverFactory.getWebDriver()
        String toCellId = CELL_ID_PREFIX + timeToIndex(toTime)
        WebElement toCell = driver.findElement(By.id(toCellId))
        WebElement apptElement = findAppointmentByTime(driver, fromTime)

        dragElementToTarget(driver, apptElement, toCell)
    }

    /**
     * ALTERNATIVE (RECOMMENDED) APPROACH: instead of drag-and-drop, use the
     * appointment's own right-click "Reschedule Appointment" menu item.
     * Looking at the page's embedded script, that menu item is wired to a
     * real function - ReschedulePtAppointmentMain(...) - which opens a
     * proper form dialog (date/time fields you set directly), not a drag
     * interaction. That's a far more reliable thing to automate than
     * pixel-perfect dragging on a legacy jQuery scheduler widget.
     *
     * This method gets you as far as: find the appointment -> right-click
     * it -> click "Reschedule Appointment" -> the dialog is now open.
     * I don't yet know the dialog's field IDs (they're not in the markup
     * you shared, since the dialog is injected after this menu click) -
     * share that dialog's HTML once it's open and I'll fill in
     * setNewDateAndTime() below to complete the flow.
     */
    @Keyword
    def openRescheduleDialog(String fromTime) {
        WebDriver driver = DriverFactory.getWebDriver()
        WebElement apptElement = findAppointmentByTime(driver, fromTime)

        // Native right-click - a single discrete event, not a multi-step
        // drag, so it doesn't suffer from the coordinate/auto-scroll issues
        // we hit with dragging.
        new Actions(driver).contextClick(apptElement).perform()

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10))
        WebElement rescheduleMenuItem = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("scheduler_aptMenuBlock_SMAPT_DXI2_"))
        )
        rescheduleMenuItem.click()

        // At this point the reschedule dialog should be open in the DOM.
        // TODO: once you share its HTML, I'll add a setNewDateAndTime(...)
        // method here that fills the new date/time and confirms.
    }

    /**
     * Finds the appointment DIV whose vertical span covers the given time,
     * by matching against the corresponding DXCntv0_N time-ruler cell.
     * Pulled out as a shared helper so both the drag-based and menu-based
     * approaches can locate the source appointment the same way.
     */
    private static WebElement findAppointmentByTime(WebDriver driver, String time) {
        JavascriptExecutor js = (JavascriptExecutor) driver
        String cellId = CELL_ID_PREFIX + timeToIndex(time)
        WebElement cell = driver.findElement(By.id(cellId))
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", cell)

        WebElement apptElement = (WebElement) js.executeScript('''
            var targetCell = document.getElementById(arguments[0]);
            var targetRect = targetCell.getBoundingClientRect();
            var targetMidY = targetRect.top + (targetRect.height / 2);
            var apts = document.querySelectorAll('[id^="scheduler_aptsBlock_AptDiv"]');
            for (var i = 0; i < apts.length; i++) {
                var rect = apts[i].getBoundingClientRect();
                if (rect.height === 0) continue;
                if (targetMidY >= rect.top && targetMidY <= rect.bottom) {
                    return apts[i];
                }
            }
            return null;
        ''', cellId)

        if (apptElement == null) {
            throw new Exception("No appointment found starting at ${time}. " +
                    "Check the time is on a booked slot and within scheduler hours.")
        }
        return apptElement
    }

    /**
     * Drags source to target by dispatching real mousedown/mousemove/mouseup
     * DOM events directly via JavaScript, instead of using Selenium's
     * Actions API.
     *
     * WHY: Selenium's Actions class drives input through WebDriver's own
     * pipeline, which does its own viewport-bounds validation and can
     * auto-scroll the page mid-sequence to bring an element into view.
     * When that auto-scroll fires partway through a multi-step drag, every
     * coordinate computed before it becomes stale - which is what caused
     * both the MoveTargetOutOfBoundsException earlier and the "just
     * dragging up and down" behavior after that (the driver's own
     * corrective scrolling fighting our programmatic moves).
     *
     * DevExpress's scheduler, like most jQuery-era widgets, just listens
     * for plain mousedown/mousemove/mouseup DOM events - it doesn't care
     * whether they came from real hardware or were dispatched via JS. So
     * we dispatch them ourselves with clientX/clientY coordinates we
     * compute and control completely, which has no viewport-bounds
     * restriction and never triggers WebDriver-level auto-scroll.
     */
    private static void dragElementToTarget(WebDriver driver, WebElement source, WebElement target) {
        JavascriptExecutor js = (JavascriptExecutor) driver
        driver.manage().timeouts().scriptTimeout(java.time.Duration.ofSeconds(15))

        js.executeAsyncScript('''
            var sourceEl = arguments[0];
            var targetEl = arguments[1];
            var callback = arguments[arguments.length - 1];

            function fireMouseEvent(el, type, x, y) {
                var evt = new MouseEvent(type, {
                    view: window,
                    bubbles: true,
                    cancelable: true,
                    clientX: x,
                    clientY: y,
                    button: 0
                });
                el.dispatchEvent(evt);
            }

            var srcRect = sourceEl.getBoundingClientRect();
            var tgtRect = targetEl.getBoundingClientRect();

            var startX = srcRect.left + srcRect.width / 2;
            var startY = srcRect.top + srcRect.height / 2;
            var endX = tgtRect.left + (tgtRect.width / 2);
            var endY = tgtRect.top + (tgtRect.height / 2);

            var steps = 15;
            var stepDelayMs = 40;

            // mousedown ON the appointment itself, exactly like a real user.
            fireMouseEvent(sourceEl, 'mousedown', startX, startY);

            var i = 0;
            function stepMove() {
                if (i <= steps) {
                    var x = startX + (endX - startX) * i / steps;
                    var y = startY + (endY - startY) * i / steps;
                    // mousemove is dispatched on document, matching how these
                    // drag handlers typically bind (mousedown on the element,
                    // mousemove/mouseup on document, so dragging still tracks
                    // even once the cursor leaves the original element).
                    fireMouseEvent(document, 'mousemove', x, y);
                    i++;
                    setTimeout(stepMove, stepDelayMs);
                } else {
                    fireMouseEvent(document, 'mouseup', endX, endY);
                    callback(true);
                }
            }
            setTimeout(stepMove, stepDelayMs);
        ''', source, target)
    }

    /**
     * Converts a "HH:mm" 24-hour time string into the DevExpress row index
     * used in element ids like scheduler_containerBlock_DXCntv0_18 (== 09:30).
     * index = (hour - START_HOUR) * 12 + (minute / 5)
     */
    private static int timeToIndex(String time) {
        def parts = time.trim().split(':')
        if (parts.length != 2) {
            throw new IllegalArgumentException("Time must be in HH:mm format, got: ${time}")
        }
        int hour = parts[0].toInteger()
        int minute = parts[1].toInteger()

        if (minute % 5 != 0) {
            throw new IllegalArgumentException("Time must fall on a 5-minute boundary, got: ${time}")
        }
        int index = (hour - START_HOUR) * 12 + (minute / 5)
        if (index < 0) {
            throw new IllegalArgumentException("Time ${time} is before the scheduler's visible start (${START_HOUR}:00).")
        }
        return index
    }
}