                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) ty = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();

                if (tokenizer.ttype == EnhancedStreamTokenizer.TT_NUMBER) t = (int) tokenizer.nval; else throw (new DBGParseException(errorString, tokenizer, fileName));

                tokenizer.nextToken();
