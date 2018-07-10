    public int sendTextResponse(String text) {

        synchronized (sendBuffer) {

            int sentBytes = 0;

            byte[] bytes = text.trim().getBytes(Charset.forName("UTF-8"));

            int sendBytesCount = bytes.length;

            try {

                while (sentBytes < sendBytesCount) {

                    int len = Math.min(Consts.XmlClientBufferSize - 1, bytes.length - sentBytes);

                    sendBuffer.clear();

                    sendBuffer.put(bytes, sentBytes, len);

                    sentBytes += len;

                    if (sentBytes >= sendBytesCount) {

                        sendBuffer.put(Consts.NullByte);

                        len++;

                    }

                    sendBuffer.flip();

                    int n = 0;

                    while (n < len) {

                        n += socket.write(sendBuffer);

                    }

                }

                Log.write(Level.INFO, String.format(Messages.getString("XmlClientSentBytes"), sendBytesCount, remoteAddress), this);

                Log.write(Level.FINE, "XmlClient", "SendTextResponse", text.trim(), this);

            } catch (Exception ex) {

                Log.write(Level.SEVERE, "XmlClient", "SendTextResponse", ex.toString(), this);

            }

            return sendBytesCount;

        }

    }
