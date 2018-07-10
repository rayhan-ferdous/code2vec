    public void showFirstRow() {

        if (rs == null) return;

        {

            try {

                if (rs.first()) {

                    for (int i = 1; i <= fields.size(); i++) {

                        String field = rs.getString(i);

                        JTextField tb = (JTextField) fields.get(i - 1);

                        tb.setText(field);

                    }

                } else {

                    rs.close();

                    rs = null;

                }

            } catch (Exception e) {

                warnme("Error " + e);

            }

        }

    }
