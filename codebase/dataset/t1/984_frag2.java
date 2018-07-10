            dataInStream.read(header, 0, FIXED_HEADER_LENGTH);

            dataLength = (byteToInt(header[0]) + (byteToInt(header[1]) << 8) + (byteToInt(header[2]) << 16) + (byteToInt(header[3]) << 32));

            channelBits = header[4];

            fileNameLen = header[5];

            config.setUseCompression(header[6] == 1);

            config.setUseEncryption(header[7] == 1);

            if (fileNameLen == 0) {

                fileName = new byte[0];

            } else {

                fileName = new byte[fileNameLen];

                dataInStream.read(fileName, 0, fileNameLen);

            }

        } catch (OpenStegoException osEx) {
