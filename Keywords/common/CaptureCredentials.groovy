package common

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.ObjectRepository
import internal.GlobalVariable

class CaptureCredentials {

    @Keyword
    def captureAndStoreCredentials(String objectRepoPath) {

        /* =========================
           GET TEST OBJECT SAFELY
           ========================= */
        TestObject obj = ObjectRepository.findTestObject(objectRepoPath)

        assert obj != null :
                "❌ Test Object is NULL. Check path: " + objectRepoPath

        /* =========================
           WAIT FOR VISIBILITY
           ========================= */
        boolean isVisible = WebUI.waitForElementVisible(obj, 20)
        assert isVisible :
                "❌ Element not visible for Test Object: " + objectRepoPath

        /* =========================
           GET FULL TEXT
           ========================= */
        String fullText = WebUI.getText(obj)
        assert fullText?.trim() :
                "❌ Retrieved text is EMPTY from: " + objectRepoPath

        WebUI.comment("📄 Full captured text: " + fullText)

        /* =========================
           EXTRACT VALUES
           ========================= */

        // URL
        def urlMatcher = (fullText =~ /(https?:\/\/\S+)/)
        assert urlMatcher.find() : "❌ URL not found in text"
        String url = urlMatcher.group(1)

        // Username
        def userMatcher = (fullText =~ /Username:\s*(\S+)/)
        assert userMatcher.find() : "❌ Username not found in text"
        String username = userMatcher.group(1)

        // Password
        def passMatcher = (fullText =~ /Password:\s*(\S+)/)
        assert passMatcher.find() : "❌ Password not found in text"
        String password = passMatcher.group(1)

        /* =========================
           STORE VALUES
           ========================= */
        GlobalVariable.PortalURL     = url
        GlobalVariable.RestUserName  = username
        GlobalVariable.RestPassword  = password

        /* =========================
           LOGGING
           ========================= */
        WebUI.comment("✅ Portal URL captured: " + url)
        WebUI.comment("✅ Username captured: " + username)
        WebUI.comment("✅ Password captured successfully")
    }
}
