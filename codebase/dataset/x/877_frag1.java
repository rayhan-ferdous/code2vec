                Token tok;

                int type;

                while (true) {

                    tok = m_lexer.nextToken();

                    type = tok.getType();

                    String s;

                    if (VlogppLexer.EOF == type) {

                        break;

                    }

                    s = tok.getText();

                    m_writer.append(s);
