    public void Undo() {

        if (move != 0) {

            for (int i = 0; i < 8; i++) {

                for (int id = 0; id < 8; id++) {

                    TheBoard[i][id] = OldBoard[i][id][move - 1];

                }

            }

            move--;

            repaint();

        }

    }
