        } else if (comp.equals(entry)) {

            String text = entry.getText();

            if (text.length() <= 0) {

                return;

            }

            if (text.charAt(0) == '/') {

                text = text.substring(1);

                if (text.trim().length() > 0) {

                    eirc.sendCommand(text, this);

                }

            } else {

                printMyPrivmsg(entry.getText());

                sendText(entry.getText());

            }

            entry.setText("");

        } else if (comp.equals(text_attr_picker)) {

            String text = entry.getText();

            String new_text = ev.getActionCommand();

            int pos = entry.getCaretPosition();

            text = text.substring(0, pos).concat(new_text).concat(text.substring(pos));

            entry.setText(text);

            entry.setCaretPosition(pos + new_text.length());

        }

    }
