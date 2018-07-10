                token = st.nextToken();

                negative = false;

                if (token.charAt(0) == '-') {

                    negative = true;

                    token = token.substring(1);

                }

                ar.dec = Integer.parseInt(token) * Math.PI / 180;

                if (noSeconds) {

                    ar.dec += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (180 * 60);

                } else {

                    ar.dec += Integer.parseInt(st.nextToken()) * Math.PI / (180 * 60);

                    ar.dec += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (180 * 60 * 60);

                }

                if (negative) ar.dec = -ar.dec;

                token = st.nextToken();
