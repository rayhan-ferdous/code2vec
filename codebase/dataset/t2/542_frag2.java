    public String[] getVideoEncoders() throws EncoderException {

        ArrayList res = new ArrayList();

        FFMPEGExecutor ffmpeg = locator.createExecutor();

        ffmpeg.addArgument("-formats");

        try {

            ffmpeg.execute();

            RBufferedReader reader = null;

            reader = new RBufferedReader(new InputStreamReader(ffmpeg.getInputStream()));

            String line;

            boolean evaluate = false;

            while ((line = reader.readLine()) != null) {

                if (line.trim().length() == 0) {

                    continue;

                }

                if (evaluate) {

                    Matcher matcher = ENCODER_DECODER_PATTERN.matcher(line);

                    if (matcher.matches()) {

                        String encoderFlag = matcher.group(2);

                        String audioVideoFlag = matcher.group(3);

                        if ("E".equals(encoderFlag) && "V".equals(audioVideoFlag)) {

                            String name = matcher.group(4);

                            res.add(name);

                        }

                    } else {

                        break;

                    }

                } else if (line.trim().equals("Codecs:")) {

                    evaluate = true;

                }

            }

        } catch (IOException e) {

            throw new EncoderException(e);

        } finally {

            ffmpeg.destroy();

        }

        int size = res.size();

        String[] ret = new String[size];

        for (int i = 0; i < size; i++) {

            ret[i] = (String) res.get(i);

        }

        return ret;

    }



    /**

	 * Returns a list with the names of all the file formats supported at

	 * encoding time by the underlying ffmpeg distribution. A multimedia file

	 * could be encoded and generated only if the specified format is in this

	 * list.

	 * 

	 * @return A list with the names of all the supported file formats at

	 *         encoding time.

	 * @throws EncoderException

	 *             If a problem occurs calling the underlying ffmpeg executable.

	 */

    public String[] getSupportedEncodingFormats() throws EncoderException {

        ArrayList res = new ArrayList();

        FFMPEGExecutor ffmpeg = locator.createExecutor();

        ffmpeg.addArgument("-formats");

        try {

            ffmpeg.execute();

            RBufferedReader reader = null;

            reader = new RBufferedReader(new InputStreamReader(ffmpeg.getInputStream()));

            String line;

            boolean evaluate = false;

            while ((line = reader.readLine()) != null) {
