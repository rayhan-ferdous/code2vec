                if (resolve) return getCall1();

                return basicGetCall1();

            case ActionstepPackage.VOICEMAIL_MAIN__MAILBOX:

                return getMailbox();

            case ActionstepPackage.VOICEMAIL_MAIN__SKIP_PASSWORD_CHECK:

                return isSkipPasswordCheck();

            case ActionstepPackage.VOICEMAIL_MAIN__USE_PREFIX:

                return isUsePrefix();

            case ActionstepPackage.VOICEMAIL_MAIN__RECORDING_GAIN:
