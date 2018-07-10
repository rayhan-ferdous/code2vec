    public void add(int index, GameObject element) {

        if (index > size || index < 0) {

            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

        }

        ensureCapacity(size + 1);

        System.arraycopy(gameObjects, index, gameObjects, index + 1, size - index);

        gameObjects[index] = element;

        size++;

    }
