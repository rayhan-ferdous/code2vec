    private Text createBrightnessFields(final Composite composite, String string) {

        final Button Light = new Button(composite, SWT.RADIO);

        Light.setText(string);

        Light.setFont(composite.getFont());

        SelectionAdapter action = new SelectionAdapter() {



            public void widgetSelected(final SelectionEvent e) {

                if (Light.getSelection()) {

                    channel = 2;

                    setColorFromFields();

                }

            }

        };

        Light.addSelectionListener(action);

        Text text = new Text(composite, SWT.BORDER);

        text.setFont(composite.getFont());

        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        return text;

    }
