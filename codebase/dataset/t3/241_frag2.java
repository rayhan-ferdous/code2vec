                    program.setAttribute("channel", channelName);

                    Element titleElement = doc.createElement("title");

                    Text titleText = doc.createTextNode(item.getName());

                    titleElement.appendChild(titleText);

                    program.appendChild(titleElement);

                    Element subTitleElement = doc.createElement("sub-title");

                    Text subTitleText = doc.createTextNode(item.getSubName());
