                        parser.parseHeader();

                    }

                    if (parser.getReadTag() != Tags.SeqDelimitationItem) {

                        throw new DcmParseException("Unexpected Tag:" + Tags.toString(parser.getReadTag()));

                    }

                    if (parser.getReadLength() != 0) {

                        throw new DcmParseException("(fffe,e0dd), Length:" + parser.getReadLength());

                    }

                    ds.writeHeader(out, netParam, Tags.SeqDelimitationItem, VRs.NONE, 0);

                } else {

                    boolean swap = fileParam.byteOrder != netParam.byteOrder && parser.getReadVR() == VRs.OW;

                    writeValueTo(out, swap);

                }
