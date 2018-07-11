        private JSlider create14BitSlider(String name, JPanel p) {

            JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 16383, 8192);

            slider.addChangeListener(this);

            TitledBorder tb = new TitledBorder(new EtchedBorder());

            tb.setTitle(name + " = 8192");

            slider.setBorder(tb);

            p.add(slider);

            p.add(Box.createHorizontalStrut(5));

            return slider;

        }
