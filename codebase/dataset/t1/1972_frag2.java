                g.setColor(Color.RED);

                if (rf.strand() == Strand.FORWARD) g.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height); else g.drawLine(r.x, r.y, r.x, r.y + r.height);

            }
