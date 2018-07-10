    public String[] getAudioDecoders() throws EncoderException {

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

                        String decoderFlag = matcher.group(1);

                        String audioVideoFlag = matcher.group(3);

                        if ("D".equals(decoderFlag) && "A".equals(audioVideoFlag)) {

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

	 * Returns a list with the names of all the audio encoders bundled with the

	 * ffmpeg distribution in use. An audio stream can be encoded using one of

	 * these encoders.

	 * 

	 * @return A list with the names of all the included audio encoders.

	 * @throws EncoderException

	 *             If a problem occurs calling the underlying ffmpeg executable.

	 */

    public String[] getAudioEncoders() throws EncoderException {

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
