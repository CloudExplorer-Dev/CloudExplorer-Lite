/*******************************************************************************
 * Copyright (c) 2015 VMware, Inc.  All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 ******************************************************************************/
package com.fit2cloud.provider.impl.vsphere.util;

import com.vmware.vapi.std.LocalizableMessage;
import com.vmware.vcenter.ovf.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Helper class to deal with result of OVF operations.
 */
@Slf4j
public class OvfUtil {

    private OvfUtil() {
    }

    private static final String HEADING_ADDITIONAL_INFO = "Additional information :";

    /**
     * Displays the result of OVF operations.
     *
     * @param operationSucceeded status of the operation.
     * @param operationResult    {@link LibraryItemTypes.ResultInfo}.
     * @param messageOnSuccess   message to display when operation succeeds.
     * @param messageOnFailure   message to display when operation fails.
     */
    public static void displayOperationResult(boolean operationSucceeded,
                                              LibraryItemTypes.ResultInfo operationResult, String messageOnSuccess, String messageOnFailure) {
        boolean displayHeader = true;
        LibraryItemTypes.ResultInfo info = operationResult;

        if (operationSucceeded) {
            log.info(messageOnSuccess);
        } else {
            log.info(messageOnFailure);
            // print only failure information here
            if (info != null) {
                List<OvfError> errors = info.getErrors();
                if (!errors.isEmpty() /* to decide if header needs to be printed */) {
                    if (displayHeader) {
                        log.info(HEADING_ADDITIONAL_INFO);
                    }
                    displayHeader = false;

                    for (OvfError error : errors) {
                        printOvfMessage(error._convertTo(OvfMessage.class));
                    }
                }
            }
        }

        // display information in both the success and failure cases
        if (info != null) {
            List<OvfWarning> warnings = info.getWarnings();
            List<OvfInfo> additionalInfo = info.getInformation();

            // little bit of pretty print
            if (!warnings.isEmpty() || !additionalInfo.isEmpty()) {
                if (displayHeader) {
                    log.info(HEADING_ADDITIONAL_INFO);
                }
                displayHeader = false; // for completeness
            }
            // display warnings
            for (OvfWarning warning : warnings) {
                printOvfMessage(warning._convertTo(OvfMessage.class));
            }
            // display addition info
            for (OvfInfo information : additionalInfo) {
                List<LocalizableMessage> messages =
                        information.getMessages();
                for (LocalizableMessage message : messages) {
                    log.info("Information: " + message.getDefaultMessage());
                }
            }
        }
    }

    private static void printOvfMessage(OvfMessage ovfMessage) {
        if (ovfMessage.getCategory().equals(OvfMessage.Category.SERVER)) {
            List<LocalizableMessage> messages =
                    ovfMessage.getError()._convertTo(com.vmware.vapi.std.errors.Error.class).getMessages();
            for (LocalizableMessage message : messages) {
                log.info("Server error message: " + message);
            }
        } else if (ovfMessage.getCategory().equals(
                OvfMessage.Category.VALIDATION)) {
            for (ParseIssue issue : ovfMessage.getIssues()) {
                log.info("Issue message: " + issue.getMessage());
            }
        } else if (ovfMessage.getCategory().equals(OvfMessage.Category.INPUT)) {
            log.info("Input validation message: " + ovfMessage.getMessage());
        }
    }
}
