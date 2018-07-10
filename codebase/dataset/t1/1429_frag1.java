    public void debugPrint() {

        PrintStream tty = System.err;

        tty.println("Compressed texture: " + isCompressed());

        if (isCompressed()) {

            int fmt = getCompressionFormat();

            String name = getCompressionFormatName(fmt);

            tty.println("Compression format: 0x" + Integer.toHexString(fmt) + " (" + name + ")");

        }

        tty.println("Width: " + header.width + " Height: " + header.height);

        tty.println("header.pitchOrLinearSize: " + header.pitchOrLinearSize);

        tty.println("header.pfRBitMask: 0x" + Integer.toHexString(header.pfRBitMask));

        tty.println("header.pfGBitMask: 0x" + Integer.toHexString(header.pfGBitMask));

        tty.println("header.pfBBitMask: 0x" + Integer.toHexString(header.pfBBitMask));

        tty.println("SurfaceDesc flags:");

        boolean recognizedAny = false;

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_CAPS, "DDSD_CAPS");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_HEIGHT, "DDSD_HEIGHT");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_WIDTH, "DDSD_WIDTH");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_PITCH, "DDSD_PITCH");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_BACKBUFFERCOUNT, "DDSD_BACKBUFFERCOUNT");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_ZBUFFERBITDEPTH, "DDSD_ZBUFFERBITDEPTH");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_ALPHABITDEPTH, "DDSD_ALPHABITDEPTH");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_LPSURFACE, "DDSD_LPSURFACE");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_PIXELFORMAT, "DDSD_PIXELFORMAT");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_MIPMAPCOUNT, "DDSD_MIPMAPCOUNT");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_LINEARSIZE, "DDSD_LINEARSIZE");

        recognizedAny |= printIfRecognized(tty, header.flags, DDSD_DEPTH, "DDSD_DEPTH");

        if (!recognizedAny) {

            tty.println("(none)");

        }

        tty.println("Raw SurfaceDesc flags: 0x" + Integer.toHexString(header.flags));

        tty.println("Pixel format flags:");

        recognizedAny = false;

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ALPHAPIXELS, "DDPF_ALPHAPIXELS");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ALPHA, "DDPF_ALPHA");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_FOURCC, "DDPF_FOURCC");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED4, "DDPF_PALETTEINDEXED4");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXEDTO8, "DDPF_PALETTEINDEXEDTO8");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED8, "DDPF_PALETTEINDEXED8");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_RGB, "DDPF_RGB");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_COMPRESSED, "DDPF_COMPRESSED");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_RGBTOYUV, "DDPF_RGBTOYUV");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_YUV, "DDPF_YUV");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ZBUFFER, "DDPF_ZBUFFER");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED1, "DDPF_PALETTEINDEXED1");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_PALETTEINDEXED2, "DDPF_PALETTEINDEXED2");

        recognizedAny |= printIfRecognized(tty, header.pfFlags, DDPF_ZPIXELS, "DDPF_ZPIXELS");

        if (!recognizedAny) {

            tty.println("(none)");

        }

        tty.println("Raw pixel format flags: 0x" + Integer.toHexString(header.pfFlags));

        tty.println("Depth: " + getDepth());

        tty.println("Number of mip maps: " + getNumMipMaps());

        int fmt = getPixelFormat();

        tty.print("Pixel format: ");

        switch(fmt) {

            case D3DFMT_R8G8B8:

                tty.println("D3DFMT_R8G8B8");

                break;

            case D3DFMT_A8R8G8B8:

                tty.println("D3DFMT_A8R8G8B8");

                break;

            case D3DFMT_X8R8G8B8:

                tty.println("D3DFMT_X8R8G8B8");

                break;

            case D3DFMT_DXT1:

                tty.println("D3DFMT_DXT1");

                break;

            case D3DFMT_DXT2:

                tty.println("D3DFMT_DXT2");

                break;

            case D3DFMT_DXT3:

                tty.println("D3DFMT_DXT3");

                break;

            case D3DFMT_DXT4:

                tty.println("D3DFMT_DXT4");

                break;

            case D3DFMT_DXT5:

                tty.println("D3DFMT_DXT5");

                break;

            case D3DFMT_UNKNOWN:

                tty.println("D3DFMT_UNKNOWN");

                break;

            default:

                tty.println("(unknown pixel format " + fmt + ")");

                break;

        }

    }



    private static final int MAGIC = 0x20534444;



    static class Header {



        int size;



        int flags;



        int height;



        int width;



        int pitchOrLinearSize;



        int backBufferCountOrDepth;



        int mipMapCountOrAux;



        int alphaBitDepth;



        int reserved1;



        int surface;



        int colorSpaceLowValue;



        int colorSpaceHighValue;



        int destBltColorSpaceLowValue;



        int destBltColorSpaceHighValue;



        int srcOverlayColorSpaceLowValue;



        int srcOverlayColorSpaceHighValue;



        int srcBltColorSpaceLowValue;



        int srcBltColorSpaceHighValue;



        int pfSize;



        int pfFlags;



        int pfFourCC;



        int pfRGBBitCount;



        int pfRBitMask;



        int pfGBitMask;



        int pfBBitMask;



        int pfABitMask;



        int ddsCaps1 = 0x401000;



        int ddsCaps2;



        int ddsCapsReserved1;



        int ddsCapsReserved2;



        int textureStage;



        void read(ByteBuffer buf) throws IOException {

            int magic = buf.getInt();

            if (magic != MAGIC) {

                throw new IOException("Incorrect magic number 0x" + Integer.toHexString(magic) + " (expected " + MAGIC + ")");

            }

            size = buf.getInt();

            flags = buf.getInt();

            height = buf.getInt();

            width = buf.getInt();

            pitchOrLinearSize = buf.getInt();

            backBufferCountOrDepth = buf.getInt();

            mipMapCountOrAux = buf.getInt();

            alphaBitDepth = buf.getInt();

            reserved1 = buf.getInt();

            surface = buf.getInt();

            colorSpaceLowValue = buf.getInt();

            colorSpaceHighValue = buf.getInt();

            destBltColorSpaceLowValue = buf.getInt();

            destBltColorSpaceHighValue = buf.getInt();

            srcOverlayColorSpaceLowValue = buf.getInt();

            srcOverlayColorSpaceHighValue = buf.getInt();

            srcBltColorSpaceLowValue = buf.getInt();

            srcBltColorSpaceHighValue = buf.getInt();

            pfSize = buf.getInt();

            pfFlags = buf.getInt();

            pfFourCC = buf.getInt();

            pfRGBBitCount = buf.getInt();

            pfRBitMask = buf.getInt();

            pfGBitMask = buf.getInt();

            pfBBitMask = buf.getInt();

            pfABitMask = buf.getInt();

            ddsCaps1 = buf.getInt();

            ddsCaps2 = buf.getInt();

            ddsCapsReserved1 = buf.getInt();

            ddsCapsReserved2 = buf.getInt();

            textureStage = buf.getInt();

        }



        void write(ByteBuffer buf) {

            buf.putInt(MAGIC);

            buf.putInt(size);

            buf.putInt(flags);

            buf.putInt(height);

            buf.putInt(width);

            buf.putInt(pitchOrLinearSize);

            buf.putInt(backBufferCountOrDepth);

            buf.putInt(mipMapCountOrAux);

            buf.putInt(alphaBitDepth);

            buf.putInt(reserved1);

            buf.putInt(surface);

            buf.putInt(colorSpaceLowValue);

            buf.putInt(colorSpaceHighValue);

            buf.putInt(destBltColorSpaceLowValue);

            buf.putInt(destBltColorSpaceHighValue);

            buf.putInt(srcOverlayColorSpaceLowValue);

            buf.putInt(srcOverlayColorSpaceHighValue);

            buf.putInt(srcBltColorSpaceLowValue);

            buf.putInt(srcBltColorSpaceHighValue);

            buf.putInt(pfSize);

            buf.putInt(pfFlags);

            buf.putInt(pfFourCC);

            buf.putInt(pfRGBBitCount);

            buf.putInt(pfRBitMask);

            buf.putInt(pfGBitMask);

            buf.putInt(pfBBitMask);

            buf.putInt(pfABitMask);

            buf.putInt(ddsCaps1);

            buf.putInt(ddsCaps2);

            buf.putInt(ddsCapsReserved1);

            buf.putInt(ddsCapsReserved2);

            buf.putInt(textureStage);

        }



        private static int size() {

            return 124;

        }



        private static int pfSize() {

            return 32;

        }



        private static int writtenSize() {

            return 128;

        }

    }



    private DDSImage() {

    }



    private void readFromFile(File file) throws IOException {

        fis = new FileInputStream(file);

        chan = fis.getChannel();

        ByteBuffer buf = chan.map(FileChannel.MapMode.READ_ONLY, 0, (int) file.length());

        readFromBuffer(buf);

    }



    private void readFromBuffer(ByteBuffer buf) throws IOException {

        this.buf = buf;

        buf.order(ByteOrder.LITTLE_ENDIAN);

        header = new Header();

        header.read(buf);

        fixupHeader();

    }



    private void initFromData(int d3dFormat, int width, int height, ByteBuffer[] mipmapData) throws IllegalArgumentException {
