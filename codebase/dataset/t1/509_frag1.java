        Element e = doc.getElementById(targetId);

        if (e == null) {

            DefaultTestReport report = new DefaultTestReport(this);

            report.setErrorCode(ERROR_GET_ELEMENT_BY_ID_FAILED);

            report.addDescriptionEntry(ENTRY_KEY_ID, targetId);

            report.setPassed(false);

            return report;

        }

        Element fc = null;
