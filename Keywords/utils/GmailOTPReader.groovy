package utils

import java.util.Properties
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.mail.BodyPart
import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Message
import javax.mail.Multipart
import javax.mail.Session
import javax.mail.Store
import javax.mail.internet.MimeMultipart
import javax.mail.search.AndTerm
import javax.mail.search.FromStringTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SubjectTerm

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil


class GmailOTPReader {

    @Keyword
    String getVerificationCode(
            String email,
            String appPassword,
            int retryCount = 6,
            int delaySeconds = 10) {

        String host = "imap.gmail.com"

        /*
         * Record the time when this method starts.
         * Any OTP email received before this time will be ignored.
         */
        Date requestTime = new Date()

        KeywordUtil.logInfo(
                "OTP request time : " + requestTime
        )

        /*
         * Give Gmail some time to receive the new OTP email
         * before performing the first search.
         */
        int initialDelaySeconds = 5

        KeywordUtil.logInfo(
                "Waiting ${initialDelaySeconds} seconds for new OTP email..."
        )

        Thread.sleep(initialDelaySeconds * 1000)

        for (int attempt = 1; attempt <= retryCount; attempt++) {

            Store store = null
            Folder inbox = null

            try {

                KeywordUtil.logInfo(
                        "Searching Gmail for OTP - Attempt ${attempt}/${retryCount}"
                )

                // ---------------------------------------------------------
                // Gmail IMAP configuration
                // ---------------------------------------------------------

                Properties props = new Properties()

                props.put(
                        "mail.store.protocol",
                        "imaps"
                )

                props.put(
                        "mail.imaps.host",
                        host
                )

                props.put(
                        "mail.imaps.port",
                        "993"
                )

                props.put(
                        "mail.imaps.ssl.enable",
                        "true"
                )

                props.put(
                        "mail.imaps.ssl.trust",
                        host
                )


                // ---------------------------------------------------------
                // Connect to Gmail
                // ---------------------------------------------------------

                Session session = Session.getInstance(props)

                store = session.getStore("imaps")

                store.connect(
                        host,
                        email,
                        appPassword
                )


                // ---------------------------------------------------------
                // Open Inbox
                // ---------------------------------------------------------

                inbox = store.getFolder("INBOX")

                inbox.open(Folder.READ_WRITE)


                // ---------------------------------------------------------
                // Search MaximEyes OTP emails
                // ---------------------------------------------------------

                SearchTerm searchTerm = new AndTerm(

                        new SubjectTerm(
                                "MaximEyes 2FA Verification"
                        ),

                        new FromStringTerm(
                                "do-not-reply@maximeyes.com"
                        )
                )


                Message[] messages = inbox.search(searchTerm)


                KeywordUtil.logInfo(
                        "MaximEyes OTP emails found : " + messages.length
                )


                // ---------------------------------------------------------
                // Search from newest email to oldest
                // ---------------------------------------------------------

                for (int i = messages.length - 1; i >= 0; i--) {

                    Message message = messages[i]

                    Date receivedDate = message.getReceivedDate()


                    KeywordUtil.logInfo(
                            "Checking email received at : " + receivedDate
                    )


                    // -----------------------------------------------------
                    // Ignore emails received before current OTP request
                    // -----------------------------------------------------

                    if (receivedDate == null) {

                        KeywordUtil.logInfo(
                                "Skipping email because received date is null."
                        )

                        continue
                    }


                    if (receivedDate.before(requestTime)) {

                        KeywordUtil.logInfo(
                                "Skipping OLD OTP email. Received : "
                                + receivedDate
                                + " | Request : "
                                + requestTime
                        )

                        continue
                    }


                    // -----------------------------------------------------
                    // Read email body
                    // -----------------------------------------------------

                    String body = getTextFromMessage(message)


                    if (body == null || body.trim().isEmpty()) {

                        KeywordUtil.logInfo(
                                "Skipping email because email body is empty."
                        )

                        continue
                    }


                    // -----------------------------------------------------
                    // Extract 6 digit OTP
                    // -----------------------------------------------------

                    Pattern otpPattern = Pattern.compile(
                            "Verification\\s*Code\\s*:\\s*(\\d{6})",
                            Pattern.CASE_INSENSITIVE
                    )


                    Matcher matcher = otpPattern.matcher(body)


                    if (matcher.find()) {

                        String otp = matcher.group(1)


                        KeywordUtil.logInfo(
                                "New MaximEyes OTP found : " + otp
                        )


                        // -------------------------------------------------
                        // Mark email as read
                        // -------------------------------------------------

                        try {

                            message.setFlag(
                                    Flags.Flag.SEEN,
                                    true
                            )

                            KeywordUtil.logInfo(
                                    "OTP email marked as READ."
                            )

                        } catch (Exception flagException) {

                            KeywordUtil.logInfo(
                                    "Unable to mark OTP email as READ : "
                                    + flagException.getMessage()
                            )
                        }


                        return otp
                    }


                    KeywordUtil.logInfo(
                            "OTP pattern not found in this email."
                    )
                }


                // ---------------------------------------------------------
                // OTP not found in this attempt
                // ---------------------------------------------------------

                KeywordUtil.logInfo(
                        "Attempt ${attempt}/${retryCount} : "
                        + "New OTP email not found."
                )


            } catch (Exception e) {

                KeywordUtil.logInfo(
                        "Gmail OTP error on attempt "
                        + "${attempt}/${retryCount} : "
                        + e.getMessage()
                )


            } finally {

                // ---------------------------------------------------------
                // Close Inbox
                // ---------------------------------------------------------

                try {

                    if (inbox != null && inbox.isOpen()) {

                        inbox.close(false)
                    }

                } catch (Exception e) {

                    KeywordUtil.logInfo(
                            "Error closing inbox : "
                            + e.getMessage()
                    )
                }


                // ---------------------------------------------------------
                // Close Gmail connection
                // ---------------------------------------------------------

                try {

                    if (store != null && store.isConnected()) {

                        store.close()
                    }

                } catch (Exception e) {

                    KeywordUtil.logInfo(
                            "Error closing Gmail connection : "
                            + e.getMessage()
                    )
                }
            }


            // -------------------------------------------------------------
            // Wait before next attempt
            // Don't wait after final attempt
            // -------------------------------------------------------------

            if (attempt < retryCount) {

                KeywordUtil.logInfo(
                        "Waiting ${delaySeconds} seconds before next OTP search..."
                )

                Thread.sleep(
                        delaySeconds * 1000
                )
            }
        }


        // -------------------------------------------------------------
        // OTP not found
        // -------------------------------------------------------------

        KeywordUtil.markFailedAndStop(
                "Unable to find NEW MaximEyes 2FA email after "
                + retryCount
                + " attempts."
        )

        return null
    }


    // =====================================================================
    // GET TEXT FROM EMAIL
    // =====================================================================

    private String getTextFromMessage(Message message) throws Exception {

        if (message.isMimeType("text/plain")) {

            return message.getContent().toString()
        }


        if (message.isMimeType("text/html")) {

            return message.getContent()
                    .toString()
                    .replaceAll("<[^>]+>", " ")
                    .replace("&nbsp;", " ")
                    .replaceAll("\\s+", " ")
                    .trim()
        }


        if (message.isMimeType("multipart/*")) {

            return getTextFromMultipart(
                    (Multipart) message.getContent()
            )
        }


        return ""
    }


    // =====================================================================
    // GET TEXT FROM MULTIPART EMAIL
    // =====================================================================

    private String getTextFromMultipart(
            Multipart multipart) throws Exception {

        StringBuilder result = new StringBuilder()


        for (int i = 0; i < multipart.getCount(); i++) {

            BodyPart part = multipart.getBodyPart(i)


            // -------------------------------------------------------------
            // Plain text
            // -------------------------------------------------------------

            if (part.isMimeType("text/plain")) {

                result.append(
                        part.getContent().toString()
                )
            }


            // -------------------------------------------------------------
            // HTML
            // -------------------------------------------------------------

            else if (part.isMimeType("text/html")) {

                result.append(
                        part.getContent()
                                .toString()
                                .replaceAll("<[^>]+>", " ")
                                .replace("&nbsp;", " ")
                                .replaceAll("\\s+", " ")
                                .trim()
                )
            }


            // -------------------------------------------------------------
            // Nested multipart
            // -------------------------------------------------------------

            else if (part.getContent() instanceof MimeMultipart) {

                result.append(
                        getTextFromMultipart(
                                (Multipart) part.getContent()
                        )
                )
            }
        }


        return result.toString().trim()
    }
}