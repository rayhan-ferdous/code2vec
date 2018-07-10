            return null;

        } else return channels[channelno - MCS.MCS_GLOBAL_CHANNEL - 1];

    }



    /**

     * Remove all registered virtual channels

     */

    public void clear() {

        channels = new VChannel[MAX_CHANNELS];

        num_channels = 0;

    }



    /**

     * Register a new virtual channel

     * @param v Virtual channel to be registered

     * @return True if successful

     * @throws RdesktopException

     */

    public boolean register(VChannel v) throws RdesktopException {

        if (!Options.use_rdp5) {

            return false;

        }

        if (num_channels >= MAX_CHANNELS) throw new RdesktopException("Channel table full. Could not register channel.");

        channels[num_channels] = v;

        v.set_mcs_id(MCS.MCS_GLOBAL_CHANNEL + 1 + num_channels);

        num_channels++;

        return true;

    }



    /**

     * Process a packet sent on a numbered channel

     * @param data Packet sent to channel

     * @param mcsChannel Number specified for channel

     * @throws RdesktopException

     * @throws IOException

     * @throws CryptoException

     */

    public void channel_process(RdpPacket_Localised data, int mcsChannel) throws RdesktopException, IOException, CryptoException {

        int length, flags;
