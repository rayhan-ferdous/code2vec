                if (line.startsWith("[%")) {

                    String brokerName = line.substring("[% ".length());

                    brokers.put(brokerName, broker);

                    line = reader.readLine();

                    continue;

                } else if (line.startsWith("[")) {
