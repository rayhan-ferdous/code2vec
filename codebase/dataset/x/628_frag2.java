                            } else if ("class java.awt.Color".equals(type)) {

                                final JButton button = new JButton();

                                try {

                                    final Color colorObj = ((Color) readMethod.invoke(bean, null));

                                    button.setText("...");

                                    button.setBackground(colorObj);

                                    ActionListener actionListener = new ActionListener() {



                                        public void actionPerformed(ActionEvent e) {

                                            Color newColor = JColorChooser.showDialog(JCalendarDemo.this, "Choose Color", colorObj);

                                            button.setBackground(newColor);

                                            try {

                                                writeMethod.invoke(currentBean, new Object[] { newColor });

                                            } catch (Exception e1) {

                                                e1.printStackTrace();

                                            }

                                        }

                                    };

                                    button.addActionListener(actionListener);

                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                                addProperty(propertyDescriptors[i], button, gridbag);

                                count += 1;
