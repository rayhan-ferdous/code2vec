    private void endTransfer(Channel channel, EndTransferPacket packet) throws OpenR66RunnerErrorException, OpenR66ProtocolSystemException, OpenR66ProtocolNotAuthenticatedException {

        if (!session.isAuthenticated()) {

            throw new OpenR66ProtocolNotAuthenticatedException("Not authenticated");

        }

        if (packet.isToValidate()) {

            session.newState(ENDTRANSFERS);

            if (!localChannelReference.getFutureRequest().isDone()) {

                R66Result result = new R66Result(session, false, ErrorCode.TransferOk, session.getRunner());

                session.newState(ENDTRANSFERR);

                session.setFinalizeTransfer(true, result);

                packet.validate();

                try {

                    ChannelUtils.writeAbstractLocalPacket(localChannelReference, packet).awaitUninterruptibly();

                } catch (OpenR66ProtocolPacketException e) {

                }

            } else {

                logger.error("Error since end of transfer signaled but already done");

                session.setStatus(23);

                Channels.close(channel);

                return;

            }

        } else {

            session.newState(ENDTRANSFERR);

            if (!localChannelReference.getFutureRequest().isDone()) {

                R66Result result = new R66Result(session, false, ErrorCode.TransferOk, session.getRunner());

                session.setFinalizeTransfer(true, result);

            }

        }

    }
