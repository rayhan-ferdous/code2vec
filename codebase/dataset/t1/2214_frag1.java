                    response = generateReply(query, in, indp.getLength(), null);

                    if (response == null) continue;

                } catch (IOException e) {

                    response = formerrMessage(in);

                }

                if (outdp == null) outdp = new DatagramPacket(response, response.length, indp.getAddress(), indp.getPort()); else {

                    outdp.setData(response);

                    outdp.setLength(response.length);

                    outdp.setAddress(indp.getAddress());

                    outdp.setPort(indp.getPort());

                }

                sock.send(outdp);

            }

        } catch (IOException e) {
