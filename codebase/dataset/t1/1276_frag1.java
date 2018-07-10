        public void write(final int theByte) throws java.io.IOException {

            if (this.suspendEncoding) {

                super.out.write(theByte);

                return;

            }

            if (this.encode) {

                this.buffer[this.position++] = (byte) theByte;

                if (this.position >= this.bufferLength) {

                    this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));

                    this.lineLength += 4;

                    if (this.breakLines && (this.lineLength >= Base64.MAX_LINE_LENGTH)) {

                        this.out.write(Base64.NEW_LINE);

                        this.lineLength = 0;

                    }

                    this.position = 0;

                }

            } else {

                if (this.decodabet[theByte & 0x7f] > Base64.WHITE_SPACE_ENC) {

                    this.buffer[this.position++] = (byte) theByte;

                    if (this.position >= this.bufferLength) {

                        final int len = Base64.decode4to3(this.buffer, 0, this.b4, 0, this.options);

                        this.out.write(this.b4, 0, len);

                        this.position = 0;

                    }

                } else if (this.decodabet[theByte & 0x7f] != Base64.WHITE_SPACE_ENC) {

                    throw new java.io.IOException("Invalid character in Base64 data.");

                }

            }

        }
