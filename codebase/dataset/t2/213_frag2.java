                        readInts(byteCount / 4, idata);

                    } else if (compression == COMP_LZW) {

                        stream.readFully(data, 0, byteCount);

                        byte byteArray[] = new byte[unitsInThisTile * 4];

                        lzwDecoder.decode(data, byteArray, newRect.height);

                        interpretBytesAsInts(byteArray, idata, unitsInThisTile);

                    } else if (compression == COMP_PACKBITS) {

                        stream.readFully(data, 0, byteCount);

                        int bytesInThisTile = unitsInThisTile * 4;

                        byte byteArray[] = new byte[bytesInThisTile];

                        decodePackbits(data, bytesInThisTile, byteArray);

                        interpretBytesAsInts(byteArray, idata, unitsInThisTile);

                    } else if (compression == COMP_DEFLATE) {

                        stream.readFully(data, 0, byteCount);

                        byte byteArray[] = new byte[unitsInThisTile * 4];
