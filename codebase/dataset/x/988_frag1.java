        public String convert(String source, SourceDetails details) throws Exception {

            List<String> lines = stripLines(source);

            StringWriter writer = new StringWriter();

            String indent = "";

            for (String line : lines) {

                line = line.replaceAll("\\s*$", "");

                writer.append(indent);

                if (indent == null || indent.equals("")) {

                    for (String packageName : NAMESPACE.keySet()) {

                        if (line.matches(packageName + ".*")) {

                            indent = INDENT;

                            line = NAMESPACE.get(packageName);

                            break;

                        }

                    }

                }

                writer.append(line);

                writer.append("\n");

            }

            writer.append("}");

            return writer.toString();

        }
