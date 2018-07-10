        if (file.isDirectory()) {

            File[] files = file.listFiles();

            for (int i = 0; i < files.length; ++i) {

                append(builder, files[i], counter);

            }

        } else {

            try {

                counter[1] += builder.addFileRef(file);

                ++counter[0];

                System.out.print('.');

            } catch (DcmParseException e) {

                System.out.println(MessageFormat.format(messages.getString("insertFailed"), new Object[] { file }));

                e.printStackTrace(System.out);

            } catch (IllegalArgumentException e) {

                System.out.println(MessageFormat.format(messages.getString("insertFailed"), new Object[] { file }));

                e.printStackTrace(System.out);

            }

        }

    }
