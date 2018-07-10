        if (result.exists()) {

            redirectTo(filename, result);

            return;

        }

        savePathInfo();

        File template = computePath(file, true);

        if (!metaPathVariables.isEmpty()) {

            pathVariables = metaPathVariables;

            pathVariableNames = metaPathVariableNames;

            displayNeedInfoForm(filename, null, true, MISSING_META, file);

            return;

        }

        if (!foundTemplate) {

            restorePathInfo();

            displayNeedInfoForm(filename, result, false, CANNOT_LOCATE, file);

            return;

        }

        String templateURL = null;

        if (isTemplateURL(template)) try {

            templateURL = template.toURL().toString();

            templateURL = templateURL.substring(templateURL.indexOf(TEMPLATE_ROOT_UNIX) + TEMPLATE_ROOT_UNIX.length() - 1);

        } catch (MalformedURLException mue) {

        }

        if (template == null || (templateURL == null && !template.exists())) {

            displayNeedInfoForm(filename, template, true, MISSING_INFO, file);

            return;

        }

        File resultDir = result.getParentFile();

        if (!resultDir.exists()) if (!resultDir.mkdirs()) {
