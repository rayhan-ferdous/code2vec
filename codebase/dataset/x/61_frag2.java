                    pst.setString(4, req.persLong.getSource());

                    pst.setString(5, req.persLong.getPrivateText());

                    pst.setString(6, req.persLong.getRefn());

                    pst.setInt(7, req.persLong.getPid());

                    if (req.persLong.getModified() != null) {
