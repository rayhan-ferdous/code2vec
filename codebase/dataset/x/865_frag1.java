    private String findInternalJavadoc(BinMember member) {

        Comment comment = Comment.findFor(member);

        if (comment != null) {

            return comment.getText();

        }

        return null;

    }
