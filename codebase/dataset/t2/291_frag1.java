        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, audio, org.jdesktop.beansbinding.ELProperty.create("${editor}"), jTextField3, org.jdesktop.beansbinding.BeanProperty.create("text"));

        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 1;

        gridBagConstraints.gridy = 2;

        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;

        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;

        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);

        jPanel4.add(jTextField3, gridBagConstraints);
