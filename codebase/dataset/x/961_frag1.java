            @Override

            public void keyTyped(KeyEvent e) {

                if (e.getKeyChar() == '\n') {

                    HTMLDocument doc = (HTMLDocument) getDocument();

                    HTMLEditorKit editorKit = (HTMLEditorKit) getEditorKit();

                    try {

                        editorKit.insertHTML(doc, getCaretPosition(), "<br>", 0, 0, null);

                        int pos = getCaretPosition();

                        setChangedStatus();

                        String txt = getText();

                        setText("");

                        setText(txt);

                        setCaretPosition(pos - 2);

                    } catch (BadLocationException ex) {

                        System.out.println(ex);

                    } catch (IOException ex) {

                        System.out.println(ex);

                    }

                }

                if (!e.isControlDown()) setChangedStatus();

            }
