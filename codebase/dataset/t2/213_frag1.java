                        readShorts(byteCount / 2, sdata);

                    } else if (compression == COMP_LZW) {

                        stream.readFully(data, 0, byteCount);

                        byte byteArray[] = new byte[unitsInThisTile * 2];

                        lzwDecoder.decode(data, byteArray, newRect.height);

                        interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);

                    } else if (compression == COMP_PACKBITS) {

                        stream.readFully(data, 0, byteCount);

                        int bytesInThisTile = unitsInThisTile * 2;

                        byte byteArray[] = new byte[bytesInThisTile];

                        decodePackbits(data, bytesInThisTile, byteArray);

                        interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);

                    } else if (compression == COMP_DEFLATE) {

                        stream.readFully(data, 0, byteCount);

                        byte byteArray[] = new byte[unitsInThisTile * 2];
