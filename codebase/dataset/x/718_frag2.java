            ImageInputStream iis = ImageIO.createImageInputStream(is);

            reader.setInput(iis, false, false);

            BufferedImage img = reader.read(0);

            WritableRaster r = img.getRaster();

            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);

            ColorModel targetCM = new ComponentColorModel(cs, new int[] { 16, 16, 16 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
