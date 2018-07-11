            try {

                in = new BufferedInputStream(new FileInputStream(file));

                parser = pFact.newDcmParser(in);

                try {

                    FileFormat format = parser.detectFileFormat();

                    ds = oFact.newDataset();

                    parser.setDcmHandler(ds.getDcmHandler());

                    parser.parseDcmFile(format, Tags.PixelData);

                    if (parser.getReadTag() == Tags.PixelData) {

                        if (parser.getStreamPosition() + parser.getReadLength() > file.length()) {

                            throw new EOFException("Pixel Data Length: " + parser.getReadLength() + " exceeds file length: " + file.length());

                        }

                    }

                    log.info(MessageFormat.format(messages.getString("readDone"), new Object[] { file }));

                } catch (DcmParseException e) {

                    log.error(MessageFormat.format(messages.getString("failformat"), new Object[] { file }));

                    return false;

                }

            } catch (IOException e) {

                log.error(MessageFormat.format(messages.getString("failread"), new Object[] { file, e }));

                return false;

            }

            sendDataset(active, file, parser, ds);
