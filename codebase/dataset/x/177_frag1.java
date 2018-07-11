                public void mouseMoved(MouseEvent me) {

                    Icon icon;

                    if (isSelected()) {

                        ignoreNextMousePressed = false;

                    }

                    if (isCloseHit(me)) {

                        icon = closeIcon_rollover;

                    } else {

                        pressed = false;

                        icon = closeIcon;

                    }

                    if (icon != currentIcon) {

                        currentIcon = icon;

                        tabbedPane.repaint();

                    }

                }
