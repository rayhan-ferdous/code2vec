                } else {

                    boolean swap = fileParam.byteOrder != netParam.byteOrder && parser.getReadVR() == VRs.OW;

                    writeValueTo(out, swap);

                }

                if (truncPostPixelData) {

                    return;

                }

                ds.clear();

                try {

                    parser.parseDataset(fileParam, -1);
