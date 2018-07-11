        public void recordDeleted(RecordStore arg0, int recordId) {

            synchronized (ObjectStore.this) {

                if (ObjectStore.this.objectListeners != null) {

                    for (int i = ObjectStore.this.objectListeners.size() - 1; i >= 0; i--) {

                        WeakReference ref = (WeakReference) ObjectStore.this.objectListeners.elementAt(i);

                        ObjectListener listener = (ObjectListener) ref.get();

                        if (listener != null) {

                            listener.objectDeleted(ObjectStore.this, recordId);

                        } else {

                            ObjectStore.this.objectListeners.removeElement(ref);

                        }

                    }

                }

                notifyStorageListeners(recordId);

            }

        }
