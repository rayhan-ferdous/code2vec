            @Override

            public InputStream[] get(Key<InputStream[], WebParam> inputStreamWebParamKey, WebParam an, String sign, NativeProvider<?> nativeProvider) {

                HttpServletRequest request = container.getInstance(HttpServletRequest.class);

                if (request == null) return null;

                Map<String, List<FileInfo>> map = (Map<String, List<FileInfo>>) request.getAttribute(WFU);

                if (map == null) return null;

                List<FileInfo> list = map.get(sign);

                InputStream[] is = new InputStream[list.size()];

                for (int i = 0; i < is.length; i++) {

                    is[i] = list.get(i).getStream();

                }

                return is;

            }
