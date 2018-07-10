        } else if (url instanceof PopURL) {

            for (BufferIterator it = new BufferIterator(); it.hasNext(); ) {

                Buffer buf = it.nextBuffer();

                if (buf instanceof PopMailbox) {

                    PopMailbox mb = (PopMailbox) buf;

                    if (mb.getUrl().equals(url)) return mb;

                }

            }

            final PopURL popUrl = (PopURL) url;
