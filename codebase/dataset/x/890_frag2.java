    public AgiRequestImpl(final Map<String, String> request) {

        this.request = request;

        script = request.get("network_script");

        if (script != null) {

            Matcher scriptMatcher = SCRIPT_PATTERN.matcher(script);

            if (scriptMatcher.matches()) {

                script = scriptMatcher.group(1);

                parameters = scriptMatcher.group(2);

            }

        }

    }
