            if (comment.length > 0xffff) throw new ZipException("Comment too long.");

            writeLeShort(name.length);

            writeLeShort(extra.length);

            writeLeShort(comment.length);

            writeLeShort(0);

            writeLeShort(0);

            writeLeInt(0);

            writeLeInt(entry.offset);

            out.write(name);

            out.write(extra);

            out.write(comment);

            numEntries++;

            sizeEntries += CENHDR + name.length + extra.length + comment.length;

        }

        writeLeInt(ENDSIG);

        writeLeShort(0);

        writeLeShort(0);

        writeLeShort(numEntries);

        writeLeShort(numEntries);

        writeLeInt(sizeEntries);

        writeLeInt(offset);

        writeLeShort(zipComment.length);

        out.write(zipComment);

        out.flush();

        entries = null;

    }
