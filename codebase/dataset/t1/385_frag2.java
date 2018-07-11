    private void setTemplatePathVariable(String valSetting) {

        int bracePos = valSetting.indexOf(']');

        if (bracePos < 1) return;

        String valName = valSetting.substring(1, bracePos);

        int equalsPos = valSetting.indexOf('=', bracePos);

        if (equalsPos == -1) return;

        String setting = valSetting.substring(equalsPos + 1).trim();

        PathVariable pathVar = getPathVariable(valName);

        pathVar.dataName = null;

        pathVar.value = setting;

    }
