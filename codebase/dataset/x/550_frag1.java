                if (_ec.hasAttribute("ou")) {

                    _q.addCondition("ou", String.valueOf(_ec.getAttribute("ou")[0]), Query.BRANCH);

                } else if (_ec.hasAttribute("cn")) {

                    _q.addCondition("cn", String.valueOf(_ec.getAttribute("cn")[0]), Query.BRANCH);

                }

                write(_log, "Reading entries from remote directory .. ");

                List<Entry> _results = this._eb.search(_q);

                writeLine(_log, "done");

                for (Entry _u : _results) {
