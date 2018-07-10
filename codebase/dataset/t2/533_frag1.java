                response.setBody(body, allowGzip);

                response.setHeaders(wContext.createHeader(null, WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED, response.getBodySize(), 0L, "text/html", false, allowGzip));

                response.setResponse(WebResponseCodes.HTTP_BANDWIDTH_EXCEEDED);

                return response;

            }
