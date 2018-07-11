    private Text createGreenFields(final Composite composite, String string) {

        final Button green = new Button(composite, SWT.RADIO);

        green.setText(string);

        green.setFont(composite.getFont());

        SelectionAdapter action = new SelectionAdapter() {



            public void widgetSelected(final SelectionEvent e) {

                if (green.getSelection()) {

                    channel = 4;

                    setColorFromFields();

                }

            }

        };

        green.addSelectionListener(action);

        Text text = new Text(composite, SWT.BORDER);

        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        text.setFont(composite.getFont());

        return text;

    }
