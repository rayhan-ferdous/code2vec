                System.out.println(MessageFormat.format(messages.getString("deleteFailed"), new Object[] { f }));

            } else {

                ++counter[0];

            }

        }

        for (DirRecord child = rec.getFirstChild(true); child != null; child = child.getNextSibling(true)) {
