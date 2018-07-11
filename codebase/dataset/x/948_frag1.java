        public void read_scalefactor(SoundStream stream, Frame header) {

            if (allocation != 0) scalefactor = scalefactors[stream.get_bits(6)];

        }



        /**

	   *

	   */

        public boolean read_sampledata(SoundStream stream) {

            if (allocation != 0) {

                sample = (float) (stream.get_bits(samplelength));

            }

            if (++samplenumber == 12) {

                samplenumber = 0;

                return true;

            }

            return false;

        }



        /**

	   *

	   */

        public boolean put_next_sample(int channels, SynthesisFilter filter1, SynthesisFilter filter2) {

            if ((allocation != 0) && (channels != OutputChannels.RIGHT_CHANNEL)) {

                float scaled_sample = (sample * factor + offset) * scalefactor;

                filter1.input_sample(scaled_sample, subbandnumber);

            }

            return true;

        }

    }



    ;



    /**

	 * Class for layer I subbands in joint stereo mode.

	 */

    static class SubbandLayer1IntensityStereo extends SubbandLayer1 {



        protected float channel2_scalefactor;



        /**

	   * Constructor

	   */

        public SubbandLayer1IntensityStereo(int subbandnumber) {

            super(subbandnumber);

        }



        /**

	   *

	   */

        public void read_allocation(SoundStream stream, Frame header, Crc16 crc) throws DecoderException {

            super.read_allocation(stream, header, crc);

        }



        /**

	   *

	   */

        public void read_scalefactor(SoundStream stream, Frame header) {

            if (allocation != 0) {

                scalefactor = scalefactors[stream.get_bits(6)];

                channel2_scalefactor = scalefactors[stream.get_bits(6)];

            }

        }



        /**

	   *

	   */

        public boolean read_sampledata(SoundStream stream) {

            return super.read_sampledata(stream);

        }



        /**

	   *

	   */

        public boolean put_next_sample(int channels, SynthesisFilter filter1, SynthesisFilter filter2) {

            if (allocation != 0) {

                sample = sample * factor + offset;

                if (channels == OutputChannels.BOTH_CHANNELS) {

                    float sample1 = sample * scalefactor, sample2 = sample * channel2_scalefactor;

                    filter1.input_sample(sample1, subbandnumber);

                    filter2.input_sample(sample2, subbandnumber);

                } else if (channels == OutputChannels.LEFT_CHANNEL) {

                    float sample1 = sample * scalefactor;

                    filter1.input_sample(sample1, subbandnumber);

                } else {

                    float sample2 = sample * channel2_scalefactor;

                    filter1.input_sample(sample2, subbandnumber);

                }

            }

            return true;

        }

    }



    ;



    /**

	 * Class for layer I subbands in stereo mode.

	 */

    static class SubbandLayer1Stereo extends SubbandLayer1 {



        protected int channel2_allocation;



        protected float channel2_scalefactor;



        protected int channel2_samplelength;



        protected float channel2_sample;



        protected float channel2_factor, channel2_offset;



        /**

	   * Constructor

	   */

        public SubbandLayer1Stereo(int subbandnumber) {

            super(subbandnumber);

        }



        /**

	   *

	   */

        public void read_allocation(SoundStream stream, Frame header, Crc16 crc) throws DecoderException {

            allocation = stream.get_bits(4);

            channel2_allocation = stream.get_bits(4);

            if (crc != null) {

                crc.add_bits(allocation, 4);

                crc.add_bits(channel2_allocation, 4);

            }

            if (allocation != 0) {

                samplelength = allocation + 1;

                factor = table_factor[allocation];

                offset = table_offset[allocation];

            }

            if (channel2_allocation != 0) {

                channel2_samplelength = channel2_allocation + 1;

                channel2_factor = table_factor[channel2_allocation];

                channel2_offset = table_offset[channel2_allocation];

            }

        }



        /**

	   *

	   */

        public void read_scalefactor(SoundStream stream, Frame header) {

            if (allocation != 0) scalefactor = scalefactors[stream.get_bits(6)];

            if (channel2_allocation != 0) channel2_scalefactor = scalefactors[stream.get_bits(6)];

        }



        /**

	   *

	   */

        public boolean read_sampledata(SoundStream stream) {

            boolean returnvalue = super.read_sampledata(stream);

            if (channel2_allocation != 0) {

                channel2_sample = (float) (stream.get_bits(channel2_samplelength));

            }

            return (returnvalue);

        }



        /**

	   *

	   */

        public boolean put_next_sample(int channels, SynthesisFilter filter1, SynthesisFilter filter2) {

            super.put_next_sample(channels, filter1, filter2);

            if ((channel2_allocation != 0) && (channels != OutputChannels.LEFT_CHANNEL)) {

                float sample2 = (channel2_sample * channel2_factor + channel2_offset) * channel2_scalefactor;

                if (channels == OutputChannels.BOTH_CHANNELS) filter2.input_sample(sample2, subbandnumber); else filter1.input_sample(sample2, subbandnumber);

            }

            return true;

        }

    }



    ;

}
