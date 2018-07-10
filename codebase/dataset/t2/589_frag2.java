    public NotificationChain basicSetUser(DynamicValue newUser, NotificationChain msgs) {

        DynamicValue oldUser = user;

        user = newUser;

        if (eNotificationRequired()) {

            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ActionstepPackage.MEET_ME_ADMIN__USER, oldUser, newUser);

            if (msgs == null) msgs = notification; else msgs.add(notification);

        }

        return msgs;

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    public void setUser(DynamicValue newUser) {

        if (newUser != user) {

            NotificationChain msgs = null;

            if (user != null) msgs = ((InternalEObject) user).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.MEET_ME_ADMIN__USER, null, msgs);

            if (newUser != null) msgs = ((InternalEObject) newUser).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ActionstepPackage.MEET_ME_ADMIN__USER, null, msgs);

            msgs = basicSetUser(newUser, msgs);

            if (msgs != null) msgs.dispatch();

        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, ActionstepPackage.MEET_ME_ADMIN__USER, newUser, newUser));

    }



    /**

	 * <!-- begin-user-doc -->

   * <!-- end-user-doc -->

	 * @generated

	 */

    @Override

    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
