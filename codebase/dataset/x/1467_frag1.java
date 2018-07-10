    private Sha256(Sha256 md) {

        this();

        this.h0 = md.h0;

        this.h1 = md.h1;

        this.h2 = md.h2;

        this.h3 = md.h3;

        this.h4 = md.h4;

        this.h5 = md.h5;

        this.h6 = md.h6;

        this.h7 = md.h7;

        this.count = md.count;

        this.buffer = (byte[]) md.buffer.clone();

    }



    public static final int[] G(int hh0, int hh1, int hh2, int hh3, int hh4, int hh5, int hh6, int hh7, byte[] in, int offset) {

        return sha(hh0, hh1, hh2, hh3, hh4, hh5, hh6, hh7, in, offset);

    }



    public Object clone() {

        return new Sha256(this);

    }



    protected void transform(byte[] in, int offset) {

        int[] result = sha(h0, h1, h2, h3, h4, h5, h6, h7, in, offset);

        h0 = result[0];

        h1 = result[1];

        h2 = result[2];

        h3 = result[3];

        h4 = result[4];

        h5 = result[5];

        h6 = result[6];

        h7 = result[7];

    }



    protected byte[] padBuffer() {
