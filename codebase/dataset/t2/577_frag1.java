                token = st.nextToken();

                negative = false;

                if (token.charAt(0) == '-') {

                    negative = true;

                    token = token.substring(1);

                }

                ar.ra = Integer.parseInt(token) * Math.PI / 12;

                if (noSeconds) {

                    ar.ra += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (12 * 60);

                } else {

                    ar.ra += Integer.parseInt(st.nextToken()) * Math.PI / (12 * 60);

                    ar.ra += Double.valueOf(st.nextToken()).doubleValue() * Math.PI / (12 * 60 * 60);

                }

                if (negative) ar.ra = -ar.ra;

                token = st.nextToken();
