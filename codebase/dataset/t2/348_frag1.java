    private void faderMoved(int fader, int value) {

        Logger.reportStatus("FaderMoved: fader: " + fader + ", value: " + value);

        if (fader == 32) {

            nextFader();

            return;

        } else {

            if (fader == 31) {

                prevFader();

                return;

            } else {

                if (fader > 16) {

                    fader = (byte) (0 - (fader - 16) - (faderBank * 16));

                } else {

                    fader += (faderBank * 16);

                }

            }

        }

        if (recentWidget != null) {

            SysexWidget w = recentWidget;

            if (fader == faderBank * 16) {

                fader = lastFader;

            }

            if (w.getSliderNum() == fader && w.isShowing()) {

                if (w.getNumFaders() == 1) {

                    w.setValue((int) (w.getValueMin() + ((float) (value) / 127 * (w.getValueMax() - w.getValueMin()))));

                } else {

                    w.setFaderValue(fader, value);

                }

                w.repaint();

                return;

            }

        }

        lastFader = fader;

        for (SysexWidget w : widgetList) {

            if ((w.getSliderNum() == fader || (w.getSliderNum() < fader && w.getSliderNum() + w.getNumFaders() > fader)) && w.isShowing()) {

                if (w.getNumFaders() == 1) {

                    w.setValue((int) (w.getValueMin() + ((float) (value) / 127 * (w.getValueMax() - w.getValueMin()))));

                } else {

                    w.setFaderValue(fader, value);

                }

                w.repaint();

                recentWidget = w;

                return;

            }

        }

    }
