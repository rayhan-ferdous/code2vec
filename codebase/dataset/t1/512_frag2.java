        DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));

        String id = readWord(dis);

        if (!id.equals("s3")) {

            throw new IOException("Not proper s3 binary file " + location + path);

        }

        String name;

        while ((name = readWord(dis)) != null) {

            if (!name.equals("endhdr")) {

                String value = readWord(dis);

                props.setProperty(name, value);

            } else {

                break;

            }

        }

        int byteOrderMagic = dis.readInt();

        if (byteOrderMagic == BYTE_ORDER_MAGIC) {

            swap = false;

        } else if (byteSwap(byteOrderMagic) == BYTE_ORDER_MAGIC) {

            swap = true;

        } else {

            throw new IOException("Corrupt S3 file " + location + path);

        }

        return dis;

    }
