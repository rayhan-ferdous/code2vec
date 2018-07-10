        if (url instanceof ImapURL) {

            for (BufferIterator it = new BufferIterator(); it.hasNext(); ) {

                Buffer buf = it.nextBuffer();

                if (buf instanceof ImapMailbox) {

                    ImapMailbox mb = (ImapMailbox) buf;

                    if (mb.getUrl().equals(url)) return mb;

                }

            }

            final ImapURL imapUrl = (ImapURL) url;
