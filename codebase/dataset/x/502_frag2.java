            out.write("                map.addControl(new OpenLayers.Control.PanZoomBar({\r\n");

            out.write("                    position: new OpenLayers.Pixel(2, 15)\r\n");

            out.write("                }));\t\t\t\r\n");

            out.write("                controls = {\r\n");

            out.write("                    start: new OpenLayers.Control.DrawFeature(start, SinglePoint),\r\n");

            out.write("                    stop: new OpenLayers.Control.DrawFeature(stop, SinglePoint)\r\n");

            out.write("                }\r\n");

            out.write("                for (var key in controls) {\r\n");

            out.write("                    map.addControl(controls[key]);\r\n");

            out.write("                }\r\n");

            out.write("                map.addLayers([tiled, start, stop,result]);\t\r\n");
