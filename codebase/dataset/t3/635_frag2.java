        public synchronized EventPacket extractPacket(AEPacketRaw in) {

            if (out == null) {

                out = new EventPacket(chip.getEventClass());

            } else {

                out.clear();

            }

            if (in == null) {

                return out;

            }

            int n = in.getNumEvents();

            int skipBy = 1;

            if (isSubSamplingEnabled()) {

                while (n / skipBy > getSubsampleThresholdEventCount()) {

                    skipBy++;

                }

            }

            int[] datas = in.getAddresses();

            int[] timestamps = in.getTimestamps();

            OutputEventIterator outItr = out.outputIterator();

            for (int i = 0; i < n; i++) {
