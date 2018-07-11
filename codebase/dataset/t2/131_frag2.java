                this.scanner.resetTo(previousEnd, commentStart);

                try {

                    int token = this.scanner.getNextToken();

                    if (token != TerminalTokens.TokenNameWHITESPACE || this.scanner.currentPosition != commentStart) {

                        if (idx == startIdx) {

                            return nodeEnd;

                        }

                        break;

                    }

                } catch (InvalidInputException e) {

                    return nodeEnd;

                }

                char[] gap = this.scanner.getCurrentIdentifierSource();

                int nbrLine = 0;

                int pos = -1;

                while ((pos = CharOperation.indexOf('\n', gap, pos + 1)) >= 0) {

                    nbrLine++;

                }

                if (nbrLine > 1) {

                    break;

                }

            }
