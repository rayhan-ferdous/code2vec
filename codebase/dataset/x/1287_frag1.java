        private void advance() {

            int idx = m_cur.nextSetBit(m_val + 1);

            if (idx < 0) {

                if (m_next != null) {

                    m_cur = m_next;

                    m_next = null;

                    m_val = -1;

                    advance();

                } else {

                    m_val = -2;

                }

                return;

            } else {

                m_val = idx;

            }

        }
