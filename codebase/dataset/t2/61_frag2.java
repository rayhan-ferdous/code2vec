        public void complementMissingArguments() {

            if (mapping == null) return;

            ArgumentDefinition[] arguList = mapping.getArgumentDefinitions();

            Map<String, List<Object>> options = args.getOptions();

            for (int a = 0; a < arguList.length; a++) {

                ArgumentDefinition argDef = arguList[a];

                if (!argDef.isRequiredArgument() && argDef.getDefaultValue() != null) {

                    if (!options.containsKey(argDef.getName())) {

                        List<Object> defaultOption = new ArrayList<Object>(1);

                        defaultOption.add(argDef.getDefaultValue());

                        options.put(argDef.getName(), defaultOption);

                    }

                }

            }

        }
