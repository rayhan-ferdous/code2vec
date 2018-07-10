            file.readFully(inBuffer);

            readBits = (inBuffer.length * 8) - startBitOffset - (8 - endBitOffset) + startBitOffset;

            byteOffset = 0;

            readBitOffset = startBitOffset;

            bitOffset = startBitOffset;

        } catch (IOException ioe) {

            logger.error("Input/Output exception while reading from a random access file. Stack trace follows", ioe);
